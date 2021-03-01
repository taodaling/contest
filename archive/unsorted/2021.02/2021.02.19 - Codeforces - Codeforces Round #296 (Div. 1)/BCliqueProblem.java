package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.MaximumNotIntersectIntervals;

import java.util.Arrays;

public class BCliqueProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        MaximumNotIntersectIntervals.Interval[] intervals = new MaximumNotIntersectIntervals.Interval[n];
        for(int i = 0; i < n; i++){
            int x = in.ri();
            int w = in.ri();
            intervals[i] = new MaximumNotIntersectIntervals.Interval(x - w, x + w);
        }
        long ans = new MaximumNotIntersectIntervals(intervals).query(-(long)1e18, (long)1e18);
        out.println(ans);
    }
}
