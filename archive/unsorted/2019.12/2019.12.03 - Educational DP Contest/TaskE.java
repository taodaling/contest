package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int w = in.readInt();
        Good[] goods = new Good[n + 1];
        for (int i = 1; i <= n; i++) {
            goods[i] = new Good();
            goods[i].w = in.readInt();
            goods[i].v = in.readInt();
        }
        int limit = 100000;
        long[][] dp = new long[n + 1][limit + 1];
        SequenceUtils.deepFill(dp, (long) 1e18);
        dp[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= limit; j++) {
                dp[i][j] = dp[i - 1][j];
                if (goods[i].v <= j) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - goods[i].v]
                            + goods[i].w);
                }
            }
        }

        int ans = 0;
        for(int i = 0; i <= limit; i++){
            if(dp[n][i] <= w){
                ans = i;
            }
        }

        out.println(ans);
    }
}

class Good {
    int w;
    int v;
}
