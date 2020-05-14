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
            CEverHungryKrakozyabra solver = new CEverHungryKrakozyabra();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CEverHungryKrakozyabra {
        Radix radix = new Radix(10);
        Debug debug = new Debug(true);
        int[] lBits;
        int[] rBits;
        LongList all = new LongList(5000000);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            long l = in.readLong();
            long r = in.readLong();
            lBits = radix.decompose(l);
            rBits = radix.decompose(r);

            dfs(1, 0, 0);

            int ans = 0;
            int[] cnt = new int[10];
            debug.debug("m", all.size());
            //all.unique();
            for (int i = all.size() - 1; i >= 0; i--) {
                long val = all.get(i);
                int sum = 0;

                if (val >= l && val <= r) {
                    ans++;
                    continue;
                }

                while (val > 0) {
                    cnt[(int) (val % 10)]++;
                    val /= 10;
                    sum++;
                }
                cnt[0] = (int) 1e9;
                if (test(cnt, sum, 18, true, true)) {
                    ans++;
                    // debug.debug("val", all.get(i));
                } else {
                    Arrays.fill(cnt, 0);
                }
            }

            out.println(ans);
        }

        public boolean test(int[] cnts, int sum, int i, boolean ceil, boolean floor) {
            if (sum > i + 1) {
                return false;
            }
            if (i < 0 || !ceil && !floor) {
                return true;
            }

            int l = lBits[i];
            int r = rBits[i];
            int start = floor ? l : 0;
            int end = ceil ? r : 9;
            for (int j = start; j <= end; j++) {
                if (cnts[j] == 0) {
                    continue;
                }
                cnts[j]--;
                if (test(cnts, sum - (j > 0 ? 1 : 0), i - 1, ceil && j == r, floor && j == l)) {
                    return true;
                }
                cnts[j]++;
            }

            return false;
        }

        public void dfs(int val, int cnt, long built) {
            if (val == 10) {
                all.add(built);
                return;
            }
            for (; cnt <= 18; cnt++, built = built * 10 + val) {
                dfs(val + 1, cnt, built);
            }
        }

    }

    static class LongList implements LongRandomAccess, Cloneable {
        private int size;
        private int cap;
        private long[] data;
        private static final long[] EMPTY = new long[0];

        public LongList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new long[cap];
            }
        }

        public LongList(LongList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public LongList() {
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

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public long get(int i) {
            checkRange(i);
            return data[i];
        }

        public void add(long x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public void addAll(long[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(LongList list) {
            addAll(list.data, 0, list.size);
        }

        public long tail() {
            checkRange(0);
            return data[size - 1];
        }

        public int size() {
            return size;
        }

        public long[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof LongList)) {
                return false;
            }
            LongList other = (LongList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Long.hashCode(data[i]);
            }
            return h;
        }

        public LongList clone() {
            LongList ans = new LongList();
            ans.addAll(this);
            return ans;
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

    }

    static class Radix {
        private long[] pow;
        private long base;

        public Radix(long base) {
            if (base <= 1) {
                throw new IllegalArgumentException();
            }
            this.base = base;
            LongList ll = new LongList(64);
            ll.add(1);
            while (!DigitUtils.isMultiplicationOverflow(ll.tail(), base, Long.MAX_VALUE)) {
                ll.add(ll.tail() * base);
            }
            pow = ll.toArray();
        }

        public int bitCount() {
            return pow.length;
        }

        public int[] decompose(long x) {
            return decompose(x, new int[bitCount()]);
        }

        public int[] decompose(long x, int[] ans) {
            for (int i = 0; i < ans.length; i++) {
                ans[i] = (int) (x % base);
                x /= base;
            }
            return ans;
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

        public FastOutput println(int c) {
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

    static interface LongRandomAccess {
    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static boolean isMultiplicationOverflow(long a, long b, long limit) {
            if (limit < 0) {
                limit = -limit;
            }
            if (a < 0) {
                a = -a;
            }
            if (b < 0) {
                b = -b;
            }
            if (a == 0 || b == 0) {
                return false;
            }
            //a * b > limit => a > limit / b
            return a > limit / b;
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, int x) {
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

    static class SequenceUtils {
        public static boolean equal(long[] a, int al, int ar, long[] b, int bl, int br) {
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
}

