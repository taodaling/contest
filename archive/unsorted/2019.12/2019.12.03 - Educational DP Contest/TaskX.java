package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class TaskX {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Stone[] stones = new Stone[n];
        for (int i = 0; i < n; i++) {
            stones[i] = new Stone();
            stones[i].w = in.readInt();
            stones[i].s = in.readInt();
            stones[i].v = in.readInt();
        }
        Arrays.sort(stones, (a, b) -> Integer.compare(a.s + a.w, b.s + b.w));
        int limit = 20000;
        long[][] dp = new long[n + 1][limit + 1];
        SequenceUtils.deepFill(dp, -(long) 1e18);
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            Stone s = stones[i - 1];
            for (int j = 0; j <= limit; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j >= s.w && j - s.w <= s.s) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - s.w]
                            + s.v);
                }
            }
        }

        long ans = 0;
        for(int i = 0; i <= limit; i++){
            ans = Math.max(ans, dp[n][i]);
        }

        out.println(ans);
    }
}

class Stone {
    int v;
    int w;
    int s;
}
