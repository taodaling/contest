package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.KahanSummation;
import template.utils.SequenceUtils;

public class Roshambo {
    double[][] comb = new double[101][101];
    double[] pow3 = new double[101];

    public double comb(int n, int m) {
        if (comb[n][m] == -1) {
            if (n < m) {
                return comb[n][m] = 0;
            }
            if (m == 0) {
                return comb[n][m] = 1;
            }
            comb[n][m] = comb(n - 1, m) + comb(n - 1, m - 1);
        }
        return comb[n][m];
    }

    public double pow3(int n) {
        if (pow3[n] == -1) {
            if (n == 0) {
                return pow3[n] = 1;
            }
            pow3[n] = pow3(n - 1) * 3;
        }
        return pow3[n];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        SequenceUtils.deepFill(comb, -1D);
        SequenceUtils.deepFill(pow3, -1D);

        int n = in.readInt();
        double[] dp = new double[n + 1];
        dp[1] = 0;
        for (int i = 2; i <= n; i++) {
            KahanSummation prob = new KahanSummation();
            KahanSummation exp = new KahanSummation();
            exp.add(1);

            for (int to = 1; to < i; to++) {
                double p = 3 * comb(i, to) / pow3(i);
                prob.add(p);
                exp.add(p * dp[to]);
            }

            dp[i] = exp.sum() / prob.sum();
        }

        double ans = dp[n];
        out.println(ans);
    }
}
