package template.math;

import template.polynomial.Polynomials;
import template.primitve.generated.IntegerList;

import java.util.BitSet;

public class LinearRecurrenceSolver {
    Modular mod;
    IntegerList coe;
    IntegerList p;
    IntegerList remainder;
    Power pow;
    int n;

    private LinearRecurrenceSolver(IntegerList coe, Modular mod) {
        this.coe = coe;
        this.mod = mod;
        n = coe.size();
        pow = new Power(mod);
        remainder = new IntegerList(coe.size());
        p = new IntegerList(coe.size() + 1);
        for (int i = n - 1; i >= 0; i--) {
            p.add(mod.valueOf(-coe.get(i)));
        }
        p.add(1);
    }

    /**
     * a_i = coe(0) * (a_{i-1}) + ...
     */
    public static LinearRecurrenceSolver newSolverFromLinearRecurrence(IntegerList coe, Modular mod) {
        return new LinearRecurrenceSolver(coe, mod);
    }

    /**
     * Auto detect linear recurrence from given sequence
     */
    public static LinearRecurrenceSolver newSolverFromSequence(IntegerList seq, Modular mod) {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, seq.size());
        for (int i = 0; i < seq.size(); i++) {
            lfsr.add(seq.get(i));
        }
        IntegerList coes = new IntegerList(lfsr.length());
        for (int i = 1; i <= lfsr.length(); i++) {
            coes.add(lfsr.codeAt(i));
        }
        return newSolverFromLinearRecurrence(coes, mod);
    }

    private int solve(IntegerList a) {
        int ans = 0;
        remainder.expandWith(0, n);
        for (int i = 0; i < n; i++) {
            ans = mod.plus(ans, mod.mul(remainder.get(i), a.get(i)));
        }
        return ans;
    }

    /**
     * when a_0 = a[0], a_1 = a[1], ...
     * <br>
     * Get a_k
     */
    public int solve(long k, IntegerList a) {
        if (k < a.size()) {
            return a.get((int) k);
        }
        Polynomials.module(k, p, remainder, pow);
        return solve(a);
    }

    public IntegerList getRemainder(long k, IntegerList remainder) {
        Polynomials.module(k, p, remainder, pow);
        remainder.expandWith(0, n);
        return remainder;
    }

    /**
     * Get a_k
     */
    public int solve(BitSet k, IntegerList a) {
        Polynomials.module(k, p, remainder, pow);
        return solve(a);
    }
}
