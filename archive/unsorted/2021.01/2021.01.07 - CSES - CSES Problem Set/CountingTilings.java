package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class CountingTilings {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[][] dp = new long[n * m + 1][1 << n];
        dp[0][0] = 1;
        for (int i = 0; i < n * m; i++) {
            for (int j = 0; j < 1 << n; j++) {
                long way = dp[i][j] = DigitUtils.modWithoutDivision(dp[i][j], mod);
                if (way == 0) {
                    continue;
                }
                int row = i % n;
                if (Bits.get(j, row) == 1) {
                    //do nothing
                    dp[i + 1][Bits.clear(j, row)] += way;
                } else {
                    //put 1 * 2
                    dp[i + 1][Bits.set(j, row)] += way;
                    //put 2 * 1
                    if (row + 1 < n && Bits.get(j, row + 1) == 0) {
                        dp[i + 2][j] += way;
                    }
                }
            }
        }

        long ans = dp[n * m][0];
        ans = DigitUtils.modWithoutDivision(ans, mod);
        out.println(ans);
    }
}
