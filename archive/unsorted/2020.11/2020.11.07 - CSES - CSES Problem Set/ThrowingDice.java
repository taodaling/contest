package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPolyFFT;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ThrowingDice {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int mod = (int) 1e9 + 7;
        int[] prefix = new int[12];
        prefix[0] = 1;
        for (int i = 1; i < 12; i++) {
            for (int j = 1; j <= 6 && i - j >= 0; j++) {
                prefix[i] += prefix[i - j];
                if (prefix[i] >= mod) {
                    prefix[i] -= mod;
                }
            }
        }
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver
                .newSolverByAutoDetectLinearRecurrence(new IntegerArrayList(prefix), mod, new IntPolyFFT(mod));
        long n = in.readLong();
        int ans = solver.solve(n, new IntegerArrayList(prefix));
        out.println(ans);

    }
}
