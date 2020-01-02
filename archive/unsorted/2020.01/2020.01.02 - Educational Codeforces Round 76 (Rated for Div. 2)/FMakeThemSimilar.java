package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.math.DigitUtils;
import template.math.LinearProgramming;

public class FMakeThemSimilar {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        LinearProgramming lp = new LinearProgramming(n * 2 + 30, 31, 1e-8);
       // lp.setTargetCoefficient(31, -1);
        for (int i = 1; i <= n; i++) {
            int constant = 0;
            for (int j = 1; j <= 30; j++) {
                int bit = Bits.bitAt(a[i - 1], j - 1);
                if (bit == 1) {
                    constant += 1;
                    lp.setConstraintCoefficient(i, j, -1);
                    lp.setConstraintCoefficient(i + n, j, 1);
                } else {
                    lp.setConstraintCoefficient(i, j, 1);
                    lp.setConstraintCoefficient(i + n, j, -1);
                }
            }
            lp.setConstraintCoefficient(i, 31, -1);
            lp.setConstraintCoefficient(i + n, 31, -1);
            lp.setConstraintConstant(i, -constant);
            lp.setConstraintConstant(i + n, constant);
        }
        for (int i = 1; i <= 30; i++) {
            lp.setConstraintCoefficient(n * 2 + i, i, 1);
            lp.setConstraintConstant(n * 2 + i, 1);
        }
        lp.solve();
        if (lp.isInfeasible()) {
            out.println(-1);
            return;
        }
        int ans = 0;
        for (int i = 1; i <= 30; i++) {
            int x = DigitUtils.roundToInt(lp.getAssignmentValueForVariable(i));
            ans = Bits.setBit(ans, i - 1, x == 1);
        }

        out.println(ans);
    }
}
