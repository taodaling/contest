package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.GcdLcmProblem;

public class FMakeItOne {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        GcdLcmProblem problem = new GcdLcmProblem(300000, a);
        int ans = problem.minimumSizeOfSubsetWhoseGCDDivisibleBy(1);
        if (ans == GcdLcmProblem.INF) {
            out.println(-1);
        } else {
            out.println(ans);
        }
    }
}
