package on2021_06.on2021_06_20_Luogu.Task;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.primitve.generated.datastructure.IntegerArrayList;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] f = new int[k];
        int[] a = new int[k];

        in.populate(f);
        in.populate(a);
        int mod = 998244353;
        for (int i = 0; i < k; i++) {
            f[i] = DigitUtils.mod(f[i], mod);
            a[i] = DigitUtils.mod(a[i], mod);
        }
        IntPoly poly = new IntPolyFFT(mod);
        IntegerArrayList fList = new IntegerArrayList();
        fList.addAll(f);
        IntegerArrayList aList = new IntegerArrayList();
        aList.addAll(a);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver.newSolverFromLinearRecurrence(fList, mod, poly);
        int ans = solver.solve(n, aList);
        out.println(ans);
    }
}
