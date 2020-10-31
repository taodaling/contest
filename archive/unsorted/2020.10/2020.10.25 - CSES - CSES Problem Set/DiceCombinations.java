package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.math.ModLinearRecurrenceSolver;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.io.PrintWriter;

public class DiceCombinations {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int mod = (int) 1e9 + 7;
        int[] dp = new int[18];
        dp[0] = 1;
        for (int i = 1; i < 18; i++) {
            for (int j = 1; j <= 6; j++) {
                if (i - j < 0) {
                    continue;
                }
                dp[i] = DigitUtils.modplus(dp[i], dp[i - j], mod);
            }
        }
        IntPoly poly = new IntPolyFFT(mod);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver.newSolverByAutoDetectLinearRecurrence(new IntegerArrayList(dp), mod, poly);
        int ans = solver.solve(n, new IntegerArrayList(dp));
        out.println(ans);
    }
}
