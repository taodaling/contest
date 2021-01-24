package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongRadix;
import template.utils.SequenceUtils;

public class CountingNumbers {
    long limit;
    LongRadix radix = new LongRadix(10);
    long[][][][] dp = new long[19][2][10][2];

    public long dp(int b, int ceil, int last, int actived) {
        if (b < 0) {
            return 1;
        }
        if (dp[b][ceil][last][actived] == -1) {
            dp[b][ceil][last][actived] = 0;
            long d = radix.get(limit, b);
            for (int i = 0; i < 10; i++) {
                if (i == last || ceil == 1 && i > d) {
                    if (i == 0 && actived == 0) {
                    } else {
                        continue;
                    }
                }
                dp[b][ceil][last][actived] += dp(b - 1, ceil == 1 && i == d ? 1 : 0, i, actived == 0 && i == 0 ? 0 : 1);
            }
        }
        return dp[b][ceil][last][actived];
    }

    public long solve(long x) {
        if (x < 0) {
            return 0;
        }
        limit = x;
        SequenceUtils.deepFill(dp, -1L);
        return dp(18, 1, 0, 0);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        long ans = solve(b) - solve(a - 1);
        out.println(ans);
    }
}
