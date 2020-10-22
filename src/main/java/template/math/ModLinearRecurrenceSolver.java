package template.math;

import template.datastructure.BitSet;
import template.polynomial.IntPoly;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

public class ModLinearRecurrenceSolver {
    int mod;
    int[] p;
    Power pow;
    int n;
    IntPoly poly;
    IntegerArrayList coe;

    public IntegerArrayList getCoe() {
        return coe;
    }


    private ModLinearRecurrenceSolver(IntegerArrayList coe, int mod, IntPoly poly) {
        this.coe = coe;
        this.mod = mod;
        this.poly = poly;
        n = coe.size();
        pow = new Power(mod);
        p = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            p[n - 1 - i] = DigitUtils.negate(coe.get(i), mod);
        }
        p[n] = 1;
    }

    /**
     * a_i = coe(0) * (a_{i-1}) + ...
     */
    public static ModLinearRecurrenceSolver newSolverFromLinearRecurrence(IntegerArrayList coe, int mod, IntPoly poly) {
        return new ModLinearRecurrenceSolver(coe, mod, poly);
    }

    /**
     * Auto detect linear recurrence from given sequence
     */
    public static ModLinearRecurrenceSolver newSolverByAutoDetectLinearRecurrence(IntegerArrayList seq, int mod, IntPoly poly) {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, seq.size());
        for (int i = 0; i < seq.size(); i++) {
            lfsr.add(seq.get(i));
        }
        IntegerArrayList coes = new IntegerArrayList(lfsr.length());
        for (int i = 1; i <= lfsr.length(); i++) {
            coes.add(lfsr.codeAt(i));
        }
        return newSolverFromLinearRecurrence(coes, mod, poly);
    }

    private int solve(IntegerArrayList a, int[] remainder) {
        long ans = 0;
        for (int i = 0; i < n && i < remainder.length; i++) {
            ans += remainder[i] * (long) a.get(i) % mod;
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
        int[] remainder = poly.module(k, p);
        int ans = solve(a, remainder);
        PrimitiveBuffers.release(remainder);
        return ans;
    }

    public int[] getRemainder(long k) {
        int[] ans = poly.module(k, p);
        int rank = poly.rankOf(ans);
        return PrimitiveBuffers.replace(Arrays.copyOf(ans, rank + 1), ans);
    }

    /**
     * Get a_k
     */
    public int solve(BitSet k, IntegerArrayList a) {
        int[] remainder = poly.module(k, p);
        int ans = solve(a, remainder);
        PrimitiveBuffers.release(remainder);
        return ans;
    }

    @Override
    public String toString() {
        return coe.toString();
    }
}
