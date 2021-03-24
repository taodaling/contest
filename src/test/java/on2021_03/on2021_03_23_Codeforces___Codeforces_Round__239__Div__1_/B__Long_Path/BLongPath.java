package on2021_03.on2021_03_23_Codeforces___Codeforces_Round__239__Div__1_.B__Long_Path;



import template.io.FastInput;
import template.io.FastOutput;

public class BLongPath {
    int mod = (int) 1e9 + 7;

    public long interval(long[] ps, int l, int r) {
        if (l > r) {
            return 0;
        }
        long ans = ps[r];
        if (l > 0) {
            ans -= ps[l - 1];
        }
        if (ans < 0) {
            ans += mod;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] p = in.ri(n);
        for (int i = 0; i < n; i++) {
            p[i]--;
        }
        long[] dp = new long[n];
        long[] ps = new long[n];
        for (int i = 0; i < n; i++) {
            int l = p[i];
            int r = i - 1;
            dp[i] = interval(ps, l, r) + r - l + 1 + 1;
            ps[i] = dp[i];
            if (i > 0) {
                ps[i] += ps[i - 1];
                if (ps[i] >= mod) {
                    ps[i] -= mod;
                }
            }
        }
        long ans = n + ps[n - 1];
        ans %= mod;
        out.println(ans);
    }
}
