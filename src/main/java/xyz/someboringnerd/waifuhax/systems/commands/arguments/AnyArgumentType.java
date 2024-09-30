package xyz.someboringnerd.waifuhax.systems.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.settings.AbstractSetting;
import xyz.someboringnerd.waifuhax.settings.BooleanSetting;
import xyz.someboringnerd.waifuhax.settings.EnumSetting;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class AnyArgumentType implements ArgumentType<Object> {

    private static final AnyArgumentType instance = new AnyArgumentType();

    public static ArgumentType<Object> get() {
        return instance;
    }

    @Override
    public Object parse(StringReader reader) throws CommandSyntaxException {
        final String text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return text;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        AbstractSetting set = null;
        try {
            set = SettingArgumentType.getSetting(context);
        } catch (CommandSyntaxException e) {
            LogManager.printToChat("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            e.printStackTrace();
        }

        if (set == null)
            return ArgumentType.super.listSuggestions(context, builder);

        if (set instanceof EnumSetting enumSetting) {
            List<String> options = List.of(Arrays.stream(enumSetting.getValue().getDeclaringClass().getEnumConstants()).map(Object::toString).toArray(String[]::new));
            return CommandSource.suggestMatching(options.stream(), builder);
        } else if (set instanceof BooleanSetting) {
            return CommandSource.suggestMatching(Stream.of("true", "false"), builder);
        }

        return ArgumentType.super.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return List.of(new String[]{"", "", ""});
    }
}
