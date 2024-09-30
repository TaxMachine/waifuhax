package xyz.someboringnerd.waifuhax;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import xyz.someboringnerd.imutils.managers.ImguiManager;
import xyz.someboringnerd.waifuhax.managers.CommandManager;
import xyz.someboringnerd.waifuhax.managers.FriendManager;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.managers.ModuleManager;
import xyz.someboringnerd.waifuhax.systems.macro.MacroInterpreter;
import xyz.someboringnerd.waifuhax.util.GlobalOptions;

import java.lang.invoke.MethodHandles;

public class WaifuHax implements ClientModInitializer {

    public static final IEventBus EVENT_BUS = new EventBus();

    public static ModMetadata MOD_META;
    public static String VERSION;

    @Override
    public void onInitializeClient() {

        MOD_META = FabricLoader.getInstance().getModContainer("waifuhax").orElseThrow().getMetadata();
        VERSION = MOD_META.getVersion().getFriendlyString();

        EVENT_BUS.registerLambdaFactory(this.getClass().getPackage().getName(), (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        EVENT_BUS.subscribe(this);

        ImguiManager.setBeforeRenderingFrame(args -> {
            ImGui.getStyle().setWindowMinSize(250, 150);
            ImGui.getIO().addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
            ImGui.getIO().setConfigWindowsMoveFromTitleBarOnly(true);
        });

        MacroInterpreter.init();
        LogManager.print("BY THIS POINTS, LUA FUNCTIONS SHOULD BE REGISTERED !!!!");
        CommandManager.init();
        ModuleManager.init();
        new GlobalOptions();

        FriendManager.loadFriendList();
    }

}
