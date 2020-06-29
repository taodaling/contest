package on2020_06.on2020_06_30_Virtual_Judge___2020_06_29__.D___Reversed_LCS;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class DReversedLCS {
    char[] s;
    int[][][] dp;

    public int dp(int l, int r, int k) {
        if (l > r) {
            return 0;
        }
        if (l == r) {
            return 1;
        }
        if (dp[l][r][k] == -1) {
            dp[l][r][k] = 0;
            //change nothing
            dp[l][r][k] = Math.max(dp(l + 1, r, k), dp(l, r - 1, k));
            if (s[l] == s[r]) {
                dp[l][r][k] = dp(l + 1, r - 1, k) + 2;
            }
            if (k > 0) {
                dp[l][r][k] = Math.max(dp[l][r][k], dp(l + 1, r - 1, k - 1) + 2);
            }
        }
        return dp[l][r][k];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        s = in.readString().toCharArray();
        int k = in.readInt();
        dp = new int[s.length][s.length][k + 1];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(0, s.length - 1, k);
        out.println(ans);
    }
}
