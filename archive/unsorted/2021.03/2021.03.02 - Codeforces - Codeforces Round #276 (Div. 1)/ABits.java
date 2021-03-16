package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongRadix;
import template.utils.SequenceUtils;

public class ABits {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        low = in.rl();
        high = in.rl();
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(60 - 1, 1, 1);
        out.println(ans);
    }

    long low;
    long high;
    long[][][] dp = new long[60][2][2];

    public long dp(int i, int ceil, int floor) {
        if (i < 0) {
            return 0;
        }
        if (dp[i][ceil][floor] == -1) {
            int lowBit = Bits.get(low, i);
            int highBit = Bits.get(high, i);
            dp[i][ceil][floor] = 0;
            for (int k = 0; k < 2; k++) {
                if (k < lowBit && floor == 1) {
                    continue;
                }
                if (k > highBit && ceil == 1) {
                    continue;
                }
                long ans = dp(i - 1, ceil == 1 && k == highBit ? 1 : 0,
                        floor == 1 && k == lowBit ? 1 : 0);
                if (k == 1) {
                    ans = Bits.set(ans, i);
                }
                int comp = Long.bitCount(dp[i][ceil][floor]) - Long.bitCount(ans);
                if (comp == 0) {
                    comp = Long.compare(ans, dp[i][ceil][floor]);
                }
                if (comp < 0) {
                    dp[i][ceil][floor] = ans;
                }
            }
        }
        return dp[i][ceil][floor];
    }
}
