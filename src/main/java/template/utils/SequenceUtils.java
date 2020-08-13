package template.utils;

import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SequenceUtils {
    /**
     * Find a index k while data[k] >= x and data[k - 1] < x. If it doesn't exist, r+1 will return.
     */
    public static int upperBound(int[] data, int x, int l, int r) {
        int excess = r + 1;
        while (l < r) {
            int m = (l + r) >>> 1;
            if (data[m] < x) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return data[l] >= x ? l : excess;
    }

    /**
     * Find a index k while f(data[k]) >= 0 and f(data[k - 1]) < 0. If it doesn't exist, r+1 will return.
     */
    public static int upperBound(int[] data, int l, int r, IntToIntegerFunction f) {
        int excess = r + 1;
        while (l < r) {
            int m = (l + r) >>> 1;
            if (f.apply(data[m]) < 0) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return f.apply(data[l]) >= 0 ? l : excess;
    }

    /**
     * Find a index k while data[k] <= x and data[k + 1] > x. If it doesn't exist, l-1 will return.
     */
    public static int lowerBound(int[] data, int l, int r, IntToIntegerFunction f) {
        int excess = l - 1;
        while (l < r) {
            int m = (l + r + 1) >>> 1;
            if (f.apply(data[m]) <= 0) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return f.apply(data[l]) <= 0 ? l : excess;
    }

    /**
     * Find a index k while data[k] >= x and data[k - 1] < x. If it doesn't exist, r+1 will return.
     */
    public static <T> int upperBound(T[] data, T x, int l, int r, Comparator<T> comp) {
        int excess = r + 1;
        while (l < r) {
            int m = (l + r) >>> 1;
            if (comp.compare(data[m], x) < 0) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return comp.compare(data[l], x) >= 0 ? l : excess;
    }

    /**
     * Find a index k while data[k] >= x and data[k - 1] < x. If it doesn't exist, r+1 will return.
     */
    public static <T> int upperBound(List<T> data, T x, int l, int r, Comparator<T> comp) {
        int excess = r + 1;
        while (l < r) {
            int m = (l + r) >>> 1;
            if (comp.compare(data.get(m), x) < 0) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return comp.compare(data.get(l), x) >= 0 ? l : excess;
    }

    /**
     * Find a index k while data[k] <= x and data[k + 1] > x. If it doesn't exist, l-1 will return.
     */
    public static int lowerBound(int[] data, int x, int l, int r) {
        int excess = l - 1;
        while (l < r) {
            int m = (l + r + 1) >>> 1;
            if (data[m] <= x) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return data[l] <= x ? l : excess;
    }

    /**
     * Find a index k while data[k] >= x and data[k - 1] < x. If it doesn't exist, r+1 will return.
     */
    public static int upperBound(long[] data, long x, int l, int r) {
        int excess = r + 1;
        while (l < r) {
            int m = (l + r) >>> 1;
            if (data[m] < x) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return data[l] >= x ? l : excess;
    }

    /**
     * Find a index k while data[k] <= x and data[k + 1] > x. If it doesn't exist, l-1 will return.
     */
    public static int lowerBound(long[] data, long x, int l, int r) {
        int excess = l - 1;
        while (l < r) {
            int m = (l + r + 1) >>> 1;
            if (data[m] <= x) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return data[l] <= x ? l : excess;
    }

    /**
     * Find a index k while data[k] >= x and data[k - 1] < x. If it doesn't exist, r+1 will return.
     */
    public static int upperBound(double[] data, double x, int l, int r) {
        int excess = r + 1;
        while (l < r) {
            int m = (l + r) >>> 1;
            if (data[m] < x) {
                l = m + 1;
            } else {
                r = m;
            }
        }
        return data[l] >= x ? l : excess;
    }

    /**
     * Find a index k while data[k] <= x and data[k + 1] > x. If it doesn't exist, l-1 will return.
     */
    public static int lowerBound(double[] data, double x, int l, int r) {
        int excess = l - 1;
        while (l < r) {
            int m = (l + r + 1) >>> 1;
            if (data[m] <= x) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return data[l] <= x ? l : excess;
    }

    /**
     * Find a index k while data[k] <= x and data[k + 1] > x. If it doesn't exist, l-1 will return.
     */
    public static <T> int lowerBound(T[] data, T x, int l, int r, Comparator<T> comp) {
        int excess = l - 1;
        while (l < r) {
            int m = (l + r + 1) >>> 1;
            if (comp.compare(data[m], x) <= 0) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return comp.compare(data[l], x) <= 0 ? l : excess;
    }


    /**
     * Find a index k while data[k] <= x and data[k + 1] > x. If it doesn't exist, l-1 will return.
     */
    public static <T> int lowerBound(List<T> data, T x, int l, int r, Comparator<T> comp) {
        int excess = l - 1;
        while (l < r) {
            int m = (l + r + 1) >>> 1;
            if (comp.compare(data.get(m), x) <= 0) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return comp.compare(data.get(l), x) <= 0 ? l : excess;
    }

    public static int countElementOccurrenceInSortedArray(int[] data, int x, int l, int r) {
        int lBound = upperBound(data, x, l, r);
        if (lBound > r || data[lBound] != x) {
            return 0;
        }
        return lowerBound(data, x, l, r) - lBound + 1;
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

    public static <T> void swap(boolean[] data, int i, int j) {
        boolean tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void swap(IntegerArrayList data, int i, int j) {
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

    public static void deepFill(Object array, boolean val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof boolean[]) {
            boolean[] intArray = (boolean[]) array;
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

    /**
     * 旋转区间[l,r],使得原本在l的元素落在to处
     */
    public static <T> void rotate(List<T> list, int l, int r, int to) {
        SequenceUtils.reverse(list, l, r);
        SequenceUtils.reverse(list, l, to - 1);
        SequenceUtils.reverse(list, to, r);
    }

    /**
     * 旋转区间[l,r],使得原本在l的元素落在to处
     */
    public static void rotate(int[] list, int l, int r, int to) {
        SequenceUtils.reverse(list, l, r);
        SequenceUtils.reverse(list, l, to - 1);
        SequenceUtils.reverse(list, to, r);
    }

    /**
     * 旋转区间[l,r],使得原本在l的元素落在to处
     */
    public static void rotate(long[] list, int l, int r, int to) {
        SequenceUtils.reverse(list, l, r);
        SequenceUtils.reverse(list, l, to - 1);
        SequenceUtils.reverse(list, to, r);
    }


    /**
     * 旋转区间[l,r],使得原本在l的元素落在to处
     */
    public static <T> void rotate(T[] list, int l, int r, int to) {
        SequenceUtils.reverse(list, l, r);
        SequenceUtils.reverse(list, l, to - 1);
        SequenceUtils.reverse(list, to, r);
    }

    public static <T> void reverse(List<T> data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static <T> void reverse(List<T> data) {
        reverse(data, 0, data.size() - 1);
    }

    public static <T> void reverse(T[] data){
        reverse(data, 0, data.length - 1);
    }

    public static <T> void reverse(T[] data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void reverse(char[] data, int l, int r) {
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

    public static void reverse(boolean[] data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void reverse(int[] data) {
        reverse(data, 0, data.length - 1);
    }

    public static void reverse(long[] data) {
        reverse(data, 0, data.length - 1);
    }

    public static void reverse(double[] data) {
        reverse(data, 0, data.length - 1);
    }

    public static void reverse(char[] data) {
        reverse(data, 0, data.length - 1);
    }

    public static void reverse(boolean[] data) {
        reverse(data, 0, data.length - 1);
    }


    public static void reverse(long[] data, int l, int r) {
        while (l < r) {
            swap(data, l, r);
            l++;
            r--;
        }
    }

    public static void reverse(double[] data, int l, int r) {
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
            if (Objects.equals(array[i], val)) {
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

    public static boolean equal(long[] a, int al, int ar, long[] b, int bl, int br) {
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

    public static boolean equal(long[] a, long[] b) {
        return equal(a, 0, a.length - 1, b, 0, b.length - 1);
    }

    public static boolean equal(double[] a, int al, int ar, double[] b, int bl, int br) {
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
