package org.ofus.core.feature.pets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public final class PetFinder {

    private PetFinder() {
    }

    public static List<Tameable> findPets(Player player) {
        UUID owner = player.getUniqueId();

        return Bukkit.getWorlds().stream()
                .flatMap(world -> world.getEntitiesByClass(Tameable.class).stream())
                .filter(Tameable::isTamed)
                .filter(pet -> owner.equals(pet.getOwnerUniqueId()))
                .sorted(Comparator.comparing(pet -> pet.getType().name()))
                .toList();
    }
}