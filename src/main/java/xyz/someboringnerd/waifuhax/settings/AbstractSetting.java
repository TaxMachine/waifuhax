package xyz.someboringnerd.waifuhax.settings;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractSetting<T> {

    @Getter
    @Setter
    private String name, description;

    public String getDisplayName() {
        return name.split(" ")[0];
    }

    @Getter
    @Setter
    private T value;

    public Runnable onModified = null;

    protected final List<String> aliases = new ArrayList<>();

    public List<String> getAliases() {
        return new ArrayList<String>(aliases);
    }

    @Getter
    @Setter
    private boolean shouldDraw = true;


    public AbstractSetting(String name, String description, T defaultValue, Runnable onModified, String... aliases) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;
        this.aliases.addAll(List.of(aliases));
        this.aliases.add(getDisplayName());
        this.onModified = onModified;
    }

    public boolean matchAnyAliases(String optionName) {

        AtomicBoolean found = new AtomicBoolean(false);

        aliases.forEach(alias -> {
            if (alias.equalsIgnoreCase(optionName))
                found.set(true);
        });

        return found.get();
    }

    @Nullable
    public abstract T fromString(String string);

    public void save(JSONObject saveData) {
        saveData.put(name, value);
    }

    public abstract void load(JSONObject savedSettings);

    public abstract void render(AbstractModule module);
}
