package org.ofus.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public record Settings(
        boolean homesEnabled,
        boolean gravesEnabled,
        boolean chestSortEnabled,
        boolean quickStackEnabled,
        boolean petsEnabled,
        int maxHomes,
        int quickStackRadius,
        int maxRenameLength,
        int maxLoreLineLength
) {

    public static Settings load(JavaPlugin plugin) {
        plugin.saveDefaultConfig();

        FileConfiguration config = plugin.getConfig();
        return new Settings(
                config.getBoolean("features.homes", true),
                config.getBoolean("features.graves", true),
                config.getBoolean("features.chest-sort", true),
                config.getBoolean("features.quick-stack", true),
                config.getBoolean("features.pets", true),
                Math.max(1, config.getInt("homes.max", 5)),
                Math.max(0, config.getInt("quick-stack.radius", 5)),
                Math.max(1, config.getInt("items.max-rename-length", 64)),
                Math.max(1, config.getInt("items.max-lore-line-length", 128))
        );
    }
}
