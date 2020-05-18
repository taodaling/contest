import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Deque;
import java.util.function.Supplier;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.io.Closeable;
import java.io.Writer;
import java.util.ArrayDeque;
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
            P5245 solver = new P5245();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P5245 {
        Modular mod = new Modular(998244353);
        Debug debug = new Debug(false);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            char[] s = new char[(int) 1e5];
            int m = in.readString(s, 0);
            int k = 0;
            for (int i = 0; i < m; i++) {
                k = mod.valueOf((long) k * 10 + s[i] - '0');
            }

            IntegerList a = new IntegerList(n);
            for (int i = 0; i < n; i++) {
                a.push(in.readInt());
            }

            debug.debug("k", k);
            debug.debug("a", a);

            NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
            IntegerList ans = new IntegerList(n);
            ntt.powmod(a, ans, k, n);
            a = ans;

//
//
//        IntegerList lna = new IntegerList();
//        ntt.ln(a, lna, n, inv);
//        debug.debug("lna", lna);
//        Polynomials.mul(lna, k, mod);
//        debug.debug("k * lna", lna);
//        ntt.exp(lna, a, n, inv);
//
//        debug.debug("b", a);

            a.expandWith(0, n);
            for (int i = 0; i < n; i++) {
                out.append(a.get(i)).append(' ');
            }
        }

    }

    static class PrimitiveRoot {
        private int[] factors;
        private Modular mod;
        private Power pow;
        int phi;
        private static PollardRho rho = new PollardRho();

        public PrimitiveRoot(Modular x) {
            phi = x.getMod() - 1;
            mod = x;
            pow = new Power(mod);
            factors = rho.findAllFactors(phi).keySet().stream().mapToInt(Integer::intValue).toArray();
        }

        public PrimitiveRoot(int x) {
            this(new Modular(x));
        }

        public int findMinPrimitiveRoot() {
            if (mod.getMod() == 2) {
                return 1;
            }
            return findMinPrimitiveRoot(2);
        }

        public int findMinPrimitiveRoot(int since) {
            for (int i = since; i < mod.m; i++) {
                boolean flag = true;
                for (int f : factors) {
                    if (pow.pow(i, phi / f) == 1) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return i;
                }
            }
            return -1;
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

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
            return deque.isEmpty() ? supplier.get() : deque.removeFirst();
        }

        public void release(T e) {
            releaseTime++;
            cleaner.accept(e);
            deque.addLast(e);
        }

    }

    static class Polynomials {
        public static Buffer<IntegerList> listBuffer = new Buffer<>(IntegerList::new, list -> list.clear());

        public static int rankOf(IntegerList p) {
            int[] data = p.getData();
            int r = p.size() - 1;
            while (r >= 0 && data[r] == 0) {
                r--;
            }
            return Math.max(0, r);
        }

        public static void normalize(IntegerList list) {
            list.retain(rankOf(list) + 1);
        }

        public static void module(IntegerList list, int n) {
            list.remove(n, list.size() - 1);
            normalize(list);
        }

    }

    static class Log2 {
        public static int ceilLog(int x) {
            return 32 - Integer.numberOfLeadingZeros(x - 1);
        }

    }

    static class MillerRabin {
        Modular modular;
        Power power;
        Random random = new Random();

        public boolean mr(int n, int s) {
            if (n <= 1) {
                return false;
            }
            if (n == 2) {
                return true;
            }
            if (n % 2 == 0) {
                return false;
            }
            int m = n - 1;
            while (m % 2 == 0) {
                m /= 2;
            }
            modular = new Modular(n);
            power = new Power(modular);
            for (int i = 0; i < s; i++) {
                int x = random.nextInt(n - 2) + 2;
                if (!mr0(x, n, m)) {
                    return false;
                }
            }
            return true;
        }

        private boolean mr0(int x, int n, int m) {
            return test(power.pow(x, m), m, n);
        }

        private boolean test(int y, int exp, int n) {
            int y2 = modular.mul(y, y);
            if (!(exp == n - 1 || test(y2, exp * 2, n))) {
                return false;
            }
            if (exp != n - 1 && y2 != 1) {
                return true;
            }
            if (y != 1 && y != n - 1) {
                return false;
            }
            return true;
        }

    }

    static class NumberTheoryTransform {
        private Modular modular;
        private Power power;
        private int g;
        private int[] wCache = new int[30];
        private int[] invCache = new int[30];
        public static Buffer<IntegerList> listBuffer = Polynomials.listBuffer;

        public NumberTheoryTransform(Modular mod) {
            this(mod, mod.getMod() == 998244353 ? 3 : new PrimitiveRoot(mod.getMod()).findMinPrimitiveRoot());
        }

        public NumberTheoryTransform(Modular mod, int g) {
            this.modular = mod;
            this.power = new Power(mod);
            this.g = g;
            for (int i = 0, until = wCache.length; i < until; i++) {
                int s = 1 << i;
                wCache[i] = power.pow(this.g, (modular.getMod() - 1) / 2 / s);
                invCache[i] = power.inverseByFermat(s);
            }
        }

        public void dotMul(int[] a, int[] b, int[] c, int m) {
            for (int i = 0, n = 1 << m; i < n; i++) {
                c[i] = modular.mul(a[i], b[i]);
            }
        }

        public void dft(int[] p, int m) {
            int n = 1 << m;

            int shift = 32 - Integer.numberOfTrailingZeros(n);
            for (int i = 1; i < n; i++) {
                int j = Integer.reverse(i << shift);
                if (i < j) {
                    int temp = p[i];
                    p[i] = p[j];
                    p[j] = temp;
                }
            }

            int w = 0;
            int t = 0;
            for (int d = 0; d < m; d++) {
                int w1 = wCache[d];
                int s = 1 << d;
                int s2 = s << 1;
                for (int i = 0; i < n; i += s2) {
                    w = 1;
                    for (int j = 0; j < s; j++) {
                        int a = i + j;
                        int b = a + s;
                        t = modular.mul(w, p[b]);
                        p[b] = modular.plus(p[a], -t);
                        p[a] = modular.plus(p[a], t);
                        w = modular.mul(w, w1);
                    }
                }
            }
        }

        public void idft(int[] p, int m) {
            dft(p, m);

            int n = 1 << m;
            int invN = invCache[m];

            p[0] = modular.mul(p[0], invN);
            p[n / 2] = modular.mul(p[n / 2], invN);
            for (int i = 1, until = n / 2; i < until; i++) {
                int a = p[n - i];
                p[n - i] = modular.mul(p[i], invN);
                p[i] = modular.mul(a, invN);
            }
        }

        private IntegerList clone(IntegerList list) {
            Polynomials.normalize(list);
            IntegerList ans = listBuffer.alloc();
            ans.addAll(list);
            return ans;
        }

        public void mul(IntegerList a, IntegerList b, IntegerList ans) {
            a = clone(a);
            b = clone(b);
            int rank = a.size() + b.size() - 1;
            int proper = Log2.ceilLog(rank + 1);
            a.expandWith(0, 1 << proper);
            b.expandWith(0, 1 << proper);
            ans.clear();
            ans.expandWith(0, 1 << proper);
            dft(a.getData(), proper);
            dft(b.getData(), proper);
            dotMul(a.getData(), b.getData(), ans.getData(), proper);
            idft(ans.getData(), proper);
            Polynomials.normalize(ans);
            listBuffer.release(a);
            listBuffer.release(b);
        }

        public void modmul(IntegerList a, IntegerList b, IntegerList ans, int n) {
            mul(a, b, ans);
            Polynomials.module(ans, n);
        }

        public void pow2(IntegerList a) {
            int rankAns = (a.size() - 1) * 2;
            int proper = Log2.ceilLog(rankAns + 1);
            a.expandWith(0, (1 << proper));
            dft(a.getData(), proper);
            dotMul(a.getData(), a.getData(), a.getData(), proper);
            idft(a.getData(), proper);
            Polynomials.normalize(a);
        }

        public void powmod(IntegerList p, IntegerList ans, long k, int n) {
            if (k == 0) {
                ans.clear();
                ans.push(1);
                return;
            }
            powmod(p, ans, k / 2, n);
            pow2(ans);
            Polynomials.module(ans, n);
            if (k % 2 == 1) {
                IntegerList buf = listBuffer.alloc();
                modmul(ans, p, buf, n);
                ans.clear();
                ans.addAll(buf);
                listBuffer.release(buf);
            }
        }

    }

    static class PollardRho {
        MillerRabin mr = new MillerRabin();
        Random random = new Random(1);

        public int findFactor(int n) {
            if (mr.mr(n, 5)) {
                return n;
            }
            while (true) {
                int f = rho(n);
                if (f != n) {
                    return f;
                }
            }
        }

        public Map<Integer, Integer> findAllFactors(int n) {
            Map<Integer, Integer> map = new HashMap();
            findAllFactors(map, n);
            return map;
        }

        private void findAllFactors(Map<Integer, Integer> map, int n) {
            if (n == 1) {
                return;
            }
            int f = findFactor(n);
            if (f == n) {
                Integer value = map.get(f);
                if (value == null) {
                    value = 1;
                }
                map.put(f, value * f);
                return;
            }
            findAllFactors(map, f);
            findAllFactors(map, n / f);
        }

        private int rho(int n) {
            if (n % 2 == 0) {
                return 2;
            }
            if (n % 3 == 0) {
                return 3;
            }
            int x = 0, y = x, t, q = 1, c = random.nextInt(n - 1) + 1;
            for (int k = 2; ; k <<= 1, y = x, q = 1) {
                for (int i = 1; i <= k; ++i) {
                    x = DigitUtils.mod((long) x * x + c, n);
                    q = DigitUtils.mod((long) q * Math.abs(x - y), n);
                    if ((i & 127) == 0) {
                        t = GCDs.gcd(q, n);
                        if (t > 1) {
                            return t;
                        }
                    }
                }
                if ((t = GCDs.gcd(q, n)) > 1) {
                    return t;
                }
            }
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

        public int readString(char[] data, int offset) {
            skipBlank();

            int originalOffset = offset;
            while (next > 32) {
                data[offset++] = (char) next;
                next = read();
            }

            return offset - originalOffset;
        }

    }

    static class GCDs {
        private GCDs() {
        }

        public static int gcd(int a, int b) {
            return a >= b ? gcd0(a, b) : gcd0(b, a);
        }

        private static int gcd0(int a, int b) {
            return b == 0 ? a : gcd0(b, a % b);
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

    static class Power implements InverseNumber {
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

        public int inverseByFermat(int x) {
            return pow(x, modular.m - 2);
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int mod(long x, int mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return (int) x;
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

    static interface InverseNumber {
    }

    static class Modular {
        int m;

        public int getMod() {
            return m;
        }

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

    static class IntegerList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public int[] getData() {
            return data;
        }

        public void remove(int l, int r) {
            if (l > r) {
                return;
            }
            checkRange(l);
            checkRange(r);
            if (r == size - 1) {
                size = l;
                return;
            } else {
                System.arraycopy(data, r + 1, data, l, size - (r + 1));
                size -= (r - l + 1);
            }
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

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public int get(int i) {
            checkRange(i);
            return data[i];
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

        public void expandWith(int x, int len) {
            ensureSpace(len);
            while (size < len) {
                data[size++] = x;
            }
        }

        public void retain(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n >= size) {
                return;
            }
            size = n;
        }

        public void push(int x) {
            add(x);
        }

        public int size() {
            return size;
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

