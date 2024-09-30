package xyz.someboringnerd.waifuhax.settings;

import imgui.ImGui;
import net.minecraft.client.util.InputUtil;
import org.jetbrains.annotations.Nullable;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

public class KeybindSetting extends IntegerSetting {

    public static KeybindSetting currentBind = null;

    public boolean shouldSave = false;

    public KeybindSetting(String name, String description, Integer defaultValue, int min, int max, String... aliases) {
        super(name, description, defaultValue, min, max, aliases);
    }

    @Nullable
    @Override
    public Integer fromString(String string) {
        Integer v = super.fromString(string);
        if (v != null) return v;
        if (string.isEmpty()) return null;
        return (int) string.charAt(0);
    }

    @Override
    public void render(AbstractModule module) {
        if (!isShouldDraw()) return;
        if (currentBind == this) {
            ImGui.text("Press a key to bind");
            if (shouldSave) {
                currentBind = null;
                shouldSave = false;
                if (module != null) module.save();
            }
        } else {
            String name = getValue() < 0 ? "NONE" : InputUtil.fromKeyCode(getValue(), -1).getLocalizedText().getString();

            if (ImGui.button(getName() + ": " + name + " ##" + module.getName())) {
                if (currentBind != null && currentBind != this) {
                    currentBind.shouldSave = true;
                } else {
                    currentBind = this;
                }
            }
        }
    }

}
