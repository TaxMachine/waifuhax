package xyz.someboringnerd.waifuhax.systems.modules;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.events.RenderImGuiEvent;
import xyz.someboringnerd.waifuhax.screens.HudEditor;

public abstract class HudElement extends AbstractModule {
    @Getter
    private final HUDSIDE hudside;

    private String name;
    private int imguiProperty;

    @Setter(AccessLevel.PROTECTED)
    private boolean closable;

    protected final ImBoolean active = new ImBoolean(true);

    public boolean isActive() {
        return active.get();
    }

    // note: definitively a bad idea
    @Override
    public String getDescription() {
        return "";
    }

    public HudElement(String name, int imguiProperty, boolean closable, HUDSIDE hudside) {
        super(CATEGORY.HUD);
        this.name = name;
        this.imguiProperty = imguiProperty;
        this.closable = closable;
        this.hudside = hudside;
    }

    public abstract void render();

    public void toggle() {
        isEnabled.setValue(!isEnabled.getValue());
        if (!active.get())
            active.set(true);
        onToggle();
    }

    public void toggle(boolean state) {
        if (state) {
            WaifuHax.EVENT_BUS.unsubscribe(this);
        } else {
            WaifuHax.EVENT_BUS.subscribe(this);
        }
        active.set(!state);
    }

    @EventHandler
    public void internalRender(RenderImGuiEvent event) {
        if (!active.get())
            return;

        int property = imguiProperty;

        if (hudside == HUDSIDE.HUD_ELEMENT && !(MinecraftClient.getInstance().currentScreen instanceof HudEditor))
            property |= ImGuiWindowFlags.NoMove;

        if (closable) {
            if (ImGui.begin(name, active, property)) {
                render();
            }
        } else {
            if (ImGui.begin(name, property)) {
                render();
            }
        }

        ImGui.end();

        if (!active.get())
            toggle(true);
    }
}
