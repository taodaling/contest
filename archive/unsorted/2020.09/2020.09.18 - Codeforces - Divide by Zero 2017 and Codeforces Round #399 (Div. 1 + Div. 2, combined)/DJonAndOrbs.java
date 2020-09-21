package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DJonAndOrbs {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int q = in.readInt();
        int threshold = 10000;
        double[][] dp = new double[threshold + 1][k + 1];
        dp[0][0] = 1;
        for (int i = 0; i < threshold; i++) {
            for (int j = 0; j <= k; j++) {
                double prob = (double) j / k;
                dp[i + 1][j] += prob * dp[i][j];
                if (j + 1 <= k) {
                    dp[i + 1][j + 1] += (1 - prob) * dp[i][j];
                }
            }
        }

        for (int i = 0; i < q; i++) {
            int p = in.readInt();
            double atLeast = p / 2000d;
            int ans = 0;
            while (dp[ans][k] < atLeast) {
                ans++;
            }
            out.println(ans);
        }
    }
}
