package org.ofus.core.feature.pets;

import org.bukkit.Location;

import java.util.UUID;

public record PetData(
        UUID id,
        UUID owner,
        String type,
        Location location
) {
}