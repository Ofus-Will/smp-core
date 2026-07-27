package org.ofus.core.feature.chestsort;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;

public final class ChestSorter {

    private ChestSorter() {}

    public static void sort(Inventory inventory) {
        ItemStack[] items = Arrays.stream(inventory.getStorageContents())
                .filter(item -> item != null && !item.getType().isAir())
                .map(ItemStack::clone)
                .sorted(Comparator.comparing(ItemStack::getType))
                .toArray(ItemStack[]::new);

        inventory.clear();
        inventory.addItem(items);
    }
}