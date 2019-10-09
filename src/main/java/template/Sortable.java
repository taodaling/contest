package template;

import java.util.Comparator;

/**
 * Created by dalt on 2018/5/20.
 */
public class Sortable {
    private static <T> void swap(T[] data, int i, int j) {
        T tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
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
        swap(data, f, Randomized.nextInt(f, t - 1));
        int l = f;
        int r = t;
        int m = l + 1;
        while (m < r) {
            int c = cmp.compare(data[m], data[l]);
            if (c == 0) {
                m++;
            } else if (c < 0) {
                swap(data, l, m);
                l++;
                m++;
            } else {
                swap(data, m, --r);
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
