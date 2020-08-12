package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.ContinuousIntervalProblem;

public class P4747CERC2017IntrinsicInterval {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
        }
        ContinuousIntervalProblem problem = new ContinuousIntervalProblem(p);
        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            int[] ans = problem.findMinContinuousIntervalContains(l, r);
            out.append(ans[0] + 1).append(' ').append(ans[1] + 1).println();
        }
    }
}
