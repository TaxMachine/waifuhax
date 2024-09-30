package xyz.someboringnerd.waifuhax.systems.modules.impl.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import xyz.someboringnerd.waifuhax.events.OnKeyPress;
import xyz.someboringnerd.waifuhax.settings.IntegerSetting;
import xyz.someboringnerd.waifuhax.settings.KeybindSetting;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;
import xyz.someboringnerd.waifuhax.util.CombatUtils;
import xyz.someboringnerd.waifuhax.util.MathUtils;
import xyz.someboringnerd.waifuhax.util.TpUtils;

public class ClickTp extends AbstractModule {

    public KeybindSetting tpKeybindSetting = new KeybindSetting("TpKeybind", "Key to trigger tp.", -1, -1, Integer.MAX_VALUE);
    public IntegerSetting maxDistanceSetting = new IntegerSetting("MaxDistance", "Maximum tp distance.", 150, 0, 200);
    public IntegerSetting entityAngleSetting = new IntegerSetting("PlayerAngle", "Tp to entities if you look at them.", 5, 0, 45);
    public IntegerSetting distanceToEntitySetting = new IntegerSetting("PlayerDistance", "How close to tp to the entity (negative = behind).", 3, -10, 10);

    public ClickTp() {
        super(CATEGORY.MOVEMENT);
    }

    @EventHandler
    public void onKeyPress(OnKeyPress event) {
        if (event.getKey() != tpKeybindSetting.getValue()) return;
        Vec3d origin = mc.player.getEyePos();
        Vec3d dir = Vec3d.fromPolar(mc.player.getPitch(), mc.player.getYaw());
        if (TpEntity(origin, dir)) return;
        TpBlock(origin, dir);
    }

    public boolean TpEntity(Vec3d origin, Vec3d dir) {
        if (entityAngleSetting.getValue() == 0) return false;
        LivingEntity target = CombatUtils.GetPlayerTarget(CombatUtils::ClosestCrosshair, maxDistanceSetting.getValue() - distanceToEntitySetting.getValue());
        if (target == null) return false;
        Vec3d end = target.getPos().add(0d, 1d, 0d);
        Vec3d dirToTarget = end.subtract(origin).normalize();
        if (MathUtils.Angle(dir, dirToTarget) > entityAngleSetting.getValue()) return false;
        BlockHitResult bhr = mc.world.raycast(new RaycastContext(origin, end.subtract(dir.multiply(distanceToEntitySetting.getValue())), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, mc.player));
        if (bhr.getType() != HitResult.Type.MISS) return false;
        TpUtils.Tp(bhr.getPos());
        return true;
    }

    public boolean TpBlock(Vec3d origin, Vec3d dir) {
        BlockHitResult bhr = mc.world.raycast(new RaycastContext(origin, origin.add(dir.multiply(maxDistanceSetting.getValue())), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, mc.player));
        if (bhr.getType() != HitResult.Type.BLOCK) return false;
        BlockPos pos = bhr.getBlockPos().offset(bhr.getSide());
        if (TpUtils.cannotGo(pos.up())) pos = pos.down();
        TpUtils.Tp(Vec3d.ofBottomCenter(pos));
        return true;
    }

    @Override
    public String getDescription() {
        return "Teleport where you are looking at";
    }
}
