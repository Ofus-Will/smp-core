package org.ofus.core.feature.hologram.commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.feature.hologram.HologramManager;
import org.ofus.core.util.LocationUtils;
import org.ofus.core.util.command.CommandNode;
import org.ofus.core.util.command.PlayerExecutable;

public class CreateHologramCommand extends CommandNode implements PlayerExecutable {

    private final HologramManager manager;

    public CreateHologramCommand(HologramManager manager) {
        name("create");
        usage("/hologram create <id> <lines>");
        aliases("add");
        this.manager = manager;
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        if(args.length < 2) return false;

        String id = args[0];
        Location loc = LocationUtils.getHeadLocation(player);
        String input = joinArgs(args, 1);
        String[] lines = input.split("\\s*//\\s*");
        manager.create(id, loc, lines);

        return reply(player, "&aSuccessfully created hologram '" + id + "'");
    }
}