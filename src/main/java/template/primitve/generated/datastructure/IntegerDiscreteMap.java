package template.primitve.generated.datastructure;

import template.rand.Randomized;

import java.util.Arrays;

public class IntegerDiscreteMap {
    int[] val;
    int f;
    int t;

    public IntegerDiscreteMap(int[] val, int f, int t) {
        Randomized.shuffle(val, f, t);
        Arrays.sort(val, f, t);
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
    public int rankOf(int x) {
        return Arrays.binarySearch(val, f, t, x) - f;
    }

    public int floorRankOf(int x) {
        int index = Arrays.binarySearch(val, f, t, x);
        if (index >= 0) {
            return index - f;
        }
        index = -(index + 1);
        return index - 1 - f;
    }

    public int ceilRankOf(int x) {
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
    public int iThElement(int i) {
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

    public static void discrete(int[] data) {
        IntegerArrayList list = new IntegerArrayList(data);
        list.unique();
        for (int i = 0; i < data.length; i++) {
            data[i] = list.binarySearch(data[i]);
        }
    }
}
