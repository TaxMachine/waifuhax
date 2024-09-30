package xyz.someboringnerd.waifuhax.mixins.events;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.someboringnerd.imutils.managers.ImguiManager;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.RenderHudEvent;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(at = @At("TAIL"), method = "render")
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        WaifuHax.EVENT_BUS.post(RenderHudEvent.get(context));
        ImguiManager.render();
    }

}
