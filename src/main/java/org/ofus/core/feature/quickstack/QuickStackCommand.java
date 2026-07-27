package org.ofus.core.feature.quickstack;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

public class QuickStackCommand extends PlayerCommand {

    private final int radius;

    public QuickStackCommand(int radius) {
        super("quickstack");
        this.radius = radius;

        aliases("qs");
        usage("/quickstack");
        permission("core.quickstack", PermissionDefault.TRUE);
        description("Move matching inventory items into nearby chests");
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        int moved = QuickStacker.stack(player, radius);

        if (moved == 0) return reply(player, "&cNo matching items could be quick stacked");
        return reply(player, "&fQuick stacked &a" + moved + " items");
    }
}
