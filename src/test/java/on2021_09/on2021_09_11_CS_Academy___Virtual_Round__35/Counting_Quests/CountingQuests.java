package on2021_09.on2021_09_11_CS_Academy___Virtual_Round__35.Counting_Quests;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.FastPow2;
import template.rand.RandomWrapper;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CountingQuests {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int mod = in.ri();
        FastPow2 p2 = new FastPow2(2, mod);
        Combination comb = new Combination((int) 1e5, mod);
        long ans = 0;
        //cut
        for (int k = 1; k <= n; k++) {
            long contrib = (long) comb.combination(n - 1, k - 1)
                    * p2.pow(k * (k + 1) / 2) % mod;
            if ((n - k) % 2 == 1) {
                contrib = -contrib;
            }
            ans += contrib;
            debug.debug("k", k);
            debug.debug("contrib", contrib);
        }
        if (n > 1) {
            for (int k = 1; k <= n; k++) {
                int actual = Math.max(1, k - 1);
                //n - k
                long contrib = (long) comb.combination(n - 1, k - 1)
                        * (p2.pow(k * (k + 1) / 2 - (k - 1) * 2)) % mod;
                if ((n - k + 1) % 2 == 1) {
                    contrib = -contrib;
                }
                ans += contrib;
            }
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);

        debug.run(() -> {
            debug.debug("invalid", bf(n, mod));
        });
    }

    private void add(long[] ps, int l, int r, long x) {
        if (l > r) {
            return;
        }
        ps[l] += x;
        if (r + 1 < ps.length) {
            ps[r + 1] -= x;
        }
    }

    public int bf(int n, int mod) {
        List<int[]> intervals = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                intervals.add(new int[]{i, j});
            }
        }
        int ans = 0;
        int m = intervals.size();
        for (int i = 0; i < 1 << m; i++) {
            long[] ps = new long[n];
            for (int j = 0; j < m; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                int[] interval = intervals.get(j);
                add(ps, interval[0], interval[1], RandomWrapper.INSTANCE.nextInt((int) 1e9));
            }
            for (int j = 1; j < n; j++) {
                ps[j] += ps[j - 1];
            }
            HashSet<Long> sets = new HashSet<>();
            for (long x : ps) {
                sets.add(x);
            }
        }

        return ans;
    }

    Debug debug = new Debug(false);
}
