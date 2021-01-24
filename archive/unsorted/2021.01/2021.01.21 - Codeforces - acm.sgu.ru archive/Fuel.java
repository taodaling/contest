package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LinearProgramming;
import template.utils.Debug;

public class Fuel {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int A = in.ri();
        int B = in.ri();
        int[] a = new int[n];
        int[] b = new int[n];
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
            b[i] = in.ri();
            c[i] = in.ri();
        }

        LinearProgramming lp = new LinearProgramming(2, n, 1e-8);
        for (int i = 0; i < n; i++) {
            lp.setConstraintCoefficient(1, i + 1, b[i]);
            lp.setConstraintCoefficient(2, i + 1, a[i]);
            lp.setTargetCoefficient(i + 1, c[i]);
        }
        lp.setConstraintConstant(1, B);
        lp.setConstraintConstant(2, A);
        debug.debug("lp", lp);
        lp.solve();
        assert !lp.isInfeasible();
        assert !lp.isUnbound();
        out.println(lp.maxSolution());
    }
}
