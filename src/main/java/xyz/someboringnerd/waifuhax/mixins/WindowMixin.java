package xyz.someboringnerd.waifuhax.mixins;

import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.someboringnerd.imutils.managers.ImguiManager;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.RenderImGuiEvent;

@Mixin(Window.class)
public class WindowMixin {
    @Shadow
    @Final
    private long handle;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void onWindowOpen(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, String fullscreenVideoMode, String title, CallbackInfo ci) {
        ImguiManager.init(handle, (args -> WaifuHax.EVENT_BUS.post(new RenderImGuiEvent())));
    }
}
