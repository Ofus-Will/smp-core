package org.ofus.core.feature.home;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.ofus.core.util.command.PlayerCommand;

import java.util.Map;

public class HomeCommand extends PlayerCommand {

    private static final String INVALID_HOME_NAME = "&cInvalid home name.";

    private final HomeManager homeManager;

    public HomeCommand(HomeManager homeManager) {
        super("home");
        permission("core.homes", PermissionDefault.TRUE);
        description("Teleport to one of your homes");
        this.homeManager = homeManager;
    }

    @Override
    public boolean run(Player player, String[] args) {
        Map<String, Location> homes = homeManager.getHomes(player.getUniqueId());

        if(args.length == 0 && homes.size() > 1) {
            String names = String.join(", ", homes.keySet());
            return reply(player, "&fHomes: &a" + names);
        }

        String home = args.length == 0 ? "home" : args[0].toLowerCase();
        if (!homeManager.isValidName(home)) {
            return reply(player, INVALID_HOME_NAME);
        }

        Location loc = homes.get(home);

        if (loc == null) return reply(player, "&cYou don't have this home set!");

        player.teleport(loc);
        String message = home.equals("home") ? "Teleported to your home" : ("Teleported to home " + home);
        return reply(player, "&a" + message);
    }
}
