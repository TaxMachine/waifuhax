package xyz.someboringnerd.waifuhax.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.settings.AbstractSetting;
import xyz.someboringnerd.waifuhax.settings.BooleanSetting;
import xyz.someboringnerd.waifuhax.settings.IntegerSetting;
import xyz.someboringnerd.waifuhax.settings.StringSetting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalOptions {

    public static BooleanSetting displayEmptyCategories = new BooleanSetting("Display Empty Categories", "", false);
    public static BooleanSetting printToggleMessage = new BooleanSetting("Print Toggle Message", "", true);
    public static BooleanSetting showClickguiOutsideOfScreen = new BooleanSetting("Show Clickgui outside of screen", "", false);
    public static BooleanSetting syncFriendlistContinuously = new BooleanSetting("Sync Friendlist", "Sync your friendlist accross multiple clients", false);

    @Getter(AccessLevel.PUBLIC)
    private static GlobalOptions instance;

    public GlobalOptions() {
        instance = this;
        File file = new File(PathUtils.join(".", "WaifuHax", "clickgui.json"));

        if (file.exists())
            load(PathUtils.join(".", "WaifuHax", "clickgui.json"));
        else
            save(PathUtils.join(".", "WaifuHax", "clickgui.json"));
    }

    public static void save() {
        instance.save(PathUtils.join(".", "WaifuHax", "clickgui.json"));
    }

    private String readFileToString(String filePath) {
        Path path = Paths.get(filePath);

        try {
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AbstractSetting> getSettings() {
        List<AbstractSetting> tmp = new ArrayList<>();

        Arrays.stream(this.getClass().getDeclaredFields()).forEach(field -> {
            if (AbstractSetting.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true); // Make the field accessible, especially if it is private
                    tmp.add((AbstractSetting) field.get(this));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return tmp;
    }

    private void load(String path) {
        JSONObject savedSettings = new JSONObject(readFileToString(path));

        getSettings().forEach(setting -> {
            if (setting instanceof IntegerSetting integerSetting) {
                if (savedSettings.has(setting.getName())) {
                    integerSetting.setValue(savedSettings.getInt(setting.getName()));
                    LogManager.print("%s was set to %s", setting.getName(), String.valueOf(setting.getValue()));
                }
            } else if (setting instanceof BooleanSetting booleanSetting) {
                if (savedSettings.has(setting.getName())) {
                    booleanSetting.setValue(savedSettings.getBoolean(setting.getName()));
                    LogManager.print("%s was set to %s", setting.getName(), String.valueOf(setting.getValue()));
                }
            } else if (setting instanceof StringSetting stringSetting) {
                if (savedSettings.has(setting.getName())) {
                    stringSetting.setValue(savedSettings.getString(setting.getName()));
                    LogManager.print("%s was set to %s", setting.getName(), String.valueOf(setting.getValue()));
                }
            }
        });
    }

    @SneakyThrows
    public void save(String path) {
        JSONObject saveData = new JSONObject();

        getSettings().forEach(setting -> {
            if (setting instanceof IntegerSetting integerSetting) {
                saveData.put(setting.getName(), integerSetting.getValue());
            } else if (setting instanceof BooleanSetting booleanSetting) {
                saveData.put(setting.getName(), booleanSetting.getValue());
            } else if (setting instanceof StringSetting stringSetting) {
                saveData.put(setting.getName(), stringSetting.getValue());
            }
        });

        File file = new File(path);

        if (!file.exists())
            file.createNewFile();

        FileWriter writer = new FileWriter(path);
        writer.write(saveData.toString(4));
        writer.close();
    }

}
