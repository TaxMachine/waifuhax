package xyz.someboringnerd.waifuhax.systems.modules.impl.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import xyz.someboringnerd.waifuhax.events.TickEvent;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.settings.FloatSetting;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;
import xyz.someboringnerd.waifuhax.util.CombatUtils;
import xyz.someboringnerd.waifuhax.util.TpUtils;

public class Follow extends AbstractModule {

    public FloatSetting maxDistanceSetting = new FloatSetting("TargetDistance", "Target distance.", 100f, 0, 200);
    public FloatSetting yOffsetSetting = new FloatSetting("YOffset", "Y offset to the target.", 0f, -6, 6);

    public Follow() {
        super(CATEGORY.MOVEMENT);
    }

    PlayerEntity target = null;

    @Override
    public void onActivate(boolean live) {
        if (mc.player == null) return;
        target = CombatUtils.GetPlayerTarget(CombatUtils::ClosestCrosshair, maxDistanceSetting.getValue());
        if (target == null) {
            LogManager.printToChat("Target not found.");
            toggle();
        }
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (target == null) return;
        TpUtils.Tp(target.getPos().add(0, yOffsetSetting.getValue(), 0));
        mc.player.setVelocity(Vec3d.ZERO);
    }

    @Override
    public String getDescription() {
        return "Also known as teleport aura";
    }
}
