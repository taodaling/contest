package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class TaskF {
    long[][][][][] dp = new long[32][2][2][2][2];
    int[] bitsOfL;
    int[] bitsOfR;

    public long dp(int bit, int abot, int atop, int bbot, int btop) {
        if (bit == -1) {
            return 1;
        }

        if (dp[bit][abot][atop][bbot][btop] == -1) {
            long ans = 0;
            for (int av = 0; av < 2; av++) {
                for (int bv = 0; bv < 2; bv++) {
                    if (av == 1 && bv == 1) {
                        continue;
                    }
                    if (abot == 1 && av < bitsOfL[bit] || atop == 1 && av > bitsOfR[bit]
                                    || bbot == 1 && bv < bitsOfL[bit] || btop == 1 && bv > bitsOfR[bit]) {
                        continue;
                    }
                    ans += dp(bit - 1, (abot == 1 && av == bitsOfL[bit]) ? 1 : 0,
                                    (atop == 1 && av == bitsOfR[bit]) ? 1 : 0,
                                    (bbot == 1 && bv == bitsOfL[bit]) ? 1 : 0,
                                    (btop == 1 && bv == bitsOfR[bit]) ? 1 : 0);
                }
            }
            dp[bit][abot][atop][bbot][btop] = ans;
        }
        return dp[bit][abot][atop][bbot][btop];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int l = in.readInt();
        int r = in.readInt();

        bitsOfL = new int[32];
        bitsOfR = new int[32];
        DigitUtils.BitOperator bo = new DigitUtils.BitOperator();
        for (int i = 0; i < 32; i++) {
            bitsOfL[i] = bo.bitAt(l, i);
            bitsOfR[i] = bo.bitAt(r, i);
        }

        SequenceUtils.deepFill(dp, -1L);

        long ans = dp(31, 1, 1, 1, 1);
        out.println(ans);
    }
}
