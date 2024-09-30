package xyz.someboringnerd.waifuhax.events;

import lombok.Getter;
import meteordevelopment.orbit.ICancellable;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;

public class OnRenderEvent implements ICancellable {
    private static final OnRenderEvent onRenderEvent = new OnRenderEvent();

    @Getter
    private VertexConsumerProvider.Immediate vertexConsumers;
    @Getter
    private Camera camera;

    public static OnRenderEvent get(VertexConsumerProvider.Immediate vertexConsumers, Camera camera) {
        onRenderEvent.vertexConsumers = vertexConsumers;
        onRenderEvent.camera = camera;
        return onRenderEvent;
    }

    @Override
    public void setCancelled(boolean cancelled) {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
