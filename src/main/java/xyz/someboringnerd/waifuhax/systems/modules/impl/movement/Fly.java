package xyz.someboringnerd.waifuhax.systems.modules.impl.movement;

import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

public class Fly extends AbstractModule {
    public Fly() {
        super(CATEGORY.MOVEMENT);
    }

    @Override
    public String getDescription() {
        return "Fly module for servers with no anticheat";
    }
}
