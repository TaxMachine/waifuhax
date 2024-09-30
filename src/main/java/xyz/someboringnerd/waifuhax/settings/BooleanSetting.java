package xyz.someboringnerd.waifuhax.settings;

import imgui.ImGui;
import imgui.type.ImBoolean;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

public class BooleanSetting extends AbstractSetting<Boolean> {

    public BooleanSetting(String name, String description, boolean defaultValue, String... aliases) {
        super(name, description, defaultValue, null, aliases);
    }

    public BooleanSetting(String name, String description, boolean defaultValue, Runnable onModified, String... aliases) {
        super(name, description, defaultValue, onModified, aliases);
    }

    @Nullable
    @Override
    public Boolean fromString(String string) {
        return string.equalsIgnoreCase("true") || string.equalsIgnoreCase("1");
    }

    @Override
    public void load(JSONObject savedSettings) {
        if (savedSettings.has(getName())) {
            setValue(savedSettings.getBoolean(getName()));
        }
    }

    @Override
    public void render(AbstractModule module) {
        if (!isShouldDraw()) return;
        ImBoolean cbox = new ImBoolean(getValue());
        ImGui.checkbox(getName(), cbox);
        if (getValue() != cbox.get()) {
            setValue(cbox.get());
            if (module != null)
                module.save();
            if (onModified != null) onModified.run();
        }
    }


}
