package template.math;

import template.datastructure.BitSet;
import template.polynomial.IntPoly;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.PrimitiveBuffers;


public class ModVectorLinearRecurrenceSolver {
    int mod;
    int[][] a;
    int[] p;
    Power pow;
    int n;
    int m;
    IntPoly poly;

    private void init(int[][] a, IntegerArrayList coe, int mod, IntPoly poly) {
        this.poly = poly;
        this.a = a;
        this.mod = mod;
        n = a[0].length;
        m = coe.size();
        pow = new Power(mod);
        p = coe.toArray();
    }

    private int[] solve(int[] remainder) {
        int[] ans = new int[n];
        for (int i = 0; i < m && i < remainder.length; i++) {
            long r = remainder[i];
            for (int j = 0; j < n; j++) {
                ans[j] = (int) ((ans[j] + r * a[i][j]) % mod);
            }
        }
        return ans;
    }

    /**
     * O(n\log_2n log_2k) get A^k vec
     */
    public int[] solve(long k) {
        int[] remainder = poly.module(k, p);
        int[] ans = solve(remainder);
        PrimitiveBuffers.release(remainder);
        return ans;
    }

    /**
     * O(n\log_2n log_2k) get A^k vec
     */
    public int[] solve(BitSet k) {
        int[] remainder = poly.module(k, p);
        int[] ans = solve(remainder);
        PrimitiveBuffers.release(remainder);
        return ans;
    }

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
    public ModVectorLinearRecurrenceSolver(ModSparseMatrix mat, int[] vec, int mod, IntPoly poly) {
        IntegerArrayList coe = mat.getMinimalPolynomialByRandom(mod);
        int m = coe.size();
        int n = vec.length;
        int[][] lists = new int[m][];
        lists[0] = vec;
        for (int i = 1; i < m; i++) {
            lists[i] = new int[n];
            mat.rightMul(lists[i - 1], lists[i], mod);
        }
        init(lists, coe, mod, poly);
    }
}
