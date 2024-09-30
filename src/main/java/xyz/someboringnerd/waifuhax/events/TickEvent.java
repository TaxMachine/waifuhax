package xyz.someboringnerd.waifuhax.events;

import meteordevelopment.orbit.ICancellable;

public class TickEvent implements ICancellable {
    private static final TickEvent onTickEvent = new TickEvent();

    public static TickEvent get() {
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
