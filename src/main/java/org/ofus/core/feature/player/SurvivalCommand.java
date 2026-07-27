package org.ofus.core.feature.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

public class SurvivalCommand extends PlayerCommand {

    public SurvivalCommand() {
        super("gms");
        permission("core.gamemode", PermissionDefault.OP);
        description("Switch to survival mode");
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        player.setGameMode(GameMode.SURVIVAL);
        return reply(player, "&aGamemode set to survival");
    }
}
