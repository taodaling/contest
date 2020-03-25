package template.datastructure;

import template.rand.Randomized;

import java.util.Arrays;
import java.util.Comparator;

public class DiscreteMap<T> {
    T[] val;
    int f;
    int t;
    Comparator<T> comp;

    public DiscreteMap(T[] val, int f, int t, Comparator<T> comp) {
        this.comp = comp;
        Randomized.shuffle(val, f, t);
        Arrays.sort(val, f, t, comp);
        int wpos = f + 1;
        for (int i = f + 1; i < t; i++) {
            if (val[i] == val[i - 1]) {
                continue;
            }
            val[wpos++] = val[i];
        }
        this.val = val;
        this.f = f;
        this.t = wpos;
    }

    /**
     * Return 0, 1, so on
     */
    public int rankOf(T x) {
        return Arrays.binarySearch(val, f, t, x, comp) - f;
    }

    public int floorRankOf(T x) {
        int index = Arrays.binarySearch(val, f, t, x, comp);
        if (index >= 0) {
            return index - f;
        }
        index = -(index + 1);
        return index - 1 - f;
    }

    public int ceilRankOf(T x) {
        int index = Arrays.binarySearch(val, f, t, x);
        if (index >= 0) {
            return index - f;
        }
        index = -(index + 1);
        return index - f;
    }

    /**
     * Get the i-th smallest element
     */
    public T iThElement(int i) {
        return val[f + i];
    }

    public int minRank() {
        return 0;
    }

    public int maxRank() {
        return t - f - 1;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOfRange(val, f, t));
    }
}
