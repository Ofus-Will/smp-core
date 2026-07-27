package org.ofus.core.feature.world;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

public class SpawnCommand extends PlayerCommand {

    public SpawnCommand() {
        super("spawn");
        permission("core.spawn", PermissionDefault.TRUE);
        description("Teleport to your world's spawn");
    }

    @Override
    public boolean run(@NotNull Player player, @NotNull String @NotNull [] args) {
        Location loc = player.getWorld().getSpawnLocation().clone().add(0,1,0);
        player.teleport(loc);

        return reply(player, "&fTeleported to spawn of world &a" + player.getWorld().getName());
    }
}
