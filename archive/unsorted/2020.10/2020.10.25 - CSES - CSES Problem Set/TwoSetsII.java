package contest;

import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;

public class TwoSetsII {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int mod = (int) 1e9 + 7;
        int sum = n * (n + 1) / 2;
        if (sum % 2 == 1) {
            out.println(0);
            return;
        }
        int half = sum / 2;
        int[] dp = new int[half + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = half; j - i >= 0; j--) {
                dp[j] = DigitUtils.modplus(dp[j], dp[j - i], mod);
            }
        }
        int inv2 = (mod + 1) / 2;
        long ans = dp[half];
        ans = ans * inv2 % mod;
        out.println(ans);
    }
}
