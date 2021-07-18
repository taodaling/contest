package on2021_07.on2021_07_15_DMOJ___DMOPC__20_Contest_1.P3___Victor_s_Algorithm;



import template.io.FastInput;
import template.io.FastOutput;

public class P3VictorsAlgorithm {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] preMax = new int[n];
        int[] postMax = new int[n];
        long[] preCost = new long[n];
        long[] postCost = new long[n];
        for (int i = 0; i < n; i++) {
            preMax[i] = a[i];
            if (i > 0) {
                preMax[i] = Math.max(preMax[i - 1], preMax[i]);
                preCost[i] = preCost[i - 1];
            }
            preCost[i] += preMax[i] - a[i];
        }
        for (int i = n - 1; i >= 0; i--) {
            postMax[i] = a[i];
            if (i + 1 < n) {
                postMax[i] = Math.max(postMax[i + 1], postMax[i]);
                postCost[i] = postCost[i + 1];
            }
            postCost[i] += postMax[i] - a[i];
        }
        long best = (long) 1e18;
        for (int i = 0; i < n; i++) {
            long cost = preCost[i] + postCost[i];
            cost -= Math.min(preMax[i], postMax[i]) - a[i];
            best = Math.min(best, cost);
        }
        out.println(best);
    }
}
