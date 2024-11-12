package org.example.task5.utils.generator;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdGenerator {
    private static final int MAX_ID = 100000;
    private final Random random;

    public int generateId() {
        return random.nextInt(MAX_ID);
    }
}
