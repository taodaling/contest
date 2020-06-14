package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class DYetAnotherPalindromePartitioning {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e5];
        int n = in.readString(s, 0);
        int charset = 'z' - 'a' + 1;
        int[] dp = new int[1 << charset];
        int inf = (int) 1e8;
        Arrays.fill(dp, inf);
        int pre = 0;
        dp[pre] = 0;
        for (int i = 0; i < n; i++) {
            int x = s[i] - 'a';
            int bit = 1 << x;
            pre ^= bit;

            int ans = inf;
            ans = Math.min(ans, dp[pre] + 1);
            for (int j = 0; j < charset; j++) {
                ans = Math.min(ans, dp[pre ^ (1 << j)] + 1);
            }
            dp[pre] = Math.min(dp[pre], ans);

            if (i == n - 1) {
                out.println(ans);
                return;
            }
        }
    }
}
