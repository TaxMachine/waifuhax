package xyz.someboringnerd.waifuhax.settings;

import imgui.ImGui;
import imgui.type.ImInt;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

import javax.annotation.Nullable;

public class StringArraySetting extends AbstractSetting<String[]> {

    private String[] possibleValues;

    private int index = 0;

    public StringArraySetting(String name, String description, String defaultValue, String[] possibleValues, String... aliases) {
        super(name, description, new String[]{defaultValue}, null, aliases);
        this.possibleValues = possibleValues;
    }

    @Nullable
    @Override
    public String[] fromString(String string) {
        for (int i = 0; i < possibleValues.length; i++) {
            if (possibleValues[i].equalsIgnoreCase(string)) {
                index = i;
                break;
            }
        }
        setValue(new String[]{possibleValues[index]});
        return null;
    }

    @Override
    public void load(JSONObject savedSettings) {
        if (savedSettings.has(getName())) {
            for (int i = 0; i < possibleValues.length; i++) {
                if (possibleValues[i].equals(savedSettings.getString(getName()))) {
                    index = i;
                    break;
                }
            }
            setValue(new String[]{possibleValues[index]});
        }
    }

    @Override
    public void save(JSONObject saveData) {
        saveData.put(getName(), getSelectedValue());
    }

    public String getSelectedValue() {
        return getValue()[0];
    }

    @Override
    public void render(AbstractModule module) {
        if (!isShouldDraw()) return;
        ImInt tmp = new ImInt(index);
        ImGui.combo(getName(), tmp, possibleValues, possibleValues.length);

        if (tmp.get() != index) {
            index = tmp.get();
            setValue(new String[]{possibleValues[index]});
            module.save();
        }
    }
}