package org.ofus.core.feature.pets;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class PetListener implements Listener {

    private final PetManager petManager;

    public PetListener(PetManager petManager) {
        this.petManager = petManager;
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        savePets(event.getChunk());
    }

    private void savePets(Chunk chunk) {
        for (Entity entity : chunk.getEntities()) {
            if (!(entity instanceof Tameable pet)) continue;
            if (!pet.isTamed() || pet.getOwnerUniqueId() == null) continue;

            petManager.save(pet);
        }
    }
}