package contest;

import template.algo.DoubleGeqSlopeOptimizer;
import template.algo.DoubleLeqSlopeOptimizer;
import template.algo.GeqSlopeOptimizer;
import template.algo.WQSBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class P5308COCI2019Quiz {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        double[] dp = new double[n + 1];
        int[] times = new int[n + 1];
        DoubleGeqSlopeOptimizer optimizer = new DoubleGeqSlopeOptimizer(n + 1);

        WQSBinarySearch bs = new WQSBinarySearch() {
            double best;
            int time;

            @Override
            public double getBest() {
                return best;
            }

            @Override
            public int getTime() {
                return time;
            }

            @Override
            public void check(double costPerOperation) {
                time = 0;
                times[0] = 0;
                dp[0] = 0;
                optimizer.clear();
                optimizer.add(0, 0, 0);
                for (int i = 1; i <= n; i++) {
                    int j = optimizer.getBestChoice(1D / i);
                    times[i] = times[j] + 1;
                    dp[i] = 1 + dp[j] - (double) j / i - costPerOperation;
                    optimizer.add(dp[i], i, i);
                }
                best = dp[n];
                time = times[n];
            }
        };

        double ans = bs.search(0, 1, 60, k, true);
        out.printf("%.9f", ans);
    }
}
