package template.polynomial;

import template.datastructure.BitSet;
import template.math.*;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

public class IntPoly {


    protected int mod;
    protected Power power;
    QuadraticResidue qr;

    public IntPoly(int mod) {
        this.mod = mod;
        this.power = new Power(mod);
        qr = new QuadraticResidue(mod);
    }

    public int[] convolution(int[] a, int[] b) {
        return mulBF(a, b);
    }

    public static void main(String[] args) {
        IntPoly poly = new IntPoly(998244353);
        poly.divide(new int[]{1, 2, 3, 4}, new int[]{5, 6, 7, 0});
    }

    /**
     * <p>return a / b</p>
     */
    public int[] divide(int[] a, int[] b) {
        int rankA = rankOf(a);
        int rankB = rankOf(b);
        int rankC = rankA - rankB;
        if (rankC < 0) {
            return PrimitiveBuffers.allocIntPow2(1);
        }

        //mod x^{rankC+1}
        int[] aa = PrimitiveBuffers.allocIntPow2(rankC + rankC + 1);
        int[] bb = PrimitiveBuffers.allocIntPow2(rankC + rankC + 1);
        for (int i = 0; i <= rankA && i < aa.length; i++) {
            aa[i] = a[rankA - i];
        }
        for (int i = 0; i <= rankB && i < bb.length; i++) {
            bb[i] = b[rankB - i];
        }

        int[] cc = PrimitiveBuffers.replace(inverse(bb, rankC + 1), bb);
        //cc = PrimitiveBuffers.replace(PrimitiveBuffers.allocIntPow2(cc, rankC + rankC + 1), cc);
        int[] dm = PrimitiveBuffers.replace(convolution(aa, cc), aa, cc);

        for (int i = rankC + 1; i < dm.length; i++) {
            dm[i] = 0;
        }

        SequenceUtils.reverse(dm, 0, rankC);
        return Polynomials.normalizeAndReplace(dm);
    }


    /**
     * return list % x^n
     */
    public int[] module(int[] p, int n) {
        int rp = rankOf(p);
        int[] ans = PrimitiveBuffers.allocIntPow2(Math.min(rp + 1, n));
        for (int i = 0; i < n && i <= rp; i++) {
            ans[i] = p[i];
        }
        return ans;
    }


    public int[] dotMul(int[] a, int[] b) {
        assert a.length == b.length;
        int[] c = PrimitiveBuffers.allocIntPow2(a.length);
        for (int i = 0; i < a.length; i++) {
            c[i] = (int) (((long) a[i] * b[i]) % mod);
        }
        return c;
    }


    public int[] differential(int[] p) {
        int rp = rankOf(p);
        int[] ans = PrimitiveBuffers.allocIntPow2(rp);
        for (int i = 1; i <= rp; i++) {
            ans[i - 1] = (int) (((long) p[i] * i) % mod);
        }
        return ans;
    }

    public int[] integral(int[] p) {
        int rp = rankOf(p);
        int[] ans = PrimitiveBuffers.allocIntPow2(rp + 2);
        int[] buf = PrimitiveBuffers.allocIntPow2(rp + 2);
        InverseNumber inv = new ModPrimeInverseNumber(buf, buf.length - 1, mod);
        for (int i = 0; i <= rp; i++) {
            ans[i + 1] = (int) (((long) inv.inverse(i + 1) * p[i]) % mod);
        }
        PrimitiveBuffers.release(buf);
        return ans;
    }


    /**
     * calculate lists[0] * lists[1] * ... * lists[lists.length - 1]
     * by dividing and conquer technology.
     * <br>
     * The total time complexity is O(m \log m \log n) while m = lists.length and
     * n = lists[0].length + lists[1].length + ... + lists[m - 1].length.
     */
    public int[] dacMul(int[][] lists) {
        int[] prod = dacMul(lists, 0, lists.length - 1);
        return Polynomials.normalizeAndReplace(prod);
    }

    public int rankOf(int[] p) {
        int r = p.length - 1;
        while (r >= 0 && p[r] == 0) {
            r--;
        }
        return Math.max(0, r);
    }


    public int[] rightShift(int[] p, int step) {
        int r = rankOf(p);
        int[] ans = PrimitiveBuffers.allocIntPow2(r + step + 1);
        for (int i = 0; i <= r; i++) {
            ans[i + step] = p[i];
        }
        return ans;
    }

