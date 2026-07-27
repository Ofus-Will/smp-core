package org.ofus.core.feature.grave;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.ofus.core.util.Texts;
import org.ofus.core.util.gui.GUI;

import java.util.Collection;
import java.util.UUID;

public class GraveGUI extends GUI {

    private final String id;
    private final UUID owner;
    private final String ownerName;
    private final Location location;
    private final GraveManager manager;
    private int experience;

    public GraveGUI(
            String id,
            UUID owner,
            String ownerName,
            Location location,
            GraveManager manager,
            Collection<ItemStack> items,
            int experience
    ) {
        super(ownerName + "'s Grave", 6);

        this.id = id;
        this.owner = owner;
        this.ownerName = ownerName;
        this.location = location.clone();
        this.manager = manager;
        this.experience = experience;

        getInventory().addItem(items.toArray(ItemStack[]::new));
    }

    @Override
    public void open(Player player) {
        if (!player.getUniqueId().equals(owner)) {
            Texts.send(player, "&cThis is not your grave.");
            return;
        }
        super.open(player);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Override
    public void onClose(Player player) {
        if (!getInventory().isEmpty()) {
            manager.save(this);
            return;
        }

        int storedExperience = experience;
        experience = 0;

        manager.remove(this);
        player.giveExp(storedExperience);
    }

    public String getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Location getLocation() {
        return location.clone();
    }

    public int getExperience() {
        return experience;
    }
}