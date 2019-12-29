package on2019_12.on2019_12_29_.LUOGU5308;



import template.algo.DoubleBinarySearch;
import template.algo.DoubleLeqSlopeOptimizer;
import template.io.FastInput;
import template.io.FastOutput;

public class LUOGU5308 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        double answer = 0;
        double l = 0;
        double r = 1;
        while (true) {
            double mid = (l + r) / 2;
            Recorder recorder = new Recorder();
            solve(n, mid, recorder);
            if (recorder.time == k) {
                answer = recorder.time * mid + recorder.ans;
                break;
            }
            if (recorder.time < k) {
                r = mid;
            } else {
                l = mid;
            }
        }

        out.printf("%.9f", answer);
    }

    public void solve(int n, double cost, Recorder recorder) {
        double[] dp = new double[n + 1];
        int[] time = new int[n + 1];
        dp[0] = 0;
        DoubleLeqSlopeOptimizer optimizer = new DoubleLeqSlopeOptimizer(n);
        optimizer.add(-dp[0], 1.0D / n, 0);
        for (int i = 1; i <= n; i++) {
            int j = optimizer.getBestChoice(i - n);
            dp[i] = dp[j] + (double) (i - j) / (n - j) - cost;
            time[i] = time[j] + 1;
            if (i != n) {
                optimizer.add(-dp[i], 1.0D / (n - i), i);
            }
        }
        recorder.ans = dp[n];
        recorder.time = time[n];
    }
}

class Recorder {
    double ans;
    int time;
}