package on2021_01.on2021_01_22_Luogu.P6244__USACO06OPEN_County_Fair_Events_S;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.MaximumNotIntersectIntervals;

public class P6244USACO06OPENCountyFairEventsS {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        MaximumNotIntersectIntervals.Interval[] intervals = new MaximumNotIntersectIntervals.Interval[n];
        for (int i = 0; i < n; i++) {
            int l = in.ri();
            int len = in.ri();
            intervals[i] = new MaximumNotIntersectIntervals.Interval(l, l + len);
        }
        MaximumNotIntersectIntervals mi = new MaximumNotIntersectIntervals(intervals);
        int ans = mi.query(-(int)1e9, (int)1e9);
        out.println(ans);
    }
}
