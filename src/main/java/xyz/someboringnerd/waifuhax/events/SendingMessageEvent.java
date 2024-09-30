package xyz.someboringnerd.waifuhax.events;

import lombok.Getter;
import meteordevelopment.orbit.ICancellable;

public class SendingMessageEvent implements ICancellable {
    private static final SendingMessageEvent ON_MESSAGE_SEND = new SendingMessageEvent();
    @Getter
    private String message;
    @Getter
    private boolean modified;
    private boolean cancelled;

    public static SendingMessageEvent get(String message) {
        ON_MESSAGE_SEND.setCancelled(false);
        ON_MESSAGE_SEND.message = message;
        ON_MESSAGE_SEND.modified = false;
        return ON_MESSAGE_SEND;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public void setMessage(String message) {
        this.message = message;
        this.modified = true;
    }
}