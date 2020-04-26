import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
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
            TaskD solver = new TaskD();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskD {
        Debug debug = new Debug(false);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] A = new int[n];
            int[] B = new int[n];
            for (int i = 0; i < n; i++) {
                A[i] = in.readInt();
            }
            for (int i = 0; i < n; i++) {
                B[i] = in.readInt();
            }
            long ans = 0;
            for (int i = 0; i < 29; i++) {
                int mask = (1 << (i + 1)) - 1;
                CompareUtils.radixSort(A, 0, n - 1, a -> a & mask);
                CompareUtils.radixSort(B, 0, n - 1, b -> b & mask);
                int am = separate(A, 0, n - 1, 1 << i);
                int bm = separate(B, 0, n - 1, 1 << i);

                long cnt = countNotExceed(A, 0, am, B, bm + 1, n - 1, mask >>> 1)
                        + countNotExceed(A, am + 1, n - 1, B, 0, bm, mask >>> 1)
                        + countExceed(A, 0, am, B, 0, bm, mask >>> 1)
                        + countExceed(A, am + 1, n - 1, B, bm + 1, n - 1, mask >>> 1);

                ans = Bits.setBit(ans, i, (cnt % 2) == 1);
                debug.debug("cnt", cnt);
            }

            out.println(ans);
        }

        public int separate(int[] A, int l, int r, int bit) {
            int am = l - 1;
            while (am + 1 <= r && (A[am + 1] & bit) == 0) {
                am++;
            }
            return am;
        }

        public long countExceed(int[] a, int al, int ar, int[] b, int bl, int br, int mask) {
            int l = br + 1;
            long ans = 0;
            for (int i = al; i <= ar; i++) {
                while (l > bl && (b[l - 1] & mask) + (a[i] & mask) > mask) {
                    l--;
                }
                ans += br - l + 1;
            }
            return ans;
        }

        public long countNotExceed(int[] a, int al, int ar, int[] b, int bl, int br, int mask) {
            int l = br;
            long ans = 0;
            for (int i = al; i <= ar; i++) {
                while (l >= bl && (b[l] & mask) + (a[i] & mask) > mask) {
                    l--;
                }
                ans += l - bl + 1;
            }
            return ans;
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

    }

    static class IntegerList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public int[] getData() {
            return data;
        }

        public IntegerList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntegerList(IntegerList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntegerList() {
            this(0);
        }

        public void ensureSpace(int req) {
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntegerList list) {
            addAll(list.data, 0, list.size);
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public void clear() {
            size = 0;
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntegerList)) {
                return false;
            }
            IntegerList other = (IntegerList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Integer.hashCode(data[i]);
            }
            return h;
        }

        public IntegerList clone() {
            IntegerList ans = new IntegerList();
            ans.addAll(this);
            return ans;
        }

    }

    static interface IntToIntFunction {
        int apply(int x);

    }

    static class SequenceUtils {
        public static boolean equal(int[] a, int al, int ar, int[] b, int bl, int br) {
            if ((ar - al) != (br - bl)) {
                return false;
            }
            for (int i = al, j = bl; i <= ar; i++, j++) {
                if (a[i] != b[j]) {
                    return false;
                }
            }
            return true;
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(long c) {
            return append(c).println();
        }

        public FastOutput println() {
            cache.append(System.lineSeparator());
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

    static class Bits {
        private Bits() {
        }

        public static long setBit(long x, int i, boolean v) {
            if (v) {
                x |= 1L << i;
            } else {
                x &= ~(1L << i);
            }
            return x;
        }

    }

    static class CompareUtils {
        private static final int[] BUF8 = new int[1 << 8];
        private static final IntegerList INT_LIST_A = new IntegerList();
        private static final IntegerList INT_LIST_B = new IntegerList();

        private CompareUtils() {
        }

        public static void radixSort(int[] data, int l, int r, IntToIntFunction func) {
            int n = r - l + 1;
            INT_LIST_A.clear();
            INT_LIST_A.addAll(data, l, n);
            INT_LIST_B.clear();
            INT_LIST_B.ensureSpace(n);

            for (int i = 0; i < 4; i += 2) {
                radixSort0(n, INT_LIST_A.getData(), INT_LIST_B.getData(), BUF8, i * 8, func);
                radixSort0(n, INT_LIST_B.getData(), INT_LIST_A.getData(), BUF8, (i + 1) * 8, func);
            }
            System.arraycopy(INT_LIST_A.getData(), 0, data, l, r - l + 1);
        }

        private static void radixSort0(int n, int[] data, int[] output, int[] buf, int rightShift, IntToIntFunction func) {
            Arrays.fill(buf, 0);
            int mask = buf.length - 1;
            for (int i = 0; i < n; i++) {
                buf[(int) ((func.apply(data[i]) >>> rightShift) & mask)]++;
            }
            for (int i = 1; i < buf.length; i++) {
                buf[i] += buf[i - 1];
            }
            for (int i = n - 1; i >= 0; i--) {
                output[--buf[(int) ((func.apply(data[i]) >>> rightShift) & mask)]] = data[i];
            }
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, long x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
        }

    }
}

