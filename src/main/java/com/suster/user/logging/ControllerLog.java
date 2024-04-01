package com.suster.user.logging;

import io.netty.handler.logging.LogLevel;

import java.time.LocalDateTime;
import java.util.UUID;

public record ControllerLog(
        UUID uuid,
        LogLevel level,
        LocalDateTime timestamp,
        String method,
        String path,
        Object request
) {
}
