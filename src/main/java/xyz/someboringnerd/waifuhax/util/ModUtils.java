package xyz.someboringnerd.waifuhax.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;

import java.util.List;

public class ModUtils {
    public static List<ModMetadata> getLoadedMods() {
        return FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).toList();
    }
}
