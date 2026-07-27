package org.ofus.core.util.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ofus.core.util.Texts;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class CommandNode {

    private String name;
    private String usage;
    private String description = "";
    private List<String> aliases = List.of();

    protected CommandNode name(String name) {
        this.name = name;
        return this;
    }

    protected CommandNode usage(String usage) {
        this.usage = usage;
        return this;
    }

    protected CommandNode description(String description) {
        this.description = description;
        return this;
    }

    protected CommandNode aliases(String... aliases) {
        this.aliases = List.of(aliases);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }

    public String getDescription() {
        return description;
    }

    public boolean run(@NotNull CommandSender sender, String @NotNull [] args) {
        if (this instanceof PlayerExecutable executable) {
            if (!(sender instanceof Player player)) {
                return reply(sender, executable.getPlayerOnlyMessage());
            }

            return executable.run(player, args);
        }

        throw new UnsupportedOperationException(getClass().getName() + " must override run(CommandSender, String[])");
    }

    public Collection<String> tabComplete(@NotNull CommandSender sender, String @NotNull [] args) {
        return List.of();
    }

    public final boolean reply(@NotNull CommandSender sender, String message) {
        Texts.send(sender, message);
        return true;
    }

    public final String joinArgs(String[] args, int start) {
        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }
}
