package template.polynomial;

import template.binary.Log2;
import template.utils.PrimitiveBuffers;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class FastFourierTransform {
    private static double eps = 1e-12;
    private static double[][] realLevels = new double[30][];
    private static double[][] imgLevels = new double[30][];


    private static void prepareLevel(int i) {
        if (realLevels[i] == null) {
            realLevels[i] = new double[1 << i];
            imgLevels[i] = new double[1 << i];
            for (int j = 0, s = 1 << i; j < s; j++) {
                realLevels[i][j] = Math.cos(Math.PI / s * j);
                imgLevels[i][j] = Math.sin(Math.PI / s * j);
            }
        }
    }

    public static double[] pow2(double[] a) {
        int rank = Polynomials.rankOf(a);
        double[][] da = PrimitiveBuffers.allocDoublePow2Array(rank * 2 + 1, 2);
        for (int i = 0; i <= rank; i++) {
            da[0][i] = a[i];
        }
        fft(da, false);
        dotMulInplace(da, da);
        fft(da, true);
        PrimitiveBuffers.release(da[1]);
        return da[0];
    }

    public static double[] mod(double[] a, int m) {
        for (int i = m; i < a.length; i++) {
            a[i] = 0;
        }
        return Polynomials.normalizeAndReplace(a);
    }

    /**
     * c[i]=\sum_{j} a[i-j]*b[j]
     */
    public static double[] convolution(double[] a, double[] b) {
        int rA = Polynomials.rankOf(a);
        int rB = Polynomials.rankOf(b);
        int rC = rA + rB;


        double[] aReal = PrimitiveBuffers.allocDoublePow2(a, rC + 1);
        double[] aImg = PrimitiveBuffers.allocDoublePow2(rC + 1);
        double[] bReal = PrimitiveBuffers.allocDoublePow2(b, rC + 1);
        double[] bImg = PrimitiveBuffers.allocDoublePow2(rC + 1);

        double[][] aa = new double[][]{aReal, aImg};
        double[][] bb = new double[][]{bReal, bImg};
        fft(aa, false);
        fft(bb, false);
        double[][] cc = PrimitiveBuffers.replace(dotMul(aa, bb), aa, bb);
        fft(cc, true);

        PrimitiveBuffers.release(cc[1]);
        return cc[0];
    }


    /**
     * c[i]=\sum_{j} a[i+j]*b[j]
     */
    public static double[] deltaConvolution(double[] a, double[] b) {
        int rA = Polynomials.rankOf(a);
        int rB = Polynomials.rankOf(b);
        int rC = rA + rB;

        double[] aReal = PrimitiveBuffers.allocDoublePow2(a, rC + 1);
        double[] aImg = PrimitiveBuffers.allocDoublePow2(rC + 1);
        double[] bReal = PrimitiveBuffers.allocDoublePow2(b, rC + 1);
        double[] bImg = PrimitiveBuffers.allocDoublePow2(rC + 1);

        SequenceUtils.reverse(aReal, 0, rA);
        double[][] aa = new double[][]{aReal, aImg};
        double[][] bb = new double[][]{bReal, bImg};
        fft(aa, false);
        fft(bb, false);
        double[][] c = PrimitiveBuffers.replace(dotMul(aa, bb), aa, bb);
        fft(c, true);
        SequenceUtils.reverse(c[0], 0, rA);
        Arrays.fill(c[0], rA + 1, c[0].length, 0);
        PrimitiveBuffers.release(c[1]);
        return c[0];
    }

    public static double[][] dotMul(double[][] a, double[][] b) {
        int n = a[0].length;
        double[][] ans = PrimitiveBuffers.allocDoublePow2Array(n, 2);
        for (int i = 0; i < n; i++) {
            mul(a[0][i], a[1][i], b[0][i], b[1][i], ans, i);
        }
        return ans;
    }

    public static void dotMulInplace(double[][] a, double[][] b) {
        int n = a[0].length;
        for (int i = 0; i < n; i++) {
            mul(a[0][i], a[1][i], b[0][i], b[1][i], a, i);
        }
    }

    public static void fft(double[][] p, boolean inv) {
        int m = Log2.ceilLog(p[0].length);
        int n = 1 << m;
        int shift = 32 - Integer.numberOfTrailingZeros(n);
        for (int i = 1; i < n; i++) {
            int j = Integer.reverse(i << shift);
            if (i < j) {
                SequenceUtils.swap(p[0], i, j);
                SequenceUtils.swap(p[1], i, j);
            }
        }

        double[][] t = new double[2][1];
        for (int d = 0; d < m; d++) {
            int s = 1 << d;
            int s2 = s << 1;
            prepareLevel(d);
            for (int i = 0; i < n; i += s2) {
                for (int j = 0; j < s; j++) {
                    int a = i + j;
                    int b = a + s;
                    mul(realLevels[d][j], imgLevels[d][j], p[0][b], p[1][b], t, 0);
                    sub(p[0][a], p[1][a], t[0][0], t[1][0], p, b);
                    add(p[0][a], p[1][a], t[0][0], t[1][0], p, a);
                }
            }
        }

        if (inv) {
            for (int i = 0, j = 0; i <= j; i++, j = n - i) {
                double a = p[0][j];
                double b = p[1][j];
                div(p[0][i], p[1][i], n, p, j);
                if (i != j) {
                    div(a, b, n, p, i);
                }
            }
        }
    }

    public static void add(double r1, double i1, double r2, double i2, double[][] r, int i) {
        r[0][i] = r1 + r2;
        r[1][i] = i1 + i2;
    }

    public static void sub(double r1, double i1, double r2, double i2, double[][] r, int i) {
        r[0][i] = r1 - r2;
        r[1][i] = i1 - i2;
    }

    public static void mul(double r1, double i1, double r2, double i2, double[][] r, int i) {
        r[0][i] = r1 * r2 - i1 * i2;
        r[1][i] = r1 * i2 + i1 * r2;
    }

    public static void div(double r1, double i1, double r2, double[][] r, int i) {
        r[0][i] = r1 / r2;
        r[1][i] = i1 / r2;
    }

    /**
     * return a * b % x^m
     *
     * @param a
     * @param b
     * @param m
     * @return
     */
    public static double[] modmul(double[] a, double[] b, int m) {
        double[] ans = convolution(a, b);
        for (int i = m; i < ans.length; i++) {
            ans[i] = 0;
        }
        return Polynomials.normalizeAndReplace(ans);
    }

    public static double[] inverse(double[] p, int m) {
        if (m == 0) {
            return PrimitiveBuffers.allocDoublePow2(1);
        }
        return inverse0(p, m);
    }

    /**
     * find g where g * p = 1 \mod x^m
     * O(m\log_2m)
     *
     * @param p
     * @param m
     * @return
     */
    private static double[] inverse0(double[] p, int m) {
        if (m == 1) {
            double[] ans = PrimitiveBuffers.allocDoublePow2(1);
            ans[0] = 1 / p[0];
            return ans;
        }
        int prevMod = (m + 1) / 2;
        double[] ans = inverse0(p, prevMod);
        int maxRank = (prevMod - 1) * 2 + m - 1;
        double[][] a = new double[][]{
                PrimitiveBuffers.allocDoublePow2(ans, prevMod, maxRank + 1),
                PrimitiveBuffers.allocDoublePow2(maxRank + 1),
        };
        double[][] b = new double[][]{
                PrimitiveBuffers.allocDoublePow2(p, m, maxRank + 1),
                PrimitiveBuffers.allocDoublePow2(maxRank + 1)
        };
        PrimitiveBuffers.release(ans);
        fft(a, false);
        fft(b, false);
        int len = a[0].length;
        for (int i = 0; i < len; i++) {
            mul(a[0][i], a[1][i], b[0][i], b[1][i], b, i);
            mul(a[0][i], a[1][i], b[0][i], b[1][i], b, i);
            sub(a[0][i] * 2, a[1][i] * 2, b[0][i], b[1][i], a, i);
        }
        fft(a, true);
        double[] res = PrimitiveBuffers.allocDoublePow2(a[0], m);
        PrimitiveBuffers.release(a);
        PrimitiveBuffers.release(b);
        return res;
    }

}