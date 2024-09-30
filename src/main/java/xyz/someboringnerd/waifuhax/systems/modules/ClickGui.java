package xyz.someboringnerd.waifuhax.systems.modules;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import xyz.someboringnerd.waifuhax.managers.ModuleManager;
import xyz.someboringnerd.waifuhax.settings.StringSetting;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.Hidden;
import xyz.someboringnerd.waifuhax.util.GlobalOptions;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ClickGui extends HudElement {

    private final Set<AbstractModule> mods = new LinkedHashSet<>();

    private final CATEGORY category;

    public ClickGui(CATEGORY category) {
        super(category.getDisplayName(), ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoResize, true, HUDSIDE.CLICK_GUI);
        mods.addAll(ModuleManager.getModulesOfCategory(category));
        this.category = category;
    }

    @Override
    public String getName() {
        return category.name();
    }

    public boolean isEmpty() {
        return mods.isEmpty();
    }

    @Override
    public void render() {
        if ((isEmpty() && !GlobalOptions.displayEmptyCategories.getValue()) && category.isActive()) {
            category.setActive(false);
            active.set(false);
            return;
        }

        mods.forEach(module -> {
            if (module.getClass().getAnnotation(Hidden.class) == null) {
                AtomicInteger size = new AtomicInteger(0);
                if (category != CATEGORY.HUD || (module instanceof HudElement && ((HudElement) module).getHudside().equals(HUDSIDE.HUD_ELEMENT))) {
                    if (ImGui.collapsingHeader(module.getName())) {
                        module.getSettings().forEach(setting -> {
                            if (!(setting instanceof StringSetting)) {
                                setting.render(module);
                                if (ImGui.isItemHovered()) {
                                    ImGui.setTooltip(setting.getDescription());
                                }
                            }
                        });
                    }
                    if (ImGui.isItemHovered()) {
                        ImGui.setTooltip(module.getDescription());
                    }
                    if (size.get() < mods.size() && size.get() != 0) {
                        ImGui.separator();
                    }
                    size.set(size.get() + 1);
                }
            }
        });
    }
}
