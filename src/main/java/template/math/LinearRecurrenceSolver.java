package template.math;

import template.datastructure.IntList;
import template.polynomial.GravityModLagrangeInterpolation;
import template.polynomial.Polynomials;

import java.util.BitSet;

public class LinearRecurrenceSolver {
    Modular mod;
    IntList a;
    IntList coe;
    IntList p;
    IntList remainder;
    Power pow;
    int n;

    private void init(IntList a, IntList coe, Modular mod) {
        if (a.size() < coe.size()) {
            throw new IllegalArgumentException();
        }
        this.a = a;
        this.coe = coe;
        this.mod = mod;
        n = coe.size();
        pow = new Power(mod);
        remainder = new IntList(a.size());
        p = new IntList(a.size());
        for (int i = n - 1; i >= 0; i--) {
            p.add(mod.valueOf(-coe.get(i)));
        }
        p.add(1);

    }

    /**
     * a_i = coe(0) * (a_{i-1}) + ...
     */
    public LinearRecurrenceSolver(IntList a, IntList coe, Modular mod) {
        init(a, coe, mod);
    }

    /**
     * Auto detect linear recurrence from given sequence
     */
    public LinearRecurrenceSolver(IntList seq, Modular mod) {
        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, seq.size());
        for (int i = 0; i < seq.size(); i++) {
            lfsr.add(seq.get(i));
        }
        IntList coes = new IntList(lfsr.length());
        for (int i = 1; i <= lfsr.length(); i++) {
            coes.add(lfsr.codeAt(i));
        }
        init(seq, coes, mod);
    }

    /**
     * Auto detect linear recurrence from given matrix and vec
     */
    public LinearRecurrenceSolver(ModMatrix mat, IntList vec, Modular mod) {
        GravityModLagrangeInterpolation.Polynomial p = mat.getCharacteristicPolynomial(new Power(mod));
        IntList coe = new IntList(p.getRank() + 1);
        for (int i = p.getRank(); i >= 0; i--) {
            coe.add(p.getCoefficient(i));
        }
        init(vec, coe, mod);
    }

    private int solve() {
        int ans = 0;
        remainder.expandWith(0, n);
        for (int i = 0; i < n; i++) {
            ans = mod.plus(ans, mod.mul(remainder.get(i), a.get(i)));
        }
        return ans;
    }

    /**
     * Get a_k
     */
    public int solve(long k) {
        if (k < a.size()) {
            return a.get((int) k);
        }
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }

    /**
     * Get a_k
     */
    public int solve(BitSet k) {
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }
}
