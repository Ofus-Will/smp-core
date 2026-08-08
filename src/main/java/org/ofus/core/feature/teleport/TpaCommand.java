package org.ofus.core.feature.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

import java.util.Collection;

public class TpaCommand extends PlayerCommand {

    private final TeleportRequestManager manager;

    public TpaCommand(TeleportRequestManager manager) {
        super("tpa");
        permission("core.tpa", PermissionDefault.TRUE);
        usage("/tpa <player>");
        description("Request to teleport to another player");
        this.manager = manager;
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        if (args.length != 1) return false;

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) return reply(player, "&cThat player is not online");

        if (target.equals(player)) return reply(player, "&cYou can not send a teleport request to yourself");

        manager.createRequest(player, target);

        reply(player, "&aTeleport request sent to " + target.getName());
        reply(target, "&a" + player.getName() + " wants to teleport to you");
        reply(target, "&aUse /tpaccept to accept or /tpdeny to deny");
        return true;
    }

    @Override
    public Collection<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length != 1) return super.tabComplete(sender, args);

        String input = args[0].toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> !(sender instanceof Player player) || !name.equals(player.getName()))
                .filter(name -> name.toLowerCase().startsWith(input))
                .sorted()
                .toList();
    }
}