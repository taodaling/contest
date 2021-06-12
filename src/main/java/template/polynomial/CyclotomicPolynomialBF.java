package template.polynomial;

import template.utils.PrimitiveBuffers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CyclotomicPolynomialBF {
    private static Map<Integer, int[]> cache = new HashMap<>();
    static {
        cache.put(1, new int[]{-1, 1});
    }
    /**
     * O(\sum_{d|n} d^2)
     */
    public static int[] get(int n) {
        int[] ans = cache.get(n);
        if (ans == null) {
            int[] a = PrimitiveBuffers.allocIntPow2(cache.get(1));
            for (int i = 2; i * i <= n; i++) {
                if (n % i != 0) {
                    continue;
                }
                a = PrimitiveBuffers.replace(Polynomials.mul(a, get(i)), a);
                if (i * i != n) {
                    a = PrimitiveBuffers.replace(Polynomials.mul(a, get(n / i)), a);
                }
            }
            int[] top = PrimitiveBuffers.allocIntPow2(n + 1);
            top[n] = 1;
            top[0] = -1;
            int[][] res = Polynomials.divAndRemainder(top, a);
            ans = Arrays.copyOf(res[0], Polynomials.rankOf(res[0]) + 1);
            assert ans.length <= n + 1;
            PrimitiveBuffers.release(a, top, res[0], res[1]);
            cache.put(n, ans);
        }

        return ans;
    }
}
