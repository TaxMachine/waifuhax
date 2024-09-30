package xyz.someboringnerd.waifuhax.systems.modules.impl.misc;

import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

public class CoordinateSpoofer extends AbstractModule {
    public CoordinateSpoofer() {
        super(CATEGORY.MISC);
    }

    @Override
    public String getDescription() {
        return "Do literally nothing, but can fool people into thinking it's not your actual coordinates if you leak them by accident";
    }
}
