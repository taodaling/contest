package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] cost = new int[]{0, 100, 101, 102, 103, 104, 105};
        int m = cost.length;
        int x = in.readInt();
        boolean[][] dp = new boolean[m][x + 1];
        dp[0][0] = true;
        for(int i = 1; i < m; i++){
            for(int j = 0; j <= x; j++){
                dp[i][j] = dp[i - 1][j];
                if(j - cost[i] >= 0){
                    dp[i][j] = dp[i][j] || dp[i][j - cost[i]];
                }
            }
        }

        out.println(dp[m - 1][x] ? "1" : "0");
    }
}
