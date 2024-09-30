package xyz.someboringnerd.waifuhax.events;

import lombok.Getter;
import meteordevelopment.orbit.ICancellable;

public class MouseButtonPressedEvent implements ICancellable {
    private static final MouseButtonPressedEvent ON_MOUSE_BUTTON = new MouseButtonPressedEvent();
    @Getter
    private String message;
    @Getter
    private int button;
    @Getter
    private int action;
    private boolean cancelled;

    public static MouseButtonPressedEvent get(int button, int action) {
        ON_MOUSE_BUTTON.setCancelled(false);
        ON_MOUSE_BUTTON.button = button;
        ON_MOUSE_BUTTON.action = action;
        return ON_MOUSE_BUTTON;
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