package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] profits = new int[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                profits[i][j] = in.readInt();
            }
        }
        int[][] dp = new int[n][3];
        for(int i = 0; i < 3; i++){
            dp[0][i] = profits[0][i];
        }
        for(int i = 1; i < n; i++){
            for(int j = 0; j < 3; j++){
                dp[i][j] = 0;
                for(int k = 0; k < 3; k++){
                    if(k == j){
                        continue;
                    }
                    dp[i][j] = Math.max(dp[i][j],
                            dp[i - 1][k] + profits[i][j]);
                }
            }
        }

        int max = 0;
        for(int i = 0; i < 3; i++){
            max = Math.max(max, dp[n - 1][i]);
        }
        out.println(max);
    }
}
