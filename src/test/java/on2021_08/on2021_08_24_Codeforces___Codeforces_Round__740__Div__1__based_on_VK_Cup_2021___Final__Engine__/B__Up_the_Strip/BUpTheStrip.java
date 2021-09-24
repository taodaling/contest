package on2021_08.on2021_08_24_Codeforces___Codeforces_Round__740__Div__1__based_on_VK_Cup_2021___Final__Engine__.B__Up_the_Strip;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class BUpTheStrip {
    public long sum(long[] suf, int l, int r) {
        if (r < l || l >= suf.length) {
            return 0;
        }
        long ans = suf[l];
        if (r + 1 < suf.length) {
            ans -= suf[r + 1];
        }
        return ans;
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int mod = in.ri();
        long[] dp = new long[n + 1];
        long[] suf = new long[n + 1];
        dp[n] = suf[n] = 1;
        for (int i = n - 1; i >= 1; i--) {
            long way = sum(suf, i + 1, n);
            for (int j = 2; j * i <= n; j++) {
                way += sum(suf, j * i, j * i + j - 1);
            }
            dp[i] = way % mod;
            suf[i] = (suf[i + 1] + dp[i]) % mod;
        }
        debug.debug("dp", dp);
        long ans = dp[1];
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
