package org.ofus.core.util.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ParentCommand extends RootCommand {

    private final Map<String, CommandNode> subCommands = new HashMap<>();
    private final List<CommandNode> registeredCommands = new ArrayList<>();

    protected ParentCommand(String name) {
        super(name);
    }

    protected void register(CommandNode... commands) {
        for (CommandNode command : commands) {
            registeredCommands.add(command);

            registerName(command.getName(), command);

            for (String alias : command.getAliases()) {
                registerName(alias, command);
            }
        }
    }

    // avoid duplicate aliases from separate sub commands
    private void registerName(String name, CommandNode command) {
        String key = name.toLowerCase();

        if (subCommands.putIfAbsent(key, command) != null) {
            throw new IllegalArgumentException("Subcommand name or alias already registered: " + name);
        }
    }


    @Override
    public boolean run(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length == 0) return reply(sender, getUsage());

        CommandNode subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) return reply(sender, getUsage());

        boolean success = subCommand.run(sender, Arrays.copyOfRange(args, 1, args.length));
        if (success) return true;

        String usage = subCommand.getUsage();
        return usage == null ? reply(sender, getUsage()) : reply(sender, usage);
    }

    @Override
    public Collection<String> tabComplete(CommandSender sender, String[] args) {
            if (args.length <= 1) {
                String input = args.length == 0 ? "" : args[0].toLowerCase();

                return registeredCommands.stream()
                        .map(CommandNode::getName)
                        .filter(name -> name.toLowerCase().startsWith(input))
                        .sorted()
                        .toList();
            }

            CommandNode subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand == null) return List.of();

            return subCommand.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public String getUsage() {
        return "/" + getName() + " <" +
                registeredCommands.stream()
                        .map(CommandNode::getName)
                        .sorted()
                        .collect(Collectors.joining("|")) +
                ">";
    }
}
