package xyz.someboringnerd.waifuhax.systems.modules.impl.misc;

import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

public class AntiSpam extends AbstractModule {
    public AntiSpam() {
        super(CATEGORY.MISC);
    }

    @Override
    public String getDescription() {
        return "Advanced spam filter";
    }
}
