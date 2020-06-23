package template.rand;

import java.util.Random;

public class RandomWrapper {
    private Random random;

    public RandomWrapper() {
        this(new Random());
    }

    public RandomWrapper(Random random) {
        this.random = random;
    }

    public int nextInt(int l, int r) {
        return random.nextInt(r - l + 1) + l;
    }

    public double nextDouble(double l, double r) {
        return random.nextDouble() * (r - l) + l;
    }

    public long nextLong(long l, long r) {
        return (long) nextDouble(l, r);
    }

    public String nextString(char l, char r, int len) {
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            builder.append((char) nextInt(l, r));
        }
        return builder.toString();
    }

    public Random getRandom() {
        return random;
    }


    public static final RandomWrapper INSTANCE = new RandomWrapper(new Random());
}
