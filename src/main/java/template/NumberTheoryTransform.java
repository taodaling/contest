package template;

import java.util.Arrays;

public class NumberTheoryTransform {
    public static final NumberTheoryTransform STANDARD = new NumberTheoryTransform(new NumberTheory.Modular(998244353), 3);
    private NumberTheory.Modular MODULAR;
    private NumberTheory.Power POWER;
    private int G;
    private int[] wCache = new int[30];
    private int[] invCache = new int[30];


    public NumberTheoryTransform(NumberTheory.Modular mod){
        this(mod, new NumberTheory.PrimitiveRoot(mod.getMod()).findMinPrimitiveRoot());
    }

    public NumberTheoryTransform(NumberTheory.Modular mod, int g) {
        this.MODULAR = mod;
        this.POWER = new NumberTheory.Power(mod);
        this.G = g;
        for (int i = 0, until = wCache.length; i < until; i++) {
            int s = 1 << i;
            wCache[i] = POWER.pow(G, (MODULAR.m - 1) / 2 / s);
            invCache[i] = POWER.inverse(s);
        }
    }

    public void dotMul(int[] a, int[] b, int[] c, int m) {
        for (int i = 0, n = 1 << m; i < n; i++) {
            c[i] = MODULAR.mul(a[i], b[i]);
        }
    }

    public void prepareReverse(int[] r, int b) {
        int n = 1 << b;
        r[0] = 0;
        for (int i = 1; i < n; i++) {
            r[i] = (r[i >> 1] >> 1) | ((1 & i) << (b - 1));
        }
    }

    public void dft(int[] r, int[] p, int m) {
        int n = 1 << m;

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
                    t = MODULAR.mul(w, p[b]);
                    p[b] = MODULAR.plus(p[a], -t);
                    p[a] = MODULAR.plus(p[a], t);
                    w = MODULAR.mul(w, w1);
                }
            }
        }
    }

    public void idft(int[] r, int[] p, int m) {
        dft(r, p, m);

        int n = 1 << m;
        int invN = invCache[m];

        p[0] = MODULAR.mul(p[0], invN);
        p[n / 2] = MODULAR.mul(p[n / 2], invN);
        for (int i = 1, until = n / 2; i < until; i++) {
            int a = p[n - i];
            p[n - i] = MODULAR.mul(p[i], invN);
            p[i] = MODULAR.mul(a, invN);
        }
    }

    public void reverse(int[] p, int l, int r) {
        while (l < r) {
            int tmp = p[l];
            p[l] = p[r];
            p[r] = tmp;
            l++;
            r--;
        }
    }

    public int rankOf(int[] p) {
        for (int i = p.length - 1; i >= 0; i--) {
            if (p[i] > 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * calc a = b * c + remainder, m >= 1 + ceil(log_2 max(|a|, |b|))
     */
    public void divide(int[] r, int[] a, int[] b, int[] c, int[] remainder, int m) {
        int rankA = rankOf(a);
        int rankB = rankOf(b);
        reverse(a, 0, rankA);
        reverse(b, 0, rankB);
        inverse(r, b, c, remainder, m - 1);
        dft(r, a, m);
        dft(r, c, m);
        dotMul(a, c, c, m);
        idft(r, a, m);
        idft(r, c, m);
        reverse(a, 0, rankA);
        reverse(b, 0, rankB);
        for (int i = rankA - rankB + 1; i < c.length; i++) {
            c[i] = 0;
        }
        reverse(c, 0, rankA - rankB);

        dft(r, a, m);
        dft(r, b, m);
        dft(r, c, m);
        for (int i = 0; i < remainder.length; i++) {
            remainder[i] = MODULAR.subtract(a[i], MODULAR.mul(b[i], c[i]));
        }
        idft(r, a, m);
        idft(r, b, m);
        idft(r, c, m);
        idft(r, remainder, m);
    }

    /**
     * return polynomial g while p * g = 1 (mod x^m).
     * <br>
     * You are supposed to guarantee the lengths of all arrays are greater than or equal to 2^{ceil(log2(m)) + 1}
     */
    private void inverse(int[] r, int[] p, int[] inv, int[] buf, int m) {
        if (m == 0) {
            inv[0] = POWER.inverse(p[0]);
            return;
        }
        inverse(r, p, inv, buf, m - 1);
        int n = 1 << (m + 1);
        System.arraycopy(p, 0, buf, 0, 1 << m);
        Arrays.fill(buf, 1 << m, 1 << (m + 1), 0);
        prepareReverse(r, (m + 1));
        dft(r, buf, (m + 1));
        dft(r, inv, (m + 1));
        for (int i = 0; i < n; i++) {
            inv[i] = MODULAR.mul(inv[i], 2 - MODULAR.mul(buf[i], inv[i]));
        }
        idft(r, inv, m + 1);
        for (int i = 1 << m; i < n; i++) {
            inv[i] = 0;
        }
    }
}