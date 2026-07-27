package org.ofus.core;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.feature.PluginFeature;
import org.ofus.core.feature.chestsort.ChestSortFeature;
import org.ofus.core.feature.damage.DamageHologramListener;
import org.ofus.core.feature.door.DoubleDoorListener;
import org.ofus.core.feature.grave.GraveFeature;
import org.ofus.core.feature.hologram.HologramFeature;
import org.ofus.core.feature.home.HomeFeature;
import org.ofus.core.feature.item.LoreCommand;
import org.ofus.core.feature.item.RenameCommand;
import org.ofus.core.feature.pets.PetsFeature;
import org.ofus.core.feature.player.CreativeCommand;
import org.ofus.core.feature.player.SurvivalCommand;
import org.ofus.core.feature.quickstack.QuickStackCommand;
import org.ofus.core.feature.recipe.RecipeFeature;
import org.ofus.core.feature.world.SpawnCommand;
import org.ofus.core.feature.world.WorldCommand;
import org.ofus.core.util.command.RootCommand;
import org.ofus.core.util.gui.GUIListener;

import java.util.ArrayList;
import java.util.List;

public class Core extends JavaPlugin {

    private final List<PluginFeature> features = new ArrayList<>();
    private final List<RootCommand> commands = new ArrayList<>();

    private DamageHologramListener damageHologramListener;
    private DoubleDoorListener doubleDoorListener;

    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled!");

        createFeatures();
        features.forEach(PluginFeature::enable);

        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");

        features.forEach(PluginFeature::disable);
        if (damageHologramListener != null) damageHologramListener.removeAll();
    }

    private void createFeatures() {
        Settings settings = Settings.load(this);

        if (settings.homesEnabled()) features.add(new HomeFeature(this, settings));
        if (settings.gravesEnabled()) features.add(new GraveFeature(this));
        if (settings.hologramsEnabled()) features.add(new HologramFeature(this));
        if (settings.damageHologramsEnabled()) damageHologramListener = new DamageHologramListener(this);
        if (settings.doubleDoorsEnabled()) doubleDoorListener = new DoubleDoorListener();
        if (settings.chestSortEnabled()) features.add(new ChestSortFeature());
        if (settings.quickStackEnabled()) commands.add(new QuickStackCommand(settings.quickStackRadius()));
        if (settings.petsEnabled()) features.add(new PetsFeature(this, settings));
        if (settings.recipesEnabled()) features.add(new RecipeFeature(this));

        commands.addAll(List.of(
                new RenameCommand(settings.maxRenameLength()),
                new LoreCommand(settings.maxLoreLineLength()),
                new WorldCommand(),
                new SpawnCommand(),
                new CreativeCommand(),
                new SurvivalCommand()
        ));
    }

    private void registerListeners() {
        registerListenerBatch(new GUIListener());
        if (damageHologramListener != null) registerListenerBatch(damageHologramListener);
        if (doubleDoorListener != null) registerListenerBatch(doubleDoorListener);

        for (PluginFeature feature : features) {
            registerListenerBatch(feature.listeners().toArray(Listener[]::new));
        }
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> {
                    List<RootCommand> registeredCommands = new ArrayList<>(commands);

                    for (PluginFeature feature : features) {
                        registeredCommands.addAll(feature.commands());
                    }

                    registerCommandBatch(event.registrar(), registeredCommands.toArray(RootCommand[]::new));
                });
    }

    private void registerPermission(String node, PermissionDefault defaultValue) {
        Permission permission = getServer().getPluginManager().getPermission(node);

        if (permission == null) {
            getServer().getPluginManager().addPermission(new Permission(node, defaultValue));
            return;
        }

        permission.setDefault(defaultValue);
    }

    private void registerCommand(io.papermc.paper.command.brigadier.Commands registrar, RootCommand command) {
        if (command.permission() != null) {
            registerPermission(command.permission(), command.getPermissionDefault());
        }

        registrar.register(command.getName(), command.getDescription(), command.getAliases(), command);
    }

    private void registerCommandBatch(io.papermc.paper.command.brigadier.Commands registrar, RootCommand... commands) {
        for (RootCommand command : commands) {
            registerCommand(registrar, command);
        }
    }

    private void registerListenerBatch(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}