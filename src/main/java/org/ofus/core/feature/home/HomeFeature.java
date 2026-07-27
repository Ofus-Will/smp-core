package org.ofus.core.feature.home;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.Settings;
import org.ofus.core.feature.PluginFeature;
import org.ofus.core.util.command.RootCommand;

import java.util.List;

public class HomeFeature implements PluginFeature {

    private final HomeManager homeManager;

    public HomeFeature(JavaPlugin plugin, Settings settings) {
        HomesRepository repository = new HomesRepository(plugin);
        this.homeManager = new HomeManager(repository, settings.maxHomes());
    }

    @Override
    public List<Listener> listeners() {
        return List.of(new HomeListener(homeManager));
    }

    @Override
    public List<RootCommand> commands() {
        return List.of(
                new HomeCommand(homeManager),
                new SetHomeCommand(homeManager),
                new DeleteHomeCommand(homeManager)
        );
    }
}
