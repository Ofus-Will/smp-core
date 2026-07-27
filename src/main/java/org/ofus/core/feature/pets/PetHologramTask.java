package org.ofus.core.feature.pets;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.ofus.core.feature.hologram.Hologram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PetHologramTask {

    private static final PlainTextComponentSerializer PLAIN_TEXT = PlainTextComponentSerializer.plainText();
    private static final long INTERVAL_TICKS = 5L;
    private static final double HEIGHT_OFFSET = -0.5;
    private static final String HEALTH_SYMBOL = "\u25A0";

    private final JavaPlugin plugin;
    private final int radius;
    private final Map<UUID, Hologram> holograms = new HashMap<>();

    private BukkitTask task;

    public PetHologramTask(JavaPlugin plugin, int radius) {
        this.plugin = plugin;
        this.radius = radius;
    }

    public void start() {
        if (radius <= 0 || task != null) return;

        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 1L, INTERVAL_TICKS);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        removeAll();
    }

    private void tick() {
        Set<UUID> visiblePets = new HashSet<>();

        for (Tameable pet : findPets()) {
            if (!(pet instanceof LivingEntity livingPet)) continue;

            visiblePets.add(pet.getUniqueId());
            updateHologram(pet, livingPet);
        }

        holograms.entrySet().removeIf(entry -> {
            if (visiblePets.contains(entry.getKey())) return false;

            entry.getValue().remove();
            return true;
        });
    }

    private Set<Tameable> findPets() {
        Set<Tameable> pets = new HashSet<>();

        for (World world : Bukkit.getWorlds()) {
            world.getEntitiesByClass(Tameable.class).stream()
                    .filter(Tameable::isTamed)
                    .filter(pet -> pet.getOwnerUniqueId() != null)
                    .forEach(pets::add);
        }

        return pets;
    }

    private void updateHologram(Tameable pet, LivingEntity livingPet) {
        Hologram hologram = holograms.computeIfAbsent(pet.getUniqueId(), id -> createHologram(pet, livingPet));

        hologram.update(formatName(pet, livingPet), formatHealth(livingPet));
        hologram.mount(livingPet, livingPet.getHeight() + HEIGHT_OFFSET);
    }

    private Hologram createHologram(Tameable pet, LivingEntity livingPet) {
        Hologram hologram = new Hologram(livingPet.getLocation(), formatName(pet, livingPet), formatHealth(livingPet));
        hologram.mount(livingPet, livingPet.getHeight() + HEIGHT_OFFSET);
        return hologram;
    }

    private String formatName(Tameable pet, LivingEntity livingPet) {
        Component customName = livingPet.customName();
        if (customName != null) return "&f" + PLAIN_TEXT.serialize(customName);

        return "&f" + getOwnerName(pet) + "'s " + formatType(livingPet);
    }

    private String getOwnerName(Tameable pet) {
        UUID owner = pet.getOwnerUniqueId();
        if (owner == null) return "Unknown";

        String name = Bukkit.getOfflinePlayer(owner).getName();
        return name == null ? "Unknown" : name;
    }

    private String formatType(LivingEntity pet) {
        return pet.getType().name().toLowerCase(Locale.ROOT).replace('_', ' ');
    }

    private String formatHealth(LivingEntity pet) {
        int filled = (int) Math.ceil((pet.getHealth() / getMaxHealth(pet)) * 10);
        filled = Math.max(0, Math.min(10, filled));

        return "&a" + HEALTH_SYMBOL.repeat(filled) + "&c" + HEALTH_SYMBOL.repeat(10 - filled);
    }

    private double getMaxHealth(LivingEntity pet) {
        AttributeInstance attribute = pet.getAttribute(Attribute.MAX_HEALTH);
        return attribute == null ? pet.getHealth() : attribute.getValue();
    }

    private void removeAll() {
        holograms.values().forEach(Hologram::remove);
        holograms.clear();
    }
}