package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class EmptyString {
    int mod = (int) 1e9 + 7;
    char[] s;
    long[][] dp;

    Combination comb = new Combination(10000, mod);

    public long dp(int l, int r) {
        if (l > r) {
            return 1;
        }
        if (dp[l][r] == -1) {
            if(l == r){
                return dp[l][r] = 0;
            }
            long ans = 0;
            for (int i = l + 1; i <= r; i += 2) {
                if (s[i] == s[l]) {
                    ans += dp(l + 1, i - 1) * dp(i + 1, r) % mod * comb.combination((r - l + 1) / 2,
                            (r - i) / 2) % mod;
                }
            }
            dp[l][r] = ans % mod;
        }
        return dp[l][r];
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        s = in.readString().toCharArray();
        dp = new long[s.length][s.length];
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(0, s.length - 1);
        debug.debugMatrix("dp", dp);
        out.println(ans);
    }
}
