package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LinearRecurrenceSolver;
import template.math.ModMatrix;
import template.math.ModMatrixLinearRecurrenceSolver;
import template.math.Modular;

public class LUOGU3390 {
    IntegerList a = new IntegerList();
    Modular mod = new Modular(1e9 + 7);
    LinearRecurrenceSolver solver;

    {
        a.expandWith(1, 3);
        IntegerList seq = new IntegerList();
        seq.addAll(new int[]{1, 1, 1, 2, 3, 4});
        solver = LinearRecurrenceSolver.newSolverFromSequence(seq, mod);
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int ans = solver.solve( n - 1, a);
        out.println(ans);
    }
}
