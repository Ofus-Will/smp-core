package org.ofus.core.feature.pets;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

import java.util.List;

public class PetsCommand extends PlayerCommand {

    private final PetManager petManager;

    public PetsCommand(PetManager petManager) {
        super("pets");
        this.petManager = petManager;

        usage("/pets");
        permission("core.pets", PermissionDefault.TRUE);
        description("Open a list of your pets");
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        List<PetData> pets = petManager.getPets(player);

        if (pets.isEmpty()) return reply(player, "&eYou do not have any known pets.");

        new PetsGUI(petManager, pets).open(player);
        return true;
    }
}