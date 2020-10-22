package on2020_10.on2020_10_20_Codeforces___Educational_Codeforces_Round_96__Rated_for_Div__2_.G__Yet_Another_DAG_Problem;



import template.io.FastInput;
import template.math.DualLinearProgramming;

import java.io.PrintWriter;

public class GYetAnotherDAGProblem {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        DualLinearProgramming lp = new DualLinearProgramming(m, n, 1e-10);
        int[] weights = new int[n];
        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            int w = in.readInt();
            weights[a - 1] += w;
            weights[b - 1] -= w;
            lp.setConstraintCoefficient(i + 1, a, 1);
            lp.setConstraintCoefficient(i + 1, b, -1);
            lp.setConstraintConstant(i + 1, 1);
        }
        for (int i = 0; i < n; i++) {
            lp.setTargetCoefficient(i + 1, weights[i]);
        }
        lp.solve();
        assert !lp.isInfeasible();
        assert !lp.isUnbound();
        for (int i = 0; i < n; i++) {
            long x = Math.round(lp.getAssignmentValueForVariable(i + 1));
            out.print(x);
            out.append(' ');
        }
    }
}
