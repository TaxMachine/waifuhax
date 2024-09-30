package xyz.someboringnerd.waifuhax.systems.modules.impl.hud;


import imgui.ImGui;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import xyz.someboringnerd.imutils.windows.ImguiMenu;
import xyz.someboringnerd.waifuhax.events.RenderImGuiEvent;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.managers.ModuleManager;
import xyz.someboringnerd.waifuhax.screens.ClickGuiScreen;
import xyz.someboringnerd.waifuhax.screens.HudEditor;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.AutoEnable;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.Hidden;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.ListHidden;
import xyz.someboringnerd.waifuhax.util.GlobalOptions;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// This one is a special hud module.
@AutoEnable
@ListHidden
@Hidden
public class TopMenuBar extends AbstractModule {

    private final Set<ImguiMenu> tabs = new LinkedHashSet<>();

    private final ImguiMenu showEmptyCategoriesSetting = new ImguiMenu().setName("Show empty categories " + (GlobalOptions.displayEmptyCategories.getValue() ? "[V]" : "[X]"));
    private final ImguiMenu chooseHiddenCategory = new ImguiMenu().setName("Hide Category");

    public TopMenuBar() {
        super(CATEGORY.HUD);

        showEmptyCategoriesSetting.setMenuItem(true)
                .setCallback(args -> {
                    showEmptyCategoriesSetting.setName("Show empty categories " + (GlobalOptions.displayEmptyCategories.getValue() ? "[V]" : "[X]"));
                    GlobalOptions.displayEmptyCategories.setValue(true);
                    GlobalOptions.save();
                });

        Arrays.stream(CATEGORY.values()).forEach(category -> {
            if (!category.equals(CATEGORY.HUD)) {
                ImguiMenu temp = new ImguiMenu().setName(category.getDisplayName() + (category.isActive() ? " [V]" : " [X]")).setMenuItem(true);
                chooseHiddenCategory.addChild(temp.setCallback(args -> {
                    category.setActive(!category.isActive());
                    temp.setName(category.getDisplayName() + (category.isActive() ? " [V]" : " [X]"));
                }));
            }
        });

        tabs.addAll(List.of(
                new ImguiMenu().setName("Modules").addChild(
                        new ImguiMenu().setName("Reload all").setMenuItem(true).setCallback(args -> ModuleManager.loadAll()),
                        new ImguiMenu().setName("Save all").setMenuItem(true).setCallback(args -> ModuleManager.saveAll())
                ),
                new ImguiMenu().setName("Systems").addChild(
                        new ImguiMenu().setName("Lua macros").setMenuItem(true).setCallback(args -> LogManager.printToChat("Work In Progress")),
                        new ImguiMenu().setName("Wasp System").setMenuItem(true).setCallback(args -> LogManager.printToChat("Work In Progress"))
                ),
                new ImguiMenu().setName("Friend").addChild(
                        new ImguiMenu().setName("Friend Management UI").setMenuItem(true).setCallback(args -> LogManager.printToChat("Work In Progress")),
                        new ImguiMenu().setName("Sync accross all client").setMenuItem(true).setCallback(args -> LogManager.printToChat("Work In Progress")),
                        new ImguiMenu().setName("Load from loaded clients").setMenuItem(true).setCallback(args -> LogManager.printToChat("Work In Progress")),
                        new ImguiMenu().setName("Settings").setMenuItem(true).setCallback(args -> LogManager.printToChat("Work In Progress"))
                ),
                new ImguiMenu().setName("HUD").addChild(
                        new ImguiMenu().setName("Open Click GUI").setMenuItem(true).setCallback(args -> MinecraftClient.getInstance().setScreen(new ClickGuiScreen())),
                        new ImguiMenu().setName("Open Theme Editor").setMenuItem(true).setCallback(args -> ModuleManager.getModule(ThemeEditor.class).toggle()),
                        new ImguiMenu().setName("Open Hud Editor").setMenuItem(true).setCallback(args -> MinecraftClient.getInstance().setScreen(new HudEditor())),
                        new ImguiMenu().setName("settings").setMenuItem(true).setCallback(args -> LogManager.printToChat("Work In Progress")),
                        showEmptyCategoriesSetting,
                        chooseHiddenCategory
                )
        ));
    }

    @Override
    public String getDescription() {
        return "";
    }

    @EventHandler
    public void onImguiRender(RenderImGuiEvent event) {
        if (ImGui.beginMainMenuBar()) {
            tabs.forEach(ImguiMenu::draw);
            ImGui.endMainMenuBar();
        }
    }
}
