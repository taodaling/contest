package on2020_04.on2020_04_19_TopCoder_SRM__763.ProductAndProduct;



import template.math.Combination;
import template.math.Modular;
import template.polynomial.NumberTheoryTransform;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;

public class ProductAndProduct {
   // Debug debug = new Debug(false);

    public int findExpectedProduct(int N, int[] B, int M, int seed) {
        int[] A = new int[N];
        A[0] = seed;
        for (int i = 1; i <= N - 1; i++) {
            A[i] = (int) ((A[i - 1] * 1103515245L + 12345) % 2147483648L);
        }
        int[] C = Arrays.copyOf(B, N);
        for (int i = B.length; i <= N - 1; i++) {
            C[i] = A[i] % 998244353;
        }

        IntegerList[] ps = new IntegerList[N];
        for (int i = 0; i < N; i++) {
            ps[i] = new IntegerList(2);
            ps[i].add(1);
            ps[i].add(C[i]);
        }
        Modular mod = new Modular(998244353);
        Combination comb = new Combination(N + M, mod);
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);

        IntegerList f = new IntegerList();
        ntt.dacMul(ps, f);

        //debug.debug("f", f);
        f.expandWith(0, N + 1);
        int ans = 0;
        for (int i = 0; i <= N; i++) {
            int w1 = f.get(N - i);
            int w2 = comb.combination(N + M - 1, N + i - 1);
            ans = mod.plus(ans, mod.mul(w1, w2));
        }
        ans = mod.mul(ans, comb.invCombination(N + M - 1, M));

        return ans;
    }
}
