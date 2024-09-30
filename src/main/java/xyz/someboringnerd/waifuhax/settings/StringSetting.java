package xyz.someboringnerd.waifuhax.settings;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

public class StringSetting extends AbstractSetting<String> {
    public StringSetting(String name, String description, String defaultValue, String... aliases) {
        super(name, description, defaultValue, null, aliases);
    }

    @Nullable
    @Override
    public String fromString(String string) {
        return string;
    }

    @Override
    public void load(JSONObject savedSettings) {
        if (savedSettings.has(getName())) {
            setValue(savedSettings.getString(getName()));
        }
    }

    @Override
    public void render(AbstractModule module) {
        if (!isShouldDraw()) return;
    }


}
