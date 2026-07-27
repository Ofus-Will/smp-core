package org.ofus.core.feature.pets;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Tameable;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PetManager {

    private final PetRepository repository;

    public PetManager(PetRepository repository) {
        this.repository = repository;
    }

    public List<PetData> getPets(Player player) {
        UUID owner = player.getUniqueId();
        Map<UUID, PetData> pets = new LinkedHashMap<>();

        for (PetData pet : repository.getByOwner(owner)) {
            pets.put(pet.id(), pet);
        }

        for (Tameable pet : findPets(owner)) {
            PetData data = save(pet);
            pets.put(data.id(), data);
        }

        return pets.values().stream()
                .sorted(Comparator.comparing(PetData::type))
                .toList();
    }

    public PetData save(Tameable pet) {
        PetData data = new PetData(
                pet.getUniqueId(),
                pet.getOwnerUniqueId(),
                pet.getType().name(),
                pet.getLocation()
        );

        repository.save(data);
        return data;
    }

    public boolean teleportToPlayer(Player player, UUID petId) {
        Tameable pet = getPet(player, petId);
        if (pet == null) return false;
        if (!pet.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN)) return false;

        save(pet);
        return true;
    }

    public boolean teleportPlayerToPet(Player player, UUID petId) {
        Tameable pet = getPet(player, petId);
        if (pet == null) return false;
        if (!player.teleport(pet, PlayerTeleportEvent.TeleportCause.PLUGIN)) return false;

        save(pet);
        return true;
    }

    private Tameable getPet(Player player, UUID petId) {
        PetData savedPet = repository.get(petId);
        if (savedPet == null || !savedPet.owner().equals(player.getUniqueId())) return null;

        Tameable pet = findLoadedPet(player, petId);
        return pet == null ? loadPet(player, savedPet) : pet;
    }

    private Tameable findLoadedPet(Player player, UUID petId) {
        Entity entity = player.getServer().getEntity(petId);
        if (!(entity instanceof Tameable pet) || !pet.isValid()) return null;
        if (!player.getUniqueId().equals(pet.getOwnerUniqueId())) return null;

        return pet;
    }

    private Tameable loadPet(Player player, PetData pet) {
        Location location = pet.location();
        if (location.getWorld() == null) return null;

        Chunk chunk = location.getChunk();
        if (!chunk.isLoaded() && !chunk.load()) return null;

        Tameable loadedPet = findLoadedPet(player, pet.id());
        if (loadedPet != null) return loadedPet;

        for (Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Tameable tameable)) continue;
            if (!entity.getUniqueId().equals(pet.id())) continue;
            if (!player.getUniqueId().equals(tameable.getOwnerUniqueId())) continue;

            return tameable;
        }

        return null;
    }

    public static List<Tameable> findPets(Player player) {
        return findPets(player.getUniqueId());
    }

    public static List<Tameable> findPets(UUID owner) {
        return Bukkit.getWorlds().stream()
                .flatMap(world -> world.getEntitiesByClass(Tameable.class).stream())
                .filter(Tameable::isTamed)
                .filter(pet -> owner.equals(pet.getOwnerUniqueId()))
                .sorted(Comparator.comparing(pet -> pet.getType().name()))
                .toList();
    }
}