package xyz.someboringnerd.waifuhax.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class InventoryUtils {

    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final int OFF_HAND = 40;
    public static final List<Integer> ARMOR = List.of(36, 37, 38, 39);
    public static final List<Integer> SPECIAL_SLOTS = List.of(36, 37, 38, 39, 40); // armor + offhand
    private static int lastSlot = -1;

    public static Pair<Item, Integer> searchMost(Item[] items, Inventory inv, List<Integer> avoid) {
        Map<Item, Integer> map = new HashMap<>();
        for (int i = 0; i < inv.size(); i++) {
            if (avoid.contains(i)) continue;
            for (Item item : items) {
                ItemStack stack = inv.getStack(i);
                if (stack.getItem() == item) map.put(item, map.getOrDefault(item, 0) + stack.getCount());
            }
        }
        Pair<Item, Integer> best = new Pair<>(Items.ACACIA_BOAT, 0);
        map.forEach((item, amount) -> {
            if (amount >= best.getRight()) {
                best.setLeft(item);
                best.setRight(amount);
            }
        });
        return best;
    }

    public static int count(Function<ItemStack, Boolean> valid) {
        return count(valid, mc.player.getInventory());
    }

    public static int count(Function<ItemStack, Boolean> valid, List<Integer> avoid) {
        return count(valid, mc.player.getInventory(), avoid);
    }

    public static int count(Function<ItemStack, Boolean> valid, Inventory inv) {
        return count(valid, inv, List.of());
    }

    public static int count(Function<ItemStack, Boolean> valid, Inventory inv, List<Integer> avoid) {
        int amount = 0;
        for (int i = 0; i < inv.size(); i++) {
            if (!avoid.contains(i) && valid.apply(inv.getStack(i))) amount++;
        }
        return amount;
    }

    public static int search(Function<ItemStack, Boolean> valid) {
        return search(valid, mc.player.getInventory());
    }

    public static int search(Function<ItemStack, Boolean> valid, Inventory inv) {
        return search(valid, inv, 0, -1, List.of());
    }

    public static int search(Function<ItemStack, Boolean> valid, int start, int end) {
        return search(valid, mc.player.getInventory(), start, end, List.of());
    }

    public static int search(Function<ItemStack, Boolean> valid, Inventory inv, int start, int end, List<Integer> avoid) {
        int i = start;
        int size = end == -1 ? inv.size() : end;
        while (i < size && (avoid.contains(i) || !valid.apply(inv.getStack(i)))) i++;
        return i == size ? -1 : i;
    }

    public static List<Integer> searchAll(Function<ItemStack, Boolean> valid) {
        return searchAll(valid, mc.player.getInventory(), Integer.MAX_VALUE);
    }

    public static List<Integer> searchAll(Function<ItemStack, Boolean> valid, int max) {
        return searchAll(valid, mc.player.getInventory(), max);
    }

    public static List<Integer> searchAll(Function<ItemStack, Boolean> valid, int max, List<Integer> avoid) {
        return searchAll(valid, mc.player.getInventory(), 0, -1, max, avoid);
    }

    public static List<Integer> searchAll(Function<ItemStack, Boolean> valid, Inventory inv, int max) {
        return searchAll(valid, inv, 0, -1, max, List.of());
    }

    public static List<Integer> searchAll(Function<ItemStack, Boolean> valid, int start, int end, int max) {
        return searchAll(valid, mc.player.getInventory(), start, end, max, List.of());
    }

    public static List<Integer> searchAll(Function<ItemStack, Boolean> valid, Inventory inv, int start, int end, int max, List<Integer> avoid) {
        if (end == -1) end = inv.size();
        List<Integer> slots = new ArrayList<>();
        for (int i = start; i < end && slots.size() <= max; i++) {
            if (!avoid.contains(i) && valid.apply(inv.getStack(i))) slots.add(i);
        }
        return slots;
    }

    public static List<Integer> searchAll(Function<ItemStack, Boolean> valid, ScreenHandler screenHandler, int start, int end, int max, List<Integer> avoid) {
        if (end == -1) end = screenHandler.slots.size();
        List<Integer> slots = new ArrayList<>();
        for (int i = start; i < end && slots.size() <= max; i++) {
            if (!avoid.contains(i) && valid.apply(screenHandler.getSlot(i).getStack())) slots.add(i);
        }
        return slots;
    }

    public static int searchHotbar(Function<ItemStack, Boolean> valid) {
        return search(valid, 0, 9);
    }

    public static int searchNonHotbar(Function<ItemStack, Boolean> valid) {
        return search(valid, 9, -1);
    }

    public static void selectBack() {
        selectSlot(lastSlot);
    }

    public static void selectSlot(int slot) {
        selectSlot(slot, true);
    }

    public static void selectSlot(int slot, boolean setLastSlot) {
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        if (setLastSlot) lastSlot = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = slot;
    }

    public static boolean refill(Function<ItemStack, Boolean> valid) {
        int to = searchHotbar(ItemStack::isEmpty);
        if (to == -1) return false;
        return refill(valid, to);
    }

    public static boolean refill(Function<ItemStack, Boolean> valid, int to) {
        int slot = searchNonHotbar(valid);
        if (slot == -1) return false;
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                slot, to, SlotActionType.SWAP, mc.player);
        return true;
    }

}