    /**
     * right shift and module x^n
     *
     * @param p
     * @param step
     * @param n
     * @return
     */
    public int[] rightShift(int[] p, long step, int n) {
        if (step >= n) {
            return PrimitiveBuffers.allocIntPow2(1);
        }
        int r = rankOf(p);
        int[] ans = PrimitiveBuffers.allocIntPow2((int) Math.min(r + step + 1, n));
        int shift = (int) step;
        for (int i = 0; i <= r && i + shift < n; i++) {
            ans[i + shift] = p[i];
        }
        return ans;
    }

    public int[] leftShift(int[] p, int step) {
        int r = rankOf(p);
        if (r - step < 0) {
            return PrimitiveBuffers.allocIntPow2(1);
        }
        int[] ans = PrimitiveBuffers.allocIntPow2(r - step + 1);
        for (int i = 0; i + step <= r; i++) {
            ans[i] = p[i + step];
        }
        return ans;
    }

    public int[] mulBF(int[] a, int[] b) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        if (rA > rB) {
            {
                int tmp = rA;
                rA = rB;
                rB = tmp;
            }
            {
                int[] tmp = a;
                a = b;
                b = tmp;
            }
        }
        int[] c = PrimitiveBuffers.allocIntPow2(rA + rB + 1);
        for (int i = 0; i <= rA; i++) {
            for (int j = 0; j <= rB; j++) {
                c[i + j] = (int) ((c[i + j] + (long) a[i] * b[j]) % mod);
            }
        }
        return c;
    }


    public int[] plus(int[] a, int[] b) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        int[] ans = PrimitiveBuffers.allocIntPow2(Math.max(rA, rB) + 1);

        for (int i = 0; i <= rA; i++) {
            ans[i] = a[i];
        }
        for (int i = 0; i <= rB; i++) {
            ans[i] = DigitUtils.modplus(ans[i], b[i], mod);
        }
        return ans;
    }

    public int[] subtract(int[] a, int[] b) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        int[] ans = PrimitiveBuffers.allocIntPow2(Math.max(rA, rB) + 1);

        for (int i = 0; i <= rA; i++) {
            ans[i] = a[i];
        }
        for (int i = 0; i <= rB; i++) {
            ans[i] = DigitUtils.modsub(ans[i], b[i], mod);
        }
        return ans;
    }

    public void mul(int[] a, int k) {
        for (int i = a.length - 1; i >= 0; i--) {
            a[i] = (int) (((long) a[i] * k) % mod);
        }
    }


    public int[] modmul(int[] a, int[] b, int n) {
        int[] c = convolution(a, b);
        return PrimitiveBuffers.replace(module(c, n), c);
    }

    /**
     * <p>
     * make ans = ln a mod x^n
     * </p>
     * <p>
     * precondition: a[0] = 1
     * </p>
     * <p>
     * it run in O(n\log_2 n)
     * </p>
     */
    public int[] ln(int[] a, int n) {
        int[] diff = differential(a);
        a = module(a, n);
        diff = PrimitiveBuffers.replace(module(diff, n), diff);
        int[] inv = PrimitiveBuffers.replace(inverse(a, n), a);
        int[] prod = PrimitiveBuffers.replace(modmul(diff, inv, n), diff, inv);
        int[] ans = PrimitiveBuffers.replace(integral(prod), prod);
        ans = PrimitiveBuffers.replace(module(ans, n), ans);
        return ans;
    }


    /**
     * <p> ans = exp(a) mod x^n </p>
     * <p> it run in O(n\log_2 n) </p>
     * <p> precondition: a[0] = 0 </p>
     */
    public int[] exp(int[] a, int n) {
        if (n == 0) {
            return PrimitiveBuffers.allocIntPow2(1);
        }
        a = module(a, n);
        return PrimitiveBuffers.replace(exp0(a, n), a);
    }

    protected int[] exp0(int[] a, int n) {
        if (n == 1) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = 1;
            return ans;
        }
        int[] ans = exp0(a, (n + 1) / 2);
        int[] ln = ln(ans, n);
        ln = PrimitiveBuffers.resize(ln, n);
        for (int i = 0; i < n; i++) {
            int ai = i < a.length ? a[i] : 0;
            ln[i] = DigitUtils.modsub(ai, ln[i], mod);
        }
        ln[0] = DigitUtils.modplus(ln[0], 1, mod);
        return PrimitiveBuffers.replace(modmul(ans, ln, n), ans, ln);
    }

    public int[] pow2(int[] a) {
        return convolution(a, a);
    }

    /**
     * 多项式多点插值 O(p\log p+m(lg m)^2)
     */
    public void multiApply(int[] p, int[] x, int[] y, int n) {
        int l = 0;
        int r = n - 1;
        PolynomialTree tree = build(x, l, r);
        multiApply(p, x, y, l, r, tree);
        releaseTree(tree, l, r);
    }


    public int apply(int[] p, int x) {
        long y = 0;
        for (int i = p.length - 1; i >= 0; i--) {
            y = (y * x + p[i]) % mod;
        }
        return (int) y;
    }


    private static final int MULTI_APPLY_THRESHOLD = 128;

    protected void multiApply(int[] p, int[] x, int[] y, int l, int r, PolynomialTree tree) {
        if (r - l + 1 <= MULTI_APPLY_THRESHOLD) {
            for (int i = l; i <= r; i++) {
                y[i] = apply(p, x[i]);
            }
            return;
        }
        int[][] qd = divideAndRemainder(p, tree.p);
        int[] remainder = qd[1];
        PrimitiveBuffers.release(qd[0]);

        int m = DigitUtils.floorAverage(l, r);
        multiApply(remainder, x, y, l, m, tree.left);
        multiApply(remainder, x, y, m + 1, r, tree.right);
        PrimitiveBuffers.release(remainder);
    }

    protected PolynomialTree build(int[] x, int l, int r) {
        PolynomialTree tree = new PolynomialTree();
        if (l == r) {
            tree.p = PrimitiveBuffers.allocIntPow2(2);
            tree.p[0] = DigitUtils.negate(x[l], mod);
            tree.p[1] = DigitUtils.mod(1, mod);
        } else {
            int m = DigitUtils.floorAverage(l, r);
            tree.left = build(x, l, m);
            tree.right = build(x, m + 1, r);
            tree.p = convolution(tree.left.p, tree.right.p);
        }
        return tree;
    }

    protected void releaseTree(PolynomialTree tree, int l, int r) {
        PrimitiveBuffers.release(tree.p);
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        releaseTree(tree.left, l, m);
        releaseTree(tree.right, m + 1, r);
    }

    /**
     * a = b * c + remainder, the first number of b should be relative prime with mod
     */
    public int[][] divideAndRemainder(int[] a, int[] b) {
        int rA = rankOf(a);
        int rB = rankOf(b);
        int rC = rA - rB;
        if (rC < 0) {
            int[][] ans = new int[2][];
            ans[0] = PrimitiveBuffers.allocIntPow2(1);
            ans[1] = PrimitiveBuffers.allocIntPow2(a, rA + 1);
            return ans;
        }

        int[] quotient = PrimitiveBuffers.allocIntPow2(rC + 1);
        int[] r = PrimitiveBuffers.allocIntPow2(a, rA + 1);
        long inv = power.inverse(b[rB]);
        for (int i = rA, j = rC; i >= rB; i--, j--) {
            if (r[i] == 0) {
                continue;
            }
            int factor = DigitUtils.mod(-r[i] * inv, mod);
            quotient[j] = DigitUtils.negate(factor, mod);
            for (int k = rB; k >= 0; k--) {
                r[k + j] = (int) ((r[k + j] + (long) factor * b[k]) % mod);
            }
        }

        return new int[][]{quotient, Polynomials.normalizeAndReplace(r)};
    }


    protected static class PolynomialTree {
        public int[] p;
        public PolynomialTree left;
        public PolynomialTree right;
    }

    protected int[] dacMul(int[][] lists, int l, int r) {
        if (l == r) {
            return PrimitiveBuffers.allocIntPow2(lists[l]);
        }
        int m = DigitUtils.floorAverage(l, r);
        int[] a = dacMul(lists, l, m);
        int[] b = dacMul(lists, m + 1, r);
        return PrimitiveBuffers.replace(convolution(a, b), a, b);
    }

    /**
     * return p[i]^-1 % x^m
     *
     * @param p
     * @param m
     * @return
     */
    public int[] inverse(int[] p, int m) {
        if (m == 0) {
            return PrimitiveBuffers.allocIntPow2(1);
        }
        int[] ans = inverse0(p, m);
        return ans;
    }

    /**
     * return polynomial g while p * g = 1 (mod x^m).
     */
    protected int[] inverse0(int[] p, int m) {
        if (m == 1) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = power.inverse(p[0]);
            return ans;
        }
        int prevMod = (m + 1) / 2;
        int[] C = inverse0(p, prevMod);
        C = PrimitiveBuffers.resize(C, m);
        int[] A = PrimitiveBuffers.allocIntPow2(p, m);

        //B = C(2-AC)
        int[] AC = PrimitiveBuffers.replace(modmul(A, C, m), A);
        for (int i = 0; i < AC.length; i++) {
            AC[i] = DigitUtils.negate(AC[i], mod);
        }
        AC[0] = DigitUtils.modplus(AC[0], 2, mod);
        int[] B = PrimitiveBuffers.replace(modmul(C, AC, m), AC, C);
        return B;
    }

    /**
     * Get remainder = x^k % p
     */
    public int[] module(long k, int[] p) {
        int rankOfP = rankOf(p);
        if (rankOfP == 0) {
            return PrimitiveBuffers.allocIntPow2(1);
        }

        int[] ans = module(k, p, rankOfP);
        return Polynomials.normalizeAndReplace(ans);
    }

    protected int[] module(long k, int[] b, int rb) {
        if (k < rb) {
            int[] ans = PrimitiveBuffers.allocIntPow2((int) k + 1);
            ans[(int) k] = DigitUtils.mod(1, mod);
            return ans;
        }
        int[] ans = module(k / 2, b, rb);
        ans = PrimitiveBuffers.replace(pow2(ans), ans);
        if ((k & 1) == 1) {
            ans = PrimitiveBuffers.replace(rightShift(ans, 1), ans);
        }
        int[][] qd = divideAndRemainder(ans, b);
        return PrimitiveBuffers.replace(qd[1], qd[0], ans);
    }

    /**
     * Get remainder = x^k % p
     */
    public int[] module(BitSet k, int[] p) {
        int rankOfP = rankOf(p);
        if (rankOfP == 0) {
            return PrimitiveBuffers.allocIntPow2(1);
        }

        int[] ans = module(k, 0, p, rankOfP);
        return Polynomials.normalizeAndReplace(ans);
    }

    protected int[] module(BitSet k, int i, int[] b, int rb) {
        if (i == k.capacity()) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = DigitUtils.mod(1, mod);
            return ans;
        }
        int[] ans = module(k, i + 1, b, rb);
        ans = PrimitiveBuffers.replace(pow2(ans), ans);
        if (k.get(i)) {
            ans = PrimitiveBuffers.replace(rightShift(ans, 1), ans);
        }
        int[][] qd = divideAndRemainder(ans, b);
        return PrimitiveBuffers.replace(qd[1], qd[0]);
    }


    /**
     * <p>
     * ans = p ^ k mod x^n
     * </p>
     * <p>precondition: p[0] = 1</p>
     */
    public int[] modpowByLnExp(int[] p, long k, int n) {
        int[] ln = ln(p, n);
        mul(ln, (int) (k % mod));
        return PrimitiveBuffers.replace(exp(ln, n), ln);
    }

    public int[] modpow(int[] p, long k, int n) {
        return modpow(p, k, k, k, n);
    }

    public int[] modpow(int[] p, long k, long kModPhi, long rawK, int n) {
        if (k == 0) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = 1;
            return ans;
        }
        if (p[0] == 1) {
            return modpowByLnExp(p, k, n);
        }
        if (k == 1) {
            return module(p, n);
        }
        int t = 0;
        while (t < p.length && p[t] == 0) {
            t++;
        }
        long move = DigitUtils.mul(rawK, t, Long.MAX_VALUE);
        if (t == p.length || move >= n) {
            return PrimitiveBuffers.allocIntPow2(1);
        }
        if (GCDs.gcd(p[t], mod) != 1) {
            return modpowBF(p, k, n);
        }
        p = leftShift(p, t);
        int p0 = p[0];
        long pow = DigitUtils.modPow(p0, kModPhi, mod);
        long inverse = (int) DigitUtils.modInverse(p[0], mod);
        if (inverse != 1) {
            for (int i = 0; i < p.length; i++) {
                p[i] = (int) (((long) p[i] * inverse) % mod);
            }
        }
        int[] ln = ln(p, n);
        mul(ln, (int) (k % mod));
        int[] ans = PrimitiveBuffers.replace(exp(ln, n), ln);
        PrimitiveBuffers.release(p);
        if (pow != 1) {
            for (int i = 0; i < ans.length; i++) {
                ans[i] = (int) (((long) ans[i] * pow) % mod);
            }
        }
        ans = PrimitiveBuffers.replace(rightShift(ans, move, n), ans);
        return ans;
    }

    public int[] modpowBF(int[] p, long k, int n) {
        if (k == 0) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = 1;
            return ans;
        }
        int[] ans = modpowBF(p, k >> 1, n);
        ans = PrimitiveBuffers.replace(modmul(ans, ans, n), ans);
        if ((k & 1) == 1) {
            ans = PrimitiveBuffers.replace(modmul(ans, p, n), ans);
        }
        return ans;
    }

    /**
     * c[i]=\sum_{j} a[i+j]*b[j]
     */
    public int[] deltaConvolution(int[] a, int[] b) {
        assert a != b;
        int rA = rankOf(a);
        SequenceUtils.reverse(a, 0, rA);
        int[] ans = convolution(a, b);
        SequenceUtils.reverse(a, 0, rA);
        for (int i = rA + 1; i < ans.length; i++) {
            ans[i] = 0;
        }
        SequenceUtils.reverse(ans, 0, rA);
        return Polynomials.normalizeAndReplace(ans);
    }


    private long choose2(long n) {
        return n * (n - 1) >> 1;
    }

    /**
     * <p>
     * 给定多项式p，要求计算p(c^0),p(c^1),\ldots,p(c^m).
     * </p>
     * <p>
     * O((n+m)\log_2 (n+m)
     * </p>
     *
     * @return
     */
    public void chripZ(int[] p, int c, int m, FastPow2 cpc, int[] ans) {
        int n = rankOf(p);
        int[] A = PrimitiveBuffers.allocIntPow2(n + m + 1);
        for (int i = 0; i < n + m + 1; i++) {
            A[i] = cpc.pow(choose2(i));
        }
        int[] B = PrimitiveBuffers.allocIntPow2(n + 1);
        for (int i = 0; i <= n; i++) {
            B[i] = (int) ((long) p[i] * cpc.pow(-choose2(i)) % mod);
        }
        int[] dc = PrimitiveBuffers.replace(deltaConvolution(A, B), A, B);
        for (int i = 0; i <= m; i++) {
            int coe = i < dc.length ? dc[i] : 0;
            coe = (int) ((long) coe * cpc.pow(-choose2(i)) % mod);
            ans[i] = coe;
        }
        PrimitiveBuffers.release(dc);
    }


    /**
     * O(n (lg n)^2) to got a rank n-1 polynomial f while f(xi)=yi
     */
    public int[] interpolate(int[] x, int[] y, int n) {
        PolynomialTree tree = build(x, 0, n - 1);
        int[] diffM = differential(tree.p);
        int[] v = PrimitiveBuffers.allocIntPow2(n);
        multiApply(diffM, x, v, 0, n - 1, tree);
        for (int i = 0; i < n; i++) {
            v[i] = (int) ((long) y[i] * power.inverse(v[i]) % mod);
        }
        int[] ans = interpolate0(v, 0, n - 1, tree);
        releaseTree(tree, 0, n - 1);
        return ans;
    }

    private int[] interpolate0(int[] v, int l, int r, PolynomialTree tree) {
        if (l == r) {
            int[] ans = PrimitiveBuffers.allocIntPow2(1);
            ans[0] = v[l];
            return ans;
        }
        int m = DigitUtils.floorAverage(l, r);
        int[] f0 = interpolate0(v, l, m, tree.left);
        int[] f1 = interpolate0(v, m + 1, r, tree.right);
        int[] a = PrimitiveBuffers.replace(convolution(f0, tree.right.p), f0);
        int[] b = PrimitiveBuffers.replace(convolution(f1, tree.left.p), f1);
        int[] sum = PrimitiveBuffers.replace(plus(a, b), a, b);
        return sum;
    }

    /**
     * Get (x - c[l]) * ... * (x - c[r]) in O(n(lg n)^2)
     */
    public int[] dacPointMul(int[] c, int l, int r) {
        if (l == r) {
            int[] ans = PrimitiveBuffers.allocIntPow2(2);
            ans[1] = 1;
            ans[0] = DigitUtils.negate(c[l], mod);
            return ans;
        }
        int m = DigitUtils.floorAverage(l, r);
        int[] a = dacPointMul(c, l, m);
        int[] b = dacPointMul(c, m + 1, r);
        return PrimitiveBuffers.replace(convolution(a, b), a, b);
    }

    public int[] sqrt(int[] g, int m) {
        int zero = 0;
        while (zero < g.length && g[zero] == 0) {
            zero++;
        }
        if (zero == g.length) {
            return PrimitiveBuffers.allocIntPow2(1);
        }
        if (zero % 2 != 0) {
            return null;
        }
        if (qr.square(g[zero]) == -1) {
            return null;
        }
        int[] shift = zero == 0 ? g : leftShift(g, zero);
        int[] ans = sqrt0(shift, m - zero / 2);
        if (zero != 0) {
            ans = PrimitiveBuffers.replace(rightShift(ans, zero / 2, m), ans);
        }
        if (shift != g) {
            PrimitiveBuffers.release(shift);
        }
        return ans;
    }

    private int[] sqrt0(int[] g, int m) {
        if (m == 1) {
            int[] f = PrimitiveBuffers.allocIntPow2(1);
            f[0] = qr.square(g[0]);
            assert f[0] != -1;
            return f;
        }
        int[] f = sqrt0(g, (m + 1) / 2);
        int[] invF = inverse(f, m);
        int[] prefG = PrimitiveBuffers.allocIntPow2(g, m);
        int[] gDivF = PrimitiveBuffers.replace(convolution(invF, prefG), invF, prefG);
        int[] sum = PrimitiveBuffers.replace(plus(f, gDivF), f, gDivF);
        assert mod % 2 == 1;
        int inv2 = (mod + 1) / 2;
        for (int i = 0; i < sum.length; i++) {
            sum[i] = (int) ((long) sum[i] * inv2 % mod);
        }
        return sum;
    }


    public FastDivision newFastDivision(int[] p, int maxRankOfDividend) {
        return new FastDivision(p, maxRankOfDividend);
    }

    public class FastDivision {
        int[] invb;
        int[] b;
        int rb;

        FastDivision(int[] b, int maxRankOfDividend) {
            rb = Polynomials.rankOf(b);
            this.b = PrimitiveBuffers.allocIntPow2(b, rb + 1);
            int maxRankOfC = maxRankOfDividend - rb;
            if (maxRankOfC < 0) {
                return;
            }
            int[] rev = PrimitiveBuffers.allocIntPow2(maxRankOfC + 1);
            for (int i = 0; i < rev.length && i <= rb; i++) {
                rev[i] = b[rb - i];
            }
            invb = inverse(rev, maxRankOfC + 1);
            PrimitiveBuffers.release(rev);
        }

        public void release() {
            PrimitiveBuffers.release(invb);
            PrimitiveBuffers.release(b);
        }

        protected int[] divide(int[] a) {
            int rankA = rankOf(a);
            int rankB = rb;
            int rankC = rankA - rankB;
            if (rankC < 0) {
                return PrimitiveBuffers.allocIntPow2(1);
            }

            //mod x^{rankC+1}
            int[] aa = PrimitiveBuffers.allocIntPow2(rankC + rankC + 1);
            int[] cc = PrimitiveBuffers.allocIntPow2(invb, rankC + 1);
            for (int i = 0; i <= rankA && i < aa.length; i++) {
                aa[i] = a[rankA - i];
            }

            int[] dm = PrimitiveBuffers.replace(convolution(aa, cc), aa, cc);
            for (int i = rankC + 1; i < dm.length; i++) {
                dm[i] = 0;
            }

            SequenceUtils.reverse(dm, 0, rankC);
            return Polynomials.normalizeAndReplace(dm);
        }

        static final int FAST_DIVIDE_THRESHOLD = 50;

        public int[][] divideAndRemainder(int[] a) {
            int rankA = rankOf(a);
            int rankB = rb;
            int rankC = rankA - rankB;

            if (rankC < 0) {
                int[][] ans = new int[2][];
                ans[0] = PrimitiveBuffers.allocIntPow2(1);
                ans[1] = PrimitiveBuffers.allocIntPow2(a, rankA + 1);
                return ans;
            }

//            if (rankA < FAST_DIVIDE_THRESHOLD || rankB < FAST_DIVIDE_THRESHOLD) {
//                return IntPoly.this.divideAndRemainder(a, b);
//            }

            int[] quotient = divide(a);
            int[] prod = convolution(b, quotient);
            int[] remainder = PrimitiveBuffers.replace(subtract(a, prod), prod);
            remainder = PrimitiveBuffers.replace(module(remainder, rankB), remainder);
            remainder = Polynomials.normalizeAndReplace(remainder);
            return new int[][]{quotient, remainder};
        }
    }
}
