package xyz.someboringnerd.waifuhax.screens;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.RenderImGuiEvent;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;
import xyz.someboringnerd.waifuhax.systems.modules.ClickGui;

import java.util.HashSet;
import java.util.Set;

public class ClickGuiScreen extends Screen {


    private static final Set<ClickGui> categories = new HashSet<>();

    private static ClickGui hud;

    public ClickGuiScreen() {
        super(Text.of("WH-3 ClickGui"));
        WaifuHax.EVENT_BUS.subscribe(this);
    }

    static {
        for (CATEGORY cat : CATEGORY.values()) {
            if (cat.equals(CATEGORY.HUD))
                hud = new ClickGui(cat);
            else
                categories.add(new ClickGui(cat));
        }
    }

    @Override
    public void close() {
        super.close();
        WaifuHax.EVENT_BUS.unsubscribe(this);
    }

    // hack to remove Minecraft's stupid blur effect
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    public static ClickGui getHudCategory() {
        return hud;
    }

    @EventHandler
    public void onImguiRender(RenderImGuiEvent event) {
        if (MinecraftClient.getInstance().currentScreen instanceof ClickGuiScreen)
            categories.forEach(cat -> cat.internalRender(event));
        else WaifuHax.EVENT_BUS.unsubscribe(this);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
