package on2019_12.on2019_12_03_Educational_DP_Contest.M___Candies;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.Arrays;

public class TaskM {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        Modular mod = new Modular(1e9 + 7);
        int[][] dp = new int[n + 1][k + 1];
        int[][] preSum = new int[n + 1][k + 1];
        dp[0][0] = 1;
        Arrays.fill(preSum[0], 1);
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j > 0) {
                    dp[i][j] = mod.plus(dp[i][j], preSum[i - 1][j - 1]);
                }
                if (j - a[i] - 1 >= 0) {
                    dp[i][j] = mod.subtract(dp[i][j], preSum[i - 1][j - a[i] - 1]);
                }
            }
            for(int j = 0; j <= k; j++){
                preSum[i][j] = dp[i][j];
                if(j > 0){
                    preSum[i][j] = mod.plus(preSum[i][j], preSum[i][j - 1]);
                }
            }
        }

        out.println(dp[n][k]);
    }
}
