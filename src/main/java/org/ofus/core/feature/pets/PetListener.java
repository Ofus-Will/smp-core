package org.ofus.core.feature.pets;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.ofus.core.util.Texts;

import java.text.DecimalFormat;
import java.util.Locale;

public class PetListener implements Listener {

    private static final DecimalFormat ONE_DECIMAL = new DecimalFormat("0.0");
    private static final DecimalFormat TWO_DECIMALS = new DecimalFormat("0.00");
    private static final double HORSE_SPEED_BLOCKS_PER_SECOND = 43.17;

    private final PetManager petManager;

    public PetListener(PetManager petManager) {
        this.petManager = petManager;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        savePets(event.getChunk());
    }

    @EventHandler(ignoreCancelled = true)
    public void onHorseInspect(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getPlayer().isSneaking()) return;
        if (!(event.getRightClicked() instanceof AbstractHorse horse)) return;

        event.setCancelled(true);
        sendHorseStats(event.getPlayer(), horse);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPetDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Tameable pet) || !pet.isTamed()) return;
        if (!(pet instanceof LivingEntity livingPet)) return;

        double healthAfterDamage = livingPet.getHealth() - event.getFinalDamage();
        if (healthAfterDamage <= 0 || healthAfterDamage >= getMaxHealth(livingPet) / 2) return;

        Player owner = Bukkit.getPlayer(pet.getOwnerUniqueId());
        if (owner == null) return;
        if (!pet.teleport(owner.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN)) return;

        petManager.save(pet);
    }

    private void sendHorseStats(Player player, AbstractHorse horse) {
        Texts.send(player, "&9&l" + formatType(horse) + " stats");
        Texts.send(player, "&fHealth: &a" + ONE_DECIMAL.format(horse.getHealth()) + "/" + ONE_DECIMAL.format(getMaxHealth(horse)));
        Texts.send(player, "&fSpeed: &a" + ONE_DECIMAL.format(getMovementSpeed(horse) * HORSE_SPEED_BLOCKS_PER_SECOND) + " blocks/s");
        Texts.send(player, "&fJump: &a" + ONE_DECIMAL.format(getJumpHeight(horse)) + " blocks");
        Texts.send(player, "");
        Texts.send(player, "&eRaw data: speed = " + TWO_DECIMALS.format(getMovementSpeed(horse)) + ", jump = " + TWO_DECIMALS.format(horse.getJumpStrength()));
    }

    private void savePets(Chunk chunk) {
        for (Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Tameable pet)) continue;
            if (!pet.isTamed() || pet.getOwnerUniqueId() == null) continue;

            petManager.save(pet);
        }
    }

    private double getMaxHealth(LivingEntity pet) {
        AttributeInstance attribute = pet.getAttribute(Attribute.MAX_HEALTH);
        return attribute == null ? pet.getHealth() : attribute.getValue();
    }

    private double getMovementSpeed(LivingEntity pet) {
        AttributeInstance attribute = pet.getAttribute(Attribute.MOVEMENT_SPEED);
        return attribute == null ? 0 : attribute.getValue();
    }

    private double getJumpHeight(AbstractHorse horse) {
        double strength = horse.getJumpStrength();
        return -0.1817584952 * strength * strength * strength
                + 3.689713992 * strength * strength
                + 2.128599134 * strength
                - 0.343930367;
    }

    private String formatType(Entity entity) {
        String type = entity.getType().name().toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(type.charAt(0)) + type.substring(1);
    }
}
