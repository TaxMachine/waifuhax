package xyz.someboringnerd.waifuhax.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.someboringnerd.waifuhax.managers.ModuleManager;
import xyz.someboringnerd.waifuhax.systems.modules.impl.world.CutAwayWorld;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    private float zoom, zoomX, zoomY;

    @Shadow
    @Final
    MinecraftClient client;

    @Shadow
    public abstract float getFarPlaneDistance();

    @Inject(method = "getBasicProjectionMatrix", at = @At("RETURN"), cancellable = true)
    public void getProjectionMatrix(double fov, CallbackInfoReturnable<Matrix4f> cir) {
        if (ModuleManager.getModule(CutAwayWorld.class).isEnabled.getValue()) {
            Matrix4f matrix4f = new Matrix4f();
            if (zoom != 1.0F) {
                matrix4f.translate(this.zoomX, -this.zoomY, 0.0F);
                matrix4f.scale(this.zoom, this.zoom, 1.0F);
            }
            cir.setReturnValue(matrix4f.perspective(
                    (float) (fov * 0.01745329238474369),
                    (float) client.getWindow().getFramebufferWidth() / (float) this.client.getWindow().getFramebufferHeight(),
                    ModuleManager.getModule(CutAwayWorld.class).nearPlaneDistance.getValue(),
                    getFarPlaneDistance())
            );
        }
    }
}
