package contest;

import template.binary.Bits;
import template.math.Radix;
import template.utils.SequenceUtils;

public class AllEven {
    long[][][][][] dp;

    int limit = 19;
    Radix radix = new Radix(10);
    long lo;
    long hi;

    public long dp(int i, int s, int start, int ceil, int floor) {
        if (i < 0) {
            return s == 0 ? 1 : 0;
        }
        if (dp[i][s][start][ceil][floor] == -1) {
            dp[i][s][start][ceil][floor] = 0;
            int l = radix.get(lo, i);
            int r = radix.get(hi, i);
            for (int j = 0; j < 10; j++) {
                if (ceil == 1 && j > r || floor == 1 && j < l) {
                    continue;
                }
                int next = s;
                if (j == 0 && start == 0) {
                } else {
                    next ^= 1L << j;
                }
                dp[i][s][start][ceil][floor] += dp(i - 1, next, start == 1 || j > 0 ? 1 : 0,
                        ceil == 1 && j == r ? 1 : 0, floor == 1 && j == l ? 1 : 0);
            }
        }
        return dp[i][s][start][ceil][floor];
    }

    public long countInRange(long lo, long hi) {
        lo = Math.max(lo, 1);

        this.lo = lo;
        this.hi = hi;
        dp = new long[limit][1 << 10][2][2][2];
        SequenceUtils.deepFill(dp, -1L);

        long ans = dp(limit - 1, 0, 0, 1, 1);
        return ans;
    }
}
