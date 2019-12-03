package on2019_12.on2019_12_03_Educational_DP_Contest.I___Coins;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskI {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        double[] p = new double[1 + n];
        for (int i = 1; i <= n; i++) {
            p[i] = in.readDouble();
        }
        double[][] dp = new double[n + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = dp[i - 1][j] *
                        (1 - p[i]);
                if (j - 1 >= 0) {
                    dp[i][j] += dp[i - 1][j - 1] *
                            p[i];
                }
            }
        }

        double total = 0;
        for(int i = 0; i <= n; i++){
            if(n - i >= i){
                continue;
            }
            total += dp[n][i];
        }

        out.printf("%.15f", total);
    }
}
