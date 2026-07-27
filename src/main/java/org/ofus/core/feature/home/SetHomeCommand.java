package org.ofus.core.feature.home;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

public class SetHomeCommand extends PlayerCommand {

    private static final String INVALID_HOME_NAME = "&cInvalid home name.";

    private final HomeManager homeManager;

    public SetHomeCommand(HomeManager homeManager) {
        super("sethome");
        permission("core.homes", PermissionDefault.TRUE);
        description("Save your current location as a home");
        this.homeManager = homeManager;
    }
    @Override
    public boolean run(@NotNull Player player, @NotNull String @NotNull [] args) {
        String home = args.length == 0 ? "home" : args[0].toLowerCase();

        if (!homeManager.isValidName(home)) {
            return reply(player, INVALID_HOME_NAME);
        }

        boolean success = homeManager.setHome(player.getUniqueId(), home, player.getLocation());

        if(success) {
            String message = home.equals("home") ? "Set location of your home" : ("Set location of home " + home);
            return reply(player, "&a" + message);
        }

        return reply(player, "&cYou can only have " + homeManager.getMaxHomes() + " homes.");
    }
}
