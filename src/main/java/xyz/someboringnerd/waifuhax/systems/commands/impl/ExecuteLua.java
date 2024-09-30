package xyz.someboringnerd.waifuhax.systems.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.MessageArgumentType;
import xyz.someboringnerd.waifuhax.systems.commands.AbstractCommand;
import xyz.someboringnerd.waifuhax.systems.macro.MacroInterpreter;

public class ExecuteLua extends AbstractCommand {
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("lua", MessageArgumentType.message()).executes(ctx -> {
            MacroInterpreter.execute(ctx.getArgument("lua", MessageArgumentType.MessageFormat.class).contents());
            return 1;
        }));
    }
}
