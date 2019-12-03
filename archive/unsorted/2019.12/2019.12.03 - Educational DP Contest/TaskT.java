package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.Arrays;

public class TaskT {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n - 1];
        in.readString(s, 0);

        Modular mod = new Modular(1e9 + 7);
        int[][] dp = new int[n][n];
        int[][] pre = new int[n][n];
        int[][] post = new int[n][n];
        dp[0][0] = 1;
        Arrays.fill(pre[0], 1);
        post[0][0] = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (s[i - 1] == '>') {
                    dp[i][j] = post[i - 1][j];
                } else {
                    if (j > 0) {
                        dp[i][j] = pre[i - 1][j - 1];
                    }
                }
            }

            for (int j = 0; j <= i; j++) {
                pre[i][j] = dp[i][j];
                if (j > 0) {
                    pre[i][j] = mod.plus(pre[i][j], pre[i][j - 1]);
                }
            }
            for (int j = n - 1; j >= 0; j--) {
                post[i][j] = dp[i][j];
                if (j + 1 < n) {
                    post[i][j] = mod.plus(post[i][j], post[i][j + 1]);
                }
            }
        }

        int ans = 0;
        for(int i = 0; i < n; i++){
            ans = mod.plus(ans, dp[n - 1][i]);
        }

        out.println(ans);
    }
}
