package org.ofus.core.feature.grave;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.feature.PluginFeature;

import java.util.List;

public class GraveFeature implements PluginFeature {

    private final GraveManager graveManager;

    public GraveFeature(JavaPlugin plugin) {
        GraveRepository repository = new GraveRepository(plugin);
        this.graveManager = new GraveManager(repository);
    }

    @Override
    public void enable() {
        graveManager.load();
    }

    @Override
    public void disable() {
        graveManager.saveAll();
    }

    @Override
    public List<Listener> listeners() {
        return List.of(new GraveListener(graveManager));
    }
}
