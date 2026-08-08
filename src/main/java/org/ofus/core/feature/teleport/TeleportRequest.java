package org.ofus.core.feature.teleport;

import java.time.Instant;
import java.util.UUID;

public record TeleportRequest(UUID requester, UUID target, Instant createdAt) {
}