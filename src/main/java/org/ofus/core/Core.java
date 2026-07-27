package org.ofus.core;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.ofus.core.feature.item.LoreCommand;
import org.ofus.core.feature.item.RenameCommand;
import org.ofus.core.feature.player.CreativeCommand;
import org.ofus.core.feature.player.SurvivalCommand;
import org.ofus.core.feature.quickstack.QuickStackCommand;
import org.ofus.core.feature.chestsort.ChestSortListener;
import org.ofus.core.feature.grave.GraveListener;
import org.ofus.core.feature.grave.GraveManager;
import org.ofus.core.feature.grave.GraveRepository;
import org.ofus.core.feature.home.*;
import org.ofus.core.feature.pets.PetsCommand;
import org.ofus.core.util.command.RootCommand;
import org.ofus.core.util.gui.GUIListener;
import org.ofus.core.feature.world.SpawnCommand;
import org.ofus.core.feature.world.WorldCommand;

import java.util.ArrayList;
import java.util.List;

public class Core extends JavaPlugin {

    private Settings settings;
    private HomeManager homeManager;
    private GraveManager graveManager;

    @Override
    public void onEnable() {

        getLogger().info("Plugin enabled!");

        createManagers();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled!");

        if (graveManager != null) graveManager.saveAll();
    }

    private void createManagers() {
        settings = Settings.load(this);

        if (settings.homesEnabled()) {
            HomesRepository homesRepository = new HomesRepository(this);
            homeManager = new HomeManager(
                    homesRepository,
                    settings.maxHomes()
            );
        }

        if (settings.gravesEnabled()) {
            GraveRepository graveRepository = new GraveRepository(this);
            graveManager = new GraveManager(graveRepository);
            graveManager.load();
        }
    }

    private void registerListeners() {
        List<Listener> listeners = new ArrayList<>();

        listeners.add(new GUIListener());
        if (homeManager != null) listeners.add(new HomeListener(homeManager));
        if (settings.chestSortEnabled()) listeners.add(new ChestSortListener());
        if (graveManager != null) listeners.add(new GraveListener(graveManager));

        registerListenerBatch(listeners.toArray(Listener[]::new));
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

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> {
                    List<RootCommand> commands = new ArrayList<>();

                    if (homeManager != null) {
                        commands.add(new HomeCommand(homeManager));
                        commands.add(new SetHomeCommand(homeManager));
                        commands.add(new DeleteHomeCommand(homeManager));
                    }

                    commands.addAll(List.of(
                        new RenameCommand(settings.maxRenameLength()),
                        new LoreCommand(settings.maxLoreLineLength()),
                        new WorldCommand(),
                        new SpawnCommand(),
                        new CreativeCommand(),
                        new SurvivalCommand()
                    ));

                    if (settings.quickStackEnabled()) {
                        commands.add(new QuickStackCommand(settings.quickStackRadius()));
                    }

                    if (settings.petsEnabled()) {
                        commands.add(new PetsCommand());
                    }

                    registerCommandBatch(event.registrar(), commands.toArray(RootCommand[]::new));
                });
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
