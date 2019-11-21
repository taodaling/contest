package template.rand;

import java.util.Random;

/**
 * Created by dalt on 2018/6/1.
 */
public class Randomized {
    static Random random = new Random();

    public static void randomizedArray(int[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            int tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void randomizedArray(char[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            char tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void randomizedArray(byte[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            byte tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void randomizedArray(long[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            long tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void randomizedArray(double[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            double tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void randomizedArray(float[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            float tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static<T> void randomizedArray(T[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            T tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static int nextInt(int l, int r) {
        return random.nextInt(r - l + 1) + l;
    }
}
