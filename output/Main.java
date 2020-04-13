import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 27);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            GSubstringSearch solver = new GSubstringSearch();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class GSubstringSearch {
        public long pow(int x, int n) {
            return n == 0 ? 1 : x * pow(x, n - 1);
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int[] perm = new int[26];
            for (int i = 0; i < 26; i++) {
                perm[i] = in.readInt() - 1;
            }
            int[] a = new int[(int) 2e5];
            int[] s = new int[(int) 2e5];
            int n = in.readString(a, 0);
            int m = in.readString(s, 0);

            for (int i = 0; i < n; i++) {
                a[i] -= 'a';
            }
            for (int i = 0; i < m; i++) {
                s[i] -= 'a';
            }

            SequenceUtils.reverse(s, 0, m - 1);
            int[] b = new int[n];
            for (int i = 0; i < n; i++) {
                b[i] = perm[a[i]];
            }

            int proper = Log2.ceilLog(n + m - 1);
            double[][][] s3 = new double[3][2][1 << proper];
            double[][][] right = new double[3][2][1 << proper];
            for (int i = 0; i < n; i++) {
                right[0][0][i] = -2 * a[i] * b[i] * (a[i] + b[i]);
                right[1][0][i] = pow(a[i] + b[i], 2) + 2 * a[i] * b[i];
                right[2][0][i] = -2 * (a[i] + b[i]);
            }
            for (int i = 0; i < m; i++) {
                s3[0][0][i] = s[i];
                for (int j = 1; j < 3; j++) {
                    s3[j][0][i] = s3[j - 1][0][i] * s[i];
                }
            }

            for (int i = 0; i < 3; i++) {
                FastFourierTransform.dft(s3[i], proper);
                FastFourierTransform.dft(right[i], proper);
                FastFourierTransform.dotMul(s3[i], right[i], s3[i], proper);
            }

            for (int i = 0; i < (1 << proper); i++) {
                for (int j = 0; j < 2; j++) {
                    s3[0][j][i] = s3[0][j][i] + s3[1][j][i] + s3[2][j][i];
                }
            }

            FastFourierTransform.idft(s3[0], proper);
            SequenceUtils.reverse(s3[0][0], 0, m - 1);
            SequenceUtils.reverse(s3[0][1], 0, m - 1);
            SequenceUtils.reverse(s, 0, m - 1);

            long[] s4 = new long[m];
            int l = 0;
            int r = -1;
            long sum = 0;
            for (int i = 0; i + n - 1 < m; i++) {
                while (r - i + 1 < n) {
                    r++;
                    sum += s[r] * s[r] * s[r] * s[r];
                }
                while (l < i) {
                    sum -= s[l] * s[l] * s[l] * s[l];
                    l++;
                }
                s4[i] = sum;
            }

            long fix = 0;
            for (int i = 0; i < n; i++) {
                fix += a[i] * a[i] * b[i] * b[i];
            }

            boolean[] ans = new boolean[m];
            for (int i = 0; i + n - 1 < m; i++) {
                double sub = s4[i] + fix + s3[0][0][i];
                ans[i] = DigitUtils.round(sub) == 0;
            }

            for (int i = 0; i + n - 1 < m; i++) {
                if (ans[i]) {
                    out.append(1);
                } else {
                    out.append(0);
                }
            }
        }

    }

    static class FastFourierTransform {
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

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static long round(double x) {
            if (x >= 0) {
                return (long) (x + 0.5);
            } else {
                return (long) (x - 0.5);
            }
        }

    }

    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 20];
        private int bufLen;
        private int bufOffset;
        private int next;

        public FastInput(InputStream is) {
            this.is = is;
        }

        private int read() {
            while (bufLen == bufOffset) {
                bufOffset = 0;
                try {
                    bufLen = is.read(buf);
                } catch (IOException e) {
                    bufLen = -1;
                }
                if (bufLen == -1) {
                    return -1;
                }
            }
            return buf[bufOffset++];
        }

        public void skipBlank() {
            while (next >= 0 && next <= 32) {
                next = read();
            }
        }

        public int readInt() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            int val = 0;
            if (sign == 1) {
                while (next >= '0' && next <= '9') {
                    val = val * 10 + next - '0';
                    next = read();
                }
            } else {
                while (next >= '0' && next <= '9') {
                    val = val * 10 - next + '0';
                    next = read();
                }
            }

            return val;
        }

        public int readString(int[] data, int offset) {
            skipBlank();

            int originalOffset = offset;
            while (next > 32) {
                data[offset++] = (char) next;
                next = read();
            }

            return offset - originalOffset;
        }

    }

    static class Log2 {
        public static int ceilLog(int x) {
            return 32 - Integer.numberOfLeadingZeros(x - 1);
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            return this;
        }

        public FastOutput flush() {
            try {
                os.append(cache);
                os.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                os.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }

    static class SequenceUtils {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static void swap(double[] data, int i, int j) {
            double tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static void reverse(int[] data, int l, int r) {
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
        }

        public static void reverse(double[] data, int l, int r) {
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
        }

    }
}

