package template.polynomial;

import template.math.DigitUtils;
import template.math.PrimitiveRoot;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

public class IntPolyNTT extends IntPoly {
    protected int g;
    static final int NTT_THRESHOLD = 50;
    static final int NTT_DIVIDE_THRESHOLD = 200;

    public IntPolyNTT(int mod) {
        super(mod);
        g = PrimitiveRoot.findAnyRoot(mod);
    }


    @Override
    public int[] convolution(int[] a, int[] b) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        if (Math.min(rA, rB) <= NTT_THRESHOLD) {
            return mulBF(a, b);
        }
        if (a != b) {
            a = PrimitiveBuffers.allocIntPow2(a, rA + rB + 1);
            b = PrimitiveBuffers.allocIntPow2(b, rA + rB + 1);
            NumberTheoryTransform.ntt(a, false, mod, g, power);
            NumberTheoryTransform.ntt(b, false, mod, g, power);
            int[] c = PrimitiveBuffers.replace(dotMul(a, b), a, b);
            NumberTheoryTransform.ntt(c, true, mod, g, power);
            return c;
        } else {
            a = PrimitiveBuffers.allocIntPow2(a, rA + rB + 1);
            NumberTheoryTransform.ntt(a, false, mod, g, power);
            int[] c = PrimitiveBuffers.replace(dotMul(a, a), a);
            NumberTheoryTransform.ntt(c, true, mod, g, power);
            return c;
        }
    }


    /**
     * <p>
     * return polynomial g while p * g = 1 (mod x^(2^m)).
     * </p>
     * <p>
     * You are supposed to guarantee the lengths of all arrays are greater than or equal to 2^{m + 1}.
     * </p>
     */
    @Override
    protected int[] inverse0(int[] p, int m) {
        if (m == 0) {
            int[] ans = PrimitiveBuffers.allocIntPow2(2);
            ans[0] = power.inverse(p[0]);
            return ans;
        }
        int[] ans = inverse0(p, m - 1);
        int n = 1 << (m + 1);
        ans = PrimitiveBuffers.resize(ans, n);
        int[] prefix = PrimitiveBuffers.allocIntPow2(p, 1 << m, n);
        NumberTheoryTransform.ntt(prefix, false, mod, g, power);
        NumberTheoryTransform.ntt(ans, false, mod, g, power);
        for (int i = 0; i < n; i++) {
            ans[i] = DigitUtils.mod(ans[i] * (2 - (long) prefix[i] * ans[i] % mod), mod);
        }
        NumberTheoryTransform.ntt(ans, true, mod, g, power);
        Arrays.fill(ans, 1 << m, n, 0);
        PrimitiveBuffers.release(prefix);
        return ans;
    }


    /**
     * <p>
     * calc a = b * c + remainder, m >= 1 + ceil(log_2 max(|a|, |b|))
     * </p>
     * <p>
     * ret[0] for c and ret[1] for remainder
     * </p>
     */
    @Override
    public int[][] divideAndRemainder(int[] a, int[] b) {
        int rankA = rankOf(a);
        int rankB = rankOf(b);
        int rankC = rankA - rankB;

        if (rankC < 0) {
            int[][] ans = new int[2][];
            ans[0] = PrimitiveBuffers.allocIntPow2(1);
            ans[1] = PrimitiveBuffers.allocIntPow2(a, rankA + 1);
            return ans;
        }

        if (rankB < NTT_DIVIDE_THRESHOLD || rankC < NTT_DIVIDE_THRESHOLD) {
            return super.divideAndRemainder(a, b);
        }

        int[] quotient = divide(a, b);
        int[] prod = convolution(b, quotient);
        int[] remainder = PrimitiveBuffers.replace(subtract(a, prod), prod);
        remainder = PrimitiveBuffers.replace(module(remainder, rankB), remainder);
        remainder = Polynomials.normalizeAndReplace(remainder);
        return new int[][]{quotient, remainder};
    }
}
