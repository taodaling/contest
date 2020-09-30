package template.math;

import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.BitSet;

public class ModLinearRecurrenceSolver {
    int mod;
    IntegerArrayList coe;
    IntegerArrayList p;
    IntegerArrayList remainder;
    Power pow;
    int n;

    private ModLinearRecurrenceSolver(IntegerArrayList coe, int mod) {
        this.coe = coe;
        this.mod = mod;
        n = coe.size();
        pow = new Power(mod);
        remainder = new IntegerArrayList(coe.size());
        p = new IntegerArrayList(coe.size() + 1);
        for (int i = n - 1; i >= 0; i--) {
            p.add(DigitUtils.negate(coe.get(i), mod));
        }
        p.add(1);
    }

    /**
     * a_i = coe(0) * (a_{i-1}) + ...
     */
    public static ModLinearRecurrenceSolver newSolverFromLinearRecurrence(IntegerArrayList coe, int mod) {
        return new ModLinearRecurrenceSolver(coe, mod);
    }

    /**
     * Auto detect linear recurrence from given sequence
     */
    public static ModLinearRecurrenceSolver newSolverByAutoDetectLinearRecurrence(IntegerArrayList seq, int mod) {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, seq.size());
        for (int i = 0; i < seq.size(); i++) {
            lfsr.add(seq.get(i));
        }
        IntegerArrayList coes = new IntegerArrayList(lfsr.length());
        for (int i = 1; i <= lfsr.length(); i++) {
            coes.add(lfsr.codeAt(i));
        }
        return newSolverFromLinearRecurrence(coes, mod);
    }

    private int solve(IntegerArrayList a) {
        long ans = 0;
        remainder.expandWith(0, n);
        for (int i = 0; i < n; i++) {
            ans += remainder.get(i) * (long) a.get(i) % mod;
        }
        ans %= mod;
        return (int) ans;
    }

    /**
     * when a_0 = a[0], a_1 = a[1], ...
     * <br>
     * Get a_k
     */
    public int solve(long k, IntegerArrayList a) {
        if (k < a.size()) {
            return a.get((int) k);
        }
        Polynomials.module(k, p, remainder, pow);
        return solve(a);
    }

    public IntegerArrayList getRemainder(long k, IntegerArrayList remainder) {
        Polynomials.module(k, p, remainder, pow);
        remainder.expandWith(0, n);
        return remainder;
    }

    /**
     * Get a_k
     */
    public int solve(BitSet k, IntegerArrayList a) {
        Polynomials.module(k, p, remainder, pow);
        return solve(a);
    }

    @Override
    public String toString() {
        return coe.toString();
    }
}
