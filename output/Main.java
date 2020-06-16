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
            DVaryingKibibits solver = new DVaryingKibibits();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DVaryingKibibits {
        Modular mod = new Modular(1e9 + 7);
        int limit = (int) 1e6;
        int digit = 6;
        int[][] sum = new int[7][limit];
        int[][] cnt = new int[7][limit];
        int[][] prod = new int[7][limit];
        int[] occur = new int[limit];
        CachedPow pow = new CachedPow(2, mod);
        IntRadix radix = new IntRadix(10);
        int[] way = new int[limit];
        Debug debug = new Debug(true);

        public int cnt(int i, int j) {
            if (cnt[i][j] == -1) {
                if (i == digit) {
                    return (int) (cnt[i][j] = occur[j]);
                }
                cnt[i][j] = 0;
                int cur = radix.get(j, i);
                if (cur < 9) {
                    cnt[i][j] += cnt(i, (int) radix.set(j, i, cur + 1));
                }
                cnt[i][j] += cnt(i + 1, j);
                cnt[i][j] = mod.valueOf(cnt[i][j]);
            }
            return (int) cnt[i][j];
        }

        public int prod(int i, int j) {
            if (prod[i][j] == -1) {
                if (i == digit) {
                    int ans = mod.valueOf((long) (1 + occur[j]) * occur[j] / 2);
                    ans = mod.mul(ans, j);
                    ans = mod.mul(ans, j);
                    return prod[i][j] = ans;
                }
                prod[i][j] = 0;
                int sum = 0;
                int cur = radix.get(j, i);
                if (cur < 9) {
                    prod[i][j] = mod.plus(prod[i][j], prod(i, (int) radix.set(j, i, cur + 1)));
                    sum = mod.plus(sum, sum(i, (int) radix.set(j, i, cur + 1)));
                }
                prod[i][j] = mod.plus(prod[i][j], prod(i + 1, j));
                prod[i][j] = mod.plus(prod[i][j], mod.mul(sum, sum(i + 1, j)));
            }
            return prod[i][j];
        }

        public int sum(int i, int j) {
            if (sum[i][j] == -1) {
                if (i == digit) {
                    return sum[i][j] = mod.mul(occur[j], j);
                }
                sum[i][j] = 0;
                int cur = radix.get(j, i);
                if (cur < 9) {
                    sum[i][j] = mod.plus(sum[i][j], sum(i, (int) radix.set(j, i, cur + 1)));
                }
                sum[i][j] = mod.plus(sum[i][j], sum(i + 1, j));
            }
            return sum[i][j];
        }

        public int ie(int i, int j, int cnt) {
            if (i == digit) {
                if (cnt % 2 == 1) {
                    return mod.valueOf(-way[j]);
                }
                return way[j];
            }
            int cur = radix.get(j, i);
            int ans = ie(i + 1, j, cnt);
            if (cur < 9) {
                ans = mod.plus(ans, ie(i + 1, (int) radix.set(j, i, cur + 1), cnt + 1));
            }
            return ans;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            SequenceUtils.deepFill(sum, -1);
            SequenceUtils.deepFill(cnt, -1);
            SequenceUtils.deepFill(prod, -1);

            for (int i = 0; i < n; i++) {
                occur[in.readInt()]++;
            }

            debug.elapse("init");
            for (int i = 0; i < limit; i++) {
                int c = cnt(0, i);
                int p = prod(0, i);
                if (c == 0) {
                    continue;
                }
                way[i] = mod.mul(p, pow.pow(c - 1));
            }

            debug.elapse("dp");
            //inclusion-exclusion
            int[] ie = new int[limit];
            for (int i = 0; i < limit; i++) {
                ie[i] = ie(0, i, 0);
            }

            debug.elapse("ie");
            long xor = 0;
            for (int i = 0; i < limit; i++) {
                long val = (long) i * ie[i];
                xor ^= val;
            }

            out.println(xor);
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        private long time = System.currentTimeMillis();

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public void elapse(String name) {
            debug(name, System.currentTimeMillis() - time);
            time = System.currentTimeMillis();
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

    static class IntegerList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

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

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public void add(int x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntegerList list) {
            addAll(list.data, 0, list.size);
        }

        public int tail() {
            checkRange(0);
            return data[size - 1];
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
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

    static class CachedPow {
        private int[] first;
        private int[] second;
        private Modular mod;
        private Modular powMod;
        private static int step = 16;
        private static int limit = 1 << step;
        private static int mask = limit - 1;

        public CachedPow(int x, Modular mod) {
            this.mod = mod;
            this.powMod = mod.getModularForPowerComputation();
            first = new int[limit];
            second = new int[Integer.MAX_VALUE / limit + 1];
            first[0] = 1;
            for (int i = 1; i < first.length; i++) {
                first[i] = mod.mul(x, first[i - 1]);
            }
            second[0] = 1;
            int step = mod.mul(x, first[first.length - 1]);
            for (int i = 1; i < second.length; i++) {
                second[i] = mod.mul(second[i - 1], step);
            }
        }

        public int pow(int exp) {
            return mod.mul(first[exp & mask], second[exp >> step]);
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

        public Modular getModularForPowerComputation() {
            return new Modular(m - 1);
        }

        public String toString() {
            return "mod " + m;
        }

    }

    static class IntRadix {
        private int[] pow;
        private int base;

        public IntRadix(int base) {
            if (base <= 1) {
                throw new IllegalArgumentException();
            }
            this.base = base;
            IntegerList ll = new IntegerList(32);
            ll.add(1);
            while (!DigitUtils.isMultiplicationOverflow(ll.tail(), base, Integer.MAX_VALUE)) {
                ll.add(ll.tail() * base);
            }
            pow = ll.toArray();
        }

        public int get(int x, int i) {
            return (x / pow[i] % base);
        }

        public int set(int x, int i, int val) {
            return x + (val - get(x, i)) * pow[i];
        }

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

    static class SequenceUtils {
        public static void deepFill(Object array, int val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof int[]) {
                int[] intArray = (int[]) array;
                Arrays.fill(intArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
                }
            }
        }

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
}

