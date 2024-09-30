package xyz.someboringnerd.waifuhax.systems.macro.luapi.functions;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.systems.macro.luapi.FunctionData;

@FunctionData(name = "log")
public class LogFunction extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        LogManager.printToChat(arg.toString());
        return LuaValue.NIL;
    }
}
