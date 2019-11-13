package template;

import java.util.Comparator;

public class CompareUtils {
    private CompareUtils() {}

    public static int middleOf(int a, int b, int c) {
        if (a >= b) {
            if (c <= b) {
                return b;
            }
            if (c >= a) {
                return a;
            }
            return c;
        } else {
            if (c <= a) {
                return a;
            }
            if (c >= b) {
                return b;
            }
            return c;
        }
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

    public static int compareArray(char[] a, char[] b, int al, int ar, int bl, int br) {
        for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
            if (a[i] != b[j]) {
                return a[i] - b[i];
            }
        }
        return (ar - al) - (br - bl);
    }

    public static int compareArray(int[] a, int[] b, int al, int ar, int bl, int br) {
        for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
            if (a[i] != b[j]) {
                return a[i] - b[i];
            }
        }
        return (ar - al) - (br - bl);
    }

    public static int compareArray(long[] a, long[] b, int al, int ar, int bl, int br) {
        for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
            if (a[i] != b[j]) {
                return Long.compare(a[i], b[i]);
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

    public static <T> T theKthSmallestElement(T[] data, Comparator<T> cmp, int f, int t, int k) {
        if (t - f == THRESHOLD) {
            insertSort(data, cmp, f, t - 1);
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
