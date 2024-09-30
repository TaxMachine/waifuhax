package xyz.someboringnerd.waifuhax.events;

import meteordevelopment.orbit.ICancellable;

public class RenderImGuiEvent implements ICancellable {
    private static final RenderImGuiEvent onTickEvent = new RenderImGuiEvent();

    public static RenderImGuiEvent get() {
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
