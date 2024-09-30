package xyz.someboringnerd.waifuhax.settings;

import imgui.ImGui;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

public class FloatSetting extends AbstractSetting<Float> {

    @Getter(AccessLevel.PUBLIC)
    private final float min, max;

    public FloatSetting(String name, String description, Float defaultValue, float min, float max, String... aliases) {
        super(name, description, defaultValue, null, aliases);

        this.max = max;
        this.min = min;
    }

    @Nullable
    @Override
    public Float fromString(String string) {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public void load(JSONObject savedSettings) {
        if (savedSettings.has(getName())) {
            setValue(savedSettings.getFloat(getName()));
        }
    }

    @Override
    public void render(AbstractModule module) {
        if (!isShouldDraw()) return;
        float[] temp = {getValue()};
        ImGui.sliderFloat(getName(), temp, getMin(), getMax());
        if (temp[0] != getValue()) {
            setValue(temp[0]);
            module.save();
        }
    }

    @Override
    public String toString() {
        return null;
    }
}
