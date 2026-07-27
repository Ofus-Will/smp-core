package org.ofus.core.feature.pets;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.Settings;
import org.ofus.core.feature.PluginFeature;
import org.ofus.core.util.command.RootCommand;

import java.util.List;

public class PetsFeature implements PluginFeature {

    private final JavaPlugin plugin;
    private final PetManager petManager;
    private final PetAttackTask attackTask;
    private final PetHologramTask hologramTask;
    private final double healOnKill;

    public PetsFeature(JavaPlugin plugin, Settings settings) {
        this.plugin = plugin;

        PetRepository repository = new PetRepository(plugin);
        this.petManager = new PetManager(repository);
        this.attackTask = new PetAttackTask(plugin, settings.petAutoAttackRadius());
        this.hologramTask = new PetHologramTask(plugin, settings.petHologramRadius());
        this.healOnKill = settings.petHealOnKill();
    }

    @Override
    public void enable() {
        attackTask.start();
        hologramTask.start();
    }

    @Override
    public void disable() {
        attackTask.stop();
        hologramTask.stop();
    }

    @Override
    public List<Listener> listeners() {
        return List.of(
                new PetListener(petManager),
                new PetExperienceListener(healOnKill)
        );
    }

    @Override
    public List<RootCommand> commands() {
        return List.of(new PetsCommand(petManager));
    }
}