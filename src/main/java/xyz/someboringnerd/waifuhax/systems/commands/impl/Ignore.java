package xyz.someboringnerd.waifuhax.systems.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.systems.commands.AbstractCommand;

public class Ignore extends AbstractCommand {
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", StringArgumentType.word()).executes(context -> {

            LogManager.printToChat("%s was added to the ignore list, no message containing their username will be shown to you from now on.", context.getArgument("player", String.class));
            LogManager.printToChat("Note : as it is, does absolutely nothing.");
            return 1;
        }));
    }
}
