package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;

public class DAB {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] transform = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                transform[i][j] = in.rc() == 'A' ? 0 : 1;
            }
        }
        if (n <= 3) {
            out.println(1);
            return;
        }

        if (transform[0][1] != 0) {
            int[][] tmp = new int[2][2];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    tmp[1 - j][1 - i] = 1 - transform[i][j];
                }
            }
            transform = tmp;
        }

        assert transform[0][1] == 0;
        if (transform[0][0] == 0) {
            //all a
            out.println(1);
            return;
        }
        if (transform[1][0] == 0) {
            out.println(way(n - 2));
            return;
        }
        out.println(pow.pow(2, n - 3));
    }

    public long way(int n) {
        long[][] dp = new long[n][2];
        dp[0][0] = 1;
        for (int i = 0; i + 1 < n; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    if (j == 1 && k == 1) {
                        continue;
                    }
                    dp[i + 1][k] += dp[i][j];
                }
            }
            for (int j = 0; j < 2; j++) {
                dp[i + 1][j] %= mod;
            }
        }
        return (dp[n - 1][0] + dp[n - 1][1]) % mod;
    }
}
