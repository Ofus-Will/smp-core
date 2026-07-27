package org.ofus.core.feature.hologram.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.feature.hologram.Hologram;
import org.ofus.core.feature.hologram.HologramManager;
import org.ofus.core.util.Texts;
import org.ofus.core.util.command.CommandNode;

import java.util.Map;

public class ListHologramsCommand extends CommandNode {

    private final HologramManager hologramManager;

    public ListHologramsCommand(HologramManager hologramManager) {
        name("list");
        usage("/hologram list");
        this.hologramManager = hologramManager;
    }

    @Override
    public boolean run(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length != 0) return false;

        Map<String, Hologram> holograms = hologramManager.getHolograms();

        if(holograms.isEmpty()) {
            Texts.send(sender, "&cThere are no holograms!");
            return true;
        }

        Texts.send(sender, "&aHolograms:");
        for (Hologram hologram : holograms.values()) {
            Texts.send(sender, "- " + hologram.getId());
        }

        return true;
    }
}