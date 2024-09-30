package xyz.someboringnerd.waifuhax.systems.modules.impl.world;

import meteordevelopment.orbit.EventHandler;
import xyz.someboringnerd.waifuhax.events.ChunkOcclusionEvent;
import xyz.someboringnerd.waifuhax.settings.FloatSetting;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

public class CutAwayWorld extends AbstractModule {

    public final FloatSetting nearPlaneDistance = new FloatSetting("Near Plane Distance", "How far the nearplane is set", 5.5f, 0.05f, 10);

    public CutAwayWorld() {
        super(CATEGORY.WORLD);
    }

    @Override
    public String getDescription() {
        return "Fuck with nearplane to see through the world";
    }

    @EventHandler
    public void onChunkOcclusion(ChunkOcclusionEvent event) {
        event.setCancelled(true);
    }
}
