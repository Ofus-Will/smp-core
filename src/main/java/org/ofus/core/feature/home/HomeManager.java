package org.ofus.core.feature.home;

import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class HomeManager {

    private static final Pattern HOME_NAME_PATTERN = Pattern.compile("^[a-z0-9_-]{1,16}$");

    private final HomesRepository repository;
    private final int maxHomes;

    public HomeManager(HomesRepository repository, int maxHomes) {
        this.repository = repository;
        this.maxHomes = maxHomes;
    }

    public Location getHome(UUID uuid, String name) {
        return repository.getHome(uuid, name);
    }

    public boolean setHome(UUID uuid, String name, Location location) {
        if (repository.getHomes(uuid).size() >= maxHomes && !repository.hasHome(uuid, name)) return false;

        repository.saveHome(uuid, name, location);
        return true;
    }

    public Map<String, Location> getHomes(UUID uuid) {
        return repository.getHomes(uuid);
    }

    public boolean isValidName(String name) {
        return HOME_NAME_PATTERN.matcher(name).matches();
    }

    public int getMaxHomes() {
        return maxHomes;
    }

    public void deleteHome(UUID uuid, String name) {
        repository.deleteHome(uuid, name);
    }

    public void unload(UUID uuid) {
        repository.unload(uuid);
    }

}
