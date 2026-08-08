package org.ofus.core.feature.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;
import org.ofus.core.util.ItemBuilder;

public class RenameCommand extends PlayerCommand {

    private final int maxNameLength;

    public RenameCommand(int maxNameLength) {
        super("rename");
        this.maxNameLength = maxNameLength;

        permission("core.rename", PermissionDefault.OP);
        usage("/rename <name>");
        description("Rename the item in your hand");
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        if(args.length == 0) return reply(player, "&cPlease specify an item name!");

        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType().isAir()) return reply(player, "&cYou are not holding an item!");

        String name = joinArgs(args, 0);
        if (name.length() > maxNameLength) {
            return reply(player, "&cItem names can be at most " + maxNameLength + " characters.");
        }

        ItemStack newItem = new ItemBuilder(item).name(name).build();
        player.getInventory().setItemInMainHand(newItem);

        return reply(player, "&fItem renamed to: " + name);
    }
}
