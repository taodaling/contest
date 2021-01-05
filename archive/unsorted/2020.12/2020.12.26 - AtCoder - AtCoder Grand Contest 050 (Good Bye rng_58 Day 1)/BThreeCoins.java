package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class BThreeCoins {
    Debug debug = new Debug(false);
    int[][] dp;
    int[] a;

    public int get(int l, int r) {
        if (r - l + 1 < 3) {
            return 0;
        }
        if (dp[l][r] == -1) {
            int ans = Math.max(get(l + 1, r), get(l, r - 1));
            for (int i = l; i < r; i++) {
                ans = Math.max(ans, get(l, i) + get(i + 1, r));
            }
            int l3 = l % 3;
            int r3 = r % 3;
            if ((l3 + 2) % 3 == r3) {
                for (int i = l + 1; i <= r; i += 3) {
                    ans = Math.max(ans, get(l + 1, i - 1) +
                            get(i + 1, r - 1) + a[l] + a[i] + a[r]);
                }
            }
            dp[l][r] = ans;
        }
        return dp[l][r];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int inf = (int) 1e8;
        int n = in.ri();
        a = new int[n];
        dp = new int[n][n];
        SequenceUtils.deepFill(dp, -1);
        in.populate(a);
        int ans = get(0, n - 1);
        out.println(ans);
    }
}
