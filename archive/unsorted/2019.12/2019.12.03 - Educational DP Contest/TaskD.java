package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int w = in.readInt();
        Good[] goods = new Good[n + 1];
        for(int i = 1; i <= n; i++){
            goods[i] = new Good();
            goods[i].w = in.readInt();
            goods[i].v = in.readInt();
        }

        long[][] dp = new long[n + 1][w + 1];
        SequenceUtils.deepFill(dp, -(long)1e18);
        dp[0][0] = 0;
        for(int i = 1; i <= n; i++){
            for(int j = 0; j <= w; j++){
                dp[i][j] = dp[i - 1][j];
                if(goods[i].w <= j){
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - goods[i].w] + goods[i].v);
                }
            }
        }

        long ans = 0;
        for(int i = 0; i <= w; i++){
            ans = Math.max(ans, dp[n][i]);
        }

        out.println(ans);
    }
}

class Good {
    int w;
    int v;
}
