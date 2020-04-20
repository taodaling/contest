package on2020_04.on2020_04_19_2020_TopCoder_Open_Algo.BlindBoxSets;



public class BlindBoxSets {
    double[][][][] dp;
    int m;
    int n;

    public double dp(int a, int b, int c, int d) {
        if (m <= 1 && b > 0) {
            return dp(a, 0, 0, 0);
        }
        if (m <= 2 && c > 0) {
            return dp(a, b, 0, 0);
        }
        if (m <= 3 && d > 0) {
            return dp(a, b, c, 0);
        }


        if (dp[a][b][c][d] == -1) {
            if (a == 0 && b == 0 && c == 0 && d == 0) {
                return dp[a][b][c][d] = 0;
            }
            double dp0 = (double) a / n;
            double dp1 = (double) b / n;
            double dp2 = (double) c / n;
            double dp3 = (double) d / n;
            double self = 1 - dp0 - dp1 - dp2 - dp3;
            double exp = 1;
            if (a > 0) {
                exp += dp(a - 1, b + 1, c, d) * dp0;
            }
            if (b > 0) {
                exp += dp(a, b - 1, c + 1, d) * dp1;
            }
            if (c > 0) {
                exp += dp(a, b, c - 1, d + 1) * dp2;
            }
            if (d > 0) {
                exp += dp(a, b, c, d - 1) * dp3;
            }
            exp /= 1 - self;
            dp[a][b][c][d] = exp;
        }
        return dp[a][b][c][d];
    }

    public double expectedPurchases(int numSets, int numItems) {
        this.m = numSets;
        this.n = numItems;
        dp = new double[numItems + 1][numItems + 1][numItems + 1][numItems + 1];
        for (int i = 0; i <= numItems; i++) {
            for (int j = 0; j <= numItems; j++) {
                for (int k = 0; k <= numItems; k++) {
                    for (int t = 0; t <= numItems; t++) {
                        dp[i][j][k][t] = -1;
                    }
                }
            }
        }

        double exp = dp(numItems, 0, 0, 0);
        return exp;
    }
}
