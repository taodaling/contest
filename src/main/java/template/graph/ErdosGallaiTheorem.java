package template.graph;

import template.datastructure.PreSum;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

/**
 * Check whether there is a way to construct a simple undirected graph with specified degrees.
 */
public class ErdosGallaiTheorem {
    public static boolean possible(int[] degs) {
        try {
            return maxOnAllK(degs) <= 0;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static long maxOnAllK(int[] degs) {
        long sum = 0;
        int min = 0;
        for (int i = 0; i < degs.length; i++) {
            sum += degs[i];
            min = Math.min(min, degs[i]);
        }
        if (sum % 2 == 1 || min < 0) {
            throw new IllegalArgumentException();
        }
        CompareUtils.radixSort(degs, 0, degs.length - 1);
        SequenceUtils.reverse(degs, 0, degs.length - 1);
        int n = degs.length;
        PreSum ps = new PreSum(degs);
        long[] ss = new long[n + 1];
        int rIter = n;
        for (int i = n - 1; i >= 0; i--) {
            int l = i;
            int r = n - 1;
            if (rIter - 1 >= l && degs[rIter - 1] < i) {
                rIter--;
            }
            while (rIter < n && degs[rIter] > i) {
                rIter++;
            }
            ss[i] = (long) (rIter - l) * i + ps.intervalSum(rIter, r);
        }

        long max = Long.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            max = Math.max(max, ps.intervalSum(0, i - 1) - (long) i * (i - 1) - ss[i]);
        }
        return max;
    }
}
