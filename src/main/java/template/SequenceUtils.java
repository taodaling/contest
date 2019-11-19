package template;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SequenceUtils {
    public static int floorIndex(int[] data, int x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1) - 1;
        }
        return index;
    }

    public static int floorIndex(long[] data, long x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1) - 1;
        }
        return index;
    }

    public static <T> int floorIndex(T[] data, T x, int l, int r, Comparator<T> comparator) {
        int index = Arrays.binarySearch(data, l, r + 1, x, comparator);
        if (index < 0) {
            return -(index + 1) - 1;
        }
        return index;
    }

    public static <T> int ceilIndex(T[] data, T x, int l, int r, Comparator<T> comparator) {
        int index = Arrays.binarySearch(data, l, r + 1, x, comparator);
        if (index < 0) {
            return -(index + 1);
        }
        return index;
    }

    public static int ceilIndex(int[] data, int x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1);
        }
        return index;
    }

    public static int ceilIndex(long[] data, long x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1);
        }
        return index;
    }

    public static int floorIndex(double[] data, double x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1) - 1;
        }
        return index;
    }

    public static int ceilIndex(double[] data, double x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1);
        }
        return index;
    }

    public static int floorIndex(char[] data, char x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1) - 1;
        }
        return index;
    }

    public static int ceilIndex(char[] data, char x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1);
        }
        return index;
    }

    public static int floorIndex(byte[] data, byte x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1) - 1;
        }
        return index;
    }

    public static int ceilIndex(byte[] data, byte x, int l, int r) {
        int index = Arrays.binarySearch(data, l, r + 1, x);
        if (index < 0) {
            return -(index + 1);
        }
        return index;
    }

    public static int[] wrapArray(int... x) {
        return x;
    }

    public static long[] wrapArray(long... x) {
        return x;
    }

    public static double[] wrapArray(double... x) {
        return x;
    }

    public static byte[] wrapArray(byte... x) {
        return x;
    }

    public static float[] wrapArray(float... x) {
        return x;
    }

    public static char[] wrapArray(char... x) {
        return x;
    }

    public static short[] wrapArray(short... x) {
        return x;
    }

    public static <T> T[] wrapObjectArray(T... x) {
        return x;
    }


    public static <T> void swap(T[] data, int i, int j) {
        T tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(IntList data, int i, int j) {
        int tmp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, tmp);
    }

    public static void swap(char[] data, int i, int j) {
        char tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(byte[] data, int i, int j) {
        byte tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static <T> void swap(List<T> data, int i, int j) {
        T tmp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, tmp);
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

    public static void rotate(int[] list, int l, int newBeg, int r) {
        int offset = l;
        int len = r - l + 1;
        int step = len - (newBeg - l);
        int g = gcd0(newBeg, r - l + 1);
        for (int i = 0; i < g; i++) {
            int take = list[i + offset];
            int ni = i;
            while ((ni = (ni + step) % len) != i) {
                int tmp = list[ni + offset];
                list[ni + offset] = take;
                take = tmp;
            }
            list[i + offset] = take;
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

    public static <T> void reverse(List<T> data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }


    public static <T> void reverse(T[] data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void reverse(int[] data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void reverse(long[] data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void reverse(byte[] data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void copy(Object src, Object dst, int srcf, int dstf, int len) {
        System.arraycopy(src, srcf, dst, dstf, len);
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

    public static int indexOf(int[] array, int l, int r, int val) {
        for (int i = l; i <= r; i++) {
            if (array[i] == val) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(char[] array, int l, int r, char val) {
        for (int i = l; i <= r; i++) {
            if (array[i] == val) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int indexOf(T[] array, int l, int r, T val) {
        for (int i = l; i <= r; i++) {
            if (array[i] == val) {
                return i;
            }
        }
        return -1;
    }

    public static boolean equal(char[] a, int al, int ar, char[] b, int bl, int br) {
        if ((ar - al) != (br - bl)) {
            return false;
        }
        for (int i = al, j = bl; i <= ar; i++, j++) {
            if (a[i] != b[j]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equal(int[] a, int al, int ar, int[] b, int bl, int br) {
        if ((ar - al) != (br - bl)) {
            return false;
        }
        for (int i = al, j = bl; i <= ar; i++, j++) {
            if (a[i] != b[j]) {
                return false;
            }
        }
        return true;
    }
}
