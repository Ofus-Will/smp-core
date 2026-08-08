package org.ofus.core.feature.teleport;

import org.bukkit.event.Listener;
import org.ofus.core.feature.PluginFeature;
import org.ofus.core.util.command.RootCommand;

import java.util.List;

public class TeleportRequestFeature implements PluginFeature {

    private final TeleportRequestManager manager = new TeleportRequestManager();

    @Override
    public List<RootCommand> commands() {
        return List.of(
                new TpaCommand(manager),
                new TpAcceptCommand(manager),
                new TpDenyCommand(manager)
        );
    }

    @Override
    public List<Listener> listeners() {
        return List.of();
    }
}