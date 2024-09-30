package xyz.someboringnerd.waifuhax.util;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import xyz.someboringnerd.waifuhax.mixins.AbstractBlockAccessor;

public class TpUtils {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private static class CanTp {
        public boolean isUp = false;
        public boolean isDown = false;
        public BlockPos current;
        public BlockPos to;

        public CanTp(BlockPos current, BlockPos to) {
            this.current = current;
            this.to = to;
        }
    }

    public static boolean cannotGo(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        return ((AbstractBlockAccessor) state.getBlock()).isCollidable() || !state.getFluidState().isEmpty();
    }

    public static boolean canGo(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        return !((AbstractBlockAccessor) state.getBlock()).isCollidable() && state.getFluidState().isEmpty();
    }

    public static boolean canTp(BlockPos from, BlockPos to) {
        if (cannotGo(to) || cannotGo(to.up())) return false;
        if (from.getX() == to.getX() && from.getZ() == to.getZ()) return true;
        boolean xFirst = Math.abs(to.getZ() - from.getZ()) <= Math.abs(to.getX() - from.getX());
        if (canTpY(from, to) && canTpXZ(from, to, xFirst)) return true;
        return canTpXZ(from, to, xFirst);
    }

    private static boolean canTpXZ(BlockPos from, BlockPos to, boolean firstX) {
        CanTp canTp = new CanTp(from, to);
        int dirX = (int) Math.copySign(1, to.getX() - from.getX());
        int dirZ = (int) Math.copySign(1, to.getZ() - from.getZ());
        if (firstX) {
            return canTpX(canTp, dirX) && canTpZ(canTp, dirZ);
        } else {
            return canTpZ(canTp, dirZ) && canTpX(canTp, dirX);
        }
    }

    private static boolean canTpX(CanTp canTp, int dir) {
        int finalX = canTp.to.getX();
        if (canTp.current.getX() == finalX) return true;
        canTp.current = canTp.current.add(dir, 0, 0);
        while (canTp.current.getX() != finalX) {
            if (cannotGo(canTp.current.up())) return false;
            if (cannotGo(canTp.current)) {
                canTp.isDown = true;
                if (canTp.isUp) return false;
            }
            if (cannotGo(canTp.current.up(2))) {
                canTp.isUp = true;
                if (canTp.isDown) return false;
            }
            canTp.current = canTp.current.add(dir, 0, 0);
        }
        return true;
    }

    private static boolean canTpZ(CanTp canTp, int dir) {
        int finalZ = canTp.to.getZ();
        if (canTp.current.getZ() == finalZ) return true;
        canTp.current = canTp.current.add(0, 0, dir);
        while (canTp.current.getZ() != finalZ) {
            if (cannotGo(canTp.current.up())) return false;
            if (cannotGo(canTp.current)) {
                canTp.isDown = true;
                if (canTp.isUp) return false;
            }
            if (cannotGo(canTp.current.up(2))) {
                canTp.isUp = true;
                if (canTp.isDown) return false;
            }
            canTp.current = canTp.current.add(0, 0, dir);
        }
        return true;
    }

    private static boolean canTpY(BlockPos from, BlockPos to) {
        int dirY = to.getY() - from.getY();
        if (Math.abs(dirY) <= 1) return true;
        dirY = (int) Math.copySign(1, dirY);
        BlockPos current = from.up(dirY * 2);
        while (current.getY() != to.getY()) {
            if (cannotGo(current)) return false;
            current = current.up(dirY);
        }
        return true;
    }

    public static void Move(Vec3d to) {
        Move(to, true);
    }

    public static void Move(Vec3d to, boolean setPos) {
        PlayerMoveC2SPacket.PositionAndOnGround packet = new PlayerMoveC2SPacket.PositionAndOnGround(to.x, to.y, to.z, mc.player.isOnGround());
        mc.player.networkHandler.sendPacket(packet);
        if (setPos) mc.player.setPos(to.x, to.y, to.z);
    }

    public static void Tp(Vec3d to) {
        Tp(to, null, true);
    }

    public static void Tp(Vec3d to, boolean setPos) {
        Tp(to, null, setPos);
    }

    public static void Tp(Vec3d to, Vec2f rotation, boolean setPos) {
        Vec3d from = mc.player.getPos();
        double distance = from.distanceTo(to);
        int num = (int) (Math.ceil(distance / 10) - 1);
        PlayerMoveC2SPacket.PositionAndOnGround staticPacket = new PlayerMoveC2SPacket.PositionAndOnGround(from.x, from.y, from.z, mc.player.isOnGround());
        PlayerMoveC2SPacket movePacket;
        if (rotation == null)
            movePacket = new PlayerMoveC2SPacket.PositionAndOnGround(to.x, to.y, to.z, mc.player.isOnGround());
        else
            movePacket = new PlayerMoveC2SPacket.Full(to.x, to.y, to.z, rotation.y, rotation.x, mc.player.isOnGround());

        for (int i = 0; i < num; i++) {
            mc.player.networkHandler.sendPacket(staticPacket);
        }
        mc.player.networkHandler.sendPacket(movePacket);
        if (setPos) mc.player.setPos(to.x, to.y, to.z);
    }

}
