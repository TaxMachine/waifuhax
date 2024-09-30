package xyz.someboringnerd.waifuhax.mixins.events;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.OnKeyPress;
import xyz.someboringnerd.waifuhax.managers.ModuleManager;
import xyz.someboringnerd.waifuhax.screens.ClickGuiScreen;
import xyz.someboringnerd.waifuhax.settings.KeybindSetting;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(at = @At("HEAD"), method = "onKey", cancellable = true)
    private void onKeyPressed(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen != null && !(MinecraftClient.getInstance().currentScreen instanceof ClickGuiScreen))
            return;

        if (action == 1) {
            if (KeybindSetting.currentBind == null && !(MinecraftClient.getInstance().currentScreen instanceof ClickGuiScreen)) {
                if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                    MinecraftClient.getInstance().setScreen(new ClickGuiScreen());
                } else {
                    ModuleManager.onKey(key);
                    OnKeyPress e = WaifuHax.EVENT_BUS.post(OnKeyPress.get(key, action));
                    if (e.isCancelled()) ci.cancel();
                }
                return;
            }
            if (KeybindSetting.currentBind != null) {
                if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_ESCAPE || key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
                    KeybindSetting.currentBind.setValue(-1);
                } else {
                    KeybindSetting.currentBind.setValue(key);
                }
                KeybindSetting.currentBind.shouldSave = true;
            }
        }
    }

}
