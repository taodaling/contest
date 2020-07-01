package template.utils;

import template.primitve.generated.datastructure.*;
import template.rand.RandomWrapper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Be careful. the radix sort will regard the number in sequence as unsigned integer, it means -1 > -2 > 2 > 1.
 * <br>
 */
public class CompareUtils {
    private CompareUtils() {
    }

    public static int middleOf(int a, int b, int c) {
        if (b <= a && a <= c) {
            return a;
        }
        if (a <= b && b <= c) {
            return b;
        }
        return c;
    }

    public static int minOf(int[] a, int l, int r) {
        int x = a[l];
        for (int i = l; i <= r; i++) {
            x = Math.min(x, a[i]);
        }
        return x;
    }

    public static int maxOf(int[] a, int l, int r) {
        int x = a[l];
        for (int i = l; i <= r; i++) {
            x = Math.max(x, a[i]);
        }
        return x;
    }

    public static long minOf(long[] a, int l, int r) {
        long x = a[l];
        for (int i = l; i <= r; i++) {
            x = Math.min(x, a[i]);
        }
        return x;
    }

    public static long maxOf(long[] a, int l, int r) {
        long x = a[l];
        for (int i = l; i <= r; i++) {
            x = Math.max(x, a[i]);
        }
        return x;
    }

    public static <T> T minOf(T[] a, int l, int r, Comparator<T> comp) {
        T x = a[l];
        for (int i = l; i <= r; i++) {

            x = min(x, a[i], comp);
        }
        return x;
    }

    public static <T> T maxOf(T[] a, int l, int r, Comparator<T> comp) {
        T x = a[l];
        for (int i = l; i <= r; i++) {
            x = max(x, a[i], comp);
        }
        return x;
    }

