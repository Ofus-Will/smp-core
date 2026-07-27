package org.ofus.core.feature.door;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoubleDoorListener implements Listener {

    private static final BlockFace[] HORIZONTAL_FACES = {
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDoorClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clicked = event.getClickedBlock();
        if (clicked == null || clicked.getType() == Material.IRON_DOOR) return;
        if (!(clicked.getBlockData() instanceof Door clickedDoor)) return;

        Block door = getBottom(clicked, clickedDoor);
        Door doorData = (Door) door.getBlockData();
        Block pairedDoor = findPairedDoor(door, doorData);
        if (pairedDoor == null) return;

        setOpen(pairedDoor, !doorData.isOpen());
    }

    private Block getBottom(Block block, Door door) {
        return door.getHalf() == Door.Half.TOP ? block.getRelative(BlockFace.DOWN) : block;
    }

    private Block findPairedDoor(Block door, Door doorData) {
        for (BlockFace face : HORIZONTAL_FACES) {
            Block adjacent = door.getRelative(face);
            BlockData adjacentData = adjacent.getBlockData();
            if (!(adjacentData instanceof Door adjacentDoor)) continue;
            if (!isPair(door, doorData, adjacent, adjacentDoor)) continue;

            return adjacent;
        }

        return null;
    }

    private boolean isPair(Block door, Door doorData, Block adjacent, Door adjacentDoor) {
        if (door.getType() != adjacent.getType()) return false;
        if (adjacentDoor.getHalf() != Door.Half.BOTTOM) return false;
        if (doorData.getFacing() != adjacentDoor.getFacing()) return false;

        return doorData.getHinge() != adjacentDoor.getHinge();
    }

    private void setOpen(Block bottom, boolean open) {
        setDoorHalfOpen(bottom, open);
        setDoorHalfOpen(bottom.getRelative(BlockFace.UP), open);
    }

    private void setDoorHalfOpen(Block block, boolean open) {
        if (!(block.getBlockData() instanceof Door door)) return;

        door.setOpen(open);
        block.setBlockData(door);
    }
}