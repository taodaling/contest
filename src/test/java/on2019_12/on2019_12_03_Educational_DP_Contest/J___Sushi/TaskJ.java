package on2019_12.on2019_12_03_Educational_DP_Contest.J___Sushi;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TaskJ {
    int n;
    double[][][] dp;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        dp = new double[n + 1][n + 1][n + 1];
        SequenceUtils.deepFill(dp, (double)-1);
        int[] cnts = new int[4];
        for(int i = 0; i < n; i++){
            cnts[in.readInt()]++;
        }
        double ans = dp(cnts[3], cnts[2], cnts[1]);
        out.printf("%.15f", ans);
    }

    public double dp(int a, int b, int c) {
        if(dp[a][b][c] == -1) {
            if(a + b + c == 0){
                return dp[a][b][c] = 0;
            }
            dp[a][b][c] = 1;
            int d = n - a - b - c;
            double stayProb = (double) d / n;
            if (a > 0) {
                dp[a][b][c] += dp(a - 1, b + 1, c) *
                        (double) a / n;
            }
            if (b > 0) {
                dp[a][b][c] += dp(a, b - 1, c + 1) *
                        (double) b / n;
            }
            if (c > 0) {
                dp[a][b][c] += dp(a, b, c - 1) *
                        (double) c / n;
            }
            dp[a][b][c] /= (1 - stayProb);
        }
        return dp[a][b][c];
    }
}
