package xyz.someboringnerd.waifuhax.mixins.events;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.MouseButtonPressedEvent;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(at = @At("HEAD"), method = "onMouseButton", cancellable = true)
    private void onKeyPressed(long window, int button, int action, int mods, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen != null) return;

        MouseButtonPressedEvent e = WaifuHax.EVENT_BUS.post(MouseButtonPressedEvent.get(button, action));
        if (e.isCancelled()) ci.cancel();
    }

}
