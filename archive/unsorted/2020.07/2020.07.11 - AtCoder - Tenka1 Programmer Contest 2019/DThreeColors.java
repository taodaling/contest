package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;

import java.util.Arrays;

public class DThreeColors {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int sum = Arrays.stream(a).sum();
        int[][] dp2 = dp(a, n, sum, 2);
        int[][] dp1 = dp(a, n, sum, 1);

        int ans = pow.pow(3, n);
        int invalid = 0;
        for (int i = DigitUtils.ceilDiv(sum, 2); i <= sum; i++) {
            invalid = mod.plus(invalid, dp2[n][i]);
        }
        ans = mod.subtract(ans, mod.mul(3, invalid));
        if (sum % 2 == 0) {
            ans = mod.plus(ans, mod.mul(3, dp1[n][sum / 2]));
        }
        out.println(ans);
    }

    Modular mod = new Modular(998244353);
    Power pow = new Power(mod);

    public int[][] dp(int[] a, int n, int sum, int mul) {
        int[][] dp = new int[n + 1][sum + 1];
        dp[0][0] = 1;
        Modular mod = new Modular(998244353);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= sum; j++) {
                int way = dp[i][j];
                if (way == 0) {
                    continue;
                }
                //choose
                if (j + a[i] <= sum) {
                    dp[i + 1][j + a[i]] = mod.plus(dp[i + 1][j + a[i]], way);
                }
                //not choose
                dp[i + 1][j] = mod.plus(dp[i + 1][j], mod.mul(mul, way));
            }
        }
        return dp;
    }
}
