package xyz.someboringnerd.waifuhax.events;

import lombok.AccessLevel;
import lombok.Getter;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerEnterRenderDistanceEvent implements ICancellable {

    private static final PlayerEnterRenderDistanceEvent playerJoinEvent = new PlayerEnterRenderDistanceEvent();

    @Getter(AccessLevel.PUBLIC)
    private PlayerEntity player;

    public static PlayerEnterRenderDistanceEvent get(PlayerEntity player) {
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
