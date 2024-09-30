package xyz.someboringnerd.waifuhax.systems.modules.impl.movement;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;
import xyz.someboringnerd.waifuhax.events.TickEvent;
import xyz.someboringnerd.waifuhax.settings.IntegerSetting;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

public class Strafe extends AbstractModule {

    public IntegerSetting speedBoost = new IntegerSetting("Speed", "speed modifier", 35, 1, 500);


    public Strafe() {
        super(CATEGORY.MOVEMENT);
    }

    @EventHandler
    public void onTick(TickEvent event) {
        if (mc.player != null) {
            if ((mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0)) {
                if (!mc.player.isSprinting()) {
                    mc.player.setSprinting(true);
                }

                mc.player.setVelocity(new Vec3d(0, mc.player.getVelocity().y, 0));
                mc.player.updateVelocity((float) speedBoost.getValue() / 100, new Vec3d(mc.player.sidewaysSpeed, 0, mc.player.forwardSpeed));

                double vel = Math.abs(mc.player.getVelocity().getX()) + Math.abs(mc.player.getVelocity().getZ());

                if (vel >= 0.12 && mc.player.isOnGround()) {
                    mc.player.updateVelocity(0.1f, new Vec3d(mc.player.sidewaysSpeed, 0, mc.player.forwardSpeed));
                    mc.player.jump();
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "Uhm acktualy, it's bunny hop ! :nerd:";
    }
}
