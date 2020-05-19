package contest;

import template.utils.ArrayIndex;

import java.util.Arrays;

public class MakeSquare {
    ArrayIndex ai = new ArrayIndex(51, 51);
    int[] dp = new int[ai.totalSize()];
    int inf = (int) 1e9;
    String a;
    String b;

    public int dp(int i, int j) {
        if (i < 0 || j < 0) {
            return inf;
        }
        if (dp[ai.indexOf(i, j)] == -1) {
            dp[ai.indexOf(i, j)] = inf;
            if (i == 0 || j == 0) {
                return dp[ai.indexOf(i, j)] = Math.max(i, j);
            }
            dp[ai.indexOf(i, j)] = Math.min(dp(i - 1, j), dp(i, j - 1)) + 1;
            dp[ai.indexOf(i, j)] = Math.min(dp[ai.indexOf(i, j)], dp(i - 1, j - 1) + 1);
            if (a.charAt(i - 1) == b.charAt(j - 1)) {
                dp[ai.indexOf(i, j)] = Math.min(dp[ai.indexOf(i, j)], dp(i - 1, j - 1));
            }
        }
        return dp[ai.indexOf(i, j)];
    }

    public int minChanges(String a, String b) {
        Arrays.fill(dp, -1);
        this.a = a;
        this.b = b;
        int ans = dp(a.length(), b.length());
        return ans;
    }

    public int minChanges(String S) {
        int ans = inf;
        for (int i = 0; i < S.length(); i++) {
            ans = Math.min(ans, minChanges(S.substring(0, i), S.substring(i)));
        }
        return ans;
    }
}
