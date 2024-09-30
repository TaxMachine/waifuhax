package xyz.someboringnerd.waifuhax.systems.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.systems.commands.AbstractCommand;
import xyz.someboringnerd.waifuhax.systems.macro.MacroInterpreter;
import xyz.someboringnerd.waifuhax.util.PathUtils;

import java.io.File;

public class Macro extends AbstractCommand {

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("load").then(argument("macro", StringArgumentType.word()).executes(ctx -> {

            File macro = new File(PathUtils.join("./WaifuHax", "macros", ctx.getArgument("macro", String.class)));

            if (!macro.exists()) {
                LogManager.printToChat("This macro does not exist ! make sure you store it in the macros folder of WaifuHax !");
                return 0;
            }

            MacroInterpreter.loadMacro(macro);

            return 1;
        })));
    }
}
