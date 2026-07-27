package org.ofus.core.feature.hologram;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class HologramManager {

    private final Map<String, Hologram> holograms = new HashMap<>();
    private final HologramRepository repository;

    public HologramManager(HologramRepository repository) {
        this.repository = repository;
    }

    public void load() {
        removeRuntime();

        for (HologramData data : repository.getAll().values()) {
            Hologram hologram = new Hologram(data.id(), data.loc(), data.lines().toArray(String[]::new));
            holograms.put(data.id(), hologram);
        }
    }

    public Hologram create(String id, Location location, String... lines) {
        delete(id);

        Hologram hologram = new Hologram(id, location, lines);
        holograms.put(id, hologram);
        repository.save(hologram.toData());
        return hologram;
    }

    public Hologram get(String id) {
        return holograms.get(id);
    }

    public void update(String id, String... lines) {
        Hologram hologram = get(id);
        if (hologram == null) return;

        hologram.update(lines);
        repository.save(hologram.toData());
    }

    public void teleport(String id, Location location) {
        Hologram hologram = get(id);
        if (hologram == null) return;

        hologram.teleport(location);
        repository.save(hologram.toData());
    }

    public boolean delete(String id) {
        Hologram hologram = holograms.remove(id);
        if (hologram == null) return false;

        hologram.remove();
        repository.delete(id);
        return true;
    }

    public void removeRuntime() {
        holograms.values().forEach(Hologram::remove);
        holograms.clear();
    }

    public Map<String, Hologram> getHolograms() {
        return Map.copyOf(holograms);
    }
}