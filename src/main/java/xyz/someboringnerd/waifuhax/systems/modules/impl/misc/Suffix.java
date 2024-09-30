package xyz.someboringnerd.waifuhax.systems.modules.impl.misc;

import meteordevelopment.orbit.EventHandler;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.SendingMessageEvent;
import xyz.someboringnerd.waifuhax.settings.BooleanSetting;
import xyz.someboringnerd.waifuhax.settings.StringSetting;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

import java.security.SecureRandom;

public class Suffix extends AbstractModule {

    public StringSetting suffix = new StringSetting("suffix", "stuff to put at the end of your messages", " | WaifuHaxV3-[GIT_HASH]");

    public BooleanSetting antiantispam = new BooleanSetting("Anti-Anti-Spam", "bypass antispam on some servers", true);

    private String[] emoji = "☯❤❣⚜☣⁈⁉✪✰✯✭∞♾⌀♪♩♫♬⏻⚝⚧♂♀㊗㊙☮❀✿❁✵❃✾✼❉✷❋✺✹✸✴✳✶☆⯪⯫☽☀⭐★☘❄⚔⛏⚗✂⚓✎✏✒☂☔⌛⏳⌚⚐☕☎⌨✉⌂⚒⚙⚖⚰⚱✈✁✃✄♚♔".split("");

    public Suffix() {
        super(CATEGORY.MISC);
    }

    @EventHandler
    public void onMessageSend(SendingMessageEvent event) {
        if (!event.getMessage().startsWith("!") && !event.getMessage().endsWith(suffix.getValue().replace("[GIT_HASH]", WaifuHax.VERSION))) {
            event.setMessage(event.getMessage() + suffix.getValue().replace("[GIT_HASH]", WaifuHax.VERSION) + (antiantispam.getValue() ? " " + emoji[new SecureRandom().nextInt(0, emoji.length - 1)] : ""));
        }
    }

    @Override
    public String getDescription() {
        return "Turn yourself into a advertiser with this module !";
    }

}
