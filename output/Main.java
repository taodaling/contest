import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
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
            E1ChioriAndDollPickingEasyVersion solver = new E1ChioriAndDollPickingEasyVersion();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class E1ChioriAndDollPickingEasyVersion {
        Debug debug = new Debug(true);
        Modular mod = new Modular(998244353);
        int[] cnt;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            long[] a = new long[n];
            LinearBasis lb = new LinearBasis();
            LongList list = new LongList();
            for (int i = 0; i < n; i++) {
                a[i] = in.readLong();

                if (lb.add(a[i])) {
                    list.add(a[i]);
                }
            }

            cnt = new int[m + 1];
            long[] set = lb.toArray();

            debug.debug("set", set);
            for (int i = 0; i < set.length; i++) {
                int exp = m - i + 1;
                if (Bits.bitAt(set[i], exp) == 0) {
                    int index = -1;
                    for (int j = m - 1; j >= 0; j--) {
                        if (Bits.bitAt(set[i], j) == 1) {
                            index = j;
                            break;
                        }
                    }
                    for (int j = 0; j < set.length; j++) {
                        set[j] = Bits.swapBit(set[j], exp, index);
                    }
                }
            }
            for (long x : set) {
                debug.debug("e", Long.toString(x, 2));
            }
            if (set.length <= 2) {
                dfs(set, 0, set.length - 1);
            } else {
                int[][][] dp = new int[set.length + 1][set.length + 1][1 << (m - set.length)];
                dp[0][0][0] = 1;
                int mask = dp[0][0].length - 1;
                for (int i = 1; i <= set.length; i++) {
                    long val = set[i - 1];
                    int tail = (int) (val & mask);
                    for (int j = 0; j <= set.length; j++) {
                        for (int k = 0; k <= mask; k++) {
                            dp[i][j][k] = dp[i - 1][j][k];
                            if (j > 0) {
                                dp[i][j][k] = mod.plus(dp[i][j][k], dp[i - 1][j - 1][k ^ tail]);
                            }
                        }
                    }
                }

                for (int i = 0; i <= set.length; i++) {
                    for (int j = 0; j <= mask; j++) {
                        int index = i + Integer.bitCount(j);
                        if (index <= m) {
                            cnt[index] = mod.plus(cnt[index], dp[set.length][i][j]);
                        }
                    }
                }
            }

            Power power = new Power(mod);
            int factor = power.pow(2, n - set.length);
            for (int i = 0; i <= m; i++) {
                out.append(mod.mul(factor, cnt[i])).append(' ');
            }

        }

        public void dfs(long[] set, long xor, int i) {
            if (i < 0) {
                cnt[Long.bitCount(xor)]++;
                return;
            }
            dfs(set, xor, i - 1);
            dfs(set, xor ^ set[i], i - 1);
        }

    }

    static class LinearBasis implements Cloneable {
        private long[] map = new long[64];
        private int size;

        public long[] toArray() {
            long[] ans = new long[size];
            int tail = 0;
            for (int i = 63; i >= 0; i--) {
                if (map[i] != 0) {
                    ans[tail++] = map[i];
                }
            }
            return ans;
        }

        private void afterAddBit(int bit) {
            for (int i = 63; i >= 0; i--) {
                if (i == bit || map[i] == 0) {
                    continue;
                }
                if (bitAt(map[i], bit) == 1) {
                    map[i] ^= map[bit];
                }
            }
        }

        public boolean add(long val) {
            for (int i = 63; i >= 0 && val != 0; i--) {
                if (bitAt(val, i) == 0) {
                    continue;
                }
                val ^= map[i];
            }
            if (val != 0) {
                int log = 63 - Long.numberOfLeadingZeros(val);
                map[log] = val;
                size++;
                afterAddBit(log);
                return true;
            }
            return false;
        }

        private long bitAt(long val, int i) {
            return (val >>> i) & 1;
        }

        public LinearBasis clone() {
            try {
                LinearBasis ans = (LinearBasis) super.clone();
                ans.map = ans.map.clone();
                return ans;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
        }

        public Debug debug(String name, Object x) {
            return debug(name, x, empty);
        }

        public Debug debug(String name, Object x, int... indexes) {
            if (offline) {
                if (x == null || !x.getClass().isArray()) {
                    out.append(name);
                    for (int i : indexes) {
                        out.printf("[%d]", i);
                    }
                    out.append("=").append("" + x);
                    out.println();
                } else {
                    indexes = Arrays.copyOf(indexes, indexes.length + 1);
                    if (x instanceof byte[]) {
                        byte[] arr = (byte[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof short[]) {
                        short[] arr = (short[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof boolean[]) {
                        boolean[] arr = (boolean[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof char[]) {
                        char[] arr = (char[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof int[]) {
                        int[] arr = (int[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof float[]) {
                        float[] arr = (float[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof double[]) {
                        double[] arr = (double[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof long[]) {
                        long[] arr = (long[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else {
                        Object[] arr = (Object[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    }
                }
            }
            return this;
        }

    }

    static class LongList implements Cloneable {
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

    static class Modular {
        int m;

        public Modular(int m) {
            this.m = m;
        }

        public Modular(long m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public Modular(double m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public int valueOf(int x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return x;
        }

        public int valueOf(long x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return (int) x;
        }

        public int mul(int x, int y) {
            return valueOf((long) x * y);
        }

        public int plus(int x, int y) {
            return valueOf(x + y);
        }

        public String toString() {
            return "mod " + m;
        }

    }

    static class Power {
        final Modular modular;

        public Power(Modular modular) {
            this.modular = modular;
        }

        public int pow(int x, int n) {
            if (n == 0) {
                return modular.valueOf(1);
            }
            long r = pow(x, n >> 1);
            r = modular.valueOf(r * r);
            if ((n & 1) == 1) {
                r = modular.valueOf(r * x);
            }
            return (int) r;
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

    static class Bits {
        private Bits() {
        }

        public static int bitAt(long x, int i) {
            return (int) ((x >>> i) & 1);
        }

        public static long setBit(long x, int i, boolean v) {
            if (v) {
                x |= 1L << i;
            } else {
                x &= ~(1L << i);
            }
            return x;
        }

        public static long swapBit(long x, int i, int j) {
            int bi = bitAt(x, i);
            int bj = bitAt(x, j);
            x = setBit(x, i, bj == 1);
            x = setBit(x, j, bi == 1);
            return x;
        }

    }
}

