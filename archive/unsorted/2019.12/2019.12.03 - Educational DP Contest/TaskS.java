package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class TaskS {
    int[] k;
    int d;
    int[][][] dp;
    Modular mod = new Modular(1e9 + 7);

    public int dp(int i, int j, int upTight) {
        if (i == 0) {
            return j == 0 && upTight == 1 ? 1 : 0;
        }
        if (dp[i][j][upTight] == -1) {
            dp[i][j][upTight] = 0;
            if (upTight == 1) {
                return dp[i][j][upTight] =
                        dp(i - 1, DigitUtils.mod(j - k[i], d),
                                upTight);
            }

            for (int t = 0; t < 10; t++) {
                dp[i][j][upTight] = mod.plus(dp[i][j][upTight],
                        dp(i - 1, DigitUtils.mod(j - t, d), 0));
                if (t < k[i]) {
                    dp[i][j][upTight] = mod.plus(dp[i][j][upTight],
                            dp(i - 1, DigitUtils.mod(j - t, d), 1));
                }
            }
        }
        return dp[i][j][upTight];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        k = new int[10001];
        int len = in.readString(k, 1);
        for(int i = 1; i <= len; i++){
            k[i] -= '0';
        }
        d = in.readInt();
        dp = new int[len + 1][d][2];
        SequenceUtils.deepFill(dp, -1);

        int ans = mod.plus(dp(len, 0, 0),
                dp(len, 0, 1));
        ans = mod.subtract(ans, 1);
        out.println(ans);
    }
}
