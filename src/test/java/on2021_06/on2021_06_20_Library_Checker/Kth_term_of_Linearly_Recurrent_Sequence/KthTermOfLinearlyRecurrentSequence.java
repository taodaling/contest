package on2021_06.on2021_06_20_Library_Checker.Kth_term_of_Linearly_Recurrent_Sequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.primitve.generated.datastructure.IntegerArrayList;

public class KthTermOfLinearlyRecurrentSequence {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int d = in.ri();
        long k = in.rl();
        int[] a = in.ri(d);
        int[] c = in.ri(d);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver.newSolverFromLinearRecurrence(new IntegerArrayList(c),
                mod, new IntPolyFFT(mod));
        int ans = solver.solve(k, new IntegerArrayList(a));
        out.println(ans);
    }
}
