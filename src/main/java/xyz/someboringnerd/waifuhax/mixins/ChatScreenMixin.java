package xyz.someboringnerd.waifuhax.mixins;

import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextFieldWidget.class)
public class ChatScreenMixin {

    @Inject(at = @At("HEAD"), method = "write", cancellable = true)
    private void onKeyPressed(String text, CallbackInfo ci) {
        /*if(ClickGUIScreen.getInstance().getClickgui().isEnabled()) {
            ci.cancel();
        }*/
    }

}
