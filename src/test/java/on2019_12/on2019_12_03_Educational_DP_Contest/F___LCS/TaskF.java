package on2019_12.on2019_12_03_Educational_DP_Contest.F___LCS;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.DiffUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.readString();
        String t = in.readString();
        out.println(lcs(s.toCharArray(), t.toCharArray()));
    }

    public String lcs(char[] a, char[] b) {
        int n = a.length;
        int m = b.length;
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (a[i - 1] == b[j - 1]) {
                    dp[i][j] = Math.max(dp[i][j], 1 + dp[i - 1][j - 1]);
                }
            }
        }

        StringBuilder ans = new StringBuilder();
        trace(a, b, dp, n, m, ans);
        return ans.toString();
    }

    private void trace(char[] a, char[] b, int[][] dp, int i, int j,
                       StringBuilder ans) {
        if (i == 0 || j == 0) {
            return;
        }
        if (a[i - 1] == b[j - 1]) {
            trace(a, b, dp, i - 1, j - 1, ans);
            ans.append(a[i - 1]);
            return;
        }
        if (dp[i][j] == dp[i - 1][j]) {
            trace(a, b, dp, i - 1, j, ans);
        } else {
            trace(a, b, dp, i, j - 1, ans);
        }
    }
}
