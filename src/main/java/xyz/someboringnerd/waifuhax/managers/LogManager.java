package xyz.someboringnerd.waifuhax.managers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {

    private static Logger LOGGER = LoggerFactory.getLogger("WaifuHax");

    /**
     * Print stuff to the stdout console
     *
     * @param input  stuff to print
     * @param format make use of String#format to format text
     */
    public static void print(String input, Object... format) {
        LOGGER.info(String.format(input, format));
    }

    public static void printToChat(String input, Object... format) {
        printToChatWithoutPrefix("§c[§4WaifuHax§c]§r " + input, format);
    }

    public static void printToChatWithoutPrefix(String input, Object... format) {
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().inGameHud != null && MinecraftClient.getInstance().inGameHud.getChatHud() != null)
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(String.format(input, format)));
    }

    public static void sendMessage(Text of) {
        MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(of.getString());
    }
}
