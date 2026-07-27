package org.ofus.core.util.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class GUI implements InventoryHolder {

    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> actions = new HashMap<>();
    private int nextSlot = 0;

    public GUI(String title) {
        this(title, 27);
    }

    public GUI(String title, int rows) {
        if (rows < 1 || rows > 6) throw new IllegalArgumentException("Rows must be between 1 and 6");
        this.inventory = Bukkit.createInventory(this, rows * 9, Component.text(title));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    protected boolean isEditable() {
        return false;
    }

    protected void setSlot(int slot, ItemStack item, Consumer<InventoryClickEvent> action) {
        inventory.setItem(slot, item);
        actions.put(slot, action);
    }

    protected void setSlot(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public final void onClick(InventoryClickEvent event) {
        if (!isEditable()) event.setCancelled(true);
        if (event.getClickedInventory() != inventory) return;

        Consumer<InventoryClickEvent> action = actions.get(event.getSlot());
        if (action != null) action.accept(event);
    }

    protected void addNext(ItemStack item, Consumer<InventoryClickEvent> action) {
        while (inventory.getItem(nextSlot) != null) {
            nextSlot++;
        }
        setSlot(nextSlot, item, action);
        nextSlot++;
    }

    protected void fillBorder(ItemStack item) {
        int size = inventory.getSize();
        for (int i = 0; i < size; i++) {
            if (i < 9 || i >= size - 9 || i % 9 == 0 || i % 9 == 8) {
                setSlot(i, item);
            }
        }
    }

    public void onClose(Player player) {}
    public void onOpen(Player player) {}
}
