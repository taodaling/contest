import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            HHintsOfSd0061 solver = new HHintsOfSd0061();
            try {
                int testNumber = 1;
                while (true)
                    solver.solve(testNumber++, in, out);
            } catch (UnknownError e) {
                out.close();
            }
        }
    }

    static class HHintsOfSd0061 {
        int[] ratings = new int[10000000];
        int x;
        int y;
        int z;

        {
            CompareUtils.ensureIntSpace(10000000);
        }

        int rng61() {
            int t;
            x = x ^ (x << 16);
            x = x ^ (x >>> 5);
            x = x ^ (x << 1);
            t = x;
            x = y;
            y = z;
            z = (t ^ x) ^ y;
            return z;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            if (!in.hasMore()) {
                throw new UnknownError();
            }
            out.printf("Case #%d: ", testNumber);
            int n = in.readInt();
            int m = in.readInt();
            x = (int) in.readLong();
            y = (int) in.readLong();
            z = (int) in.readLong();

            for (int i = 0; i < n; i++) {
                ratings[i] = rng61();
            }
            CompareUtils.radixSort(ratings, 0, n - 1);
            for (int i = 0; i < m; i++) {
                int x = in.readInt();
                long val = (long) ratings[x] & ((1L << 32) - 1);
                out.append(val).append(' ');
            }
            out.println();
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

        public long readLong() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            long val = 0;
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

        public boolean hasMore() {
            skipBlank();
            return next != -1;
        }

    }

    static class CompareUtils {
        private static final int[] BUF8 = new int[1 << 8];
        private static final IntegerList INT_LIST_A = new IntegerList();

        private CompareUtils() {
        }

        public static void ensureIntSpace(int n) {
            INT_LIST_A.ensureSpace(n);
        }

        public static void radixSort(int[] data, int l, int r) {
            INT_LIST_A.clear();
            INT_LIST_A.ensureSpace(r - l + 1);

            int n = r - l + 1;
            for (int i = 0; i < 8; i += 2) {
                radixSort0(data, l, r, INT_LIST_A.getData(), 0, BUF8, i * 8);
                radixSort0(INT_LIST_A.getData(), 0, n - 1, data, l, BUF8, (i + 1) * 8);
            }
        }

        private static void radixSort0(int[] data, int dl, int dr, int[] output, int ol, int[] buf, int rightShift) {
            Arrays.fill(buf, 0);
            int mask = buf.length - 1;
            for (int i = dl; i <= dr; i++) {
                buf[(int) ((data[i] >>> rightShift) & mask)]++;
            }
            for (int i = 1; i < buf.length; i++) {
                buf[i] += buf[i - 1];
            }
            for (int i = dr; i >= dl; i--) {
                output[ol + (--buf[(int) ((data[i] >>> rightShift) & mask)])] = data[i];
            }
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(1 << 20);
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

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
            return this;
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
}

