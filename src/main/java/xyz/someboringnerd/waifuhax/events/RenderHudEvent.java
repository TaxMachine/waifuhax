package xyz.someboringnerd.waifuhax.events;

import lombok.AccessLevel;
import lombok.Getter;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.gui.DrawContext;

public class RenderHudEvent implements ICancellable {
    private static final RenderHudEvent onTickEvent = new RenderHudEvent();

    @Getter(AccessLevel.PUBLIC)
    private DrawContext graphics;

    public static RenderHudEvent get(DrawContext graphics) {
        onTickEvent.graphics = graphics;
        return onTickEvent;
    }

    @Override
    public void setCancelled(boolean cancelled) {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
