package org.ofus.core.util.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerExecutable {

    default String getPlayerOnlyMessage() {
        return "&cOnly players can use this command!";
    }

    boolean run(@NotNull Player player, String @NotNull [] args);
}
