package org.ofus.core.feature.grave;

import io.papermc.paper.datacomponent.item.ResolvableProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GraveManager {

    private final Map<GraveKey, GraveGUI> graves = new HashMap<>();
    private final GraveRepository repository;

    public GraveManager(GraveRepository repository) {
        this.repository = repository;
    }

    public void load() {
        graves.clear();

        for (GraveData data : repository.getAll()) {
            Block block = data.location().getBlock();
            block.setType(Material.PLAYER_HEAD);

            GraveGUI grave = new GraveGUI(
                    data.id(),
                    data.owner(),
                    data.ownerName(),
                    data.location(),
                    this,
                    data.items(),
                    data.experience()
            );

            graves.put(GraveKey.from(data.location()), grave);
        }
    }

    public GraveGUI create(Player player, Location location, ItemStack[] items, int experience) {
        GraveKey key = GraveKey.from(location);

        if (graves.containsKey(key)) {
            throw new IllegalStateException("A grave already exists at " + location);
        }

        placeHead(location.getBlock(), player);

        GraveGUI grave = new GraveGUI(
                UUID.randomUUID().toString(),
                player.getUniqueId(),
                player.getName(),
                location,
                this,
                Arrays.asList(items),
                experience
        );

        graves.put(key, grave);
        repository.save(grave);

        return grave;
    }

    public GraveGUI get(Location location) {
        return graves.get(GraveKey.from(location));
    }

    public boolean has(Location location) {
        return graves.containsKey(GraveKey.from(location));
    }

    public void save(GraveGUI grave) {
        repository.save(grave);
    }

    public void saveAll() {
        graves.values().forEach(repository::save);
    }

    public void remove(GraveGUI grave) {
        graves.remove(GraveKey.from(grave.getLocation()));
        repository.delete(grave.getId());

        Block block = grave.getLocation().getBlock();
        if (block.getType() == Material.PLAYER_HEAD) block.setType(Material.AIR);
    }

    @SuppressWarnings("UnstableApiUsage")
    private void placeHead(Block block, Player player) {
        block.setType(Material.PLAYER_HEAD);

        if (!(block.getState() instanceof Skull skull)) return;

        skull.setProfile(ResolvableProfile.resolvableProfile(player.getPlayerProfile()));
        skull.update(true);
    }

    private record GraveKey(UUID world, int x, int y, int z) {

        private static GraveKey from(Location location) {
            return new GraveKey(
                    location.getWorld().getUID(),
                    location.getBlockX(),
                    location.getBlockY(),
                    location.getBlockZ()
            );
        }
    }
}