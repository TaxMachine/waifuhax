package xyz.someboringnerd.waifuhax.systems.commands.arguments;

import com.google.common.collect.Streams;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import xyz.someboringnerd.waifuhax.managers.ModuleManager;
import xyz.someboringnerd.waifuhax.settings.AbstractSetting;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SettingArgumentType implements ArgumentType<String> {

    private static final SettingArgumentType instance = new SettingArgumentType();

    private final static List<AbstractSetting> settingsList = new ArrayList<>();

    private List<String> lastValidSuggestions = new ArrayList<>();

    public static ArgumentType<String> from(List<AbstractSetting> settings) {
        SettingArgumentType.settingsList.clear();
        SettingArgumentType.settingsList.addAll(settings);
        return instance;
    }

    public static <S> AbstractSetting getSetting(CommandContext<S> ctx) throws CommandSyntaxException {

        String modName = ctx.getInput().split(" ")[0].trim().replace("!", "");
        AbstractModule mod = ModuleManager.getModule(modName);
        if (mod == null) {
            throw new DynamicCommandExceptionType(name -> Text.literal(String.format("Something went wrong, mod name : %s", modName))).create("");
        }

        settingsList.clear();
        settingsList.addAll(mod.getSettings());

        Optional<AbstractSetting> set = settingsList.stream()
                .filter(setting -> setting.matchAnyAliases(String.valueOf(ctx.getArgument("setting", String.class))
                        .replace("-", " ")))
                .findFirst();

        if (set.isEmpty()) {
            throw new DynamicCommandExceptionType(name -> Text.literal(String.format("No setting match this input, tested %s against the following : %s", ctx.getArgument("setting", String.class), unified(settingsList)))).create("");
        }

        return set.get();
    }

    private static String unified(List<AbstractSetting> settingsList) {
        AtomicReference<String> union = new AtomicReference<>("");

        settingsList.forEach(set -> set.getAliases().forEach(al -> union.set(union.get() + "\"" + al + "\"\n")));

        return union.get();
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        AbstractModule mod = ModuleManager.getModule(context.getInput().split(" ")[0].substring(1).trim());
        if (mod == null) {
            return CommandSource.suggestMatching(lastValidSuggestions.stream(), builder);
        }

        settingsList.clear();
        settingsList.addAll(mod.getSettings());

        lastValidSuggestions = Streams.stream(mod.getSettings())
                .map(set -> set.getDisplayName().replace(" ", "-").trim())
                .collect(Collectors.toList());

        return CommandSource.suggestMatching(lastValidSuggestions.stream(), builder);
    }
}