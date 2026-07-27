package org.ofus.core.feature.pets;

import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.command.PlayerCommand;

import java.util.List;

public class PetsCommand extends PlayerCommand {

    public PetsCommand() {
        super("pets");

        usage("/pets");
        permission("core.pets", PermissionDefault.TRUE);
        description("Open a list of your loaded pets");
    }

    @Override
    public boolean run(@NotNull Player player, String @NotNull [] args) {
        List<Tameable> pets = PetFinder.findPets(player);

        if (pets.isEmpty()) return reply(player, "&eYou do not have any loaded pets.");

        new PetsGUI(pets).open(player);
        return true;
    }
}
