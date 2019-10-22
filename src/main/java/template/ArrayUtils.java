package template;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ArrayUtils {
    public static <T> void swap(T[] data, int i, int j) {
        T tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(char[] data, int i, int j) {
        char tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(long[] data, int i, int j) {
        long tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(double[] data, int i, int j) {
        double tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void deepFill(Object array, int val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof int[]) {
            int[] intArray = (int[]) array;
            Arrays.fill(intArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

    public static void deepFill(Object array, long val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof long[]) {
            long[] longArray = (long[]) array;
            Arrays.fill(longArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

    public static void deepFill(Object array, double val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof double[]) {
            double[] doubleArray = (double[]) array;
            Arrays.fill(doubleArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

    public static void deepFill(Object array, float val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof float[]) {
            float[] floatArray = (float[]) array;
            Arrays.fill(floatArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

    public static void deepFill(Object array, char val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof char[]) {
            char[] charArray = (char[]) array;
            Arrays.fill(charArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

    public static void deepFill(Object array, byte val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof byte[]) {
            byte[] byteArray = (byte[]) array;
            Arrays.fill(byteArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

    public static <T> int min(T[] data, int from, int to, Comparator<T> cmp) {
        int m = from;
        for (int i = from + 1; i < to; i++) {
            if (cmp.compare(data[m], data[i]) > 0) {
                m = i;
            }
        }
        return m;
    }

    public static int indexOfMin(int[] data, int from, int to) {
        int m = from;
        for (int i = from + 1; i < to; i++) {
            if (data[m] > data[i]) {
                m = i;
            }
        }
        return m;
    }

    public static int indexOfMax(int[] data, int from, int to) {
        int m = from;
        for (int i = from + 1; i < to; i++) {
            if (data[m] < data[i]) {
                m = i;
            }
        }
        return m;
    }

    public static <T> void move(T[] data, int from, int to, int step) {
        int len = to - from;
        step = len - (step % len + len) % len;
        Object[] buf = new Object[len];
        for (int i = 0; i < len; i++) {
            buf[i] = data[(i + step) % len + from];
        }
        System.arraycopy(buf, 0, data, from, len);
    }

    private static int gcd(int a, int b) {
        return a >= b ? gcd0(a, b) : gcd0(b, a);
    }

    private static int gcd0(int a, int b) {
        return b == 0 ? a : gcd0(b, a % b);
    }

    public static <T> void rotate(List<T> list, int l, int newBeg, int r) {
        int offset = l;
        int len = r - l + 1;
        int step = len - (newBeg - l);
        int g = gcd0(newBeg, r - l + 1);
        for (int i = 0; i < g; i++) {
            T take = list.get(i + offset);
            int ni = i;
            while ((ni = (ni + step) % len) != i) {
                T tmp = list.get(ni + offset);
                list.set(ni + offset, take);
                take = tmp;
            }
            list.set(i + offset, take);
        }
    }

    public static <T> void rotate(T[] list, int l, int newBeg, int r) {
        int offset = l;
        int len = r - l + 1;
        int step = len - (newBeg - l);
        int g = gcd0(newBeg, r - l + 1);
        for (int i = 0; i < g; i++) {
            T take = list[i + offset];
            int ni = i;
            while ((ni = (ni + step) % len) != i) {
                T tmp = list[ni + offset];
                list[ni + offset] = take;
                take = tmp;
            }
            list[i + offset] = take;
        }
    }

    public static <T> void reverse(T[] data, int f, int t) {
        int l = f, r = t - 1;
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void reverse(int[] data, int f, int t) {
        int l = f, r = t - 1;
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void copy(Object[] src, Object[] dst, int srcf, int dstf, int len) {
        if (len < 8) {
            for (int i = 0; i < len; i++) {
                dst[dstf + i] = src[srcf + i];
            }
        } else {
            System.arraycopy(src, srcf, dst, dstf, len);
        }
    }

    public static void fill(int[][] x, int val) {
        for (int[] v : x) {
            Arrays.fill(v, val);
        }
    }

    public static void fill(int[][][] x, int val) {
        for (int[][] v : x) {
            fill(v, val);
        }
    }
}
