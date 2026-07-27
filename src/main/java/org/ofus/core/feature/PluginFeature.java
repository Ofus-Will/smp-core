package org.ofus.core.feature;

import org.bukkit.event.Listener;
import org.ofus.core.util.command.RootCommand;

import java.util.List;

public interface PluginFeature {

    default void enable() {
    }

    default void disable() {
    }

    default List<Listener> listeners() {
        return List.of();
    }

    default List<RootCommand> commands() {
        return List.of();
    }
}
