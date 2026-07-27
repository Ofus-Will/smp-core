package org.ofus.core.util.config;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class YamlConfig {

    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration config;

    public YamlConfig(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;

        createIfMissing();
        reload();
    }

    private void createIfMissing() {
        try {
            File parent = file.getParentFile();
            if (parent != null) parent.mkdirs();

            if (!file.exists() && !file.createNewFile()) {
                plugin.getLogger().warning("Failed to create " + file.getPath());
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create " + file.getPath(), e);
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public Location getLocation(String path) {
        return config.getLocation(path);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save " + file.getPath(), e);
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public void setAndSave(String path, Object value) {
        config.set(path, value);
        save();
    }

    public void delete(String path) {
        config.set(path, null);
    }

    public void deleteAndSave(String path) {
        config.set(path, null);
        save();
    }
}
