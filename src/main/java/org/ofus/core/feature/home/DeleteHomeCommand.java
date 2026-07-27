package org.ofus.core.feature.home;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

public class DeleteHomeCommand extends PlayerCommand {

    private static final String INVALID_HOME_NAME = "&cInvalid home name.";

    private final HomeManager homeManager;

    public DeleteHomeCommand(HomeManager homeManager) {
        super("delhome");
        aliases("deletehome");
        permission("core.homes", PermissionDefault.TRUE);
        description("Delete one of your homes");
        this.homeManager = homeManager;
    }

    @Override
    public boolean run(@NotNull Player player, @NotNull String @NotNull [] args) {
        String home = args.length == 0 ? "home" : args[0].toLowerCase();

        if (!homeManager.isValidName(home)) {
            return reply(player, INVALID_HOME_NAME);
        }

        if(homeManager.getHome(player.getUniqueId(), home) != null) {
            homeManager.deleteHome(player.getUniqueId(), home);
            return reply(player, "&aSuccessfully deleted your home");
        }

        return reply(player, "&cThat home does not exist");
    }
}
