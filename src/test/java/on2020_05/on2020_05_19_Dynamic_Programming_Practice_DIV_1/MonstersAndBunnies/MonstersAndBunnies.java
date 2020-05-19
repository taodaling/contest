package on2020_05.on2020_05_19_Dynamic_Programming_Practice_DIV_1.MonstersAndBunnies;



import template.utils.ArrayIndex;

import java.util.Arrays;

public class MonstersAndBunnies {
    double[] dp;
    ArrayIndex ai;

    public double pick2(int n) {
        return n * (n - 1) / 2;
    }

    public double dp(int m, int b, int u) {
        if (m < 0 || b < 0) {
            return 0;
        }
        if (dp[ai.indexOf(m, b, u)] == -1) {
            if (u == 0) {
                return dp[ai.indexOf(m, b, u)] = 0;
            }
            if (m == 0) {
                return dp[ai.indexOf(m, b, u)] = 1;
            }
            double total = pick2(m + b + u);
            //kill rabbit
            double prob = 0;
            double self = 0;
            //m, m
            prob += dp(m - 2, b, u) * pick2(m) / total;
            //r, r
            self += pick2(b) / total;
            //m, r
            prob += dp(m, b - 1, u) * m * b / total;
            //m, u
            prob += dp(m, b, u - 1) * m * u / total;

            //u, r
            {
                //kill
                double p = prob + dp(m, b - 1, u) * b * u / total;
                dp[ai.indexOf(m, b, u)] = Math.max(dp[ai.indexOf(m, b, u)],
                        p / (1 - self));
            }
            {
                //release
                double p = prob;
                dp[ai.indexOf(m, b, u)] = Math.max(dp[ai.indexOf(m, b, u)],
                        p / (1 - (self + b * u / total)));
            }
        }
        return dp[ai.indexOf(m, b, u)];
    }

    public double survivalProbability(int M, int B) {
        ai = new ArrayIndex(M + 1, B + 1, 2);
        dp = new double[ai.totalSize()];
        Arrays.fill(dp, -1);
        double ans = dp(M, B, 1);
        return ans;
    }
}
