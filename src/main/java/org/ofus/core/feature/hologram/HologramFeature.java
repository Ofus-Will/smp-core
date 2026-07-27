package org.ofus.core.feature.hologram;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.feature.PluginFeature;
import org.ofus.core.feature.hologram.commands.BaseHologramCommand;
import org.ofus.core.util.command.RootCommand;

import java.util.List;

public class HologramFeature implements PluginFeature {

    private final JavaPlugin plugin;
    private final HologramManager hologramManager;

    public HologramFeature(JavaPlugin plugin) {
        this.plugin = plugin;

        HologramRepository repository = new HologramRepository(plugin);
        this.hologramManager = new HologramManager(repository);
    }

    @Override
    public void enable() {
        Bukkit.getScheduler().runTaskLater(plugin, hologramManager::load, 20L);
    }

    @Override
    public void disable() {
        hologramManager.removeRuntime();
    }

    @Override
    public List<RootCommand> commands() {
        return List.of(new BaseHologramCommand(hologramManager));
    }
}