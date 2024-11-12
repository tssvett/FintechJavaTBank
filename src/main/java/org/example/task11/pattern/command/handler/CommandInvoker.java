package org.example.task11.pattern.command.handler;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.example.task11.pattern.command.Command;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandInvoker {
    private final Command command;
    private final ScheduledExecutorService scheduledThreadPool;
    private final Duration dataInitializationPeriod;

    public void initializeDatabase() {
        scheduledThreadPool.scheduleAtFixedRate(command::execute, 0,
                dataInitializationPeriod.toSeconds(), TimeUnit.SECONDS);
    }
}
