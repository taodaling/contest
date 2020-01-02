package on2020_01.on2020_01_02_Mail_Ru_Cup_2018_Round_3.F__Write_The_Contest;



import template.algo.DoubleTernarySearch;
import template.algo.IntTernarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.function.DoubleUnaryOperator;

public class FWriteTheContest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        double c = in.readDouble();
        double t = in.readDouble();
        double s = 1;

        double[] bests = new double[n + 1];
        for(int i = 0; i <= n; i++) {
            if(i * 10 >= t){
                continue;
            }
            double punish = 10 * i;
            DoubleUnaryOperator func = x -> (s + c * x) * (t - x - punish);
            DoubleTernarySearch dts = new DoubleTernarySearch(func, 1e-12, 1e-6);
            double best = func.applyAsDouble(dts.find(0, t));
            bests[i] = best;
        }
        int[][] problems = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                problems[i][j] = in.readInt();
            }
        }

        double[] pow = new double[n + 1];
        pow[0] = 1;
        for (int i = 1; i <= n; i++) {
            pow[i] = pow[i - 1] * 0.9D;
        }
        Arrays.sort(problems, (a, b) -> -(a[0] - b[0]));
        int limit = 10 * n;
        double[][] dp = new double[n + 1][limit + 1];
        SequenceUtils.deepFill(dp, 1e18);
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            int a = problems[i][0];
            int p = problems[i][1];
            for (int j = n; j >= 0; j--) {
                for (int k = 0; k <= limit; k++) {
                    if (j >= 1 && k >= p) {
                        dp[j][k] = Math.min(dp[j][k], dp[j - 1][k - p] + a / pow[j]);
                    }
                }
            }
        }

        for (int i = limit; i >= 0; i--) {
            for (int j = 0; j <= n; j++) {
                if (dp[j][i] <= bests[j]) {
                    out.println(i);
                    return;
                }
            }
        }
    }
}