    public static <T> int compareArray(T[] a, T[] b, int al, int ar, int bl, int br, Comparator<T> comp) {
        for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
            int c = comp.compare(a[i], b[j]);
            if (c != 0) {
                return c;
            }
        }
        return (ar - al) - (br - bl);
    }

    public static double compare(double a, double b, double prec) {
        return Math.abs(a - b) < prec ? 0 : Double.compare(a, b);
    }

    public static int compareArray(char[] a, int al, int ar, char[] b, int bl, int br) {
        for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
            if (a[i] != b[j]) {
                return a[i] - b[j];
            }
        }
        return (ar - al) - (br - bl);
    }

    public static int compareArray(int[] a, int al, int ar, int[] b, int bl, int br) {
        for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
            if (a[i] != b[j]) {
                return a[i] - b[j];
            }
        }
        return (ar - al) - (br - bl);
    }

    public static int compareArray(long[] a, int al, int ar, long[] b, int bl, int br) {
        for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
            if (a[i] != b[j]) {
                return Long.compare(a[i], b[j]);
            }
        }
        return (ar - al) - (br - bl);
    }

    public static <T> T max(T a, T b, Comparator<T> comp) {
        return comp.compare(a, b) >= 0 ? a : b;
    }

    public static <T> T min(T a, T b, Comparator<T> comp) {
        return comp.compare(a, b) <= 0 ? a : b;
    }

    public static <T> boolean equal(T a, T b, Comparator<T> comp) {
        return comp.compare(a, b) == 0;
    }

    private static final int THRESHOLD = 4;

    public static void partition(int[] data, IntPredicate predicate) {
        int l = 0;
        int r = data.length - 1;
        while (l < r) {
            if (predicate.test(data[l])) {
                SequenceUtils.swap(data, l, r);
                r--;
            } else {
                l++;
            }
        }
    }

    public static void partition(long[] data, LongPredicate predicate) {
        int l = 0;
        int r = data.length - 1;
        while (l < r) {
            if (predicate.test(data[l])) {
                SequenceUtils.swap(data, l, r);
                r--;
            } else {
                l++;
            }
        }
    }

    public static <T> void partition(T[] data, Predicate<T> predicate) {
        int l = 0;
        int r = data.length - 1;
        while (l < r) {
            if (predicate.test(data[l])) {
                SequenceUtils.swap(data, l, r);
                r--;
            } else {
                l++;
            }
        }
    }

    public static <T> void insertSort(T[] data, Comparator<T> cmp, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int j = i;
            T val = data[i];
            while (j > l && cmp.compare(data[j - 1], val) > 0) {
                data[j] = data[j - 1];
                j--;
            }
            data[j] = val;
        }
    }

    public static void insertSort(int[] data, IntegerComparator cmp, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int j = i;
            int val = data[i];
            while (j > l && cmp.compare(data[j - 1], val) > 0) {
                data[j] = data[j - 1];
                j--;
            }
            data[j] = val;
        }
    }

    public static void insertSort(long[] data, LongComparator cmp, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int j = i;
            long val = data[i];
            while (j > l && cmp.compare(data[j - 1], val) > 0) {
                data[j] = data[j - 1];
                j--;
            }
            data[j] = val;
        }
    }

    public static void insertSort(double[] data, DoubleComparator cmp, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int j = i;
            double val = data[i];
            while (j > l && cmp.compare(data[j - 1], val) > 0) {
                data[j] = data[j - 1];
                j--;
            }
            data[j] = val;
        }
    }

    public static int theKthSmallestElement(int[] data, IntegerComparator cmp, int f, int t, int k) {
        if (t - f <= THRESHOLD) {
            insertSort(data, cmp, f, t - 1);
            return data[f + k - 1];
        }
        SequenceUtils.swap(data, f, RandomWrapper.INSTANCE.nextInt(f, t - 1));
        int l = f;
        int r = t;
        int m = l + 1;
        while (m < r) {
            int c = cmp.compare(data[m], data[l]);
            if (c == 0) {
                m++;
            } else if (c < 0) {
                SequenceUtils.swap(data, l, m);
                l++;
                m++;
            } else {
                SequenceUtils.swap(data, m, --r);
            }
        }
        if (l - f >= k) {
            return theKthSmallestElement(data, cmp, f, l, k);
        } else if (m - f >= k) {
            return data[l];
        }
        return theKthSmallestElement(data, cmp, m, t, k - (m - f));
    }

    public static <T> T theKthSmallestElement(T[] data, Comparator<T> cmp, int f, int t, int k) {
        if (t - f <= THRESHOLD) {
            insertSort(data, cmp, f, t - 1);
            return data[f + k - 1];
        }
        SequenceUtils.swap(data, f, RandomWrapper.INSTANCE.nextInt(f, t - 1));
        int l = f;
        int r = t;
        int m = l + 1;
        while (m < r) {
            int c = cmp.compare(data[m], data[l]);
            if (c == 0) {
                m++;
            } else if (c < 0) {
                SequenceUtils.swap(data, l, m);
                l++;
                m++;
            } else {
                SequenceUtils.swap(data, m, --r);
            }
        }
        if (l - f >= k) {
            return theKthSmallestElement(data, cmp, f, l, k);
        } else if (m - f >= k) {
            return data[l];
        }
        return theKthSmallestElement(data, cmp, m, t, k - (m - f));
    }

    public static <T> void quickSort(T[] data, Comparator<T> cmp, int f, int t) {
        if (t - f <= THRESHOLD) {
            insertSort(data, cmp, f, t - 1);
            return;
        }
        SequenceUtils.swap(data, f, RandomWrapper.INSTANCE.nextInt(f, t - 1));
        int l = f;
        int r = t;
        int m = l + 1;
        while (m < r) {
            int c = cmp.compare(data[m], data[l]);
            if (c == 0) {
                m++;
            } else if (c < 0) {
                SequenceUtils.swap(data, l, m);
                l++;
                m++;
            } else {
                SequenceUtils.swap(data, m, --r);
            }
        }
        quickSort(data, cmp, f, l);
        quickSort(data, cmp, m, t);
    }

    public static void quickSort(int[] data, IntegerComparator cmp, int f, int t) {
        if (t - f <= THRESHOLD) {
            insertSort(data, cmp, f, t - 1);
            return;
        }
        SequenceUtils.swap(data, f, RandomWrapper.INSTANCE.nextInt(f, t - 1));
        int l = f;
        int r = t;
        int m = l + 1;
        while (m < r) {
            int c = cmp.compare(data[m], data[l]);
            if (c == 0) {
                m++;
            } else if (c < 0) {
                SequenceUtils.swap(data, l, m);
                l++;
                m++;
            } else {
                SequenceUtils.swap(data, m, --r);
            }
        }
        quickSort(data, cmp, f, l);
        quickSort(data, cmp, m, t);
    }

    public static void quickSort(long[] data, LongComparator cmp, int f, int t) {
        if (t - f <= THRESHOLD) {
            insertSort(data, cmp, f, t - 1);
            return;
        }
        SequenceUtils.swap(data, f, RandomWrapper.INSTANCE.nextInt(f, t - 1));
        int l = f;
        int r = t;
        int m = l + 1;
        while (m < r) {
            int c = cmp.compare(data[m], data[l]);
            if (c == 0) {
                m++;
            } else if (c < 0) {
                SequenceUtils.swap(data, l, m);
                l++;
                m++;
            } else {
                SequenceUtils.swap(data, m, --r);
            }
        }
        quickSort(data, cmp, f, l);
        quickSort(data, cmp, m, t);
    }

    public static void quickSort(double[] data, DoubleComparator cmp, int f, int t) {
        if (t - f <= THRESHOLD) {
            insertSort(data, cmp, f, t - 1);
            return;
        }
        SequenceUtils.swap(data, f, RandomWrapper.INSTANCE.nextInt(f, t - 1));
        int l = f;
        int r = t;
        int m = l + 1;
        while (m < r) {
            int c = cmp.compare(data[m], data[l]);
            if (c == 0) {
                m++;
            } else if (c < 0) {
                SequenceUtils.swap(data, l, m);
                l++;
                m++;
            } else {
                SequenceUtils.swap(data, m, --r);
            }
        }
        quickSort(data, cmp, f, l);
        quickSort(data, cmp, m, t);
    }

    private static final int[] BUF8 = new int[1 << 8];
    private static final IntegerArrayList INT_LIST_A = new IntegerArrayList();
    private static final LongArrayList LONG_LIST_A = new LongArrayList();

    public static void ensureIntSpace(int n) {
        INT_LIST_A.ensureSpace(n);
    }

    public static void ensureLongSpace(int n) {
        LONG_LIST_A.ensureSpace(n);
    }

    public static void radixSort(long[] data, int l, int r) {
        LONG_LIST_A.clear();
        LONG_LIST_A.ensureSpace(r - l + 1);

        int n = r - l + 1;
        for (int i = 0; i < 8; i += 2) {
            radixSort0(data, l, r, LONG_LIST_A.getData(), 0, BUF8, i * 8);
            radixSort0(LONG_LIST_A.getData(), 0, n - 1, data, l, BUF8, (i + 1) * 8);
        }
    }

    private static void radixSort0(long[] data, int dl, int dr, long[] output, int ol, int[] buf, int rightShift) {
        Arrays.fill(buf, 0);
        int mask = buf.length - 1;
        for (int i = dl; i <= dr; i++) {
            buf[(int) ((data[i] >>> rightShift) & mask)]++;
        }
        for (int i = 1; i < buf.length; i++) {
            buf[i] += buf[i - 1];
        }
        for (int i = dr; i >= dl; i--) {
            output[ol + (--buf[(int) ((data[i] >>> rightShift) & mask)])] = data[i];
        }
    }

    public static void radixSort(int[] data, int l, int r, IntToIntFunction func) {
        INT_LIST_A.clear();
        INT_LIST_A.ensureSpace(r - l + 1);

        int n = r - l + 1;
        for (int i = 0; i < 4; i += 2) {
            radixSort0(data, l, r, INT_LIST_A.getData(), 0, BUF8, i * 8, func);
            radixSort0(INT_LIST_A.getData(), 0, n - 1, data, l, BUF8, (i + 1) * 8, func);
        }
    }

    private static void radixSort0(int[] data, int dl, int dr, int[] output, int ol, int[] buf, int rightShift, IntToIntFunction func) {
        Arrays.fill(buf, 0);
        int mask = buf.length - 1;
        for (int i = dl; i <= dr; i++) {
            buf[((func.apply(data[i]) >>> rightShift) & mask)]++;
        }
        for (int i = 1; i < buf.length; i++) {
            buf[i] += buf[i - 1];
        }
        for (int i = dr; i >= dl; i--) {
            output[ol + (--buf[((func.apply(data[i]) >>> rightShift) & mask)])] = data[i];
        }
    }

    public static void radixSortLong(int[] data, int l, int r, IntToLongFunction func) {
        INT_LIST_A.clear();
        INT_LIST_A.ensureSpace(r - l + 1);

        int n = r - l + 1;
        for (int i = 0; i < 8; i += 2) {
            radixSortLong0(data, l, r, INT_LIST_A.getData(), 0, BUF8, i * 8, func);
            radixSortLong0(INT_LIST_A.getData(), 0, n - 1, data, l, BUF8, (i + 1) * 8, func);
        }
    }

    private static void radixSortLong0(int[] data, int dl, int dr, int[] output, int ol, int[] buf, int rightShift, IntToLongFunction func) {
        Arrays.fill(buf, 0);
        int mask = buf.length - 1;
        for (int i = dl; i <= dr; i++) {
            buf[(int) ((func.apply(data[i]) >>> rightShift) & mask)]++;
        }
        for (int i = 1; i < buf.length; i++) {
            buf[i] += buf[i - 1];
        }
        for (int i = dr; i >= dl; i--) {
            output[ol + (--buf[(int) ((func.apply(data[i]) >>> rightShift) & mask)])] = data[i];
        }
    }

    public static void radixSort(int[] data, int l, int r) {
        INT_LIST_A.clear();
        INT_LIST_A.ensureSpace(r - l + 1);

        int n = r - l + 1;
        for (int i = 0; i < 8; i += 2) {
            radixSort0(data, l, r, INT_LIST_A.getData(), 0, BUF8, i * 8);
            radixSort0(INT_LIST_A.getData(), 0, n - 1, data, l, BUF8, (i + 1) * 8);
        }
    }

    private static void radixSort0(int[] data, int dl, int dr, int[] output, int ol, int[] buf, int rightShift) {
        Arrays.fill(buf, 0);
        int mask = buf.length - 1;
        for (int i = dl; i <= dr; i++) {
            buf[(int) ((data[i] >>> rightShift) & mask)]++;
        }
        for (int i = 1; i < buf.length; i++) {
            buf[i] += buf[i - 1];
        }
        for (int i = dr; i >= dl; i--) {
            output[ol + (--buf[(int) ((data[i] >>> rightShift) & mask)])] = data[i];
        }
    }

    public static void mergeAscending(int[] a, int al, int ar, int[] b, int bl, int br, int[] c, int cl) {
        while (al <= ar || bl <= br) {
            if (bl > br || (al <= ar && a[al] <= b[bl])) {
                c[cl++] = a[al++];
            } else {
                c[cl++] = b[bl++];
            }
        }
    }

    public static void mergeAscending(int[] a, int al, int ar, int[] b, int bl, int br, int[] c, int cl, IntegerComparator comparator) {
        while (al <= ar || bl <= br) {
            if (bl > br || (al <= ar && comparator.compare(a[al], b[bl]) <= 0)) {
                c[cl++] = a[al++];
            } else {
                c[cl++] = b[bl++];
            }
        }
    }

    public static void mergeDescending(int[] a, int al, int ar, int[] b, int bl, int br, int[] c, int cl) {
        while (al <= ar || bl <= br) {
            if (bl > br || (al <= ar && a[al] >= b[bl])) {
                c[cl++] = a[al++];
            } else {
                c[cl++] = b[bl++];
            }
        }
    }

    public static <T> void mergeAscending(T[] a, int al, int ar, T[] b, int bl, int br, T[] c, int cl, Comparator<T> comp) {
        while (al <= ar || bl <= br) {
            if (bl > br || (al <= ar && comp.compare(a[al], b[bl]) <= 0)) {
                c[cl++] = a[al++];
            } else {
                c[cl++] = b[bl++];
            }
        }
    }

    public static boolean notStrictAscending(long[] data, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            if (data[i] < data[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public static boolean notStrictAscending(int[] data, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            if (data[i] < data[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public static boolean strictAscending(int[] data, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            if (data[i] <= data[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public static boolean strictAscending(long[] data, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            if (data[i] <= data[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public static boolean notStrictDescending(int[] data, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            if (data[i] > data[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public static boolean strictDescending(int[] data, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            if (data[i] >= data[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
