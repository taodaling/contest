package template.rand;

import java.util.List;
import java.util.Random;

/**
 * Created by dalt on 2018/6/1.
 */
public class Randomized {
    private static Random random = new Random(0);

    public static void shuffle(int[] data) {
        shuffle(data, 0, data.length - 1);
    }

    public static void shuffle(long[] data) {
        shuffle(data, 0, data.length - 1);
    }

    public static void shuffle(double[] data) {
        shuffle(data, 0, data.length - 1);
    }

    public static void shuffle(int[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            int tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void shuffle(char[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            char tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void shuffle(byte[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            byte tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void shuffle(long[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            long tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void shuffle(double[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            double tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static void shuffle(float[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            float tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static <T> void shuffle(T[] data, int from, int to) {
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

    public static void shuffle(char[] data) {
        shuffle(data, 0, data.length - 1);
    }

    public static<T> void shuffle(List<T> list){
        shuffle(list, 0, list.size());
    }

    public static<T> void shuffle(List<T> list, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            T tmp = list.get(i);
            list.set(i, list.get(s));
            list.set(s, tmp);
        }
    }
}
