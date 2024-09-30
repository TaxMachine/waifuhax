package xyz.someboringnerd.waifuhax.settings;

import imgui.ImGui;
import imgui.type.ImInt;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

import java.util.Arrays;

public class EnumSetting extends AbstractSetting<Enum> {

    private String[] options;

    private int index = 0;

    public EnumSetting(String name, String description, Enum defaultValue, String... aliases) {
        super(name, description, defaultValue, null, aliases);

        options = Arrays.stream(defaultValue.getDeclaringClass().getEnumConstants()).map(Object::toString).toArray(String[]::new);
    }

    @Nullable
    @Override
    public Enum<?> fromString(String string) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equalsIgnoreCase(string)) {
                index = i;
                break;
            }
        }
        setValue((Enum) getValue().getDeclaringClass().getEnumConstants()[index]);
        return null;
    }

    @Override
    public void load(JSONObject savedSettings) {
        if (savedSettings.has(getName())) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(savedSettings.getString(getName()))) {
                    index = i;
                    break;
                }
            }
            setValue((Enum) getValue().getDeclaringClass().getEnumConstants()[index]);
        }
    }


    @Override
    public void render(AbstractModule module) {
        if (!isShouldDraw()) return;
        ImInt tmp = new ImInt(index);
        ImGui.combo(getName(), tmp, options, options.length);

        if (tmp.get() != index) {
            index = tmp.get();
            setValue((Enum) getValue().getDeclaringClass().getEnumConstants()[index]);
            if (module != null)
                module.save();
        }
    }
}
