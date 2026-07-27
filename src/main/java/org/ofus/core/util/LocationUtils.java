package org.ofus.core.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class LocationUtils {

    private LocationUtils() {
    }

    public static String format(Location location) {
        return String.format(
                "X: %d, Y: %d, Z: %d",
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

    public static Location getHeadLocation(Player player) {
        return player.getEyeLocation().add(player.getLocation().getDirection().normalize().multiply(1.5));
    }
}