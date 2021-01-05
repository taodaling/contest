package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum2D;

public class ShipsOfTheDesert {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[][] dp = new long[n + 1][10];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            long ps = dp[i - 1][0];
            for (int j = 1; j < 10; j++) {
                ps += dp[i - 1][j];
                dp[i][j] = ps;
            }
        }
        LongPreSum2D ps = new LongPreSum2D(n + 1, 10);
        ps.init((i, j) -> dp[i][j], n + 1, 10);
        long ans = 0;
        for (int i = 0; i < n; i++) {
            long contrib = 0;
            for (int j = 1; j <= 10; j++) {
                contrib += ps.rect(i, i, 0, j - 1) *
                        ps.rect(0, n - 1 - i, 0, j - 1);
            }
            ans += contrib;
        }
        out.println(ans);
    }
}
