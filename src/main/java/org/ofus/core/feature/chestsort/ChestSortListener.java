package org.ofus.core.feature.chestsort;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.ofus.core.util.Texts;

public class ChestSortListener implements Listener {

    @EventHandler
    public void onChestClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (!event.getPlayer().isSneaking()) return;

        Block block = event.getClickedBlock();
        if (block == null || !(block.getState() instanceof Chest chest)) return;

        event.setCancelled(true);
        ChestSorter.sort(chest.getInventory());
        Texts.send(event.getPlayer(), "&aAll inventory items sorted");
    }
}