package org.ofus.core.feature.hologram.commands;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.feature.hologram.HologramManager;
import org.ofus.core.util.command.CommandNode;
import org.ofus.core.util.command.PlayerExecutable;

public class DeleteHologramCommand extends CommandNode implements PlayerExecutable {

    private final HologramManager hologramManager;

    public DeleteHologramCommand(HologramManager hologramManager) {
        name("delete");
        usage("/hologram delete <id>");
        aliases("remove");
        this.hologramManager = hologramManager;
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        if(args.length == 0) return false;

        String id = args[0];

        if(hologramManager.delete(id)) {
            return reply(player, "&aSuccessfully deleted hologram '" + id + "'");
        }

        return reply(player, "&cNo hologram found named '" + id + "'");
    }
}