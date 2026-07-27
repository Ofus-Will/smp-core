package org.ofus.core.feature.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;
import org.ofus.core.util.Texts;

import java.util.stream.Collectors;

public class WorldCommand extends PlayerCommand {

    public WorldCommand() {
        super("world");
        permission("core.world", PermissionDefault.OP);
        usage("/world [world]");
        description("List worlds or teleport to a world spawn");
    }

    @Override
    public boolean run(@NotNull Player player, @NotNull String[] args) {

        // list worlds
        if (args.length == 0) {
            String worlds = Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .collect(Collectors.joining(", "));
            Texts.send(player, "&fWorlds: &a" + worlds);
            return true;
        }

        if(args.length != 1) return false;

        // teleport to world
        World world = Bukkit.getWorld(args[0]);
        if (world == null) return reply(player, "&cThat world does not exist!");
        player.teleport(world.getSpawnLocation());

        return reply(player, "&fTeleported to &a" + world.getName());
    }
}
