package xyz.someboringnerd.waifuhax.systems.commands.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.systems.commands.AbstractCommand;
import xyz.someboringnerd.waifuhax.util.BlockUtils;
import xyz.someboringnerd.waifuhax.util.TpUtils;

public class Bottom extends AbstractCommand {
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            // tp to center
            TpUtils.Tp(mc.player.getBlockPos().toCenterPos());

            BlockPos nextPos = BlockUtils.findFirstSafeSpaceUnderOfPlayer();

            if (nextPos != null) {
                nextPos = nextPos.add(0, 1, 0);
                LogManager.printToChat("currentY=%s, newY=%s. Expected variation: %s", mc.player.getBlockY(), nextPos.getY(), (nextPos.getY() - mc.player.getBlockY()));
                TpUtils.Tp(nextPos.toCenterPos());
                return 1;
            }

            LogManager.printToChat("Could not find a suitable position to teleport you");
            return 1;
        });
    }
}
