package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;
import template.math.Modular;
import template.utils.SequenceUtils;

public class TaskO {
    int[] masks;
    int[][] dp;
    BitOperator bo = new BitOperator();
    Modular mod = new Modular(1e9 + 7);
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        masks = new int[n];
        for (int i = 0; i < n; i++) {
            int x = 0;
            for (int j = 0; j < n; j++) {
                x = bo.setBit(x, j, in.readInt() == 1);
            }
            masks[i] = x;
        }
        dp = new int[n + 1][1 << n];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(n, (1 << n) - 1);
        out.println(ans);
    }

    public int dp(int i, int s) {
        if (i == 0) {
            return 1;
        }
        if (dp[i][s] == -1) {
            dp[i][s] = 0;
            int m = s & masks[i - 1];
            for (int j = 0; j < n; j++) {
                if (bo.bitAt(m, j) == 0) {
                    continue;
                }
                dp[i][s] = mod.plus(dp[i][s], dp(i - 1, bo.setBit(s, j, false)));
            }
        }
        return dp[i][s];
    }
}
