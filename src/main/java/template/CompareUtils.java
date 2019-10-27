package template;

import java.util.Comparator;

public class CompareUtils {
    private CompareUtils() {
    }

    public static int middleOf(int a, int b, int c) {
        int signAB = Integer.compare(a, b);
        int signAC = Integer.compare(a, c);
        int signBC = Integer.compare(b, c);
        if (signAB * signAC <= 0) {
            return a;
        }
        if (signAB * signBC >= 0) {
            return b;
        }
        return c;
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

    public static <T> void insertSort(T[] data, Comparator<T> cmp, int f, int t) {
        for (int i = f + 1; i < t; i++) {
            int j = i;
            T val = data[i];
            while (j > f && cmp.compare(data[j - 1], val) > 0) {
                data[j] = data[j - 1];
                j--;
            }
            data[j] = val;
        }
    }

    public static <T> T theKthSmallestElement(T[] data, Comparator<T> cmp, int f, int t, int k) {
        if (t - f == THRESHOLD) {
            insertSort(data, cmp, f, t);
            return data[f + k - 1];
        }
        SequenceUtils.swap(data, f, Randomized.nextInt(f, t - 1));
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
}
