package org.ofus.core.feature.pets;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.ofus.core.util.Texts;

public class PetExperienceListener implements Listener {

    private final double healOnKill;

    public PetExperienceListener(double healOnKill) {
        this.healOnKill = healOnKill;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) return;

        Tameable pet = getPetDamager(entity.getLastDamageCause());
        if (pet == null) return;

        if (event.getDroppedExp() == 0) {
            int experience = getPlayerKillExperience(entity);
            if (experience > 0) event.setDroppedExp(experience);
        }

        healPet(pet);

        Player player = Bukkit.getPlayer(pet.getOwnerUniqueId());
        if (player != null) {
            Texts.actionBar(player, "&aYour pet killed a " + entity.getType().toString().toLowerCase());
        }
    }

    private Tameable getPetDamager(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent damageEvent)) return null;
        if (!(damageEvent.getDamager() instanceof Tameable pet) || !pet.isTamed()) return null;

        return pet;
    }

    private void healPet(Tameable pet) {
        if (healOnKill <= 0 || !(pet instanceof LivingEntity livingPet)) return;

        livingPet.setHealth(Math.min(getMaxHealth(livingPet), livingPet.getHealth() + healOnKill));
    }

    private double getMaxHealth(LivingEntity entity) {
        AttributeInstance attribute = entity.getAttribute(Attribute.MAX_HEALTH);
        return attribute == null ? entity.getHealth() : attribute.getValue();
    }

    private int getPlayerKillExperience(LivingEntity entity) {
        return switch (entity.getType()) {
            case BLAZE, BREEZE, ELDER_GUARDIAN, EVOKER, GUARDIAN, PIGLIN_BRUTE -> 10;
            case RAVAGER -> 20;
            case SLIME, MAGMA_CUBE -> getSlimeExperience(entity);
            default -> entity instanceof Monster ? 5 : 0;
        };
    }

    private int getSlimeExperience(LivingEntity entity) {
        if (!(entity instanceof Slime slime)) return 0;

        return slime.getSize();
    }
}