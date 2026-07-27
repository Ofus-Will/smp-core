package org.ofus.core.feature.chestsort;

import org.bukkit.event.Listener;
import org.ofus.core.feature.PluginFeature;

import java.util.List;

public class ChestSortFeature implements PluginFeature {

    @Override
    public List<Listener> listeners() {
        return List.of(new ChestSortListener());
    }
}
