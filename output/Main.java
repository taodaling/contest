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
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            LUOGU4245 solver = new LUOGU4245();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class LUOGU4245 {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int p = in.readInt();

            int[] a = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                a[i] = in.readInt();
            }
            int[] b = new int[m + 1];
            for (int i = 0; i <= m; i++) {
                b[i] = in.readInt();
            }
            int[] res = FFT.multiplyMod(a, b, p);
            for (int i = 0; i < res.length; i++) {
                out.append(res[i]).append(' ');
            }
        }

    }

    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

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

    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 13];
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

    }

    static class FFT {
        public static void fft(double[] a, double[] b, boolean inverse) {
            int n = a.length;
            int shift = 32 - Integer.numberOfTrailingZeros(n);
            for (int i = 1; i < n; i++) {
                int j = Integer.reverse(i << shift);
                if (i < j) {
                    double temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                    temp = b[i];
                    b[i] = b[j];
                    b[j] = temp;
                }
            }
            for (int len = 2; len <= n; len <<= 1) {
                int halfLen = len >> 1;
                double[] cs = new double[halfLen];
                double[] sn = new double[halfLen];
                for (int i = 0; i < halfLen; i++) {
                    double angle = 2 * Math.PI * i / len * (inverse ? -1 : 1);
                    cs[i] = Math.cos(angle);
                    sn[i] = Math.sin(angle);
                }
                for (int i = 0; i < n; i += len) {
                    for (int j = 0; j < halfLen; j++) {
                        double uA = a[i + j];
                        double uB = b[i + j];
                        double vA = a[i + j + halfLen] * cs[j] - b[i + j + halfLen] * sn[j];
                        double vB = a[i + j + halfLen] * sn[j] + b[i + j + halfLen] * cs[j];
                        a[i + j] = uA + vA;
                        b[i + j] = uB + vB;
                        a[i + j + halfLen] = uA - vA;
                        b[i + j + halfLen] = uB - vB;
                    }
                }
            }
            if (inverse) {
                for (int i = 0; i < n; i++) {
                    a[i] /= n;
                    b[i] /= n;
                }
            }
        }

        public static int[] multiplyMod(int[] a, int[] b, int m) {
            int need = a.length + b.length - 1;
            int n = Math.max(1, Integer.highestOneBit(need - 1) << 1);

            double[] aReal = new double[n];
            double[] aImag = new double[n];
            for (int i = 0; i < a.length; i++) {
                int x = (a[i] % m + m) % m;
                aReal[i] = x & ((1 << 15) - 1);
                aImag[i] = x >> 15;
            }
            fft(aReal, aImag, false);

            double[] bReal = new double[n];
            double[] bImag = new double[n];
            for (int i = 0; i < b.length; i++) {
                int x = (b[i] % m + m) % m;
                bReal[i] = x & ((1 << 15) - 1);
                bImag[i] = x >> 15;
            }
            fft(bReal, bImag, false);

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

            fft(faReal, faImag, true);
            fft(fbReal, fbImag, true);
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
}

