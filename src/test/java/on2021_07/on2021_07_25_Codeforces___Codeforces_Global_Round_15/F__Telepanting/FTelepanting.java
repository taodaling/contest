package on2021_07.on2021_07_25_Codeforces___Codeforces_Global_Round_15.F__Telepanting;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class FTelepanting {
    int mod = 998244353;

    public long sum(long[] ps, int l, int r) {
        long ans = ps[r];
        if (l > 0) {
            ans -= ps[l - 1];
        }
        if (ans < 0) {
            ans += mod;
        }
        return ans;
    }

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] T = new int[n][3];
        for (int i = 0; i < n; i++) {
            T[i][0] = in.ri();
            T[i][1] = in.ri();
            T[i][2] = in.ri();
        }
        long[] dp = new long[n];
        long[] dpPs = new long[n];
        for (int i = 0; i < n; i++) {
            int l = 0;
            int r = i;
            int target = T[i][0];
            int go = T[i][1];
            while (l < r) {
                int m = (l + r) / 2;
                if (T[m][0] >= go) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            long move = target - go;
            if (l < i) {
                move += sum(dpPs, l, i - 1);
            }
            dp[i] = DigitUtils.mod(move, mod);
            dpPs[i] = dp[i];
            if (i > 0) {
                dpPs[i] += dpPs[i - 1];
            }
        }

        debug.debug("dp", dp);
        long ans = T[n - 1][0] + 1;
        for (int i = 0; i < n; i++) {
            if (T[i][2] == 1) {
                ans += dp[i];
            }
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}

