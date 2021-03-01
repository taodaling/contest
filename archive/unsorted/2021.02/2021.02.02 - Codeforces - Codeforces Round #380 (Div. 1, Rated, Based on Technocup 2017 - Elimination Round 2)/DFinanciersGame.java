package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.SequenceUtils;

public class DFinanciersGame {
    int[][][] dp;
    int[] a;
    IntegerPreSum aps;
    int fix = 100;

    public int dp(int d, int step, int l) {
        if (dp[d + fix][step][l] == Integer.MIN_VALUE) {
            int r = a.length - 1 - (l - d);
            if (r - l + 1 < step) {
                return dp[d + fix][step][l] = 0;
            }
            int ans = Integer.MIN_VALUE;
            for (int i = step; i <= step + 1; i++) {
                if (r - l + 1 < i) {
                    continue;
                }
                int sumL = aps.intervalSum(l, l + i - 1);
                int best = Integer.MAX_VALUE;
                if (r - l + 1 < i + i) {
                    best = sumL;
                } else {
                    for (int j = i; j <= i + 1; j++) {
                        if (r - l + 1 < i + j) {
                            continue;
                        }
                        best = Math.min(best, sumL - aps.intervalSum(r - j + 1, r) + dp(d - j + i, j, l + i));
                    }
                }
                ans = Math.max(ans, best);
            }
            dp[d + fix][step][l] = ans;
        }
        return dp[d + fix][step][l];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        a = in.ri(n);
        aps = new IntegerPreSum(i -> a[i], n);
        dp = new int[200][100][n];
        SequenceUtils.deepFill(dp, Integer.MIN_VALUE);
        int ans = dp(0, 1, 0);
        out.println(ans);
    }
}
