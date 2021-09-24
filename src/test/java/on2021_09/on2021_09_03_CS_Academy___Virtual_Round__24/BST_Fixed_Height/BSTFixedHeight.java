package on2021_09.on2021_09_03_CS_Academy___Virtual_Round__24.BST_Fixed_Height;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

public class BSTFixedHeight {
    int mod = (int) 1e9 + 7;
    long[][] dp;

    public long dp(int n, int h) {
        if (h <= 0) {
            return n == 0 ? 1 : 0;
        }
        if (n == 0) {
            return 1;
        }
        if (dp[n][h] == -1) {
            long ans = 0;
            for(int l = 0; l <= n - 1; l++){
                int r = n - 1 - l;
                ans += dp(l, h - 1) * dp(r, h - 1) % mod;
            }
            dp[n][h] = ans % mod;
        }
        return dp[n][h];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int h = in.ri();
        dp = new long[n + 1][h + 1];
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(n, h) - dp(n, h - 1);
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
