package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.math.BigInteger;
import java.util.Arrays;

public class Rooks {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] rows = new int[n];
        for (int i = 0; i < n; i++) {
            rows[i] = in.readInt();
        }
        Randomized.shuffle(rows);
        Arrays.sort(rows);

        BigInteger[][] dp = new BigInteger[n + 1][k + 1];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], BigInteger.ZERO);
        }
        dp[0][0] = BigInteger.ONE;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j > 0) {
                    dp[i][j] = dp[i][j].add(BigInteger.valueOf(rows[i - 1] - (j - 1)).multiply(dp[i - 1][j - 1]));
                }
            }
        }

        debug.debug("dp", dp);
        BigInteger ans = dp[n][k];
        out.println(ans);
    }
}
