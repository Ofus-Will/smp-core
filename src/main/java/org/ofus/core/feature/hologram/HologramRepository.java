package org.ofus.core.feature.hologram;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.util.config.YamlConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HologramRepository {

    private static final String FILE_NAME = "holograms.yml";
    private static final String ROOT = "holograms";

    private final YamlConfig config;

    public HologramRepository(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), FILE_NAME);
        config = new YamlConfig(plugin, file);
    }

    public void save(HologramData data) {
        String path = ROOT + "." + data.id();

        config.set(path + ".location", data.loc());
        config.set(path + ".lines", data.lines());
        config.save();
    }

    public void delete(String id) {
        config.deleteAndSave(ROOT + "." + id);
    }

    public Map<String, HologramData> getAll() {
        Map<String, HologramData> holograms = new HashMap<>();

        ConfigurationSection root = config.getSection(ROOT);
        if (root == null) return holograms;

        for (String id : root.getKeys(false)) {
            ConfigurationSection section = root.getConfigurationSection(id);
            if (section == null) continue;

            Location location = section.getLocation("location");
            if (location == null) continue;

            HologramData data = new HologramData(id, location, section.getStringList("lines"));
            holograms.put(id, data);
        }

        return holograms;
    }
}