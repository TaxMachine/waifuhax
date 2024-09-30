package xyz.someboringnerd.waifuhax.systems.modules.impl.misc;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;
import xyz.someboringnerd.waifuhax.events.PlayerEnterRenderDistanceEvent;
import xyz.someboringnerd.waifuhax.events.PlayerJoinEvent;
import xyz.someboringnerd.waifuhax.events.PlayerLeaveEvent;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

public class Welcomer extends AbstractModule {

    public Welcomer() {
        super(CATEGORY.MISC);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        LogManager.sendMessage(Text.of(String.format("Welcome, %s !", event.getPlayer().getProfile().getName())));
    }

    @EventHandler
    public void onPlayerLeave(PlayerLeaveEvent event) {
        LogManager.sendMessage(Text.of(String.format("Goodbye %s !", event.getPlayer().getProfile().getName())));
    }

    @EventHandler
    public void onPlayerEnterRenderDistance(PlayerEnterRenderDistanceEvent event) {
        LogManager.printToChat("%s entered render distance", event.getPlayer().getName().getString());
    }


    @Override
    public String getDescription() {
        return "Automatically welcome players";
    }
}
