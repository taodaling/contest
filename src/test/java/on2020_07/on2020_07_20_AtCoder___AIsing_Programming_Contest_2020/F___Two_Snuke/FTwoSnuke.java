package on2020_07.on2020_07_20_AtCoder___AIsing_Programming_Contest_2020.F___Two_Snuke;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.LinearRecurrenceSolver;
import template.math.Modular;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class FTwoSnuke {
    Modular mod = new Modular(1e9 + 7);
    LinearRecurrenceSolver solver;
    IntegerArrayList seq;

    {
        int threshold = 42;
        IntegerArrayList p = new IntegerArrayList(threshold);
        IntegerArrayList p2 = new IntegerArrayList(threshold);
        for (int i = 0; i < threshold; i++) {
            p.add(1);
            p2.add(i % 2 == 0 ? 1 : 0);
        }
        IntegerArrayList p11 = new IntegerArrayList();
        IntegerArrayList p25 = new IntegerArrayList();
        Polynomials.modpow(p, p11, 11, mod, threshold);
        Polynomials.modpow(p2, p25, 5, mod, threshold);
        seq = new IntegerArrayList();
        Polynomials.mul(p11, p25, seq, mod);
        seq.expandWith(0, threshold);
        seq.remove(threshold, seq.size() - 1);
        solver = LinearRecurrenceSolver.newSolverByAutoDetectLinearRecurrence(seq, mod);
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = n - 5;
        if (m < 0) {
            out.println(0);
            return;
        }
        int ans = solver.solve(m, seq);
        out.println(ans);
    }
}
