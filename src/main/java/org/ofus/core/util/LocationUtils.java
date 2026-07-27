package org.ofus.core.util;

import org.bukkit.Location;

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
}