package on2019_10.on2019_10_23_codefestival_2016_final.TaskF;



import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);

        int[][][] dp = new int[m + 1][n + 1][n + 1];
        dp[m][1][0] = 1;
        for(int i = m; i > 0; i--){
            for(int j = 0; j <= n; j++){
                for(int t = 0; t <= n; t++){
                    dp[i - 1][j][t] = mod.plus(dp[i - 1][j][t],
                            mod.mul(dp[i][j][t], t));
                    if(t + 1 <= n) {
                        dp[i - 1][j][t + 1] = mod.plus(dp[i - 1][j][t + 1],
                                mod.mul(dp[i][j][t], Math.max(0, n - t - j)));
                    }
                    if(j + t <= n) {
                        dp[i - 1][j + t][0] = mod.plus(dp[i - 1][j + t][0],
                                mod.mul(dp[i][j][t], j));
                    }
                }
            }
        }

        out.println(dp[0][n][0]);
    }
}
