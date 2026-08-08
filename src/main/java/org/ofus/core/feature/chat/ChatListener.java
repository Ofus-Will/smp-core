package org.ofus.core.feature.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.ofus.core.util.Texts;

public final class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        Bukkit.broadcast(
                event.getPlayer().displayName()
                        .append(Texts.parse(": "))
                        .append(event.message())
        );
    }
}