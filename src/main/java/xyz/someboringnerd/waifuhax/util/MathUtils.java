package xyz.someboringnerd.waifuhax.util;

import net.minecraft.util.math.*;

public class MathUtils {

    public static final Direction[] HORIZONTAL_DIR = {Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH};

    public static double Angle(Vec3d a, Vec3d b) {
        return Math.toDegrees(Math.acos(a.dotProduct(b) / (a.length() * b.length())));
    }

    public static Vec2f ToPolar(Vec3d from, Vec3d to) {
        Vec3d diff = to.subtract(from);
        double diffXZ = Math.sqrt(diff.x * diff.x + diff.z * diff.z);
        float pitch = MathHelper.wrapDegrees((float) -Math.toDegrees(Math.atan2(diff.y, diffXZ)));
        float yaw = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(diff.z, diff.x)) - 90f);
        return new Vec2f(pitch, yaw);
    }

    public static BlockPos toBlockPos(Vec3d vec3d) {
        return new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
    }

}
