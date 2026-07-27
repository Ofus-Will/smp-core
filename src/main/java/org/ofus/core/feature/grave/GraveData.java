package org.ofus.core.feature.grave;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public record GraveData(
        String id,
        UUID owner,
        String ownerName,
        Location location,
        List<ItemStack> items,
        int experience
) {
}