package xyz.someboringnerd.waifuhax.managers;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import lombok.SneakyThrows;
import net.minecraft.command.CommandSource;
import org.reflections.Reflections;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.Hidden;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.ListHidden;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.NoCommand;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ModuleManager {

    private static final Set<AbstractModule> moduleSet = new HashSet<>();

    @SneakyThrows
    public static void init() {
        AtomicReference<File> file = new AtomicReference<>(new File("WaifuHax"));

        if (!file.get().exists())
            file.get().mkdir();

        Arrays.stream(CATEGORY.values()).toList().forEach(cat -> {
            file.set(new File("WaifuHax/" + cat.name()));
            if (!file.get().exists())
                file.get().mkdir();
        });
        AtomicInteger count = new AtomicInteger(0);
        new Reflections("xyz.someboringnerd.waifuhax.systems.modules.impl")
                .getSubTypesOf(AbstractModule.class)
                .stream()
                .filter(mod -> !Modifier.isAbstract(mod.getModifiers()))
                .forEach(mod -> {
                    try {
                        AbstractModule module = null;
                        module = mod.getDeclaredConstructor().newInstance();
                        moduleSet.add(module);
                        module.init();
                        // Hack : fix an issue where settings with the same name fail to work properly
                        module.isEnabled.setName(module.isEnabled.getName() + " ##" + module.getName());
                        if (!mod.isAnnotationPresent(NoCommand.class))
                            registerCommandToDispatcher(module);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    private static void registerCommandToDispatcher(AbstractModule cmd) {
        LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.literal(cmd.getName().toLowerCase());
        cmd.build(builder);
        CommandManager.getInstance().dispatcher.register(builder);
    }

    public static Set<AbstractModule> getModulesOfCategory(CATEGORY cat) {
        return moduleSet.stream()
                .filter(mod -> mod.getCategory().equals(cat) && mod.getClass().getAnnotation(Hidden.class) == null)
                .sorted(Comparator.comparing(AbstractModule::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static AbstractModule getModuleByName(String name) {
        return moduleSet.stream()
                .filter(mod -> mod.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static <T extends AbstractModule> T getModule(Class<T> moduleClass) {
        return moduleSet.stream()
                .filter(mod -> mod.getClass().equals(moduleClass))
                .map(moduleClass::cast)
                .findFirst()
                .orElse(null);
    }

    public static AbstractModule getModule(String moduleClass) {
        return moduleSet.stream()
                .filter(mod -> mod.getClass().getSimpleName().equalsIgnoreCase(moduleClass))
                .findFirst()
                .orElse(null);
    }

    public static List<AbstractModule> getEnabledModules() {
        return moduleSet.stream()
                .filter(mod -> mod.isEnabled.getValue() && mod.getClass().getAnnotation(ListHidden.class) == null)
                .sorted(Comparator.comparing(AbstractModule::getName))
                .collect(Collectors.toList());
    }

    public static List<AbstractModule> getModules() {
        return new ArrayList<>(moduleSet);
    }

    public static void onKey(int key) {
        moduleSet.forEach(mod -> {
            if (mod.keycode.getValue().equals(key)) {
                mod.toggle();
                mod.save();
            }
        });
    }

    public static void saveAll() {
        moduleSet.forEach(AbstractModule::save);
    }

    public static void loadAll() {
        moduleSet.forEach(mod -> mod.init(true));
    }

}
