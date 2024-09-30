package xyz.someboringnerd.waifuhax.screens;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.RenderImGuiEvent;
import xyz.someboringnerd.waifuhax.systems.modules.ClickGui;

public class HudEditor extends Screen {

    private static final ClickGui hudCategory = ClickGuiScreen.getHudCategory();

    public HudEditor() {
        super(Text.of("WH-3 HudEditor"));
        WaifuHax.EVENT_BUS.subscribe(this);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @EventHandler
    public void onImguiRender(RenderImGuiEvent event) {
        hudCategory.internalRender(event);
    }

    // hack to remove Minecraft's stupid blur effect
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void close() {
        super.close();
        WaifuHax.EVENT_BUS.unsubscribe(this);
    }
}
