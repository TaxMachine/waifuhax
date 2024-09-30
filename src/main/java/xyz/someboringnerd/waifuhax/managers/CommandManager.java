package xyz.someboringnerd.waifuhax.managers;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import org.reflections.Reflections;
import xyz.someboringnerd.waifuhax.systems.commands.AbstractCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CommandManager {

    public final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    public final List<AbstractCommand> commandList = new ArrayList<>();

    @Getter(AccessLevel.PUBLIC)
    private PlayerListEntry entry = null;

    @Getter(AccessLevel.PUBLIC)
    private static CommandManager instance;

    protected CommandManager() {
        instance = this;

        new Reflections("xyz.someboringnerd.waifuhax.systems.commands.impl").getSubTypesOf(AbstractCommand.class).forEach(mod -> {
            try {
                AbstractCommand cmd = mod.getDeclaredConstructor().newInstance();
                commandList.add(cmd);
                registerCommandToDispatcher(cmd);
                cmd.init();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void init() {
        new CommandManager();
    }

    private void registerCommandToDispatcher(AbstractCommand cmd) {
        LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.literal(cmd.getName());
        cmd.build(builder);
        dispatcher.register(builder);
    }

    public AbstractCommand getCommand(String command) {

        AtomicReference<AbstractCommand> commandR = new AtomicReference<>();

        commandList.forEach(cmd -> {
            if (cmd.getName().equalsIgnoreCase(command))
                commandR.set(cmd);
        });

        return commandR.get();
    }

    public void dispatch(String cmd) throws CommandSyntaxException {
        dispatcher.execute(cmd, MinecraftClient.getInstance().getNetworkHandler().getCommandSource());
    }
}
