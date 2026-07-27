package org.ofus.core.feature.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

public class CreativeCommand extends PlayerCommand {

    public CreativeCommand() {
        super("gmc");
        permission("core.gamemode", PermissionDefault.OP);
        description("Switch to creative mode");
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        player.setGameMode(GameMode.CREATIVE);
        return reply(player, "&aGamemode set to creative");
    }
}
