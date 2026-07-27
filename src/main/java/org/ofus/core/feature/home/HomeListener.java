package org.ofus.core.feature.home;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class HomeListener implements Listener {

    private final HomeManager homeManager;

    public HomeListener(HomeManager homeManager) {
        this.homeManager = homeManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        homeManager.unload(event.getPlayer().getUniqueId());
    }
}
