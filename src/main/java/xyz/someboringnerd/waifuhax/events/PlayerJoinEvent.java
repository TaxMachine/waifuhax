package xyz.someboringnerd.waifuhax.events;

import lombok.AccessLevel;
import lombok.Getter;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.network.PlayerListEntry;

public class PlayerJoinEvent implements ICancellable {

    private static final PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent();

    @Getter(AccessLevel.PUBLIC)
    private PlayerListEntry player;

    public static PlayerJoinEvent get(PlayerListEntry player) {
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
