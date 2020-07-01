package template.math;

import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.BitSet;

public class ModVectorLinearRecurrenceSolver {
    Modular mod;
    int[][] a;
    IntegerArrayList p;
    IntegerArrayList remainder;
    Power pow;
    int n;
    int m;

    private void init(int[][] a, IntegerArrayList coe, Modular mod) {
        this.a = a;
        this.mod = mod;
        n = a[0].length;
        m = coe.size();
        pow = new Power(mod);
        remainder = new IntegerArrayList(coe.size());
        p = coe;
    }

    private int[] solve() {
        int[] ans = new int[n];
        remainder.expandWith(0, m);
        for (int i = 0; i < m; i++) {
            int r = remainder.get(i);
            for (int j = 0; j < n; j++) {
                ans[j] = mod.plus(ans[j], mod.mul(r, a[i][j]));
            }
        }
        return ans;
    }

    /**
     * O(n^2log_2k)
     */
    public int[] solve(long k) {
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }

    /**
     * O(n^2log_2k)
     */
    public int[] solve(BitSet k) {
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }
//
//    /**
//     * Auto detect linear recurrence from given matrix and vec.
//     * <br>
//     * O(n^3)
//     */
//    public ModVectorLinearRecurrenceSolver(ModMatrix mat, IntegerList vec, Modular mod) {
//        ModGravityLagrangeInterpolation.Polynomial p = mat.getCharacteristicPolynomial(new Power(mod));
//        IntegerList coe = p.toIntegerList();
//        ModMatrix transpose = ModMatrix.transpose(mat);
//        int m = coe.size();
//        int n = vec.size();
//        int[][] lists = new int[m][n];
//        ModMatrix vector = new ModMatrix(1, n);
//        for (int i = 0; i < vec.size(); i++) {
//            vector.set(0, i, vec.get(i));
//        }
//        lists[0] = vec;
//        for (int i = 1; i < m; i++) {
//            vector = ModMatrix.mul(vector, transpose, mod);
//            lists[i] = new IntegerList(vec.size());
//            for (int j = 0; j < vec.size(); j++) {
//                lists[i].add(vector.get(0, j));
//            }
//        }
//        init(lists, coe, mod);
//    }

    /**
     * Auto detect linear recurrence from given matrix and vec.
     * <br>
     * O(n(m+n))
     */
    public ModVectorLinearRecurrenceSolver(ModSparseMatrix mat, int[] vec, Modular mod) {
        IntegerArrayList coe = mat.getMinimalPolynomialByRandom(mod);
        int m = coe.size();
        int n = vec.length;
        int[][] lists = new int[m][];
        lists[0] = vec;
        for (int i = 1; i < m; i++) {
            lists[i] = new int[n];
            mat.rightMul(lists[i - 1], lists[i], mod);
        }
        init(lists, coe, mod);
    }
}
