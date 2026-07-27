package org.ofus.core.util.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public abstract class RootCommand extends CommandNode implements BasicCommand {

    private String permission;
    private PermissionDefault permissionDefault = PermissionDefault.OP;

    protected RootCommand(@NotNull String name) {
        name(name);
    }

    public RootCommand permission(String permission, PermissionDefault defaultValue) {
        this.permission = permission;
        this.permissionDefault = defaultValue;
        return this;
    }

    public RootCommand permission(String permission) {
        return permission(permission, PermissionDefault.OP);
    }

    public RootCommand aliases(String... aliases) {
        super.aliases(aliases);
        return this;
    }

    public RootCommand usage(String usage) {
        super.usage(usage);
        return this;
    }

    public RootCommand description(String description) {
        super.description(description);
        return this;
    }

    public PermissionDefault getPermissionDefault() {
        return permissionDefault;
    }

    @Override
    public String permission() {
        return permission;
    }

    private boolean hasPermission(CommandSender sender) {
        String permission = permission();
        return permission == null || sender.hasPermission(permission);
    }

    @Override
    public final void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();

        if (!hasPermission(sender)) {
            reply(sender, "&cYou can not use this command!");
            return;
        }

        boolean success = run(sender, args);
        if (!success && getUsage() != null) reply(sender, getUsage());
    }

    @Override
    public Collection<String> suggest(CommandSourceStack source, String [] args) {
        return tabComplete(source.getSender(), args);
    }

    @Override
    public Collection<String> tabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
