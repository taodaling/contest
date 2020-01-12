package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.IntervalPickProblem;

public class FNewYearAndHandleChange {
    int n;
    int k;
    int l;
    long[] data;
    IntervalPickProblem.Interval[] intervals;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt();
        l = in.readInt();
        data = new long[n];
        in.readString(data, 0);
        for (int i = 0; i < n; i++) {
            data[i] = Character.isUpperCase((char)data[i]) ? 1 : 0;
        }
        //i + l - 1 < n => i < n + 1 - l
        intervals = new IntervalPickProblem.Interval[n + 1 - l];
        for(int i = 0; i < intervals.length; i++){
            intervals[i] = new IntervalPickProblem.Interval(i, i + l - 1);
        }

        long ans = solve();
        for(int i = 0; i < n; i++){
            data[i] = 1 - data[i];
        }
        ans = Math.min(ans, solve());
        out.println(ans);
    }

    public long solve() {
        int sum = 0;
        for(int i = 0; i < n; i++){
            sum += data[i];
        }
        return sum - IntervalPickProblem.solveNonNegative(data, intervals, k);
    }
}
