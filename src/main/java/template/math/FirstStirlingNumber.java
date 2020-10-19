package template.math;

import template.binary.Log2;
import template.polynomial.IntPoly;
import template.polynomial.NumberTheoryTransform;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

import java.util.Arrays;

/**
 * For all i, prepare c(n, i) in O(nlog2n)
 */
public class FirstStirlingNumber {
    private Factorial factorial;
    private int[] stirling;
    private int mod;
    private int n;

    /**
     * stirling number without sign
     */
    public int getNoSign(int i) {
        return stirling[i];
    }

    public int getSign(int i) {
        int ans = getNoSign(i);
        if (((n - i) & 1) == 1) {
            ans = DigitUtils.negate(ans, mod);
        }
        return ans;
    }

    public FirstStirlingNumber(IntPoly poly, Factorial factorial, int n) {
        this.mod = factorial.getMod();
        this.factorial = factorial;
        stirling = getStirling(n, poly);
        this.n = n;
    }

    private int[] getStirling(int n, IntPoly poly) {
        int[] ans = calcStirling(n, poly);
        ans = PrimitiveBuffers.replace(Arrays.copyOf(ans, n + 1), ans);
        return ans;
    }

    private int[] calcStirling(int n, IntPoly poly) {
        if (n == 0) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = DigitUtils.mod(1, mod);
            return ans;
        }
        if (n % 2 == 1) {
            int[] ans = calcStirling(n - 1, poly);
            ans = PrimitiveBuffers.resize(ans, n + 1);
            for (int i = n; i >= 0; i--) {
                ans[i] = (int) ((long) (n - 1) * ans[i] % mod);
                if (i >= 1) {
                    ans[i] = DigitUtils.modplus(ans[i], ans[i - 1], mod);
                }
            }
            return ans;
        }
        int half = n >> 1;
        int[] ans = calcStirling(half, poly);
        int[] A = PrimitiveBuffers.allocIntPow2(half + 1);
        int[] B = PrimitiveBuffers.allocIntPow2(half + 1);
        long ni = 1;
        for (int i = 0; i <= half; i++) {
            A[i] = (int) ((long) ans[i] * factorial.fact(i) % mod);
            B[i] = (int) (ni * factorial.invFact(i) % mod);
            ni = (int) (ni * half % mod);
        }
        A = PrimitiveBuffers.replace(poly.deltaConvolution(A, B), A, B);
        for (int i = 0; i <= half && i < A.length; i++) {
            A[i] = (int) ((long) A[i] * factorial.invFact(i) % mod);
        }
        return PrimitiveBuffers.replace(poly.convolution(A, ans), A, ans);
    }

}
