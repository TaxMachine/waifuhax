package xyz.someboringnerd.waifuhax.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class PlayerUtils {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void LookAt(Vec3d vec) {
        Vec2f polar = MathUtils.ToPolar(mc.player.getPos(), vec);
        PlayerMoveC2SPacket.LookAndOnGround packet = new PlayerMoveC2SPacket.LookAndOnGround(polar.y, polar.x, mc.player.isOnGround());
        mc.player.networkHandler.sendPacket(packet);
//        mc.player.setPitch(polar.x);
        mc.player.setYaw(polar.y);
    }

    public static void LookAt(Direction dir) {
        Vec2f polar = null;
        switch (dir) {
            case EAST -> polar = new Vec2f(0, -90);
            case WEST -> polar = new Vec2f(0, 90);
            case UP -> polar = new Vec2f(-90, -90);
            case DOWN -> polar = new Vec2f(90, -90);
            case SOUTH -> polar = new Vec2f(0, 0);
            case NORTH -> polar = new Vec2f(0, 180);
        }
        PlayerMoveC2SPacket.LookAndOnGround packet = new PlayerMoveC2SPacket.LookAndOnGround(polar.y, polar.x, mc.player.isOnGround());
        mc.player.networkHandler.sendPacket(packet);
    }

    public static Vec2f getLookAt(Direction dir) {
        Vec2f polar = null;
        switch (dir) {
            case EAST -> polar = new Vec2f(0, -90);
            case WEST -> polar = new Vec2f(0, 90);
            case UP -> polar = new Vec2f(-90, -90);
            case DOWN -> polar = new Vec2f(90, -90);
            case SOUTH -> polar = new Vec2f(0, 0);
            case NORTH -> polar = new Vec2f(0, 180);
        }
        return polar;
    }
}