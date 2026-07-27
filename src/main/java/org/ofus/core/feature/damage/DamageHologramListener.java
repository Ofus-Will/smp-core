package org.ofus.core.feature.damage;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.ofus.core.feature.hologram.Hologram;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class DamageHologramListener implements Listener {

    private static final DecimalFormat DAMAGE_FORMAT = new DecimalFormat("0.#");
    private static final long LIFETIME_TICKS = 15L;
    private static final double RISE_PER_TICK = 0.1;
    private static final int TELEPORT_INTERPOLATION_TICKS = 2;

    private final JavaPlugin plugin;
    private final Set<Hologram> holograms = new HashSet<>();

    public DamageHologramListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity damaged)) return;
//        if (damaged instanceof Player) return;
        if (getAttackingPlayer(event.getDamager()) == null) return;
        if (event.getFinalDamage() <= 0) return;

        Location location = getDisplayLocation(damaged);
        Hologram hologram = new Hologram(location, "&c\uD83D\uDDE1 &c&l" + DAMAGE_FORMAT.format(event.getFinalDamage()));
        hologram.setTeleportInterpolation(TELEPORT_INTERPOLATION_TICKS);
        holograms.add(hologram);
        animate(hologram, location);
    }

    public void removeAll() {
        holograms.forEach(Hologram::remove);
        holograms.clear();
    }

    private void animate(Hologram hologram, Location location) {
        new BukkitRunnable() {
            private int ticks;

            @Override
            public void run() {
                if (ticks >= LIFETIME_TICKS) {
                    holograms.remove(hologram);
                    hologram.remove();
                    cancel();
                    return;
                }

                double progress = ticks / (double) LIFETIME_TICKS;
                hologram.setTextOpacity((byte) Math.round(255 * (1 - progress)));
                hologram.teleport(location.add(0, RISE_PER_TICK, 0));
                ticks++;
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private Location getDisplayLocation(LivingEntity entity) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return entity.getLocation().add(
                random.nextDouble(-0.35, 0.35),
                entity.getHeight() + random.nextDouble(0.15, 0.45) - 0.5,
                random.nextDouble(-0.35, 0.35)
        );
    }

    private Player getAttackingPlayer(Entity damager) {
        if (damager instanceof Player player) return player;

        if (damager instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player player) return player;
        }

        return null;
    }
}