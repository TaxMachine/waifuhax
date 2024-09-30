package xyz.someboringnerd.waifuhax.systems.macro;

import lombok.experimental.UtilityClass;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.reflections.Reflections;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.systems.macro.luapi.FunctionData;
import xyz.someboringnerd.waifuhax.util.PathUtils;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

@UtilityClass
public class MacroInterpreter {

    private static final Globals luaInterpreter = JsePlatform.standardGlobals();
    private static String currentOutput = "";

    public static void init() {

        new Reflections("xyz.someboringnerd.waifuhax.systems.macro.luapi.functions")
                .getSubTypesOf(OneArgFunction.class)
                .forEach(function -> {
                    try {
                        luaInterpreter.set(function.getAnnotation(FunctionData.class).name(), function.getDeclaredConstructor().newInstance());
                        LogManager.print("Added %s (%s function) to library", function.getSimpleName(), function.getSuperclass().getSimpleName());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                if ((b == '\r' || b == '\n') && !currentOutput.equalsIgnoreCase("")) {
                    LogManager.printToChat(currentOutput);
                    currentOutput = "";
                } else {
                    currentOutput += (char) b;
                }
            }
        };

        luaInterpreter.STDOUT = new PrintStream(out);
    }

    public static void loadMacro(File file) {
        String code = PathUtils.readFileToString(file.getPath());
        LogManager.print("Loaded macro " + file.getPath());

        luaInterpreter.load(code).call();
    }

    public static void execute(String rawLua) {
        try {
            luaInterpreter.load(rawLua).call();
        } catch (Exception e) {
            LogManager.printToChat("An error has occurred while running script, check logs");
            e.printStackTrace();
        }
    }
}
