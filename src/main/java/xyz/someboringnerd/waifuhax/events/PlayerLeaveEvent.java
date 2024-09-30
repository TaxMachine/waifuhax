package xyz.someboringnerd.waifuhax.events;

import lombok.AccessLevel;
import lombok.Getter;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.network.PlayerListEntry;

public class PlayerLeaveEvent implements ICancellable {

    private static final PlayerLeaveEvent playerJoinEvent = new PlayerLeaveEvent();

    @Getter(AccessLevel.PUBLIC)
    private PlayerListEntry player;

    public static PlayerLeaveEvent get(PlayerListEntry player) {
        playerJoinEvent.player = player;

        return playerJoinEvent;
    }

    @Override
    public void setCancelled(boolean cancelled) {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
