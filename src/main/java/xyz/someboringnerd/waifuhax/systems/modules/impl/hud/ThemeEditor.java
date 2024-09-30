package xyz.someboringnerd.waifuhax.systems.modules.impl.hud;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import lombok.SneakyThrows;
import xyz.someboringnerd.imutils.windows.ImguiMenu;
import xyz.someboringnerd.waifuhax.systems.modules.HUDSIDE;
import xyz.someboringnerd.waifuhax.systems.modules.HudElement;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.AutoDisable;
import xyz.someboringnerd.waifuhax.systems.modules.annotations.ListHidden;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AutoDisable
@ListHidden
public class ThemeEditor extends HudElement {

    private final Set<ImguiMenu> actionBar = new LinkedHashSet<>(), tabs = new LinkedHashSet<>();

    public ThemeEditor() {
        super("Theme Editor", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.MenuBar, true, HUDSIDE.ALWAYS_RENDER);

        actionBar.addAll(List.of(
                new ImguiMenu().setName("Files")
                        .addChild(
                                new ImguiMenu().setName("Load").setMenuItem(true).setCallback(args -> load()),
                                new ImguiMenu().setName("Save").setMenuItem(true).setCallback(args -> save()),
                                new ImguiMenu().setName("Save as").setMenuItem(true).setCallback(args -> saveAs()),
                                new ImguiMenu().setName("Load from").setMenuItem(true).setCallback(args -> loadFrom())
                        ),
                new ImguiMenu().setName("Options")
                        .addChild(
                                new ImguiMenu().setName("Reload available fonts").setMenuItem(true).setCallback(args -> refreshFonts())
                        )
        ));
    }

    public void load() {
        loadFromFile(new File("./WaifuHax/themes/default.json"));
    }

    public void loadFrom() {
    }

    @SneakyThrows
    private void loadFromFile(File file) {

    }

    private void refreshFonts() {

    }

    @SneakyThrows
    private void saveToFile(File file) {
        saveToFile(new File("./WaifuHax/themes/default.json"));
    }

    public void save() {
    }

    public void saveAs() {
    }

    @Override
    public void render() {
        if (ImGui.beginMenuBar()) {
            actionBar.forEach(ImguiMenu::draw);
            ImGui.endMenuBar();
        }
        ImGui.text("test");
    }
}
