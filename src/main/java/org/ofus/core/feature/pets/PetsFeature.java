package org.ofus.core.feature.pets;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.feature.PluginFeature;
import org.ofus.core.util.command.RootCommand;

import java.util.List;

public class PetsFeature implements PluginFeature {

    private final PetManager petManager;

    public PetsFeature(JavaPlugin plugin) {
        PetRepository repository = new PetRepository(plugin);
        this.petManager = new PetManager(repository);
    }

    @Override
    public List<Listener> listeners() {
        return List.of(new PetListener(petManager));
    }

    @Override
    public List<RootCommand> commands() {
        return List.of(new PetsCommand(petManager));
    }
}