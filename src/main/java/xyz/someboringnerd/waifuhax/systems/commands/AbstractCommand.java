package xyz.someboringnerd.waifuhax.systems.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public abstract class AbstractCommand {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    public String getName() {
        return getClass().getSimpleName().toLowerCase();
    }

    public void init() {
    }

    public abstract void build(LiteralArgumentBuilder<CommandSource> builder);

    // yes, this is pasted from Meteor
    // Helper methods to painlessly infer the CommandSource generic type argument
    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(final String name) {
        return LiteralArgumentBuilder.literal(name);
    }
}
