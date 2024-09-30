package xyz.someboringnerd.waifuhax.systems.modules.impl.world;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import xyz.someboringnerd.waifuhax.events.TickEvent;
import xyz.someboringnerd.waifuhax.managers.LogManager;
import xyz.someboringnerd.waifuhax.managers.ModuleManager;
import xyz.someboringnerd.waifuhax.settings.BooleanSetting;
import xyz.someboringnerd.waifuhax.settings.IntegerSetting;
import xyz.someboringnerd.waifuhax.systems.modules.AbstractModule;
import xyz.someboringnerd.waifuhax.systems.modules.CATEGORY;
import xyz.someboringnerd.waifuhax.systems.modules.impl.exploits.AutoFrameDupe;
import xyz.someboringnerd.waifuhax.util.InventoryUtils;

public class TntHelper extends AbstractModule {

    public BooleanSetting silentPlace = new BooleanSetting("Silent Place", "Place the tnt without switching in your hotbar", false);
    public IntegerSetting placeRange = new IntegerSetting("Place Range", "Range at which tnts can be placed", 3, 0, 6);

    public BooleanSetting autoIgnite = new BooleanSetting("Auto Ignite", "Automatically ignite any tnt in range", true);
    public BooleanSetting silentIgnite = new BooleanSetting("Silent Ignite", "Silently ignite the tnts", true);
    public IntegerSetting igniteRange = new IntegerSetting("Auto Ignite Range", "Range of the auto-ignite in block", 3, 0, 6);

    public BooleanSetting autoRefill = new BooleanSetting("Auto Refill", "Automatically place tnt in your hotbar", true);

    public TntHelper() {
        super(CATEGORY.WORLD);
    }

    @EventHandler
    private void onTick(TickEvent event) {

        if (ModuleManager.getModule(AutoFrameDupe.class).isEnabled.getValue())
            return;

        // refill
        if (InventoryUtils.searchHotbar(itemStack -> itemStack.getItem().equals(Items.TNT)) == -1) {
            if (!attemptRefill()) {
                LogManager.printToChat("You ran out of tnt !");
                toggle();
            }
        }

        // place tnt


        // ignite tnt
    }

    private boolean attemptRefill() {
        int slot = InventoryUtils.searchNonHotbar(item -> item.getItem().equals(Items.TNT));

        if (slot == -1)
            return false;

        return InventoryUtils.refill(item -> item.getItem().equals(Items.TNT));
    }

    @Override
    public String getDescription() {
        return "Place tnt efficiently for griefing purpose";
    }
}
