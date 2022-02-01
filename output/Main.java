import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.util.ArrayDeque;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 29);
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
            Task solver = new Task();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Task {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.ri();
            int m = in.ri();
            int[] a = in.ri(n);
            int[] b = in.ri(m);
            int[] conv = new IntPolyFFT(998244353).convolution(a, b);
        }

    }

    static interface InverseNumber {
    }

    static class IntPolyFFT extends IntPoly {
        private static final int FFT_THRESHOLD = 50;

        public IntPolyFFT(int mod) {
            super(mod);
        }

        public int[] convolution(int[] a, int[] b) {
            if (a != b) {
                return multiplyMod(a, b);
            } else {
                return pow2(a);
            }
        }

        public int[] pow2(int[] a) {
            int rA = rankOf(a);
            if (rA < FFT_THRESHOLD) {
                return mulBF(a, a);
            }

            int need = rA * 2 + 1;

            double[] aReal = PrimitiveBuffers.allocDoublePow2(need);
            double[] aImag = PrimitiveBuffers.allocDoublePow2(need);
            int n = aReal.length;

            for (int i = 0; i <= rA; i++) {
                int x = DigitUtils.mod(a[i], mod);
                aReal[i] = x & ((1 << 15) - 1);
                aImag[i] = x >> 15;
            }
            FastFourierTransform.fft(new double[][]{aReal, aImag}, false);

            double[] bReal = PrimitiveBuffers.allocDoublePow2(aReal, aReal.length);
            double[] bImag = PrimitiveBuffers.allocDoublePow2(aImag, bReal.length);


            for (int i = 0, j = 0; i <= j; i++, j = n - i) {
                double ari = aReal[i];
                double aii = aImag[i];
                double bri = bReal[i];
                double bii = bImag[i];
                double arj = aReal[j];
                double aij = aImag[j];
                double brj = bReal[j];
                double bij = bImag[j];

                double a1r = (ari + arj) / 2;
                double a1i = (aii - aij) / 2;
                double a2r = (aii + aij) / 2;
                double a2i = (arj - ari) / 2;

                double b1r = (bri + brj) / 2;
                double b1i = (bii - bij) / 2;
                double b2r = (bii + bij) / 2;
                double b2i = (brj - bri) / 2;

                aReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                aImag[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                bReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                bImag[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;

                if (i != j) {
                    a1r = (arj + ari) / 2;
                    a1i = (aij - aii) / 2;
                    a2r = (aij + aii) / 2;
                    a2i = (ari - arj) / 2;

                    b1r = (brj + bri) / 2;
                    b1i = (bij - bii) / 2;
                    b2r = (bij + bii) / 2;
                    b2i = (bri - brj) / 2;

                    aReal[j] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                    aImag[j] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                    bReal[j] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                    bImag[j] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;
                }
            }

            FastFourierTransform.fft(new double[][]{aReal, aImag}, true);
            FastFourierTransform.fft(new double[][]{bReal, bImag}, true);

            int[] ans = PrimitiveBuffers.allocIntPow2(need);
            for (int i = 0; i < need; i++) {
                long aa = DigitUtils.mod(Math.round(aReal[i]), mod);
                long bb = DigitUtils.mod(Math.round(bReal[i]), mod);
                long cc = DigitUtils.mod(Math.round(aImag[i]), mod);
                ans[i] = DigitUtils.mod(aa + (bb << 15) + (cc << 30), mod);
            }

            PrimitiveBuffers.release(aReal, bReal, aImag, bImag);
            return ans;
        }

        private void to_string(String id, double[] a, double[] b) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < a.length; i++) {
                list.add(String.format("(%.1f,%.1f)", a[i], b[i]));
            }
            String res = id + "\n\n" + list.stream().collect(Collectors.joining(" ")) + "\n";
            System.out.println(res);
        }

        private int[] multiplyMod(int[] a, int[] b) {
            int rA = rankOf(a);
            int rB = rankOf(b);
            if (Math.min(rA, rB) < FFT_THRESHOLD) {
                return mulBF(a, b);
            }

            int need = rA + rB + 1;

            double[] aReal = PrimitiveBuffers.allocDoublePow2(need);
            double[] aImag = PrimitiveBuffers.allocDoublePow2(need);
            int n = aReal.length;

            for (int i = 0; i <= rA; i++) {
                int x = DigitUtils.mod(a[i], mod);
                aReal[i] = x & ((1 << 15) - 1);
                aImag[i] = x >> 15;
            }
            FastFourierTransform.fft(new double[][]{aReal, aImag}, false);

            double[] bReal = PrimitiveBuffers.allocDoublePow2(need);
            double[] bImag = PrimitiveBuffers.allocDoublePow2(need);
            for (int i = 0; i <= rB; i++) {
                int x = DigitUtils.mod(b[i], mod);
                bReal[i] = x & ((1 << 15) - 1);
                bImag[i] = x >> 15;

//            System.out.printf("%d %.1f %.1f\n", x, bReal[i], bImag[i]);
            }
//        to_string("", bReal, bImag);
            FastFourierTransform.fft(new double[][]{bReal, bImag}, false);
            to_string("一", aReal, aImag);
            to_string("二", bReal, bImag);

            for (int i = 0, j = 0; i <= j; i++, j = n - i) {
                double ari = aReal[i];
                double aii = aImag[i];
                double bri = bReal[i];
                double bii = bImag[i];
                double arj = aReal[j];
                double aij = aImag[j];
                double brj = bReal[j];
                double bij = bImag[j];

                double a1r = (ari + arj) / 2;
                double a1i = (aii - aij) / 2;
                double a2r = (aii + aij) / 2;
                double a2i = (arj - ari) / 2;

                double b1r = (bri + brj) / 2;
                double b1i = (bii - bij) / 2;
                double b2r = (bii + bij) / 2;
                double b2i = (brj - bri) / 2;

                aReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                aImag[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                bReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                bImag[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;

                if (i != j) {
                    a1r = (arj + ari) / 2;
                    a1i = (aij - aii) / 2;
                    a2r = (aij + aii) / 2;
                    a2i = (ari - arj) / 2;

                    b1r = (brj + bri) / 2;
                    b1i = (bij - bii) / 2;
                    b2r = (bij + bii) / 2;
                    b2i = (bri - brj) / 2;

                    aReal[j] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r;
                    aImag[j] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i;
                    bReal[j] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i;
                    bImag[j] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r;
                }
            }

            FastFourierTransform.fft(new double[][]{aReal, aImag}, true);
            FastFourierTransform.fft(new double[][]{bReal, bImag}, true);
            to_string("三", aReal, aImag);
            to_string("四", bReal, bImag);
            int[] ans = PrimitiveBuffers.allocIntPow2(need);
            for (int i = 0; i < need; i++) {
                long aa = DigitUtils.mod(Math.round(aReal[i]), mod);
                long bb = DigitUtils.mod(Math.round(bReal[i]), mod);
                long cc = DigitUtils.mod(Math.round(aImag[i]), mod);
                System.out.printf("%d %d %d\n", aa, bb, cc);
                ans[i] = DigitUtils.mod(aa + (bb << 15) + (cc << 30), mod);
            }

            PrimitiveBuffers.release(aReal, bReal, aImag, bImag);
            return ans;
        }

    }

    static class Power implements InverseNumber {
        int mod;

        public Power(int mod) {
            this.mod = mod;
        }

    }

    static class Log2 {
        public static int ceilLog(int x) {
            if (x <= 0) {
                return 0;
            }
            return 32 - Integer.numberOfLeadingZeros(x - 1);
        }

    }

    static class Buffer<T> {
        private Deque<T> deque;
        private Supplier<T> supplier;
        private Consumer<T> cleaner;
        private int allocTime;
        private int releaseTime;

        public Buffer(Supplier<T> supplier) {
            this(supplier, (x) -> {
            });
        }

        public Buffer(Supplier<T> supplier, Consumer<T> cleaner) {
            this(supplier, cleaner, 0);
        }

        public Buffer(Supplier<T> supplier, Consumer<T> cleaner, int exp) {
            this.supplier = supplier;
            this.cleaner = cleaner;
            deque = new ArrayDeque<>(exp);
        }

        public T alloc() {
            allocTime++;
            T res;
            if (deque.isEmpty()) {
                res = supplier.get();
                cleaner.accept(res);
            } else {
                res = deque.removeFirst();
            }
            return res;
        }

        public void release(T e) {
            if (e == null) {
                return;
            }
            releaseTime++;
            cleaner.accept(e);
            deque.addLast(e);
        }

    }

    static class IntPoly {
        protected int mod;
        protected Power power;
        QuadraticResidue qr;

        public IntPoly(int mod) {
            this.mod = mod;
            this.power = new Power(mod);
            qr = new QuadraticResidue(mod);
        }

        public int rankOf(int[] p) {
            int r = p.length - 1;
            while (r >= 0 && p[r] == 0) {
                r--;
            }
            return Math.max(0, r);
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

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 32 << 10;
        private OutputStream writer;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);
        private static Field stringBuilderValueField;
        private char[] charBuf = new char[THRESHOLD * 2];
        private byte[] byteBuf = new byte[THRESHOLD * 2];

        static {
            try {
                stringBuilderValueField = StringBuilder.class.getSuperclass().getDeclaredField("value");
                stringBuilderValueField.setAccessible(true);
            } catch (Exception e) {
                stringBuilderValueField = null;
            }
            stringBuilderValueField = null;
        }

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        private void afterWrite() {
            if (cache.length() < THRESHOLD) {
                return;
            }
            flush();
        }

        public FastOutput(OutputStream writer) {
            this.writer = writer;
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput flush() {
            try {
                if (stringBuilderValueField != null) {
                    try {
                        byte[] value = (byte[]) stringBuilderValueField.get(cache);
                        writer.write(value, 0, cache.length());
                    } catch (Exception e) {
                        stringBuilderValueField = null;
                    }
                }
                if (stringBuilderValueField == null) {
                    int n = cache.length();
                    if (n > byteBuf.length) {
                        //slow
                        writer.write(cache.toString().getBytes(StandardCharsets.UTF_8));
//                writer.append(cache);
                    } else {
                        cache.getChars(0, n, charBuf, 0);
                        for (int i = 0; i < n; i++) {
                            byteBuf[i] = (byte) charBuf[i];
                        }
                        writer.write(byteBuf, 0, n);
                    }
                }
                writer.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                writer.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }

    static class QuadraticResidue {
        final int mod;
        Power power;

        public QuadraticResidue(int mod) {
            this.mod = mod;
            power = new Power(mod);
        }

    }

    static class SequenceUtils {
        public static void swap(double[] data, int i, int j) {
            double tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
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

        public void populate(int[] data) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readInt();
            }
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

        public int ri() {
            return readInt();
        }

        public int[] ri(int n) {
            int[] ans = new int[n];
            populate(ans);
            return ans;
        }

        public int readInt() {
            boolean rev = false;

            skipBlank();
            if (next == '+' || next == '-') {
                rev = next == '-';
                next = read();
            }

            int val = 0;
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }

            return rev ? val : -val;
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int mod(long x, int mod) {
            if (x < -mod || x >= mod) {
                x %= mod;
            }
            if (x < 0) {
                x += mod;
            }
            return (int) x;
        }

        public static int mod(int x, int mod) {
            if (x < -mod || x >= mod) {
                x %= mod;
            }
            if (x < 0) {
                x += mod;
            }
            return x;
        }

    }

    static class PrimitiveBuffers {
        public static Buffer<int[]>[] intPow2Bufs = new Buffer[30];
        public static Buffer<double[]>[] doublePow2Bufs = new Buffer[30];

        static {
            for (int i = 0; i < 30; i++) {
                int finalI = i;
                intPow2Bufs[i] = new Buffer<>(() -> new int[1 << finalI], x -> Arrays.fill(x, 0));
                doublePow2Bufs[i] = new Buffer<>(() -> new double[1 << finalI], x -> Arrays.fill(x, 0));
            }
        }

        public static int[] allocIntPow2(int n) {
            return intPow2Bufs[Log2.ceilLog(n)].alloc();
        }

        public static double[] allocDoublePow2(int n) {
            return doublePow2Bufs[Log2.ceilLog(n)].alloc();
        }

        public static double[] allocDoublePow2(double[] data, int newLen) {
            double[] ans = allocDoublePow2(newLen);
            System.arraycopy(data, 0, ans, 0, Math.min(data.length, newLen));
            return ans;
        }

        public static void release(double[] data) {
            assert data.length == Integer.lowestOneBit(data.length);
            doublePow2Bufs[Log2.ceilLog(data.length)].release(data);
        }

        public static void release(double[]... data) {
            for (double[] x : data) {
                release(x);
            }
        }

    }
}

