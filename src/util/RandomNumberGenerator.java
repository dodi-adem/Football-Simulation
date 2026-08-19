package util;

import java.util.List;
import java.util.Random;

public class RandomNumberGenerator {

    private final Random random;

    public RandomNumberGenerator() {
        this.random = new Random();
    }

    public RandomNumberGenerator(long seed) {
        this.random = new Random(seed);
    }

    public int nextInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(
                "min (" + min + ") cannot be greater than max ("
                + max + ").");
        }
        return min + random.nextInt(max - min + 1);
    }

    public double nextDouble() {
        return random.nextDouble();
    }

    public boolean rollChance(double probability) {
        return random.nextDouble() < probability;
    }

    public <T> T pickRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot pick from an empty list.");
        }
        return list.get(random.nextInt(list.size()));
    }
}