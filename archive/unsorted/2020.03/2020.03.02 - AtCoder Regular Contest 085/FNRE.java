package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.IntervalPickProblem;

public class FNRE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] val = new long[n + 1];
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            int x = in.readInt();
            val[i] = x == 1 ? 1 : -1;
            sum += x;
        }
        int q = in.readInt();
        IntervalPickProblem.Interval[] intervals = new IntervalPickProblem.Interval[q];
        for (int i = 0; i < q; i++) {
            intervals[i] = new IntervalPickProblem.Interval(in.readInt(), in.readInt());
        }
        int minus = (int) IntervalPickProblem.solve(val, intervals);
        sum -= minus;
        out.println(sum);
    }
}
