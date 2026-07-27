package org.ofus.core.feature.hologram;

import org.bukkit.Location;

import java.util.List;

public record HologramData(String id, Location loc, List<String> lines) {

    public HologramData {
        loc = loc.clone();
        lines = List.copyOf(lines);
    }

    @Override
    public Location loc() {
        return loc.clone();
    }
}