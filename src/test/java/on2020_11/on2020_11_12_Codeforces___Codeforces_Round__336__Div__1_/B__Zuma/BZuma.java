package on2020_11.on2020_11_12_Codeforces___Codeforces_Round__336__Div__1_.B__Zuma;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class BZuma {
    int[][] dp;
    int[] colors;

    public int dp(int l, int r) {
        if (l > r) {
            return 0;
        }
        if (dp[l][r] == -1) {
            if (l == r) {
                return dp[l][r] = 1;
            }
            int ans = (int) 1e9;
            for (int i = l; i < r; i++) {
                ans = Math.min(ans, dp(l, i) + dp(i + 1, r));
            }
            if (colors[l] == colors[r]) {
                int local = dp(l + 1, r - 1);
                if (local > 0) {
                    local--;
                }
                ans = Math.min(ans, local + 1);
            }
            dp[l][r] = ans;
        }
        return dp[l][r];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        dp = new int[n][n];
        colors = new int[n];
        in.populate(colors);
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(0, n - 1);
        out.println(ans);
    }
}
