package org.ofus.core.feature.hologram.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.feature.hologram.Hologram;
import org.ofus.core.feature.hologram.HologramManager;
import org.ofus.core.util.command.CommandNode;

public class UpdateHologramCommand extends CommandNode {

    private final HologramManager manager;

    public UpdateHologramCommand(HologramManager manager) {
        name("update");
        usage("/hologram update <id> <lines>");
        aliases("edit", "setlines", "lines");
        this.manager = manager;
    }

    @Override
    public boolean run(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length < 2) return false;

        String id = args[0];
        Hologram hologram = manager.get(id);

        if (hologram == null) {
            return reply(sender, "&cA hologram with the ID '" + id + "' does not exist");
        }

        String input = joinArgs(args, 1);
        String[] lines = input.split("\\s*//\\s*");
        manager.update(id, lines);

        return reply(sender, "&aUpdated hologram '" + id + "'.");
    }
}