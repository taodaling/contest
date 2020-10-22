package on2020_10.on2020_10_20_Luogu.P5487_____Berlekamp_Massey__;



import template.io.FastInput;
import template.math.ModLinearRecurrenceSolver;
import template.math.ModMatrix;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.io.PrintWriter;

public class P5487BerlekampMassey {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int mod = 998244353;
        int n = in.readInt();
        int m = in.readInt();
        int[] p = new int[n];
        in.populate(p);

        IntegerArrayList pList = new IntegerArrayList(p);
        IntPoly poly = new IntPolyFFT(mod);
        ModLinearRecurrenceSolver solver =
                ModLinearRecurrenceSolver.newSolverByAutoDetectLinearRecurrence(pList,
                        mod, poly);
        IntegerArrayList coe = solver.getCoe();
        for (int i = 0; i < coe.size(); i++) {
            out.print(coe.get(i));
            out.append(' ');
        }
        out.println();
        out.println(solver.solve(m, pList));
    }
}
