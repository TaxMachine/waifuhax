package xyz.someboringnerd.waifuhax.systems.modules.impl.combat;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import xyz.someboringnerd.waifuhax.events.TickEvent;
import xyz.someboringnerd.waifuhax.settings.EnumSetting;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

public class AutoCrystal extends AbstractModule {

    public final EnumSetting targetMode = new EnumSetting("Target Mode", "The way the target is choosen", targetType.CLOSEST);

    private PlayerEntity target;

    public AutoCrystal() {
        super(CATEGORY.COMBAT);
    }

    @EventHandler
    private void onTick(TickEvent event) {
        if (mc.world == null) return;

        if (target == null || !target.isAlive()) {

        }
    }

    @Override
    public String getDescription() {
        return "Chinese crystal aura";
    }
}

enum targetType {
    CLOSEST,        // focus the closest enemy
    FARTHEST,       // focus the farthest enemy
    HIGHEST_HP,     // focus the enemy with the highest health
    LOWEST_HP,      // focus the enemy with the lowest health
    HIGHEST_DANGER,  // focus the enemy that represent the biggest danger for the player
    LOWEST_DANGER   // focus the enemy that represent the lowest danger for the player
}