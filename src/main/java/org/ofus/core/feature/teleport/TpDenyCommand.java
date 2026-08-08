package org.ofus.core.feature.teleport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

public class TpDenyCommand extends PlayerCommand {

    private final TeleportRequestManager manager;

    public TpDenyCommand(TeleportRequestManager manager) {
        super("tpdeny");
        permission("core.tpa", PermissionDefault.TRUE);
        aliases("tpno");
        description("Deny a teleport request");
        this.manager = manager;
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        TeleportRequest request = manager.takeRequest(player);

        if (request == null) return reply(player, "&cYou do not have any active teleport requests");

        Player requester = Bukkit.getPlayer(request.requester());
        reply(player, "&cDenied teleport request");

        if (requester != null && requester.isOnline()) {
            reply(requester, "&c" + player.getName() + " denied your teleport request");
        }

        return true;
    }
}
