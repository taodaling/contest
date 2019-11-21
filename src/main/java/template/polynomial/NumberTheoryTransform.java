package template.polynomial;

import template.math.Log2;
import template.math.Power;
import template.math.PrimitiveRoot;
import template.utils.SequenceUtils;
import template.datastructure.ByteList;
import template.datastructure.IntList;
import template.math.Modular;
import template.utils.Buffer;
import template.math.DigitUtils;

import java.util.Arrays;

public class NumberTheoryTransform {
    public static final NumberTheoryTransform STANDARD =
                    new NumberTheoryTransform(new Modular(998244353), 3);
    private Modular modular;
    private Power power;
    private int g;
    private int[] wCache = new int[30];
    private int[] invCache = new int[30];
    public static Buffer<IntList> listBuffer = Polynomials.listBuffer;
    private Log2 log2 = new Log2();

    public NumberTheoryTransform(Modular mod) {
        this(mod, mod.getMod() == 998244353 ? 3 : new PrimitiveRoot(mod.getMod()).findMinPrimitiveRoot());
    }

    public NumberTheoryTransform(Modular mod, int g) {
        this.modular = mod;
        this.power = new Power(mod);
        this.g = g;
        for (int i = 0, until = wCache.length; i < until; i++) {
            int s = 1 << i;
            wCache[i] = power.pow(this.g, (modular.getMod() - 1) / 2 / s);
            invCache[i] = power.inverse(s);
        }
    }

    public void dotMul(int[] a, int[] b, int[] c, int m) {
        for (int i = 0, n = 1 << m; i < n; i++) {
            c[i] = modular.mul(a[i], b[i]);
        }
    }

    public void prepareReverse(int[] r, int b) {
        int n = 1 << b;
        r[0] = 0;
        for (int i = 1; i < n; i++) {
            r[i] = (r[i >> 1] >> 1) | ((1 & i) << (b - 1));
        }
    }

    public void dft(int[] p, int m) {
        int n = 1 << m;

        IntList rev = listBuffer.alloc();
        rev.expandWith(0, 1 << m);
        int[] r = rev.getData();
        prepareReverse(r, m);

        for (int i = 0; i < n; i++) {
            if (r[i] > i) {
                int tmp = p[i];
                p[i] = p[r[i]];
                p[r[i]] = tmp;
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
                    t = modular.mul(w, p[b]);
                    p[b] = modular.plus(p[a], -t);
                    p[a] = modular.plus(p[a], t);
                    w = modular.mul(w, w1);
                }
            }
        }

        listBuffer.release(rev);
    }

    public void idft(int[] p, int m) {
        dft(p, m);

        int n = 1 << m;
        int invN = invCache[m];

        p[0] = modular.mul(p[0], invN);
        p[n / 2] = modular.mul(p[n / 2], invN);
        for (int i = 1, until = n / 2; i < until; i++) {
            int a = p[n - i];
            p[n - i] = modular.mul(p[i], invN);
            p[i] = modular.mul(a, invN);
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

        IntList aSnapshot = listBuffer.alloc();
        IntList bSnapshot = listBuffer.alloc();
        IntList cSnapshot = listBuffer.alloc();
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
            remainder[i] = modular.subtract(a[i], modular.mul(b[i], c[i]));
        }
        idft(remainder, m);
        System.arraycopy(aSnapshot.getData(), 0, a, 0, n);
        System.arraycopy(bSnapshot.getData(), 0, b, 0, n);
        System.arraycopy(cSnapshot.getData(), 0, c, 0, n);

        listBuffer.release(aSnapshot);
        listBuffer.release(bSnapshot);
        listBuffer.release(cSnapshot);
    }

    public void dacMul(IntList[] lists, IntList ans) {
        IntList prod = dacMul(lists, 0, lists.length - 1);
        ans.clear();
        ans.addAll(prod);
        listBuffer.release(prod);
        return;
    }

    private static final int NTT_THRESHOLD = 128;

    private IntList dacMul(IntList[] lists, int l, int r) {
        if (l == r) {
            IntList alloc = listBuffer.alloc();
            alloc.addAll(lists[l]);
            Polynomials.normalize(alloc);
            return alloc;
        }
        int m = (l + r) >> 1;
        IntList a = dacMul(lists, l, m);
        IntList b = dacMul(lists, m + 1, r);

        if (a.size() < NTT_THRESHOLD || b.size() < NTT_THRESHOLD) {
            IntList ans = listBuffer.alloc();
            Polynomials.mul(a, b, ans, modular);
            listBuffer.release(a);
            listBuffer.release(b);
            return ans;
        } else {
            int rankAns = a.size() - 1 + b.size() - 1;
            int proper = log2.ceilLog(rankAns + 1);
            a.expandWith(0, (1 << proper));
            b.expandWith(0, (1 << proper));
            dft(a.getData(), proper);
            dft(b.getData(), proper);
            dotMul(a.getData(), b.getData(), a.getData(), proper);
            idft(a.getData(), proper);
            Polynomials.normalize(a);
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
            inv[0] = power.inverse(p[0]);
            return;
        }
        inverse(p, inv, m - 1);
        IntList buf = listBuffer.alloc();
        int n = 1 << (m + 1);
        buf.addAll(p, 0, 1 << m);
        buf.expandWith(0, n);
        int[] bufData = buf.getData();
        dft(bufData, (m + 1));
        dft(inv, (m + 1));
        for (int i = 0; i < n; i++) {
            inv[i] = modular.mul(inv[i], 2 - modular.mul(bufData[i], inv[i]));
        }
        idft(inv, m + 1);
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
        IntList a = listBuffer.alloc();
        IntList c = listBuffer.alloc();

        a.expandWith(0, 1 << m);
        c.expandWith(0, 1 << m);

        module(k, a.getData(), p, c.getData(), remainder, rankOfP, m);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private void module(long k, int[] a, int[] b, int[] c, int[] remainder, int rb, int m) {
        if (k < rb) {
            Arrays.fill(remainder, 0);
            remainder[(int) k] = modular.valueOf(1);
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
    public void module(ByteList k, int[] p, int[] remainder, int m) {
        int rankOfP = rankOf(p, m);
        if (rankOfP == 0) {
            Arrays.fill(remainder, 0);
            return;
        }
        IntList r = listBuffer.alloc();
        IntList a = listBuffer.alloc();
        IntList c = listBuffer.alloc();

        r.expandWith(0, 1 << m);
        a.expandWith(0, 1 << m);
        c.expandWith(0, 1 << m);

        module(k, k.size() - 1, a.getData(), p, c.getData(), remainder, m);

        listBuffer.release(a);
        listBuffer.release(r);
        listBuffer.release(c);
    }

    private void module(ByteList k, int i, int[] a, int[] b, int[] c, int[] remainder, int m) {
        if (i < 0) {
            Arrays.fill(remainder, 0);
            remainder[0] = modular.valueOf(1);
            return;
        }
        module(k, i - 1, a, b, c, remainder, m);
        dft(remainder, m);
        dotMul(remainder, remainder, remainder, m);
        idft(remainder, m);

        if (k.get(i) == 1) {
            for (int j = (1 << m) - 1; j > 0; j--) {
                remainder[j] = remainder[j - 1];
            }
            remainder[0] = 0;
        }

        System.arraycopy(remainder, 0, a, 0, 1 << m);
        divide(a, b, c, remainder, m);
    }
}
