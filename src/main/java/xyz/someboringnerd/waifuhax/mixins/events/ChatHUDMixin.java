package xyz.someboringnerd.waifuhax.mixins.events;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.MessageReceivedEvent;

@Mixin(ChatHud.class)
public abstract class ChatHUDMixin {


    @Shadow
    public abstract void addMessage(Text message);

    @Unique
    private boolean ignore = false;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"))
    private void onMessageReceived(Text message, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
        WaifuHax.EVENT_BUS.post(MessageReceivedEvent.get("null", message.getString()));
    }

}
