package xyz.someboringnerd.waifuhax.settings;

import imgui.ImGui;
import imgui.type.ImInt;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

public class IntegerSetting extends AbstractSetting<Integer> {

    @Getter(AccessLevel.PUBLIC)
    private final int min, max;

    public IntegerSetting(String name, String description, Integer defaultValue, int min, int max, String... aliases) {
        super(name, description, defaultValue, null, aliases);

        this.max = max;
        this.min = min;
    }

    @Nullable
    @Override
    public Integer fromString(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public void load(JSONObject savedSettings) {
        if (savedSettings.has(getName())) {
            setValue(savedSettings.getInt(getName()));
        }
    }

    private boolean useInput = false;

    @Override
    public void render(AbstractModule module) {
        if (!isShouldDraw()) return;
        int temp;
        int[] val = {getValue()};
        if (!useInput) {
            ImGui.sliderInt(getName(), val, getMin(), getMax());
            temp = ((int[]) val)[0];
        } else {
            ImInt temp2 = new ImInt(getValue());
            ImGui.inputInt(getName(), temp2);
            temp = temp2.get();
        }
        if (temp != getValue()) {
            setValue(temp);
            module.save();
        }
    }

    public IntegerSetting useInput() {
        this.useInput = true;
        return this;
    }

    @Override
    public String toString() {
        return null;
    }
}
