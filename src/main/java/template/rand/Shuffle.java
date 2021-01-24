package template.rand;

import java.util.Random;

public class Shuffle {
    private Random rw = new Random();
    private long seed;

    public Shuffle() {
        this(System.currentTimeMillis());
    }

    public Shuffle(long seed) {
        this.seed = seed;
    }

    private void prepare() {
        rw.setSeed(seed);
    }

    public void shuffle(int[] x) {
        shuffle(x, x.length);
    }

    public void shuffle(int[] x, int n) {
        prepare();
        for (int i = n - 1; i >= 0; i--) {
            int j = rw.nextInt(i + 1);
            int tmp = x[i];
            x[i] = x[j];
            x[j] = tmp;
        }
    }

    public void shuffle(long[] x) {
        shuffle(x, x.length);
    }

    public void shuffle(long[] x, int n) {
        prepare();
        for (int i = n - 1; i >= 0; i--) {
            int j = rw.nextInt(i + 1);
            long tmp = x[i];
            x[i] = x[j];
            x[j] = tmp;
        }
    }

    public void shuffle(char[] x) {
        shuffle(x, x.length);
    }

    public void shuffle(char[] x, int n) {
        prepare();
        for (int i = n - 1; i >= 0; i--) {
            int j = rw.nextInt(i + 1);
            char tmp = x[i];
            x[i] = x[j];
            x[j] = tmp;
        }
    }

    public void shuffle(double[] x) {
        shuffle(x, x.length);
    }

    public void shuffle(double[] x, int n) {
        prepare();
        for (int i = n - 1; i >= 0; i--) {
            int j = rw.nextInt(i + 1);
            double tmp = x[i];
            x[i] = x[j];
            x[j] = tmp;
        }
    }

    public void shuffle(short[] x) {
        shuffle(x, x.length);
    }

    public void shuffle(short[] x, int n) {
        prepare();
        for (int i = n - 1; i >= 0; i--) {
            int j = rw.nextInt(i + 1);
            short tmp = x[i];
            x[i] = x[j];
            x[j] = tmp;
        }
    }

    public <T> void shuffle(T[] x) {
        shuffle(x, x.length);
    }

    public <T> void shuffle(T[] x, int n) {
        prepare();
        for (int i = n - 1; i >= 0; i--) {
            int j = rw.nextInt(i + 1);
            T tmp = x[i];
            x[i] = x[j];
            x[j] = tmp;
        }
    }
}
