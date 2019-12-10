package template.math;

import template.datastructure.IntList;
import template.polynomial.GravityModLagrangeInterpolation;
import template.polynomial.Polynomials;

import java.util.BitSet;

public class MatrixLinearRecurrenceSolver {
    Modular mod;
    IntList[] a;
    IntList coe;
    IntList p;
    IntList remainder;
    Power pow;
    int n;
    int m;

    private void init(IntList[] a, IntList coe, Modular mod) {
        this.a = a;
        this.coe = coe;
        this.mod = mod;
        n = coe.size();
        m = a[0].size();
        pow = new Power(mod);
        remainder = new IntList(coe.size());
        p = new IntList(coe.size() + 1);
        for (int i = n - 1; i >= 0; i--) {
            p.add(mod.valueOf(-coe.get(i)));
        }
        p.add(1);
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

    public IntList solve(int k) {
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
    public MatrixLinearRecurrenceSolver(ModMatrix mat, IntList vec, Modular mod) {
        GravityModLagrangeInterpolation.Polynomial p = mat.getCharacteristicPolynomial(new Power(mod));
        IntList coe = new IntList(p.getRank() + 1);
        for (int i = p.getRank(); i >= 0; i--) {
            coe.add(p.getCoefficient(i));
        }

        ModMatrix transpose = mat.getTransposeMatrix();
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
