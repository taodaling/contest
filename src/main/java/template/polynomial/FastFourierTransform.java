package template.polynomial;

import template.binary.CachedLog2;
import template.utils.SequenceUtils;

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

    public static void dotMul(double[][] a, double[][] b, double[][] c, int m) {
        for (int i = 0, n = 1 << m; i < n; i++) {
            mul(a[0][i], a[1][i], b[0][i], b[1][i], c, i);
        }
    }

    public static void dft(double[][] p, int m) {
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
    }

    public static void idft(double[][] p, int m) {
        dft(p, m);

        int n = 1 << m;
        div(p[0][0], p[1][0], n, p, 0);
        div(p[0][n / 2], p[1][n / 2], n, p, n / 2);
        for (int i = 1, until = n / 2; i < until; i++) {
            double a = p[0][n - i];
            double b = p[1][n - i];
            div(p[0][i], p[1][i], n, p, n - i);
            div(a, b, n, p, i);
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

    public static int rankOf(double[][] p, int m) {
        for (int i = (1 << m) - 1; i >= 1; i--) {
            if (p[0][i] < -eps || p[0][i] > eps) {
                return i;
            }
        }
        return 0;
    }

    /**
     * return polynomial g while p * g = 1 (mod x^m).
     * <br>
     * You are supposed to guarantee the lengths of all arrays are greater than or equal to 2^{ceil(log2(m)) + 1}
     */
    private static void inverse(double[][] p, double[][] inv, double[][] buf, int m) {
        if (m == 0) {
            div(1, 0, p[0][0], inv, 0);
            return;
        }
        inverse(p, inv, buf, m - 1);
        int n = 1 << (m + 1);
        for (int i = 0, until = 1 << m; i < until; i++) {
            buf[i][0] = p[i][0];
            buf[i][1] = p[i][1];
        }
        for (int i = 1 << m, until = 1 << (m + 1); i < until; i++) {
            buf[i][0] = 0;
            buf[i][1] = 0;
        }
        dft(buf, (m + 1));
        dft(inv, (m + 1));
        for (int i = 0; i < n; i++) {
            mul(buf[0][i], buf[1][i], inv[0][i], inv[1][i], buf, i);
            sub(2, 0, buf[0][i], buf[1][i], buf, i);
            mul(inv[i][0], inv[1][i], buf[0][i], buf[1][i], inv, i);
        }
        idft(inv, m + 1);
        for (int i = 1 << m; i < n; i++) {
            inv[0][i] = inv[1][i] = 0;
        }
    }
    public static int[] multiplyMod(int[] a, int[] b, int m) {
        return multiplyMod(a, a.length, b, b.length, m);
    }
    public static int[] multiplyMod(int[] a, int aLen, int[] b, int bLen, int m) {
        int need = aLen + bLen - 1;
        int n = 1 << CachedLog2.ceilLog(need);

        double[] aReal = new double[n];
        double[] aImag = new double[n];
        for (int i = 0; i < aLen; i++) {
            int x = (a[i] % m + m) % m;
            aReal[i] = x & ((1 << 15) - 1);
            aImag[i] = x >> 15;
        }
        dft(new double[][]{aReal, aImag}, CachedLog2.floorLog(n));

        double[] bReal = new double[n];
        double[] bImag = new double[n];
        for (int i = 0; i < bLen; i++) {
            int x = (b[i] % m + m) % m;
            bReal[i] = x & ((1 << 15) - 1);
            bImag[i] = x >> 15;
        }
        dft(new double[][]{bReal, bImag}, CachedLog2.floorLog(n));

        double[] faReal = new double[n];
        double[] faImag = new double[n];
        double[] fbReal = new double[n];
        double[] fbImag = new double[n];

        for (int i = 0; i < n; i++) {
            int j = (n - i) & (n - 1);

            double a1r = (aReal[i] + aReal[j]) / 2;
            double a1i = (aImag[i] - aImag[j]) / 2;
            double a2r = (aImag[i] + aImag[j]) / 2;
            double a2i = (aReal[j] - aReal[i]) / 2;

            double b1r = (bReal[i] + bReal[j]) / 2;
            double b1i = (bImag[i] - bImag[j]) / 2;
            double b2r = (bImag[i] + bImag[j]) / 2;
            double b2i = (bReal[j] - bReal[i]) / 2;

            faReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
            faImag[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;

            fbReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
            fbImag[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;
        }

        idft(new double[][]{faReal, faImag}, CachedLog2.floorLog(n));
        idft(new double[][]{fbReal, fbImag}, CachedLog2.floorLog(n));
        int[] res = new int[need];
        for (int i = 0; i < need; i++) {
            long aa = (long) (faReal[i] + 0.5);
            long bb = (long) (fbReal[i] + 0.5);
            long cc = (long) (faImag[i] + 0.5);
            res[i] = (int) ((aa % m + (bb % m << 15) + (cc % m << 30)) % m);
        }
        return res;
    }
}