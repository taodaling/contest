package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;

import java.util.Arrays;

public class HousesAndSchools {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] c = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            c[i] = in.ri();
        }
        LongPreSum cnt = new LongPreSum(i -> c[i], n + 1);
        LongPreSum cntW = new LongPreSum(i -> (long) c[i] * i, n + 1);
        cost = new long[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = i, mid = i; j <= n; j++) {
                while (mid + 1 < j && cnt.intervalSum(i, mid) < cnt.intervalSum(mid + 1, j)) {
                    mid++;
                }
                cost[i][j] = cnt.intervalSum(i, mid) * mid - cntW.intervalSum(i, mid) +
                        cntW.intervalSum(mid, j) - cnt.intervalSum(mid, j) * mid;
            }
        }

        debug.debug("cost", cost);
        long[] prev = new long[n + 1];
        long[] next = new long[n + 1];
        long inf = (long) 1e18;
        Arrays.fill(prev, inf);
        prev[0] = 0;
        for (int i = 0; i < k; i++) {
            debug.debugArray("prev", prev);
            Arrays.fill(next, inf);
            dac(prev, next, 0, n, 0, n);
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        debug.debugArray("prev", prev);
        long ans = prev[n];
        out.println(ans);
    }

    long[][] cost;

    public void dac(long[] prev, long[] next, int ll, int rr, int l, int r) {
        if (l > r) {
            return;
        }
        int mid = (l + r) / 2;
        int best = ll;
        for (int i = ll; i <= rr && i < mid; i++) {
            long cand = prev[i] + cost[i + 1][mid];
            if (cand < next[mid]) {
                next[mid] = cand;
                best = i;
            }
        }
        dac(prev, next, ll, best, l, mid - 1);
        dac(prev, next, best, rr, mid + 1, r);
    }
}
