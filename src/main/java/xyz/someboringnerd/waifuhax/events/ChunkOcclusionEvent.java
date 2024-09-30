package xyz.someboringnerd.waifuhax.events;

import lombok.Getter;
import lombok.Setter;
import meteordevelopment.orbit.ICancellable;

public class ChunkOcclusionEvent implements ICancellable {

    @Getter
    @Setter
    private boolean cancelled = false;

    private static final ChunkOcclusionEvent EVENT = new ChunkOcclusionEvent();

    public static ChunkOcclusionEvent get() {
        EVENT.setCancelled(false);
        return EVENT;
    }

}
