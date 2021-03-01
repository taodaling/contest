package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int p = in.ri();
        a = in.ri(n);
        dp = new int[n][n][p + 1];
        SequenceUtils.deepFill(dp, -1);
        out.println(dp(0, n - 1, p) == 1 ? 'F' : 'S');
    }

    int[][][] dp;
    int[] a;

    public int dp(int l, int r, int p) {
        if (r < l) {
            return 0;
        }
        if (dp[l][r][p] == -1) {
            dp[l][r][p] = 0;
            if (a[r] > p) {
                return dp[l][r][p] = dp(l, r - 1, p);
            }
            if (dp(l + 1, r, p - a[l]) == 0) {
                dp[l][r][p] = 1;
            }
            for(int i = l; i <= r; i++){
                if(i == r || p - a[i] < a[i + 1]){
                    if(dp(l, i - 1, p - a[i]) == 0){
                        dp[l][r][p] = 1;
                    }
                }
            }
        }
        return dp[l][r][p];
    }
}
