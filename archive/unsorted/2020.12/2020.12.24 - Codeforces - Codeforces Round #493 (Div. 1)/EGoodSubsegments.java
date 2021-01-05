package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.ContinuousIntervalProblem;

public class EGoodSubsegments {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }

        ContinuousIntervalProblem problem = new ContinuousIntervalProblem(p);
        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            long ans = problem.countContinuousIntervalBetween(l, r);
            out.println(ans);
        }
    }
}
