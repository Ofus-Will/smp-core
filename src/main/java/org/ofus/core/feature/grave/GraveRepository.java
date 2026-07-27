package org.ofus.core.feature.grave;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.util.config.YamlConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GraveRepository {

    private final JavaPlugin plugin;
    private final YamlConfig config;

    public GraveRepository(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = new YamlConfig(plugin, new File(plugin.getDataFolder(), "graves.yml"));
    }

    public void save(GraveGUI grave) {
        String path = "graves." + grave.getId();

        List<ItemStack> items = Arrays.stream(grave.getInventory().getStorageContents())
                .filter(Objects::nonNull)
                .filter(item -> !item.getType().isAir())
                .map(ItemStack::clone)
                .toList();

        config.set(path + ".owner", grave.getOwner().toString());
        config.set(path + ".ownerName", grave.getOwnerName());
        config.set(path + ".location", grave.getLocation());
        config.set(path + ".experience", grave.getExperience());
        config.set(path + ".items", items);
        config.save();
    }

    public List<GraveData> getAll() {
        ConfigurationSection root = config.getSection("graves");
        if (root == null) return List.of();

        List<GraveData> graves = new ArrayList<>();

        for (String id : root.getKeys(false)) {
            ConfigurationSection section = root.getConfigurationSection(id);
            if (section == null) continue;

            String ownerValue = section.getString("owner");
            Location location = section.getLocation("location");

            if (ownerValue == null || location == null) continue;

            try {
                UUID owner = UUID.fromString(ownerValue);
                String ownerName = section.getString("ownerName", "Unknown");
                int experience = section.getInt("experience");

                List<?> savedItems = section.getList("items");
                if (savedItems == null) savedItems = List.of();

                List<ItemStack> items = savedItems.stream()
                        .filter(ItemStack.class::isInstance)
                        .map(ItemStack.class::cast)
                        .map(ItemStack::clone)
                        .toList();

                graves.add(new GraveData(id, owner, ownerName, location, items, experience));
            } catch (IllegalArgumentException exception) {
                plugin.getLogger().warning("Invalid grave data: " + id);
            }
        }

        return graves;
    }

    public void delete(String id) {
        config.deleteAndSave("graves." + id);
    }
}