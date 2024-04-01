package com.suster.user.logging;

import io.netty.handler.logging.LogLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class LoggingService {

    @Inject
    @Channel("log")
    Emitter<ControllerLog> logEmitter;

    public void logInfo(String method, String path, Object request, String message) {
        logEmitter.send(new ControllerLog(UUID.randomUUID(), LogLevel.INFO, LocalDateTime.now(), method, path, request));
        log.info(message);
    }

    public void logWarn(String method, String path, Object request, String message) {
        logEmitter.send(new ControllerLog(UUID.randomUUID(),LogLevel.WARN, LocalDateTime.now(), method, path, request));
        log.warn(message);
    }
}