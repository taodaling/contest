package template.primitve.generated.datastructure;

import template.rand.Randomized;

import java.util.Arrays;

public class DoubleDiscreteMap {
    double[] val;
    int f;
    int t;

    public DoubleDiscreteMap(double[] val, int f, int t) {
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
    public int rankOf(double x) {
        return Arrays.binarySearch(val, f, t, x) - f;
    }

    public int floorRankOf(double x) {
        int index = Arrays.binarySearch(val, f, t, x);
        if (index >= 0) {
            return index - f;
        }
        index = -(index + 1);
        return index - 1 - f;
    }

    public int ceilRankOf(double x) {
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
    public double iThElement(int i) {
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

    public static int discrete(double[]... data) {
        DoubleArrayList list = new DoubleArrayList(Arrays.stream(data).mapToInt(x -> x.length).sum());
        for (double[] x : data) {
            list.addAll(x);
        }
        list.unique();
        for (double[] x : data) {
            for (int i = 0; i < x.length; i++) {
                x[i] = list.binarySearch(x[i]);
            }
        }
        return list.size();
    }
}
