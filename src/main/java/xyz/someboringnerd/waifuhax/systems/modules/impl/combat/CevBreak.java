package xyz.someboringnerd.waifuhax.systems.modules.impl.combat;

import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;

public class CevBreak extends AbstractModule {

    public CevBreak() {
        super(CATEGORY.COMBAT);
    }

    @Override
    public String getDescription() {
        return "Make campers leave their safe hole";
    }
}
