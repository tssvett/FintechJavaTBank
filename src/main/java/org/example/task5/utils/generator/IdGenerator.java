package org.example.task5.utils.generator;

import java.util.Random;

public class IdGenerator {
    private static final int MAX_ID = 100000;
    private static final Random random = new Random();

    public static int generateId() {
        return random.nextInt(MAX_ID);
    }
}
