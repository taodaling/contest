package template.polynomial;

import template.binary.Log2;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.Power;
import template.math.PrimitiveRoot;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Buffer;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.BitSet;
import java.util.PriorityQueue;

public class NumberTheoryTransform {
    private int mod;
    private Power power;
    private int g;
    private int[] wCache = new int[30];
    private int[] invCache = new int[30];
    public static Buffer<IntegerArrayList> listBuffer = Polynomials.listBuffer;

    public NumberTheoryTransform(int mod) {
        this(mod, mod == 998244353 ? 3 : new PrimitiveRoot(mod).findMinPrimitiveRoot());
    }

    public NumberTheoryTransform(int mod, int g) {
        this.mod = mod;
        this.power = new Power(mod);
        this.g = g;
        for (int i = 0, until = wCache.length; i < until; i++) {
            int s = 1 << i;
            wCache[i] = power.pow(this.g, (mod - 1) / 2 / s);
            invCache[i] = power.inverseByFermat(s);
        }
    }

    public void dotMul(int[] a, int[] b, int[] c, int m) {
        for (int i = 0, n = 1 << m; i < n; i++) {
            c[i] = (int) ((long) a[i] * b[i] % mod);
        }
    }

    public void dft(int[] p, int m) {
        int n = 1 << m;

        int shift = 32 - Integer.numberOfTrailingZeros(n);
        for (int i = 1; i < n; i++) {
            int j = Integer.reverse(i << shift);
            if (i < j) {
                int temp = p[i];
                p[i] = p[j];
                p[j] = temp;
            }
        }

        int w = 0;
        int t = 0;
        for (int d = 0; d < m; d++) {
            int w1 = wCache[d];
            int s = 1 << d;
            int s2 = s << 1;
            for (int i = 0; i < n; i += s2) {
                w = 1;
                for (int j = 0; j < s; j++) {
                    int a = i + j;
                    int b = a + s;
                    t = (int) ((long) w * p[b] % mod);
                    p[b] = DigitUtils.modsub(p[a], t, mod);
                    p[a] = DigitUtils.modplus(p[a], t, mod);
                    w = (int) ((long) w * w1 % mod);
                }
            }
        }
    }

    public void idft(int[] p, int m) {
        dft(p, m);

        int n = 1 << m;
        long invN = invCache[m];

        p[0] = (int) ((long) p[0] * invN % mod);
        p[n / 2] = (int) (p[n / 2] * invN % mod);
        for (int i = 1, until = n / 2; i < until; i++) {
            int a = p[n - i];
            p[n - i] = (int) (p[i] * invN % mod);
            p[i] = (int) (a * invN % mod);
        }
    }

