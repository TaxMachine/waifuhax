package xyz.someboringnerd.waifuhax.settings;

import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

public class LongSetting extends AbstractSetting<Long> {

    @Getter(AccessLevel.PUBLIC)
    private final long min, max;

    public LongSetting(String name, String description, Long defaultValue, long min, long max, String... aliases) {
        super(name, description, defaultValue, null, aliases);

        this.max = max;
        this.min = min;
    }

    @Nullable
    @Override
    public Long fromString(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public void load(JSONObject savedSettings) {
        if (savedSettings.has(getName())) {
            setValue(savedSettings.getLong(getName()));
            LogManager.print("%s was set to %s", getName(), String.valueOf(getValue()));
        }
    }

    @Override
    public void render(AbstractModule module) {

    }


    @Override
    public String toString() {
        return null;
    }
}
