package xyz.someboringnerd.waifuhax.systems.macro.luapi.functions;

import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import xyz.someboringnerd.waifuhax.systems.macro.luapi.FunctionData;

@FunctionData(name = "say")
public class SayFunction extends OneArgFunction {
    @Override
    public LuaValue call(LuaValue arg) {
        MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(arg.toString());
        return LuaValue.NIL;
    }
}
