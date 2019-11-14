package contest;

import template.FastInput;
import template.FastOutput;
import template.SuffixAutomaton;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();

        char[] s = new char[n];
        in.readString(s, 0);

        boolean[][] isSubstring = new boolean[n][n];
        SuffixAutomaton sa = new SuffixAutomaton();
        sa.build(s[0]);
        for (int i = 1; i < n; i++) {
            sa.beginMatch();
            for (int j = i; j < n; j++) {
                sa.match(s[j]);
                if (sa.lengthMatch() != j - i + 1) {
                    break;
                }
                isSubstring[i][j] = true;
            }
            sa.build(s[i]);
        }

        long[] dp = new long[n];
        dp[0] = a;
        for (int i = 1; i < n; i++) {
            dp[i] = dp[i - 1] + a;
            for (int j = 0; j <= i; j++) {
                if (isSubstring[j][i]) {
                    dp[i] = Math.min(dp[i], dp[j - 1] + b);
                }
            }
        }

        out.println(dp[n - 1]);
    }
}
