package org.ofus.core.feature.hologram.commands;

import org.bukkit.permissions.PermissionDefault;
import org.ofus.core.feature.hologram.HologramManager;
import org.ofus.core.util.command.ParentCommand;

public class BaseHologramCommand extends ParentCommand {

    public BaseHologramCommand(HologramManager manager) {
        super("hologram");
        aliases("holograms");
        permission("core.holograms", PermissionDefault.OP);
        description("Manage text holograms");

        register(
                new CreateHologramCommand(manager),
                new DeleteHologramCommand(manager),
                new ListHologramsCommand(manager),
                new MoveHologramCommand(manager),
                new UpdateHologramCommand(manager)
        );
    }
}