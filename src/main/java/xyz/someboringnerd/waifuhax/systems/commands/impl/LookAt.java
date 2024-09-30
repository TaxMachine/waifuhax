package xyz.someboringnerd.waifuhax.systems.commands.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Vec3d;
import xyz.someboringnerd.waifuhax.systems.commands.AbstractCommand;
import xyz.someboringnerd.waifuhax.util.PlayerUtils;

public class LookAt extends AbstractCommand {
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("x", IntegerArgumentType.integer(-30000000, 30000000))
                .then(argument("z", IntegerArgumentType.integer(-30000000, 30000000))
                        .executes(context -> {
                            int x = context.getArgument("x", int.class);
                            int z = context.getArgument("z", int.class);

                            PlayerUtils.LookAt(new Vec3d(x, mc.player.getY(), z));

                            return 1;
                        })
                ));
    }
}
