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


    @Override
    protected int[] inverse0(int[] p, int m) {
        if (m == 1) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = power.inverse(p[0]);
            return ans;
        }
        int prevLen = (m + 1) / 2;
        int[] ans = inverse0(p, prevLen);
        int n = (prevLen - 1) * 2 + m - 1 + 1;
        ans = PrimitiveBuffers.resize(ans, n);
        int[] prefix = PrimitiveBuffers.allocIntPow2(p, m, n);
        NumberTheoryTransform.ntt(prefix, false, mod, g, power);
        NumberTheoryTransform.ntt(ans, false, mod, g, power);
        for (int i = 0; i < ans.length; i++) {
            ans[i] = DigitUtils.mod(ans[i] * (2L - ((long)prefix[i] * ans[i]) % mod), mod);
        }
        NumberTheoryTransform.ntt(ans, true, mod, g, power);
        PrimitiveBuffers.release(prefix);
        return PrimitiveBuffers.replace(module(ans, m), ans);
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

    @Override
    public int[] module(long k, int[] p) {
        int rankOfP = rankOf(p);
        if (rankOfP == 0) {
            return PrimitiveBuffers.allocIntPow2(1);
        }
        FastDivision fd = new FastDivision(p, (rankOfP - 1) * 2 + 1);
        int[] ans = module(k, fd);
        fd.release();
        return Polynomials.normalizeAndReplace(ans);
    }


    protected int[] module(long k, FastDivision fd) {
        if (k < fd.rb) {
            int[] ans = PrimitiveBuffers.allocIntPow2((int) k + 1);
            ans[(int) k] = DigitUtils.mod(1, mod);
            return ans;
        }
        int[] ans = module(k / 2, fd);
        ans = PrimitiveBuffers.replace(pow2(ans), ans);
        if ((k & 1) == 1) {
            ans = PrimitiveBuffers.replace(rightShift(ans, 1), ans);
        }
        int[][] qd = fd.divideAndRemainder(ans);
        return PrimitiveBuffers.replace(qd[1], qd[0], ans);
    }


    private int hash(int a, int[] preProd) {
        int ans = 0;
        for (int i = 0; i < preProd.length - 1; i++) {
            ans += a / preProd[i];
        }
        return ans;
    }

    /**
     * O(kn\log_2n) where k = |base|
     */
    public int[] multivariateConvolution(int[] f, int[] g, int[] base) {
        base = Arrays.stream(base).filter(x -> x > 1).toArray();
        int k = base.length;
        if (k == 0) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = (int) ((long) f[0] * g[0] % mod);
            return ans;
        }
        int[] preProd = new int[k];
        for (int i = 0; i < k; i++) {
            preProd[i] = base[i] * (i == 0 ? 1 : preProd[i - 1]);
        }
        int N = preProd[k - 1];
        int[] hash = PrimitiveBuffers.allocIntPow2(N * 2 - 1);
        for (int i = 0; i < hash.length; i++) {
            hash[i] = hash(i, preProd) % k;
        }
        int[] F = PrimitiveBuffers.allocIntPow2(N * 2 - 1);
        int[][] G = PrimitiveBuffers.allocIntPow2Array(N * 2 - 1, k);
        int[][] FG = PrimitiveBuffers.allocIntPow2Array(N * 2 - 1, k);
        for (int i = 0; i < N && i < g.length; i++) {
            int dest = hash[i];
            G[dest][i] = g[i];
        }
        for (int i = 0; i < k; i++) {
            NumberTheoryTransform.ntt(G[i], false, mod, this.g, power);
        }
        for (int i = 0; i < k; i++) {
            Arrays.fill(F, 0);
            for (int j = 0; j < f.length && j < hash.length; j++) {
                if (hash[j] == i) {
                    F[j] = f[j];
                }
            }
            NumberTheoryTransform.ntt(F, false, mod, this.g, power);
            for (int j = 0; j < k; j++) {
                int dest = (i + j) % k;
                DotMul.dotMulPlus(F, G[j], FG[dest], 0, F.length - 1, mod);
            }
        }
        PrimitiveBuffers.release(G);
        PrimitiveBuffers.release(F);
        int[] fg = PrimitiveBuffers.allocIntPow2(N);
        for (int i = 0; i < k; i++) {
            NumberTheoryTransform.ntt(FG[i], true, mod, this.g, power);
        }
        for (int i = 0; i < N; i++) {
            fg[i] = FG[hash[i]][i];
        }
        PrimitiveBuffers.release(FG);
        PrimitiveBuffers.release(hash);
        return fg;
    }
}
