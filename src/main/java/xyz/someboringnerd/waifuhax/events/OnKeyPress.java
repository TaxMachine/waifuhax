package xyz.someboringnerd.waifuhax.events;

import lombok.Getter;
import meteordevelopment.orbit.ICancellable;

public class OnKeyPress implements ICancellable {
    private static final OnKeyPress ON_KEY_PRESS = new OnKeyPress();
    @Getter
    private String message;
    @Getter
    private int key;
    @Getter
    private int action;
    private boolean cancelled;

    public static OnKeyPress get(int key, int action) {
        ON_KEY_PRESS.setCancelled(false);
        ON_KEY_PRESS.key = key;
        ON_KEY_PRESS.action = action;
        return ON_KEY_PRESS;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}