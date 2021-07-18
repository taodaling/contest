package template.problem;

import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.Randomized;

import java.util.Arrays;

/**
 * main an array A contains -1 and 1
 */
public class CountContinuous01Subsequence {
    int[] cnts;
    IntegerArrayList list;
    int[] buf;
    int[] ceil;
    int zero;
    int n;

    /**
     * @param n sequence length
     */
    public CountContinuous01Subsequence(int n) {
        this.n = n;
        cnts = new int[n * 2 + 10];
        buf = new int[n];
        ceil = new int[n];
        zero = n + 5;
        list = new IntegerArrayList(n + 1);
    }

    /**
     * only A[indices[i]] is 1
     * <br>
     * O(4k) or O(k\log_2 k) if require sort
     */
    public void init(IntToIntegerFunction indices, int k) {
        list.clear();
        boolean sorted = true;
        for (int i = 0; i < k; i++) {
            int cand = indices.apply(i);
            if (i > 0 && buf[i - 1] > cand) {
                sorted = false;
            }
            buf[i] = cand;
        }
        if (!sorted) {
            Randomized.shuffle(buf, 0, k);
            Arrays.sort(buf, 0, k);
        }
        int last = 0;
        for (int i = 0; i < k; i++) {
            if (last < buf[i]) {
                list.add(-(buf[i] - last));
            }
            last = buf[i] + 1;
            list.add(1);
        }
        if (last < n) {
            list.add(-(n - last));
        }
        //remove useless points
        int[] data = list.getData();
        int m = list.size();
        int ps = 0;
        for (int i = 0; i < m; i++) {
            ceil[i] = ps;
            ps += data[i];
            if (ps < 0) {
                ps = 0;
            }
        }
        ps = 0;
        for (int i = m - 1; i >= 0; i--) {
            ceil[i] += ps;
            ps += data[i];
            if (ps < 0) {
                ps = 0;
            }
        }
        for (int i = 0; i < m; i++) {
            if (data[i] < 0) {
                int v = -data[i];
                v = Math.min(v, ceil[i] + 1);
                data[i] = -v;
            }
        }
    }

    /**
     * Count the number of non-empty continuous subsequence that has sum greater than 0
     * <br>
     * O(4k)
     */
    public long countPositive() {
        int[] data = list.getData();
        int m = list.size();
        int lower = 0;
        long ans = 0;
        int ps = zero;
        cnts[ps]++;
        for (int i = 0; i < m; i++) {
            int step = Math.abs(data[i]);
            int single = data[i] > 0 ? 1 : -1;
            for (int j = 0; j < step; j++) {
                if (single > 0) {
                    lower += cnts[ps];
                    ps++;
                } else {
                    ps--;
                    lower -= cnts[ps];
                }
                cnts[ps]++;
                ans += lower;
            }
        }
        //remove effect
        ps = zero;
        cnts[ps]--;
        for (int i = 0; i < m; i++) {
            int step = Math.abs(data[i]);
            int single = data[i] > 0 ? 1 : -1;
            for (int j = 0; j < step; j++) {
                if (single > 0) {
                    ps++;
                } else {
                    ps--;
                }
                cnts[ps]--;
            }
        }
        return ans;
    }

    /**
     * Count the number of non-empty continuous subsequence that has sum greater than or equal to 0
     * O(4k)
     */
    public long countNonNegative() {
        int[] data = list.getData();
        int m = list.size();
        int lower = 0;
        long ans = 0;
        int ps = zero;
        cnts[ps]++;
        for (int i = 0; i < m; i++) {
            int step = Math.abs(data[i]);
            int single = data[i] > 0 ? 1 : -1;
            for (int j = 0; j < step; j++) {
                if (single > 0) {
                    ps++;
                    lower += cnts[ps];
                } else {
                    lower -= cnts[ps];
                    ps--;
                }
                cnts[ps]++;
                ans += lower;
            }
        }
        //remove effect
        ps = zero;
        cnts[ps]--;
        for (int i = 0; i < m; i++) {
            int step = Math.abs(data[i]);
            int single = data[i] > 0 ? 1 : -1;
            for (int j = 0; j < step; j++) {
                if (single > 0) {
                    ps++;
                } else {
                    ps--;
                }
                cnts[ps]--;
            }
        }
        return ans;
    }
}
