package contest;

import template.io.FastInput;
import template.utils.SequenceUtils;

import java.io.PrintWriter;

public class EditDistance {
    String a;
    String b;
    int[][] dp;

    public int dp(int i, int j) {
        if (i < 0) {
            return j + 1;
        }
        if (j < 0) {
            return i + 1;
        }
        if (dp[i][j] == -1) {
            dp[i][j] = (int) 1e9;
            dp[i][j] = Math.min(dp[i][j], dp(i, j - 1) + 1);
            dp[i][j] = Math.min(dp[i][j], dp(i - 1, j) + 1);
            dp[i][j] = Math.min(dp[i][j], dp(i - 1, j - 1) + 1);
            if(a.charAt(i) == b.charAt(j)){
                dp[i][j] = Math.min(dp[i][j], dp(i - 1, j - 1));
            }
        }
        return dp[i][j];
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        a = in.readString();
        b = in.readString();
        dp = new int[a.length()][b.length()];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(a.length() - 1, b.length() - 1);
        out.println(ans);
    }
}
