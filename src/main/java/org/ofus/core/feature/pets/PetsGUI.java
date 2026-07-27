package org.ofus.core.feature.pets;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ofus.core.util.LocationUtils;
import org.ofus.core.util.Texts;
import org.ofus.core.util.gui.GUI;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class PetsGUI extends GUI {

    public PetsGUI(List<Tameable> pets) {
        super("Your Pets", getRows(pets.size()));

        pets.stream()
                .limit(54)
                .forEach(pet -> addNext(createIcon(pet), event ->
                        teleportToPet((Player) event.getWhoClicked(), pet.getUniqueId())
                ));
    }

    private static ItemStack createIcon(Tameable pet) {
        ItemStack icon = pet.getPickItemStack();

        if (icon.getType().isAir()) icon = new ItemStack(Material.NAME_TAG);
        icon.setAmount(1);

        ItemMeta meta = icon.getItemMeta();
        Component petName = pet.customName() != null ? pet.customName() : Texts.parse(formatType(pet));

        meta.customName(petName);
        meta.lore(List.of(
                Texts.parse("&a " + LocationUtils.format(pet.getLocation())),
                Texts.parse(""),
                Texts.parse("&8Click to teleport")
        ));

        icon.setItemMeta(meta);
        return icon;
    }

    private static void teleportToPet(Player player, UUID petId) {
        Entity entity = player.getServer().getEntity(petId);

        if (!(entity instanceof Tameable pet) || !pet.isValid()) {
            player.closeInventory();
            Texts.send(player, "&cThat pet is no longer available");
            return;
        }

        player.closeInventory();

        boolean teleported = player.teleport(pet, PlayerTeleportEvent.TeleportCause.PLUGIN);
        if (teleported) {
            Texts.send(player, "&aTeleported to your " + formatType(pet));
        } else {
            Texts.send(player, "&cCould not teleport to that pet");
        }
    }

    private static int getRows(int petCount) {
        return Math.max(1, Math.min(6, (petCount + 8) / 9));
    }

    private static String formatType(Tameable pet) {
        return Arrays.stream(pet.getType().name().split("_"))
                .map(word -> word.charAt(0) + word.substring(1).toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(" "));
    }
}