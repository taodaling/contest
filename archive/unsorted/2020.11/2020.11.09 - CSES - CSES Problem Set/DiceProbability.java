package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DiceProbability {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        double[][] dp = new double[n + 1][6 * n + 1];
        dp[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= 6 * n; j++) {
                double p = dp[i][j] / 6;
                for (int k = 1; k <= 6; k++) {
                    if (j + k <= 6 * n) {
                        dp[i + 1][j + k] += p;
                    }
                }
            }
        }
        double ans = 0;
        for(int i = a; i <= b; i++){
            ans += dp[n][i];
        }
        out.printf("%.6f", ans);
    }
}
