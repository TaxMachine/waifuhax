package xyz.someboringnerd.waifuhax.systems.modules;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import org.json.JSONObject;
import xyz.someboringnerd.waifuhax.WaifuHax;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.settings.*;
import xyz.someboringnerd.waifuhax.systems.commands.arguments.AnyArgumentType;
import xyz.someboringnerd.waifuhax.systems.commands.arguments.SettingArgumentType;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.AutoDisable;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.AutoEnable;
import xyz.someboringnerd.waifuhax.util.GlobalOptions;
import xyz.someboringnerd.waifuhax.util.PathUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor()
public abstract class AbstractModule {

    @Getter(AccessLevel.PUBLIC)
    private final CATEGORY category;

    @Getter(AccessLevel.PUBLIC)
    private final String name = this.getClass().getSimpleName();

    private String path;// = PathUtils.join("WaifuHax", category.name(), getName() + ".json");

    public final BooleanSetting isEnabled = new BooleanSetting("Enabled", "", false, this::onToggle, "e", "isenabled");
    public final KeybindSetting keycode = new KeybindSetting("Module Key", "Toggle button.", -1, -1, Integer.MAX_VALUE);

    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    @Setter(AccessLevel.PUBLIC)
    protected static ClientPlayerEntity player;

    public List<AbstractSetting> getSettings() {
        List<AbstractSetting> tmp = new ArrayList<>();

        Arrays.stream(this.getClass().getFields()).forEach(field -> {
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

    /**
     * 2024-06-14, 18:04 : Likely capable of causing a crash, we'll see how it goes.
     * edit 2024-06-14, 19:03 : Why did I think it was a good idea for FUCK’S SAKE
     * edit 2024-06-14, 20:29 : I'm a fucking genius
     *
     * @param builder : command builder
     */
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("setting", SettingArgumentType.from(getSettings()))
                .then(argument("newValue", AnyArgumentType.get())
                        .executes(ctx -> {
                            AbstractSetting set = SettingArgumentType.getSetting(ctx);
                            String nv = ctx.getArgument("newValue", String.class);

                            boolean failed = false, resetFlag = false;

                            try {
                                if (set instanceof BooleanSetting booleanSetting) {
                                    booleanSetting.setValue(isTrue(nv));
                                    if (booleanSetting.getDisplayName().equalsIgnoreCase("enabled")) resetFlag = true;
                                } else if (set instanceof FloatSetting floatSetting) {
                                    floatSetting.setValue(Float.parseFloat(nv));
                                } else if (set instanceof IntegerSetting integerSetting) {
                                    integerSetting.setValue(Integer.valueOf(nv));
                                } else if (set instanceof LongSetting longSetting) {
                                    longSetting.setValue(Long.valueOf(nv));
                                } else if (set instanceof StringSetting stringSetting) {
                                    stringSetting.setValue(nv);
                                } else if (set instanceof EnumSetting enumSetting) {
                                    enumSetting.fromString(nv);
                                } else {
                                    LogManager.printToChat("Type %s has not been implemented yet", set.getValue().getClass().getSimpleName());
                                    failed = true;
                                }
                            } catch (NumberFormatException e) {
                                LogManager.printToChat("An error has occurred due to a miss-input : \n%s", e.getMessage());
                                failed = true;
                            }

                            if (!failed) {
                                LogManager.printToChat("§7§o%s.%s = %s", getName().toLowerCase(), set.getDisplayName().toLowerCase(), nv.toLowerCase());

                                if (resetFlag) {
                                    if (this instanceof HudElement hlement) {
                                        hlement.toggle(!isTrue(nv));
                                    } else {
                                        onToggle(true);
                                    }
                                }

                                save();
                            }
                            return 1;
                        })
                )
        );
    }

    private static boolean isTrue(String input) {
        return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("1");
    }

    public void init() {
        init(false);
    }

    /**
     * In charge of loading module settings
     */
    public void init(boolean live) {

        path = PathUtils.join("WaifuHax", category.name(), getName() + ".json");

        File file = new File(path);

        if (file.exists()) {
            load(live);
        } else {
            save();
        }

        if (this.getClass().isAnnotationPresent(AutoDisable.class)) {
            isEnabled.setValue(false);
            save();
            onToggle(true);
        }

        if (this.getClass().isAnnotationPresent(AutoEnable.class)) {
            if (!isEnabled.getValue()) {
                isEnabled.setValue(true);
                save();
                onToggle(true);
            }
            isEnabled.setShouldDraw(false);
        }
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

    public void load() {
        load(false);
    }

    private void load(boolean live) {
        JSONObject savedSettings = new JSONObject(readFileToString(path));
        isEnabled.setValue(savedSettings.has("Enabled") && savedSettings.getBoolean("Enabled"));
        keycode.setValue(savedSettings.has("keycode") ? savedSettings.getInt("keycode") : -1);
        WaifuHax.EVENT_BUS.unsubscribe(this);
        onToggle(live);

        getSettings().forEach(setting -> setting.load(savedSettings));
    }

    @SneakyThrows
    public void save() {
        JSONObject saveData = new JSONObject();

        saveData.put("Enabled", isEnabled.getValue());
        saveData.put("keycode", keycode.getValue());

        getSettings().forEach(abstractSetting -> abstractSetting.save(saveData));

        File file = new File(path);

        if (!file.exists())
            file.createNewFile();

        FileWriter writer = new FileWriter(path);
        writer.write(saveData.toString(4));
        writer.close();
    }

    public void onToggle() {
        onToggle(false);
    }

    public void onToggle(boolean live) {
        if (isEnabled.getValue()) {
            WaifuHax.EVENT_BUS.subscribe(this);
            onActivate(live);
            if (!live && GlobalOptions.printToggleMessage.getValue() && mc.world != null)
                LogManager.printToChat("%s was §2enabled", getName());
        } else {
            WaifuHax.EVENT_BUS.unsubscribe(this);
            onDeactivate(live);
            if (!live && GlobalOptions.printToggleMessage.getValue() && mc.world != null)
                LogManager.printToChat("%s was §4disabled", getName());
        }
    }

    public void onActivate(boolean live) {
    }

    public void onDeactivate(boolean live) {
    }

    public void toggle() {
        isEnabled.setValue(!isEnabled.getValue());
        onToggle();
    }

    public abstract String getDescription();


    // yes, this is pasted from Meteor
    // Helper methods to painlessly infer the CommandSource generic type argument
    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(final String name) {
        return LiteralArgumentBuilder.literal(name);

    }
}