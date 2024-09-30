package xyz.someboringnerd.waifuhax.events;

import lombok.AccessLevel;
import lombok.Getter;
import meteordevelopment.orbit.ICancellable;

public class MessageReceivedEvent implements ICancellable {

    private static final MessageReceivedEvent mre = new MessageReceivedEvent();

    @Getter(AccessLevel.PUBLIC)
    private String sender;

    @Getter(AccessLevel.PUBLIC)
    private String message;

    public static MessageReceivedEvent get(String sender, String message) {
        mre.message = message;
        mre.sender = sender;
        return mre;
    }

    @Override
    public void setCancelled(boolean cancelled) {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
