package org.ofus.core.feature.home;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.util.config.YamlConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomesRepository {

    private static final String ROOT = "homes";
    private static final String FILE_NAME = "homes.yml";

    private final JavaPlugin plugin;
    private final File playersFolder;
    private final Map<UUID, YamlConfig> configs = new HashMap<>();

    public HomesRepository(JavaPlugin plugin) {
        this.plugin = plugin;
        this.playersFolder = new File(plugin.getDataFolder(), "players");
    }

    private String homePath(String name) {
        return ROOT + "." + name;
    }

    private YamlConfig config(UUID uuid) {
        return configs.computeIfAbsent(uuid, key -> {
            File file = new File(playersFolder, key + "/" + FILE_NAME);
            return new YamlConfig(plugin, file);
        });
    }

    public Location getHome(UUID uuid, String name) {
        return config(uuid).getLocation(homePath(name));
    }

    public void saveHome(UUID uuid, String name, Location location) {
        config(uuid).setAndSave(homePath(name), location);
    }

    public boolean deleteHome(UUID uuid, String name) {
        YamlConfig config = config(uuid);
        String path = homePath(name);

        if (!config.contains(path)) return false;

        config.deleteAndSave(path);
        return true;
    }

    public Map<String, Location> getHomes(UUID uuid) {
        ConfigurationSection section = config(uuid).getSection(ROOT);
        Map<String, Location> homes = new HashMap<>();

        if (section == null) return homes;

        for (String name : section.getKeys(false)) {
            Location location = section.getLocation(name);

            if (location != null) homes.put(name, location);
        }
        return homes;
    }

    public boolean hasHome(UUID uuid, String name) {
        return config(uuid).contains(homePath(name));
    }

    public void unload(UUID uuid) {
        configs.remove(uuid);
    }
}
