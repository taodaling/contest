package template.math;

import template.datastructure.IntList;
import template.polynomial.GravityModLagrangeInterpolation;
import template.polynomial.Polynomials;

import java.util.BitSet;

public class ModMatrixLinearRecurrenceSolver {
    Modular mod;
    IntList[] a;
    IntList p;
    IntList remainder;
    Power pow;
    int n;
    int m;

    private void init(IntList[] a, IntList coe, Modular mod) {
        this.a = a;
        this.mod = mod;
        n = coe.size();
        m = a[0].size();
        pow = new Power(mod);
        remainder = new IntList(coe.size());
        p = coe;
    }

    private IntList solve() {
        IntList ans = new IntList(m);
        remainder.expandWith(0, n);
        for (int i = 0; i < m; i++) {
            int val = 0;
            for (int j = 0; j < n; j++) {
                val = mod.plus(val, mod.mul(remainder.get(j), a[j].get(i)));
            }
            ans.add(val);
        }
        return ans;
    }

    public IntList solve(long k) {
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }

    public IntList solve(BitSet k) {
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }

    /**
     * Auto detect linear recurrence from given matrix and vec
     */
    public ModMatrixLinearRecurrenceSolver(ModMatrix mat, IntList vec, Modular mod) {
        GravityModLagrangeInterpolation.Polynomial p = mat.getCharacteristicPolynomial(new Power(mod));
        IntList coe = p.toIntList();
        ModMatrix transpose = ModMatrix.transposition(mat);
        int m = coe.size();
        IntList[] lists = new IntList[m];
        ModMatrix vector = new ModMatrix(1, vec.size());
        for (int i = 0; i < vec.size(); i++) {
            vector.set(0, i, vec.get(i));
        }
        lists[0] = vec;
        for (int i = 1; i < m; i++) {
            vector = ModMatrix.mul(vector, transpose, mod);
            lists[i] = new IntList(vec.size());
            for (int j = 0; j < vec.size(); j++) {
                lists[i].add(vector.get(0, j));
            }
        }
        init(lists, coe, mod);
    }
}
