package template.polynomial;

import template.math.DigitUtils;
import template.math.ILongModular;

public class FastWalshHadamardTransform {
    /**
     * res[i] is how many subsets of i occur in p
     */
    public static void orFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        orFWT(p, l, m);
        orFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[m + 1 + i] = a + b;
        }
    }

    /**
     * p[i] is the number of subset of i, ret[i] is the occurrence number of number i
     */
    public static void orIFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[m + 1 + i] = b - a;
        }
        orIFWT(p, l, m);
        orIFWT(p, m + 1, r);
    }

    /**
     * res[i] is how many subsets of i occur in p
     */
    public static void orFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        orFWT(p, l, m);
        orFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[m + 1 + i] = a + b;
        }
    }

    /**
     * res[i] is how many subsets of i occur in p
     */
    public static void orFWT(int[] p, int l, int r, int mod) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        orFWT(p, l, m, mod);
        orFWT(p, m + 1, r, mod);
        for (int i = 0, until = m - l; i <= until; i++) {
            int i1 = l + i;
            int i2 = m + 1 + i;
            int a = p[i1];
            int b = p[i2];
            p[i2] = a + b;
            if(p[i2] >= mod){
                p[i2] -= mod;
            }
        }
    }

    /**
     * p[i] is the number of subset of i, ret[i] is the occurrence number of number i
     */
    public static void orIFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[m + 1 + i] = b - a;
        }
        orIFWT(p, l, m);
        orIFWT(p, m + 1, r);
    }

    public static void orIFWT(int[] p, int l, int r, int mod) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int i1 = l + i;
            int i2 = m + 1 + i;
            int a = p[i1];
            int b = p[i2];
            p[i2] = b - a;
            if(p[i2] < 0){
                p[i2] += mod;
            }
        }
        orIFWT(p, l, m, mod);
        orIFWT(p, m + 1, r, mod);
    }

    /**
     * res[i] is how many superset of i occur in p
     */
    public static void andFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        andFWT(p, l, m);
        andFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[l + i] = a + b;
        }
    }

    /**
     * p[i] is the number of superset of i, ret[i] is the occurrence number of number i
     */
    public static void andIFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[l + i] = a - b;
        }
        andIFWT(p, l, m);
        andIFWT(p, m + 1, r);
    }

    /**
     * res[i] is how many superset of i occur in p
     */
    public static void andFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        andFWT(p, l, m);
        andFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = a + b;
        }
    }

    /**
     * res[i] is how many superset of i occur in p
     */
    public static void andFWT(int[] p, int l, int r, int mod) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        andFWT(p, l, m, mod);
        andFWT(p, m + 1, r, mod);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = DigitUtils.modplus(a, b, mod);
        }
    }

    /**
     * p[i] is the number of superset of i, ret[i] is the occurrence number of number i
     */
    public static void andIFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = a - b;
        }
        andIFWT(p, l, m);
        andIFWT(p, m + 1, r);
    }

    /**
     * p[i] is the number of superset of i, ret[i] is the occurrence number of number i
     */
    public static void andIFWT(int[] p, int l, int r, int mod) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = DigitUtils.modsub(a, b, mod);
        }
        andIFWT(p, l, m, mod);
        andIFWT(p, m + 1, r, mod);
    }

    public static void xorFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        xorFWT(p, l, m);
        xorFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[l + i] = a + b;
            p[m + 1 + i] = a - b;
        }
    }

    public static void xorIFWT(long[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
            p[l + i] = (a + b) / 2;
            p[m + 1 + i] = (a - b) / 2;
        }
        xorIFWT(p, l, m);
        xorIFWT(p, m + 1, r);
    }

    public static void xorFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        xorFWT(p, l, m);
        xorFWT(p, m + 1, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = a + b;
            p[m + 1 + i] = a - b;
        }
    }

    public static void xorIFWT(int[] p, int l, int r) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = (a + b) / 2;
            p[m + 1 + i] = (a - b) / 2;
        }
        xorIFWT(p, l, m);
        xorIFWT(p, m + 1, r);
    }

    public static void xorFWT(int[] p, int l, int r, int mod) {
        if (l == r) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        xorFWT(p, l, m, mod);
        xorFWT(p, m + 1, r, mod);
        for (int i = 0, until = m - l; i <= until; i++) {
            int a = p[l + i];
            int b = p[m + 1 + i];
            p[l + i] = DigitUtils.modplus(a, b, mod);
            p[m + 1 + i] = DigitUtils.modsub(a, b, mod);
        }
    }

    public static void xorIFWT(int[] p, int l, int r, int mod) {
        if (l == r) {
            return;
        }
        int inv2 = (mod + 1) / 2;
        int m = DigitUtils.floorAverage(l, r);
        for (int i = 0, until = m - l; i <= until; i++) {
            long a = p[l + i];
            long b = p[m + 1 + i];
//            assert (a & 1) == (b & 1);
            p[l + i] = (int) ((a + b) * inv2 % mod);
            p[m + 1 + i] = (int) ((a - b + mod) * inv2 % mod);
        }
        xorIFWT(p, l, m, mod);
        xorIFWT(p, m + 1, r, mod);
    }

    public static void dotMul(int[] a, int[] b, int[] c, int l, int r) {
        for (int i = l; i <= r; i++) {
            c[i] = a[i] * b[i];
        }
    }

    public static void dotMul(int[] a, int[] b, int[] c, int l, int r, int mod) {
        for (int i = l; i <= r; i++) {
            c[i] = (int) ((long) a[i] * b[i] % mod);
        }
    }

    public static void dotMulPlus(int[] a, int[] b, int[] c, int l, int r, int mod) {
        for (int i = l; i <= r; i++) {
            c[i] = (int) (((long) a[i] * b[i] + c[i]) % mod);
        }
    }

    public static void dotMul(long[] a, long[] b, long[] c, int l, int r) {
        for (int i = l; i <= r; i++) {
            c[i] = a[i] * b[i];
        }
    }

    public static void dotMul(long[] a, long[] b, long[] c, int l, int r, ILongModular mod) {
        for (int i = l; i <= r; i++) {
            c[i] = mod.mul(a[i], b[i]);
        }
    }

    /**
     * copied from https://www.luogu.com.cn/blog/cqbzllsw/cf453dlittle-pony-and-elements-of-harmony
     *
     * @param f
     * @param N
     * @param mod
     * @param rev
     */
    public static void xorFWT(long[] f, int N, long mod, boolean rev) {
        long t1, t2;
        for (int s = 2; s <= N; s <<= 1)
            for (int i = 0, t = s >> 1; i < N; i += s)
                for (int j = i; j < i + t; j++) {
                    t1 = f[j];
                    t2 = f[j + t];
                    f[j] = (t1 + t2);
                    f[j + t] = (t1 - t2);
                    if (f[j] >= mod) {
                        f[j] -= mod;
                    }
                    if (f[j + t] < 0) {
                        f[j + t] += mod;
                    }
                }
        if (!rev) return;
        for (int i = 0; i < N; i++) f[i] /= N;
    }
}
