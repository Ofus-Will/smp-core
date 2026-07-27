package org.ofus.core.feature.grave;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.ofus.core.util.LocationUtils;
import org.ofus.core.util.Texts;

public class GraveListener implements Listener {

    private final GraveManager manager;

    public GraveListener(GraveManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        if (event.getKeepInventory()) return;
        if (event.getDrops().isEmpty() && event.getDroppedExp() <= 0) return;

        Player player = event.getEntity();
        Block block = player.getLocation().getBlock();

        while (!block.getType().isAir() || manager.has(block.getLocation())) {
            if (block.getY() >= block.getWorld().getMaxHeight() - 1) {
                Texts.send(player, "&cNo space was found for your grave");
                return;
            }

            block = block.getRelative(0, 1, 0);
        }

        ItemStack[] items = event.getDrops().stream()
                .map(ItemStack::clone)
                .toArray(ItemStack[]::new);

        int experience = event.getDroppedExp();

        manager.create(player, block.getLocation(), items, experience);

        event.getDrops().clear();
        event.setDroppedExp(0);

        String locString = LocationUtils.format(block.getLocation());
        event.deathMessage(Texts.parse("&c&l☠ &c" + player.getName() + " has died! (" + locString + ")"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onGraveClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        GraveGUI grave = manager.get(block.getLocation());
        if (grave == null) return;

        event.setCancelled(true);
        grave.open(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onGraveBreak(BlockBreakEvent event) {
        GraveGUI grave = manager.get(event.getBlock().getLocation());
        if (grave == null) return;

        event.setCancelled(true);
        grave.open(event.getPlayer());
    }
}