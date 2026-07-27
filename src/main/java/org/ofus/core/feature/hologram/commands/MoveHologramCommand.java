package org.ofus.core.feature.hologram.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.feature.hologram.Hologram;
import org.ofus.core.feature.hologram.HologramManager;
import org.ofus.core.util.LocationUtils;
import org.ofus.core.util.command.CommandNode;
import org.ofus.core.util.command.PlayerExecutable;

public class MoveHologramCommand extends CommandNode implements PlayerExecutable {

    private final HologramManager manager;

    public MoveHologramCommand(HologramManager manager) {
        name("move");
        usage("/hologram move <id>");
        aliases("tp", "teleport");
        this.manager = manager;
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        if (args.length != 1) return false;

        String id = args[0];
        Hologram hologram = manager.get(id);

        if (hologram == null) return reply(player, "&cA hologram with the ID '" + id + "' does not exist");

        Location location = LocationUtils.getHeadLocation(player);
        manager.teleport(id, location);

        return reply(player, "&aMoved hologram '" + id + "'.");
    }
}