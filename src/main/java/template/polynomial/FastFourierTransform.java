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

    public static void dotMulFast(double[][] a, double[][] b) {
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
     * return polynomial g while p * g = 1 (mod x^{2^m}).
     * <br>
     * You are supposed to guarantee the lengths of all arrays are greater than or equal to 2^{m + 1)}
     */
    public static double[][] inverse(double[][] p, int m) {
        if (m == 0) {
            double[][] ans = PrimitiveBuffers.allocDoublePow2Array(1, 2);
            div(1, 0, p[0][0], ans, 0);
            return ans;
        }
        double[][] ans = inverse(p, m - 1);
        int n = 1 << (m + 1);
        ans = PrimitiveBuffers.replace(PrimitiveBuffers.allocDoublePow2Array(ans, n), ans);

        double[][] prefix = PrimitiveBuffers.allocDoublePow2Array(n, 2);
        for (int i = 0, until = 1 << m; i < until; i++) {
            prefix[0][i] = p[0][i];
            prefix[1][i] = p[1][i];
        }
        fft(prefix, false);
        fft(ans, false);
        for (int i = 0; i < n; i++) {
            mul(prefix[0][i], prefix[1][i], ans[0][i], ans[1][i], prefix, i);
            sub(2, 0, prefix[0][i], prefix[1][i], prefix, i);
            mul(ans[0][i], ans[1][i], prefix[0][i], prefix[1][i], ans, i);
        }
        fft(ans, true);
        for (int i = 1 << m; i < n; i++) {
            ans[0][i] = ans[1][i] = 0;
        }

        PrimitiveBuffers.release(prefix);
        return ans;
    }

}