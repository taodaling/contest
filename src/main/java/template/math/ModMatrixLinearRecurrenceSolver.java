package template.math;

import template.polynomial.GravityModLagrangeInterpolation;
import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerList;

import java.util.BitSet;

public class ModMatrixLinearRecurrenceSolver {
    Modular mod;
    IntegerList[] a;
    IntegerList p;
    IntegerList remainder;
    Power pow;
    int n;
    int m;

    private void init(IntegerList[] a, IntegerList coe, Modular mod) {
        this.a = a;
        this.mod = mod;
        n = coe.size();
        m = a[0].size();
        pow = new Power(mod);
        remainder = new IntegerList(coe.size());
        p = coe;
    }

    private IntegerList solve() {
        IntegerList ans = new IntegerList(m);
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

    /**
     * O(n^2log_2k)
     */
    public IntegerList solve(long k) {
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }

    /**
     * O(n^2log_2k)
     */
    public IntegerList solve(BitSet k) {
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }

    /**
     * Auto detect linear recurrence from given matrix and vec.
     * <br>
     * O(n^3)
     */
    public ModMatrixLinearRecurrenceSolver(ModMatrix mat, IntegerList vec, Modular mod) {
        GravityModLagrangeInterpolation.Polynomial p = mat.getCharacteristicPolynomial(new Power(mod));
        IntegerList coe = p.toIntegerList();
        ModMatrix transpose = ModMatrix.transposition(mat);
        int m = coe.size();
        IntegerList[] lists = new IntegerList[m];
        ModMatrix vector = new ModMatrix(1, vec.size());
        for (int i = 0; i < vec.size(); i++) {
            vector.set(0, i, vec.get(i));
        }
        lists[0] = vec;
        for (int i = 1; i < m; i++) {
            vector = ModMatrix.mul(vector, transpose, mod);
            lists[i] = new IntegerList(vec.size());
            for (int j = 0; j < vec.size(); j++) {
                lists[i].add(vector.get(0, j));
            }
        }
        init(lists, coe, mod);
    }
}
