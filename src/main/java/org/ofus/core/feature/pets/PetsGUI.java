package org.ofus.core.feature.pets;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.ofus.core.util.LocationUtils;
import org.ofus.core.util.Texts;
import org.ofus.core.util.gui.GUI;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PetsGUI extends GUI {

    private final PetManager petManager;

    public PetsGUI(PetManager petManager, List<PetData> pets) {
        super("Your Pets", getRows(pets.size()));
        this.petManager = petManager;

        pets.stream()
                .limit(54)
                .forEach(pet -> addNext(createIcon(pet), event -> handlePetClick(event, pet)));
    }

    private static ItemStack createIcon(PetData pet) {
        Material egg = Material.matchMaterial(pet.type() + "_SPAWN_EGG");
        ItemStack icon = new ItemStack(egg == null ? Material.NAME_TAG : egg);

        ItemMeta meta = icon.getItemMeta();
        meta.customName(Texts.parse("&a" + formatType(pet.type())));
        meta.lore(List.of(
                Texts.parse("&fLast seen: &a" + LocationUtils.format(pet.location())),
                Texts.parse(""),
                Texts.parse("&8⇒ &7Left click to teleport to"),
                Texts.parse("&8⇒ &7Right click to teleport to you")
        ));

        icon.setItemMeta(meta);
        return icon;
    }

    private void handlePetClick(InventoryClickEvent event, PetData pet) {
        Player player = (Player) event.getWhoClicked();

        if (event.isLeftClick()) {
            teleportPlayerToPet(player, pet);
            return;
        }

        if (event.isRightClick()) {
            teleportPetToPlayer(player, pet);
        }
    }

    private void teleportPlayerToPet(Player player, PetData pet) {
        player.closeInventory();

        if (petManager.teleportPlayerToPet(player, pet.id())) {
            Texts.send(player, "&aTeleported to your " + formatType(pet.type()));
            return;
        }

        Texts.send(player, "&cCould not teleport to that pet");
    }

    private void teleportPetToPlayer(Player player, PetData pet) {
        player.closeInventory();

        if (petManager.teleportToPlayer(player, pet.id())) {
            Texts.send(player, "&aTeleported your " + formatType(pet.type()) + " to you");
            return;
        }

        Texts.send(player, "&cCould not teleport that pet");
    }

    private static int getRows(int petCount) {
        return Math.max(1, Math.min(6, (petCount + 8) / 9));
    }

    private static String formatType(String type) {
        return Arrays.stream(type.split("_"))
                .map(word -> word.charAt(0) + word.substring(1).toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(" "));
    }
}