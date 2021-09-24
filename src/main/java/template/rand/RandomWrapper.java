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

    public RandomWrapper(long seed) {
        this(new Random(seed));
    }

    public int nextInt(int l, int r) {
        return random.nextInt(r - l + 1) + l;
    }

    public int nextInt(int n) {
        return random.nextInt(n);
    }


    public double nextDouble(double l, double r) {
        return random.nextDouble() * (r - l) + l;
    }

    public double nextDouble() {
        return random.nextDouble();
    }

    public long nextLong(long l, long r) {
        return nextLong(r - l + 1) + l;
    }

    public long nextLong(long n) {
        return Math.round(random.nextDouble() * (n - 1));
    }

    public long nextLong() {
        return random.nextLong();
    }

    public String nextString(char l, char r, int len) {
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            builder.append((char) nextInt(l, r));
        }
        return builder.toString();
    }

    public String nextString(char[] s, int len) {
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            builder.append(s[nextInt(0, s.length - 1)]);
        }
        return builder.toString();
    }


    public Random getRandom() {
        return random;
    }

    public int range(int... x) {
        return x[nextInt(0, x.length - 1)];
    }

    public char range(char... x) {
        return x[nextInt(0, x.length - 1)];
    }

    public long range(long... x) {
        return x[nextInt(0, x.length - 1)];
    }

    public <T> T rangeT(T... x) {
        return x[nextInt(0, x.length - 1)];
    }

    public static final RandomWrapper INSTANCE = new RandomWrapper();
}
