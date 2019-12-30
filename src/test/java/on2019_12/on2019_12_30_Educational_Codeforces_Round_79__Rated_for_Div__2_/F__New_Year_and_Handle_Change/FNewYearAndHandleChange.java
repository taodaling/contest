package on2019_12.on2019_12_30_Educational_Codeforces_Round_79__Rated_for_Div__2_.F__New_Year_and_Handle_Change;



import template.algo.DoubleBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.IntegerPreSum;

public class FNewYearAndHandleChange {
    int n;
    int k;
    int l;
    int[] s;
    IntegerPreSum ps;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt();
        l = in.readInt();

        s = new int[n];
        for (int i = 0; i < n; i++) {
            if (Character.isUpperCase(in.readChar())) {
                s[i] = 1;
            } else {
                s[i] = 0;
            }
        }

        int lower = solve();
        for (int i = 0; i < n; i++) {
            s[i] = 1 - s[i];
        }
        int upper = solve();
        out.println(Math.min(lower, upper));
    }

    public int solve() {
        ps = new IntegerPreSum(s);

        DoubleBinarySearch dbs = new DoubleBinarySearch(1e-8, 1e-8) {
            @Override
            public boolean check(double mid) {
                Recorder recorder = dp(mid);
                return recorder.time <= k;
            }
        };

        double cost = dbs.binarySearch(0, l);
        double ans = dp(cost).ans - k * cost;
        return DigitUtils.roundToInt(ans);
    }

    public Recorder dp(double cost) {
        double[] dp = new double[n];
        int[] time = new int[n];
        for (int i = 0; i < n; i++) {
            if (i < l) {
                if (ps.prefix(i) <= cost) {
                    dp[i] = ps.prefix(i);
                } else {
                    dp[i] = cost;
                    time[i] = 1;
                }
            } else {
                if (dp[i - 1] + s[i] < dp[i - l] + cost) {
                    dp[i] = dp[i - 1] + s[i];
                    time[i] = time[i - 1];
                } else {
                    dp[i] = dp[i - l] + cost;
                    time[i] = time[i - l] + 1;
                }
            }
        }
        Recorder recorder = new Recorder();
        recorder.ans = dp[n - 1];
        recorder.time = time[n - 1];
        return recorder;
    }
}

class Recorder {
    int time;
    double ans;
}
