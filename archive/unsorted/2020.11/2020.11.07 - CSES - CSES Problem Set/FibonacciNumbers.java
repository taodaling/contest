package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPolyFFT;
import template.primitve.generated.datastructure.IntegerArrayList;

public class FibonacciNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int mod = (int)1e9 + 7;
        IntegerArrayList list = new IntegerArrayList();
        list.add(1);
        list.add(1);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver
                .newSolverFromLinearRecurrence(list, mod, new IntPolyFFT(mod));
        long n = in.readLong();
        IntegerArrayList prefix = new IntegerArrayList();
        prefix.add(0);
        prefix.add(1);
        int ans = solver.solve(n, prefix);
        out.println(ans);
    }
}
