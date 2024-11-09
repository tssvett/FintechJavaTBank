package org.example.task11.pattern.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.initializer.Initializer;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class InitializeCommand implements Command {
    private final Initializer dataInitializer;

    @Override
    public void execute() {
        log.info("Pattern command is working!");
        dataInitializer.initializeDatabase();
        log.info("Pattern command is finished!");
    }
}