    public int rankOf(int[] p, int m) {
        for (int i = (1 << m) - 1; i >= 1; i--) {
            if (p[i] > 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * calc a = b * c + remainder, m >= 1 + ceil(log_2 max(|a|, |b|))
     */
    public void divide(int[] a, int[] b, int[] c, int[] remainder, int m) {
        int n = 1 << m;

        int rankA = rankOf(a, m);
        int rankB = rankOf(b, m);

        if (rankA < rankB) {
            Arrays.fill(c, 0, n, 0);
            System.arraycopy(a, 0, remainder, 0, n);
            return;
        }

        IntegerArrayList aSnapshot = listBuffer.alloc();
        IntegerArrayList bSnapshot = listBuffer.alloc();
        IntegerArrayList cSnapshot = listBuffer.alloc();
        aSnapshot.addAll(a, 0, n);
        bSnapshot.addAll(b, 0, n);

        SequenceUtils.reverse(a, 0, rankA);
        SequenceUtils.reverse(b, 0, rankB);
        inverse(b, c, m - 1);
        dft(a, m);
        dft(c, m);
        dotMul(a, c, c, m);
        idft(c, m);

        System.arraycopy(aSnapshot.getData(), 0, a, 0, n);
        SequenceUtils.reverse(b, 0, rankB);

        for (int i = rankA - rankB + 1; i < n; i++) {
            c[i] = 0;
        }
        SequenceUtils.reverse(c, 0, rankA - rankB);

        cSnapshot.addAll(c, 0, n);
        dft(a, m);
        dft(b, m);
        dft(c, m);
        for (int i = 0; i < n; i++) {
            remainder[i] = DigitUtils.mod(a[i] - (long) b[i] * c[i], mod);
        }
        idft(remainder, m);
        System.arraycopy(aSnapshot.getData(), 0, a, 0, n);
        System.arraycopy(bSnapshot.getData(), 0, b, 0, n);
        System.arraycopy(cSnapshot.getData(), 0, c, 0, n);

        listBuffer.release(aSnapshot);
        listBuffer.release(bSnapshot);
        listBuffer.release(cSnapshot);
    }

    /**
     * calculate lists[0] * lists[1] * ... * lists[lists.length - 1]
     * by dividing and conquer technology.
     * <br>
     * The total time complexity is O(mlogn) while m = lists.length and
     * n = lists[0].length + lists[1].length + ... + lists[m - 1].length.
     */
    public void dacMul(IntegerArrayList[] lists, IntegerArrayList ans) {
        IntegerArrayList prod = dacMul(lists, 0, lists.length - 1);
        ans.clear();
        ans.addAll(prod);
        listBuffer.release(prod);
        return;
    }

    /**
     * calculate lists[0] * lists[1] * ... * lists[lists.length - 1]
     * by dividing and conquer technology.
     * <br>
     * The total time complexity is O(nlog nlog m) while m = lists.length and
     * n = lists[0].length + lists[1].length + ... + lists[m - 1].length.
     */
    public void mulByPQ(IntegerArrayList[] lists, IntegerArrayList ans) {
        PriorityQueue<IntegerArrayList> pqs = new PriorityQueue<>(lists.length, (a, b) -> a.size() - b.size());
        for (IntegerArrayList list : lists) {
            IntegerArrayList clone = listBuffer.alloc();
            clone.addAll(list);
            pqs.add(clone);
        }
        while (pqs.size() > 1) {
            IntegerArrayList a = pqs.remove();
            IntegerArrayList b = pqs.remove();
            if (a.size() < NTT_THRESHOLD || b.size() < NTT_THRESHOLD) {
                IntegerArrayList c = listBuffer.alloc();
                Polynomials.mul(a, b, c, mod);
                listBuffer.release(a);
                listBuffer.release(b);
                pqs.add(c);
            } else {
                multiplyAndStoreAnswerIntoA(a, b);
                listBuffer.release(b);
                pqs.add(a);
            }
        }

        IntegerArrayList last = pqs.remove();
        ans.clear();
        ans.addAll(last);
        listBuffer.release(last);
        return;
    }


    private IntegerArrayList clone(IntegerArrayList list) {
        Polynomials.normalize(list);
        IntegerArrayList ans = listBuffer.alloc();
        ans.addAll(list);
        return ans;
    }

    public void mul(IntegerArrayList a, IntegerArrayList b, IntegerArrayList ans) {
        a = clone(a);
        b = clone(b);
        int rank = a.size() + b.size() - 1;
        int proper = Log2.ceilLog(rank + 1);
        a.expandWith(0, 1 << proper);
        b.expandWith(0, 1 << proper);
        ans.clear();
        ans.expandWith(0, 1 << proper);
        dft(a.getData(), proper);
        dft(b.getData(), proper);
        dotMul(a.getData(), b.getData(), ans.getData(), proper);
        idft(ans.getData(), proper);
        Polynomials.normalize(ans);
        listBuffer.release(a);
        listBuffer.release(b);
    }

    public void modmul(IntegerArrayList a, IntegerArrayList b, IntegerArrayList ans, int n) {
        mul(a, b, ans);
        Polynomials.module(ans, n);
    }

    /**
     * <p>
     * make ans = ln a mod x^n
     * </p>
     * <p>
     * it run in O(n\log_2 n)
     * </p>
     */
    public void ln(IntegerArrayList a, IntegerArrayList ans, int n, InverseNumber inverse) {
        a = clone(a);
        Polynomials.module(a, n);
        IntegerArrayList diff = listBuffer.alloc();
        IntegerArrayList inv = listBuffer.alloc();
        int proper = Log2.ceilLog(n);
        a.expandWith(0, 1 << (proper + 1));
        inv.expandWith(0, 1 << (proper + 1));
        inverse(a.getData(), inv.getData(), proper);
        Polynomials.module(a, n);
        Polynomials.module(inv, n);
        Polynomials.differential(a, diff, mod);
        Polynomials.module(diff, n);
        modmul(diff, inv, a, n);
        Polynomials.integral(a, ans, mod, inverse);
        Polynomials.module(ans, n);

        listBuffer.release(a);
        listBuffer.release(diff);
        listBuffer.release(inv);
    }

    /**
     * <p> ans = exp(a) mod x^n </p>
     * <p> it run in O(n\log_2 n) </p>
     */
    public void exp(IntegerArrayList a, IntegerArrayList ans, int n, InverseNumber inverse) {
        if (n == 0) {
            ans.clear();
            ans.push(0);
            return;
        }
        a = clone(a);
        a.expandWith(0, n);
        Polynomials.module(a, n);
        exp0(a, ans, n, inverse);
        listBuffer.release(a);
    }

    private void exp0(IntegerArrayList a, IntegerArrayList ans, int n, InverseNumber inverse) {
        if (n == 1) {
            ans.clear();
            ans.push(1);
            return;
        }
        exp0(a, ans, (n + 1) / 2, inverse);
        IntegerArrayList f0 = clone(ans);
        IntegerArrayList lnf0 = listBuffer.alloc();
        ln(f0, lnf0, n, inverse);
        lnf0.expandWith(0, n);
        {
            int[] data = lnf0.getData();
            int[] aData = a.getData();
            for (int i = 0; i < n; i++) {
                data[i] = DigitUtils.modsub(aData[i], data[i], mod);
            }
            data[0] = DigitUtils.modplus(data[0], 1, mod);
        }
        modmul(f0, lnf0, ans, n);
        listBuffer.release(f0);
        listBuffer.release(lnf0);
    }

    public void pow2(IntegerArrayList a) {
        int rankAns = (a.size() - 1) * 2;
        int proper = Log2.ceilLog(rankAns + 1);
        a.expandWith(0, (1 << proper));
        dft(a.getData(), proper);
        dotMul(a.getData(), a.getData(), a.getData(), proper);
        idft(a.getData(), proper);
        Polynomials.normalize(a);
    }

    /**
     * 多项式多点插值 O(p\log p+x(\log x)^2)
     */
    public void multiApply(IntegerArrayList p, IntegerArrayList x, IntegerArrayList y) {
        int l = 0;
        int r = x.size() - 1;
        y.expandWith(0, x.size());
        multiApply(p, x, y, l, r, build(x, l, r));
    }

    private int apply(IntegerArrayList p, int x) {
        x = DigitUtils.mod(x, mod);
        Polynomials.normalize(p);
        long y = 0;
        int[] data = p.getData();
        for (int i = p.size() - 1; i >= 0; i--) {
            y = (y * x + data[i]) % mod;
        }
        return (int) y;
    }

    /**
     * a = b * c + r
     */
    private void divide(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, IntegerArrayList r) {
        Polynomials.normalize(a);
        Polynomials.normalize(b);
        int proper = 1 + Log2.ceilLog(Math.max(a.size(), b.size()));
        a.expandWith(0, 1 << proper);
        b.expandWith(0, 1 << proper);
        c.expandWith(0, 1 << proper);
        r.expandWith(0, 1 << proper);
        divide(a.getData(), b.getData(), c.getData(), r.getData(), proper);
        Polynomials.normalize(a);
        Polynomials.normalize(b);
        Polynomials.normalize(c);
        Polynomials.normalize(r);
    }

    private void multiApply(IntegerArrayList p, IntegerArrayList x, IntegerArrayList y, int l, int r, PolynomialBTree tree) {
        if (r - l + 1 <= 4) {
            for (int i = l; i <= r; i++) {
                y.set(i, apply(p, x.get(i)));
            }
            return;
        }
        IntegerArrayList c = listBuffer.alloc();
        IntegerArrayList remainder = listBuffer.alloc();
        divide(p, tree.p, c, remainder);

        listBuffer.release(c);
        int m = DigitUtils.floorAverage(l, r);
        multiApply(remainder, x, y, l, m, tree.left);
        multiApply(remainder, x, y, m + 1, r, tree.right);
        listBuffer.release(remainder);
    }

    private PolynomialBTree build(IntegerArrayList x, int l, int r) {
        PolynomialBTree tree = new PolynomialBTree();
        if (l == r) {
            tree.p = new IntegerArrayList(2);
            tree.p.add(DigitUtils.negate(x.get(l), mod));
            tree.p.add(1 % mod);
        } else {
            int m = DigitUtils.floorAverage(l, r);
            tree.left = build(x, l, m);
            tree.right = build(x, m + 1, r);
            tree.p = new IntegerArrayList();
            mul(tree.left.p, tree.right.p, tree.p);
        }
        return tree;
    }

    private static class PolynomialBTree {
        public IntegerArrayList p;
        public PolynomialBTree left;
        public PolynomialBTree right;
    }

    private void multiplyAndStoreAnswerIntoA(IntegerArrayList a, IntegerArrayList b) {
        int rankAns = a.size() - 1 + b.size() - 1;
        int proper = Log2.ceilLog(rankAns + 1);
        a.expandWith(0, (1 << proper));
        b.expandWith(0, (1 << proper));
        dft(a.getData(), proper);
        dft(b.getData(), proper);
        dotMul(a.getData(), b.getData(), a.getData(), proper);
        idft(a.getData(), proper);
        Polynomials.normalize(a);
    }

    private static final int NTT_THRESHOLD = 128;

    private IntegerArrayList dacMul(IntegerArrayList[] lists, int l, int r) {
        if (l == r) {
            IntegerArrayList alloc = listBuffer.alloc();
            alloc.addAll(lists[l]);
            Polynomials.normalize(alloc);
            return alloc;
        }
        int m = DigitUtils.floorAverage(l, r);
        IntegerArrayList a = dacMul(lists, l, m);
        IntegerArrayList b = dacMul(lists, m + 1, r);

        if (a.size() < NTT_THRESHOLD || b.size() < NTT_THRESHOLD) {
            IntegerArrayList ans = listBuffer.alloc();
            Polynomials.mul(a, b, ans, mod);
            listBuffer.release(a);
            listBuffer.release(b);
            return ans;
        } else {
            multiplyAndStoreAnswerIntoA(a, b);
            listBuffer.release(b);
            return a;
        }
    }

    /**
     * return polynomial g while p * g = 1 (mod x^(2^m)). <br>
     * You are supposed to guarantee the lengths of all arrays are greater than or equal to 2^{m + 1}
     */
    public void inverse(int[] p, int[] inv, int m) {
        if (m == 0) {
            Arrays.fill(inv, 0);
            inv[0] = power.inverseByFermat(p[0]);
            return;
        }
        inverse(p, inv, m - 1);
        IntegerArrayList buf = listBuffer.alloc();
        int n = 1 << (m + 1);
        buf.addAll(p, 0, 1 << m);
        buf.expandWith(0, n);
        int[] bufData = buf.getData();
        dft(bufData, (m + 1));
        dft(inv, (m + 1));
        for (int i = 0; i < n; i++) {
            inv[i] = DigitUtils.mod(inv[i] * (2 - (long) bufData[i] * inv[i] % mod), mod);
        }
        idft(inv, (m + 1));
        for (int i = 1 << m; i < n; i++) {
            inv[i] = 0;
        }
        listBuffer.release(buf);
    }

    /**
     * Get remainder = x^k % p, m >= 2 + ceil(log_2 max(|p|))
     */
    public void module(long k, int[] p, int[] remainder, int m) {
        int rankOfP = rankOf(p, m);
        if (rankOfP == 0) {
            Arrays.fill(remainder, 0);
            return;
        }
        IntegerArrayList a = listBuffer.alloc();
        IntegerArrayList c = listBuffer.alloc();

        a.expandWith(0, 1 << m);
        c.expandWith(0, 1 << m);

        module(k, a.getData(), p, c.getData(), remainder, rankOfP, m);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private void module(long k, int[] a, int[] b, int[] c, int[] remainder, int rb, int m) {
        if (k < rb) {
            Arrays.fill(remainder, 0);
            remainder[(int) k] = 1 % mod;
            return;
        }
        module(k / 2, a, b, c, remainder, rb, m);
        dft(remainder, m);
        dotMul(remainder, remainder, remainder, m);
        idft(remainder, m);

        if (k % 2 == 1) {
            for (int i = (1 << m) - 1; i > 0; i--) {
                remainder[i] = remainder[i - 1];
            }
            remainder[0] = 0;
        }

        System.arraycopy(remainder, 0, a, 0, 1 << m);
        divide(a, b, c, remainder, m);
    }

    /**
     * Get remainder = x^k % p, m >= 2 + ceil(log_2 max(|p|)), the smaller index, the more significant
     * bit in k.
     */
    public void module(BitSet k, int[] p, int[] remainder, int m) {
        int rankOfP = rankOf(p, m);
        if (rankOfP == 0) {
            Arrays.fill(remainder, 0);
            return;
        }
        IntegerArrayList r = listBuffer.alloc();
        IntegerArrayList a = listBuffer.alloc();
        IntegerArrayList c = listBuffer.alloc();

        r.expandWith(0, 1 << m);
        a.expandWith(0, 1 << m);
        c.expandWith(0, 1 << m);

        module(k, k.size() - 1, a.getData(), p, c.getData(), remainder, m);

        listBuffer.release(a);
        listBuffer.release(r);
        listBuffer.release(c);
    }


    private void module(BitSet k, int i, int[] a, int[] b, int[] c, int[] remainder, int m) {
        if (i < 0) {
            Arrays.fill(remainder, 0);
            remainder[0] = 1 % mod;
            return;
        }
        module(k, i - 1, a, b, c, remainder, m);
        dft(remainder, m);
        dotMul(remainder, remainder, remainder, m);
        idft(remainder, m);

        if (k.get(i)) {
            for (int j = (1 << m) - 1; j > 0; j--) {
                remainder[j] = remainder[j - 1];
            }
            remainder[0] = 0;
        }

        System.arraycopy(remainder, 0, a, 0, 1 << m);
        divide(a, b, c, remainder, m);
    }

    /**
     * ans = p ^ k mod x^n
     */
    public void modpow(IntegerArrayList x, IntegerArrayList ans, long k, int n) {
        IntegerArrayList ret = modpow(x, k, n);
        ans.clear();
        ans.addAll(ret);
        listBuffer.release(ret);
    }

    private IntegerArrayList modpow(IntegerArrayList x, long k, int n) {
        if (k == 0) {
            IntegerArrayList ans = listBuffer.alloc();
            ans.add(1 % mod);
            return ans;
        }
        IntegerArrayList ans = modpow(x, k / 2, n);
        IntegerArrayList newAns = listBuffer.alloc();
        modmul(ans, ans, newAns, n);
        if (k % 2 == 1) {
            modmul(x, newAns, ans, n);
            IntegerArrayList tmp = newAns;
            newAns = ans;
            ans = tmp;
        }
        listBuffer.release(ans);
        return newAns;
    }

    /**
     * c[i]=\sum_{j} a[i+j]*b[j]
     */
    public void deltaNTT(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c) {
        a = clone(a);
        b = clone(b);
        int n = a.size();
        b.retain(n);
        a.reverse();

        int m = Log2.ceilLog(n + n - 1);
        a.expandWith(0, 1 << m);
        b.expandWith(0, 1 << m);
        c.clear();
        c.expandWith(0, 1 << m);

        dft(a.getData(), m);
        dft(b.getData(), m);
        dotMul(a.getData(), b.getData(), c.getData(), m);
        idft(c.getData(), m);
        c.retain(n);
        c.reverse();
        Polynomials.normalize(c);

        listBuffer.release(a);
        listBuffer.release(b);
    }
}
