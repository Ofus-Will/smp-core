package org.ofus.core.feature.teleport;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportRequestManager {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(60);

    private final Map<UUID, TeleportRequest> requestsByTarget = new HashMap<>();

    public void createRequest(Player requester, Player target) {
        TeleportRequest request = new TeleportRequest(requester.getUniqueId(), target.getUniqueId(), Instant.now());
        requestsByTarget.put(target.getUniqueId(), request);
    }

    public TeleportRequest takeRequest(Player target) {
        TeleportRequest request = requestsByTarget.remove(target.getUniqueId());
        if (request == null || isExpired(request)) return null;

        return request;
    }

    private boolean isExpired(TeleportRequest request) {
        return request.createdAt().plus(REQUEST_TIMEOUT).isBefore(Instant.now());
    }
}
