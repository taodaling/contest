package on2020_05.on2020_05_04_Codeforces___MemSQL_Start_c_UP_3_0___Round_2_and_Codeforces_Round__437__Div__1_.C__Gotta_Go_Fast;



import template.algo.DoubleBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class CGottaGoFast {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        r = in.readInt();
        dp = new double[n][r + 1];
        fast = new int[n];
        slow = new int[n];
        prob = new double[n];
        for (int i = 0; i < n; i++) {
            fast[i] = in.readInt();
            slow[i] = in.readInt();
            prob[i] = in.readInt() / 100D;
        }

        DoubleBinarySearch dbs = new DoubleBinarySearch(1e-10, 1e-10) {
            @Override
            public boolean check(double mid) {
                threshold = mid;
                SequenceUtils.deepFill(dp, -1D);
                return dp(0, 0) < mid;
            }
        };
        double ans = dbs.binarySearch(0, 1e12);
        out.println(ans);
    }

    int r;
    double threshold;
    double[][] dp;
    int[] fast;
    int[] slow;
    double[] prob;
    int n;

    public double dp(int i, int j) {
        if (j > r) {
            return threshold;
        }
        if (i == n) {
            return 0;
        }

        if (dp[i][j] == -1) {
            dp[i][j] = Math.min(threshold,
                    prob[i] * (fast[i] + dp(i + 1, j + fast[i])) +
                            (1 - prob[i]) * (slow[i] + dp(i + 1, j + slow[i])));
        }

        return dp[i][j];
    }
}
