package org.ofus.core.feature.teleport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

public class TpAcceptCommand extends PlayerCommand {

    private final TeleportRequestManager manager;

    public TpAcceptCommand(TeleportRequestManager manager) {
        super("tpaccept");
        permission("core.tpa", PermissionDefault.TRUE);
        aliases("tpyes");
        description("Accept a teleport request");
        this.manager = manager;
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        TeleportRequest request = manager.takeRequest(player);

        if (request == null) return reply(player, "&cYou do not have any active teleport requests");

        Player requester = Bukkit.getPlayer(request.requester());
        if (requester == null || !requester.isOnline()) return reply(player, "&cThat player is no longer online");

        requester.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        reply(player, "&aAccepted teleport request from " + requester.getName());
        return reply(requester, "&aTeleport request accepted");
    }
}
