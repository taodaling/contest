package on2021_08.on2021_08_19_CS_Academy___Virtual_Round__11.Max_Intersection_Partition;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.MinPairContainer;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class MaxIntersectionPartition {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[][] lrs = new int[n][];
        int[] length = new int[n];
        for (int i = 0; i < n; i++) {
            lrs[i] = in.ri(2);
            length[i] = lrs[i][1] - lrs[i][0];
        }
        Randomized.shuffle(length);
        Arrays.sort(length);
        long cand = 0;
        for (int i = 0; i < k - 1; i++) {
            cand += length[n - 1 - i];
        }
        Arrays.sort(lrs, Comparator.comparingInt(x -> x[0]));
        debug.debug("lrs", lrs);
        MinPairContainer mpc = new MinPairContainer((int) 1e4);
        long[] prev = new long[n];
        long inf = (long) 1e18;
        Arrays.fill(prev, -inf);
        long[] next = new long[n];
        for (int i = 0; i < k; i++) {
            debug.debug("i", i);
            debug.debug("prev", prev);
            mpc.clear();
            mpc.add(lrs[0][1], 0);
            long maxB = 0;
            for (int j = 0; j < n; j++) {
                next[j] = Math.max(mpc.query() - lrs[j][0], maxB);
                if (j + 1 < n) {
                    mpc.update(lrs[j + 1][1]);
                    mpc.add(lrs[j + 1][1], prev[j]);
                }
                maxB = Math.max(maxB, prev[j]);
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        debug.debug("i", k);
        debug.debug("prev", prev);

        long ans = prev[n - 1];
        ans = Math.max(ans, cand);
        out.println(ans);
    }
}
