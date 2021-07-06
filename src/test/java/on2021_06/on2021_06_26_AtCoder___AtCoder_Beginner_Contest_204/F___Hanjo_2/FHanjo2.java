package on2021_06.on2021_06_26_AtCoder___AtCoder_Beginner_Contest_204.F___Hanjo_2;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ModLinearFeedbackShiftRegister;
import template.math.ModLinearRecurrenceSolver;
import template.math.ModMatrix;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.util.Arrays;

public class FHanjo2 {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.ri();
        long w = in.rl();
        IntegerArrayList rec = dp(h, 1 << h + 1);
        IntPoly poly = new IntPolyNTT(mod);
        ModLinearRecurrenceSolver solver = ModLinearRecurrenceSolver.newSolverByAutoDetectLinearRecurrence(rec, mod, poly);
        long ans = solver.solve(w, rec);
        out.println(ans);
    }

    IntegerArrayList dp(int h, int k) {
        IntegerArrayList list = new IntegerArrayList(k);
        long[] prev = new long[1 << h];
        prev[0] = 1;
        long[] next = new long[1 << h];
        list.add(1);
        for (int t = 0; t < k; t++) {
            for (int i = 0; i < h; i++) {
                Arrays.fill(next, 0);
                for (int j = 0; j < prev.length; j++) {
                    //put hanjo or nothing
                    {
                        int to = Bits.clear(j, i);
                        next[to] += prev[j];
                    }
                    //put vertical
                    {
                        if (Bits.get(j, i) == 0) {
                            int to = Bits.set(j, i);
                            next[to] += prev[j];
                        }
                    }
                    //put horizontal
                    {
                        if (Bits.get(j, i) == 0 && Bits.get(j, i + 1) == 0 && i + 1 < h) {
                            int to = Bits.set(j, i + 1);
                            next[to] += prev[j];
                        }
                    }
                }
                for (int j = 0; j < next.length; j++) {
                    next[j] = DigitUtils.modWithoutDivision(next[j], mod);
                }
                long[] tmp = prev;
                prev = next;
                next = tmp;
            }
            list.add((int) prev[0]);
        }
        return list;
    }
}
