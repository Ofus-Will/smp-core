package org.ofus.core.feature.pets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sittable;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Comparator;
import java.util.List;

public class PetAttackTask {

    private static final long INTERVAL_TICKS = 20L;

    private final JavaPlugin plugin;
    private final int radius;

    private BukkitTask task;

    public PetAttackTask(JavaPlugin plugin, int radius) {
        this.plugin = plugin;
        this.radius = radius;
    }

    public void start() {
        if (radius <= 0 || task != null) return;

        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, INTERVAL_TICKS, INTERVAL_TICKS);
    }

    public void stop() {
        if (task == null) return;

        task.cancel();
        task = null;
    }

    private void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<Monster> targets = findTargets(player);
            if (targets.isEmpty()) continue;

            for (Wolf wolf : findWolves(player)) {
                if (!canAttack(wolf)) continue;

                Monster target = nearestTarget(wolf, targets);
                if (target != null) wolf.setTarget(target);
            }
        }
    }

    private List<Wolf> findWolves(Player player) {
        return player.getNearbyEntities(radius, radius, radius).stream()
                .filter(Wolf.class::isInstance)
                .map(Wolf.class::cast)
                .filter(Wolf::isTamed)
                .filter(wolf -> player.getUniqueId().equals(wolf.getOwnerUniqueId()))
                .toList();
    }

    private List<Monster> findTargets(Player player) {
        return player.getNearbyEntities(radius, radius, radius).stream()
                .filter(Monster.class::isInstance)
                .map(Monster.class::cast)
                .filter(this::canBeTargeted)
                .toList();
    }

    private Monster nearestTarget(Wolf wolf, List<Monster> targets) {
        return targets.stream()
                .min(Comparator.comparingDouble(target -> target.getLocation().distanceSquared(wolf.getLocation())))
                .orElse(null);
    }

    private boolean canAttack(Wolf wolf) {
        if (!wolf.isValid() || wolf.isDead()) return false;
        if (wolf instanceof Sittable sittable && sittable.isSitting()) return false;

        return true;
    }

    private boolean canBeTargeted(Entity entity) {
        if (!entity.isValid() || entity.isDead()) return false;
        if (entity instanceof Creeper) return false;

        return entity instanceof LivingEntity livingEntity && livingEntity.getHealth() > 0;
    }
}