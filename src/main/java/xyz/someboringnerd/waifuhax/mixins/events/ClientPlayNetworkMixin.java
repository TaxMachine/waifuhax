package xyz.someboringnerd.waifuhax.mixins.events;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.SendingMessageEvent;
import xyz.someboringnerd.waifuhax.managers.CommandManager;
import xyz.someboringnerd.waifuhax.managers.LogManager;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkMixin {

    @Unique
    private boolean dontSend;

    @Unique
    boolean sendAnyway;

    @Shadow
    public abstract void sendChatMessage(String content);

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        if (MinecraftClient.getInstance().getNetworkHandler() == null || dontSend)
            return;

        SendingMessageEvent e = WaifuHax.EVENT_BUS.post(SendingMessageEvent.get(message));

        if (e.isModified() && (!message.startsWith("!") || sendAnyway) && !message.startsWith("#") && !message.startsWith("*")) {
            ci.cancel();
            dontSend = true;
            sendChatMessage(e.getMessage());
            dontSend = false;
            return;
        }

        if (message.startsWith("!!")) {
            ci.cancel();
            sendAnyway = true;
            sendChatMessage(message.substring(1));
            sendAnyway = false;
        } else if (message.startsWith("!")) {
            try {
                CommandManager.getInstance().dispatch(message.substring(1));
            } catch (CommandSyntaxException ex) {
                LogManager.printToChat(ex.getMessage());
                ex.printStackTrace();
            }

            MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(message);
            ci.cancel();
        }
    }

}
