package on2020_10.on2020_10_20_Luogu.P4723______________;



import template.io.FastInput;
import template.math.DigitUtils;
import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.io.PrintWriter;

public class P4723 {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] f = new int[k];
        int[] a = new int[k];

        in.populate(f);
        in.populate(a);
        int mod = 998244353;
        for(int i = 0; i < k; i++){
            f[i] = DigitUtils.mod(f[i], mod);
            a[i] = DigitUtils.mod(a[i], mod);
        }
        IntPoly poly = new IntPolyNTT(mod);
        IntegerArrayList fList = new IntegerArrayList();
        fList.addAll(f);
        IntegerArrayList aList = new IntegerArrayList();
        aList.addAll(a);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver.newSolverFromLinearRecurrence(fList, mod, poly);
        int ans = solver.solve(n, aList);
        out.println(ans);
    }
}
