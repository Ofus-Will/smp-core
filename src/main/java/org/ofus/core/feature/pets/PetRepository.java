package org.ofus.core.feature.pets;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.util.config.YamlConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PetRepository {

    private static final String ROOT = "pets";

    private final JavaPlugin plugin;
    private final YamlConfig config;

    public PetRepository(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = new YamlConfig(plugin, new File(plugin.getDataFolder(), "pets.yml"));
    }

    public void save(PetData pet) {
        String path = ROOT + "." + pet.id();

        config.set(path + ".owner", pet.owner().toString());
        config.set(path + ".type", pet.type());
        config.set(path + ".location", pet.location());
        config.save();
    }

    public PetData get(UUID id) {
        ConfigurationSection section = config.getSection(ROOT + "." + id);
        if (section == null) return null;

        return read(id.toString(), section);
    }

    public List<PetData> getByOwner(UUID owner) {
        ConfigurationSection root = config.getSection(ROOT);
        if (root == null) return List.of();

        List<PetData> pets = new ArrayList<>();

        for (String id : root.getKeys(false)) {
            ConfigurationSection section = root.getConfigurationSection(id);
            if (section == null) continue;

            PetData pet = read(id, section);
            if (pet != null && pet.owner().equals(owner)) pets.add(pet);
        }

        return pets;
    }

    private PetData read(String id, ConfigurationSection section) {
        String ownerValue = section.getString("owner");
        String type = section.getString("type");
        Location location = section.getLocation("location");

        if (ownerValue == null || type == null || location == null) return null;

        try {
            return new PetData(
                    UUID.fromString(id),
                    UUID.fromString(ownerValue),
                    type,
                    location
            );
        } catch (IllegalArgumentException exception) {
            plugin.getLogger().warning("Invalid pet data: " + id);
            return null;
        }
    }
}