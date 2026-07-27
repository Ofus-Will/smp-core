package org.ofus.core.feature.item;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;
import org.ofus.core.util.NumberUtils;
import org.ofus.core.util.Texts;

import java.util.ArrayList;
import java.util.List;

public class LoreCommand extends PlayerCommand {

    private final int maxLineLength;

    public LoreCommand(int maxLineLength) {
        super("lore");
        this.maxLineLength = maxLineLength;

        permission("core.lore", PermissionDefault.OP);
        usage("/lore <clear|setline|removeline> <line> <lore>");
        description("Edit lore on the item in your hand");
    }

    @Override
    public boolean run(@NotNull Player player, @NotNull String @NotNull [] args) {
        if(args.length == 0) return false;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isAir()) return reply(player, "&cYou must be holding an item");

        // clear lore
        ItemMeta meta = item.getItemMeta();
        if(args[0].equals("clear")) {
            meta.lore(null);
            item.setItemMeta(meta);
            Texts.send(player, "&aItem lore cleared");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        if((!subCommand.equals("setline") && !subCommand.equals("removeline")) || args.length < 2) return false;
        if(!NumberUtils.isNumber(args[1], true)) return false;

        int line = Integer.parseInt(args[1]) - 1;
        if (line < 0) {
            Texts.send(player, "&cLine numbers start at 1");
            return true;
        }

        List<Component> lore = meta.lore() == null
            ? new ArrayList<>()
            : new ArrayList<>(meta.lore());

        // removeline
        if (subCommand.equals("removeline")) {
            if (line >= lore.size()) {
                Texts.send(player, "&cThat lore line does not exist");
                return true;
            }

            lore.remove(line);
        }
        // setline
        else {
            while (lore.size() <= line) lore.add(Component.empty());

            String text = args.length > 2 ?
                    String.join(" ", args).substring(args[0].length() + args[1].length() + 2) :
                    "";
            if (text.length() > maxLineLength) {
                return reply(player, "&cLore lines can be at most " + maxLineLength + " characters.");
            }

            lore.set(line, Texts.parse(text));
        }

        meta.lore(lore.isEmpty() ? null : lore);
        item.setItemMeta(meta);

        return reply(player, "&aItem lore has been updated");
    }
}
