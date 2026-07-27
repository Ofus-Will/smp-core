package org.ofus.core.feature.quickstack;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class QuickStacker {

    private QuickStacker() {
    }

    public static int stack(Player player, int radius) {
        List<Inventory> chests = findNearbyChests(player.getLocation(), radius);
        PlayerInventory playerInventory = player.getInventory();

        int movedItems = 0;
        int storageSize = playerInventory.getStorageContents().length;

        for (int slot = 0; slot < storageSize; slot++) {
            ItemStack item = playerInventory.getItem(slot);
            if (item == null || item.getType().isAir()) continue;

            int originalAmount = item.getAmount();
            int remainingAmount = originalAmount;

            for (Inventory chest : chests) {
                if (!containsSimilar(chest, item)) continue;

                ItemStack moving = item.clone();
                moving.setAmount(remainingAmount);

                Map<Integer, ItemStack> leftovers = chest.addItem(moving);
                remainingAmount = leftovers.values().stream()
                        .mapToInt(ItemStack::getAmount)
                        .sum();

                if (remainingAmount == 0) break;
            }

            if (remainingAmount == originalAmount) continue;

            movedItems += originalAmount - remainingAmount;

            if (remainingAmount == 0) {
                playerInventory.setItem(slot, null);
            } else {
                item.setAmount(remainingAmount);
                playerInventory.setItem(slot, item);
            }
        }

        return movedItems;
    }

    private static List<Inventory> findNearbyChests(Location centre, int radius) {
        World world = centre.getWorld();
        List<Inventory> chests = new ArrayList<>();
        Set<Inventory> found = Collections.newSetFromMap(new IdentityHashMap<>());

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location location = centre.clone().add(x, y, z);

                    if (!(world.getBlockAt(location).getState() instanceof Chest chest)) continue;

                    Inventory inventory = chest.getInventory();
                    if (found.add(inventory)) chests.add(inventory);
                }
            }
        }

        chests.sort(Comparator.comparingDouble(chest -> distanceSquared(chest, centre)));
        return chests;
    }

    private static boolean containsSimilar(Inventory inventory, ItemStack item) {
        return Arrays.stream(inventory.getStorageContents())
                .anyMatch(existing -> existing != null && existing.isSimilar(item));
    }

    private static double distanceSquared(Inventory inventory, Location centre) {
        Location location = inventory.getLocation();
        return location == null ? Double.MAX_VALUE : location.distanceSquared(centre);
    }
}