import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Deque;
import java.util.function.Supplier;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
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
            P5577CmdOI2019 solver = new P5577CmdOI2019();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P5577CmdOI2019 {
        int mod = 998244353;
        int k;

        public int asK(int x) {
            if (x == 0) {
                return 0;
            }
            return asK(x / 10) * k + x % 10;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.ri();
            k = in.ri();
            int m = in.ri();
            int km = 1;
            for (int i = 0; i < m; i++) {
                km *= k;
            }
            int[] C = new int[km];
            for (int i = 0; i < n; i++) {
                int x = in.ri();
                C[asK(x)]++;
            }
            //Debug debug = new Debug(true);
            //debug.debug("support", ModGenericFastWalshHadamardTransform.currentSupport());
            ModGenericFastWalshHadamardTransform fwt = new ModGenericFastWalshHadamardTransform(k, mod);
            int[] ans = fwt.countSubset(C);
            for (int i = 0; i < ans.length; i++) {
                out.println(ans[i]);
            }
        }

    }

    static class DigitUtils {
        private static LongExtGCDObject longExtGCDObject = new LongExtGCDObject();
        public static long INT_TO_LONG_MASK = (1L << 32) - 1;

        private DigitUtils() {
        }

        public static long asLong(int high, int low) {
            return (((long) high) << 32) | (((long) low) & INT_TO_LONG_MASK);
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

        public static long mod(long x, long mod) {
            if (x < -mod || x >= mod) {
                x %= mod;
            }
            if (x < 0) {
                x += mod;
            }
            return x;
        }

        public static long modInverse(long x, long mod) {
            long g = longExtGCDObject.extgcd(x, mod);
            assert g == 1;
            long a = longExtGCDObject.getX();
            return DigitUtils.mod(a, mod);
        }

    }

    static class Polynomials {
        public static int rankOf(int[] p) {
            int r = p.length - 1;
            while (r >= 0 && p[r] == 0) {
                r--;
            }
            return Math.max(0, r);
        }

        public static int[] normalizeAndReplace(int[] p) {
            int r = rankOf(p);
            return PrimitiveBuffers.resize(p, r + 1);
        }

        public static int[][] divAndRemainder(int[] a, int[] b) {
            int ra = Polynomials.rankOf(a);
            int rb = Polynomials.rankOf(b);
            if (ra < rb) {
                return new int[][]{PrimitiveBuffers.allocIntPow2(1), PrimitiveBuffers.allocIntPow2(a)};
            }
            int[] div = PrimitiveBuffers.allocIntPow2(ra - rb + 1);
            int[] op = PrimitiveBuffers.allocIntPow2(a);

            for (int i = ra; i >= rb; i--) {
                assert op[i] % b[rb] == 0;
                int d = op[i] / b[rb];
                div[i - rb] = d;
                if (d == 0) {
                    continue;
                }
                for (int j = rb; j >= 0; j--) {
                    op[i + j - rb] -= d * b[j];
                }
            }

            op = Polynomials.normalizeAndReplace(op);
            return new int[][]{div, op};
        }

        public static int[] mul(int[] a, int[] b) {
            int ra = Polynomials.rankOf(a);
            int rb = Polynomials.rankOf(b);
            int[] ans = PrimitiveBuffers.allocIntPow2(ra + rb + 1);
            for (int i = 0; i <= ra; i++) {
                for (int j = 0; j <= rb; j++) {
                    ans[i + j] += a[i] * b[j];
                }
            }
            return ans;
        }

    }

    static class CyclotomicPolynomialBF {
        private static Map<Integer, int[]> cache = new HashMap<>();

        static {
            cache.put(1, new int[]{-1, 1});
        }

        public static int[] get(int n) {
            int[] ans = cache.get(n);
            if (ans == null) {
                int[] a = PrimitiveBuffers.allocIntPow2(cache.get(1));
                for (int i = 2; i * i <= n; i++) {
                    if (n % i != 0) {
                        continue;
                    }
                    a = PrimitiveBuffers.replace(Polynomials.mul(a, get(i)), a);
                    if (i * i != n) {
                        a = PrimitiveBuffers.replace(Polynomials.mul(a, get(n / i)), a);
                    }
                }
                int[] top = PrimitiveBuffers.allocIntPow2(n + 1);
                top[n] = 1;
                top[0] = -1;
                int[][] res = Polynomials.divAndRemainder(top, a);
                ans = Arrays.copyOf(res[0], Polynomials.rankOf(res[0]) + 1);
                assert ans.length <= n + 1;
                PrimitiveBuffers.release(a, top, res[0], res[1]);
                cache.put(n, ans);
            }

            return ans;
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

    static class SequenceUtils {
        public static void swap(long[] data, int i, int j) {
            long tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

    }

    static class LongExtGCDObject {
        private long[] xy = new long[2];

        public long extgcd(long a, long b) {
            return ExtGCD.extGCD(a, b, xy);
        }

        public long getX() {
            return xy[0];
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 32 << 10;
        private final Writer os;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);

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

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append('\n');
        }

        public FastOutput flush() {
            try {
//            boolean success = false;
//            if (stringBuilderValueField != null) {
//                try {
//                    char[] value = (char[]) stringBuilderValueField.get(cache);
//                    os.write(value, 0, cache.length());
//                    success = true;
//                } catch (Exception e) {
//                }
//            }
//            if (!success) {
                os.append(cache);
//            }
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

    static class ExtGCD {
        public static long extGCD(long a, long b, long[] xy) {
            if (a >= b) {
                return extGCD0(a, b, xy);
            }
            long ans = extGCD0(b, a, xy);
            SequenceUtils.swap(xy, 0, 1);
            return ans;
        }

        private static long extGCD0(long a, long b, long[] xy) {
            if (b == 0) {
                xy[0] = 1;
                xy[1] = 0;
                return a;
            }
            long ans = extGCD0(b, a % b, xy);
            long x = xy[0];
            long y = xy[1];
            xy[0] = y;
            xy[1] = x - a / b * y;
            return ans;
        }

    }

    static class ModGenericFastWalshHadamardTransform {
        int[][] addC;
        int[][] iaddC;
        int[][] addCWithRoot;
        int[][] iaddCWithRoot;
        int[] phi;
        int k;
        int invK;
        int mod;
        int[][] buf;
        int one;
        static Map<Long, Integer> rootCache = new HashMap<>();
        long[] accum;
        long inf;
        int g;

        static {
            rootCache.put(4287426849551679490L, 998244352);
            rootCache.put(4287426849551679492L, 86583718);
            rootCache.put(4287426849551679495L, 14553391);
            rootCache.put(4287426849551679496L, 372528824);
            rootCache.put(4287426849551679502L, 219186804);
            rootCache.put(4287426849551679504L, 69212480);
            rootCache.put(4287426849551679505L, 503044);
            rootCache.put(4294967326064771074L, 1000000006);
            rootCache.put(4294967334654705666L, 1000000008);
            rootCache.put(4294967334654705667L, 115381398);
            rootCache.put(4294967334654705668L, 430477711);
            rootCache.put(4294967334654705670L, 115381399);
            rootCache.put(4294967334654705671L, 95932470);
            rootCache.put(4294967334654705672L, 118835338);
            rootCache.put(4294967334654705673L, 246325263);
            rootCache.put(4294967334654705676L, 86475609);
            rootCache.put(4294967334654705678L, 9196980);
            rootCache.put(4294967334654705682L, 4138593);
        }

        public static int searchRoot(int k, int mod) {
            if ((mod - 1) % k != 0) {
                return -1;
            }
            for (int i = 1; i < mod; i++) {
                int cur = i;
                int p = 1;
                while (p < k) {
                    if (cur == 1) {
                        break;
                    }
                    cur = (int) ((long) cur * i % mod);
                    p++;
                }
                if (p == k && cur == 1) {
                    return i;
                }
            }
            return -1;
        }

        public ModGenericFastWalshHadamardTransform(int k, int mod, boolean searchRoot) {
            this(k, mod, searchRoot ? searchRoot(k, mod) : -1);
        }

        public ModGenericFastWalshHadamardTransform(int k, int mod) {
            this(k, mod, -1);
        }

        public ModGenericFastWalshHadamardTransform(int k, int mod, int g) {
            one = 1 % k;
            this.g = g;
            this.mod = mod;
            this.k = k;
            addC = new int[k][k];
            iaddC = new int[k][k];
            buf = new int[k][k];
            accum = new long[k * 2];
            phi = CyclotomicPolynomialBF.get(k).clone();
            inf = Long.MAX_VALUE - (long) (mod - 1) * (mod - 1);
            invK = (int) DigitUtils.modInverse(k, mod);
            for (int i = 0; i < phi.length; i++) {
                phi[i] = DigitUtils.mod(phi[i], mod);
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    addC[i][j] = i * j % k;
                    iaddC[i][j] = (k - addC[i][j]) % k;
                }
            }

            if (g == -1) {
                g = rootCache.getOrDefault(DigitUtils.asLong(mod, k), -1);
            }
            if (g != -1) {
                int[] powSerial = new int[k];
                powSerial[0] = 1;
                for (int i = 1; i < k; i++) {
                    powSerial[i] = (int) ((long) powSerial[i - 1] * g % mod);
                }
                addCWithRoot = new int[k][k];
                iaddCWithRoot = new int[k][k];

                for (int i = 0; i < k; i++) {
                    for (int j = 0; j < k; j++) {
                        addCWithRoot[i][j] = powSerial[addC[i][j]];
                        iaddCWithRoot[i][j] = powSerial[iaddC[i][j]];
                        assert (long) addCWithRoot[i][j] * iaddCWithRoot[i][j] % mod == 1;
                    }
                }
            }
        }

        private void mulAddTo(int[][] a, int offset, int t, int[] dest) {
            for (int i = 0, to = t; i < k; i++, to = to + 1 == k ? 0 : to + 1) {
                dest[to] += a[i][offset];
                if (dest[to] >= mod) {
                    dest[to] -= mod;
                }
            }
        }

        private void mulAddTo(int[][] a, int ai, int[][] b, int bi, int t) {
            for (int i = 0, to0 = 0, to1 = t; i < k; i++) {
                b[to0][bi] += a[i][ai];
                if (b[to0][bi] >= mod) {
                    b[to0][bi] -= mod;
                }
                b[to1][bi] += a[i][ai];
                if (b[to1][bi] >= mod) {
                    b[to1][bi] -= mod;
                }
                to0 = to0 + 1 == k ? 0 : to0 + 1;
                to1 = to1 + 1 == k ? 0 : to1 + 1;
            }
        }

        private void mulAddTo(int[][] a, int ai, int[][] b, int bi) {
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    int ij = i + j;
                    accum[ij] += (long) a[i][ai] * b[j][bi];
                    if (accum[ij] >= inf) {
                        accum[ij] %= mod;
                    }
                }
            }
            for (int i = 0; i < k; i++) {
                accum[i] += accum[i + k] % mod;
                accum[i + k] = 0;
                accum[i] %= mod;
            }
        }

        public void addFWTWithRoot(int[] a, int l, int r, int[][] addCWithRoot) {
            if (l == r) {
                return;
            }
            int len = r - l + 1;
            assert len % k == 0;
            int step = len / k;
            for (int i = 0; i < len; i += step) {
                addFWTWithRoot(a, l + i, l + i + step - 1, addCWithRoot);
            }
            for (int i = 0; i < step; i++) {
                int first = l + i;
                for (int j = 0; j < k; j++) {
                    long sum = 0;
                    for (int t = 0; t < k; t++) {
                        sum += (long) addCWithRoot[j][t] * a[first + t * step];
                        if (sum >= inf) {
                            sum %= mod;
                        }
                    }
                    buf[0][j] = (int) (sum % mod);
                }
                //copy back
                for (int j = 0; j < k; j++) {
                    int index = j * step + first;
                    a[index] = buf[0][j];
                    buf[0][j] = 0;
                }
            }
        }

        public void addIFWTWithRoot(int[] a, int l, int r) {
            addIFWTWithRoot0(a, l, r);
            long invN = DigitUtils.modInverse(r - l + 1, mod);
            for (int i = l; i <= r; i++) {
                a[i] = (int) (invN * a[i] % mod);
            }
        }

        public void addIFWTWithRoot0(int[] a, int l, int r) {
            if (l == r) {
                return;
            }
            int len = r - l + 1;
            assert len % k == 0;
            int step = len / k;

            for (int i = 0; i < step; i++) {
                int first = l + i;
                for (int j = 0; j < k; j++) {
                    long sum = 0;
                    for (int t = 0; t < k; t++) {
                        sum += (long) iaddCWithRoot[j][t] * a[first + t * step];
                        if (sum >= inf) {
                            sum %= mod;
                        }
                    }
                    buf[0][j] = (int) (sum % mod);
                }
                //copy back
                for (int j = 0; j < k; j++) {
                    int index = j * step + first;
                    a[index] = buf[0][j];
                    buf[0][j] = 0;
                }
            }

            for (int i = 0; i < len; i += step) {
                addIFWTWithRoot0(a, l + i, l + i + step - 1);
            }
        }

        public void addFWT(int[][] a, int l, int r, int[][] addC) {
            if (l == r) {
                return;
            }
            int len = r - l + 1;
            assert len % k == 0;
            int step = len / k;
            for (int i = 0; i < len; i += step) {
                addFWT(a, l + i, l + i + step - 1, addC);
            }
            for (int i = 0; i < step; i++) {
                int first = l + i;
                for (int j = 0; j < k; j++) {
                    for (int t = 0; t < k; t++) {
                        mulAddTo(a, t * step + first, addC[j][t], buf[j]);
                    }
                }
                //copy back
                for (int j = 0; j < k; j++) {
                    int index = j * step + first;
                    for (int t = 0; t < k; t++) {
                        a[t][index] = buf[j][t];
                        buf[j][t] = 0;
                    }
                }
            }
        }

        public void normalize(int[][] a, int l, int r) {
            for (int i = l; i <= r; i++) {
                moduleInPlace(a, i, phi);
            }
        }

        private void moduleInPlace(int[][] a, int offset, int[] b) {
            int ra = k - 1;
            int rb = Polynomials.rankOf(b);
            if (ra < rb) {
                return;
            }
            assert b[rb] == 1;
            for (int i = ra; i >= rb; i--) {
                long d = a[i][offset];
                if (d == 0) {
                    continue;
                }
                for (int j = rb; j >= 0; j--) {
                    int ij = i + j - rb;
                    a[ij][offset] -= d * b[j] % mod;
                    if (a[ij][offset] < 0) {
                        a[ij][offset] += mod;
                    }
                }
            }
        }

        private void moduleInPlace(int[] a, int[] b) {
            int ra = k - 1;
            int rb = Polynomials.rankOf(b);
            if (ra < rb) {
                return;
            }
            assert b[rb] == 1;
            for (int i = ra; i >= rb; i--) {
                long d = a[i];
                if (d == 0) {
                    continue;
                }
                for (int j = rb; j >= 0; j--) {
                    int ij = i + j - rb;
                    a[ij] -= d * b[j] % mod;
                    if (a[ij] < 0) {
                        a[ij] += mod;
                    }
                }
            }
        }

        public void addIFWT(int[][] a, int l, int r) {
            int n = r - l + 1;
            long inv = DigitUtils.modInverse(n, mod);
            addIFWT0(a, l, r);
            normalize(a, l, r);
            for (int i = l; i <= r; i++) {
//            assert a[0][i] % n == 0;
                a[0][i] = (int) (a[0][i] * inv % mod);
            }
        }

        private void addIFWT0(int[][] a, int l, int r) {
            if (l == r) {
                return;
            }
            int len = r - l + 1;
            assert len % k == 0;
            int step = len / k;

            for (int i = 0; i < step; i++) {
                int first = l + i;
                for (int j = 0; j < k; j++) {
                    for (int t = 0; t < k; t++) {
                        mulAddTo(a, t * step + first, iaddC[j][t], buf[j]);
                    }
                }
                //copy back
                for (int j = 0; j < k; j++) {
                    int index = j * step + first;
                    for (int t = 0; t < k; t++) {
                        a[t][index] = buf[j][t];
                        buf[j][t] = 0;
                    }
                }
            }

            for (int i = 0; i < len; i += step) {
                addIFWT0(a, l + i, l + i + step - 1);
            }
        }

        public boolean constant(int[] x) {
            for (int i = 1; i < k; i++) {
                if (x[i] != 0) {
                    return false;
                }
            }
            return true;
        }

        public int[] countSubset(int[] C) {
            if (g != -1) {
                return countSubsetWithG(C);
            }

            int[][] c = new int[k][k];
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    c[i][j] = 0;
                }
            }
            int n = C.length;
            int[][][] Fr = new int[k][k][n];
            for (int i = 0; i < k; i++) {
                System.arraycopy(C, 0, Fr[i][0], 0, n);
                addFWT(Fr[i], 0, n - 1, c);
                for (int t = 0; t < k; t++) {
                    for (int j = 0; j < k; j++) {
                        c[t][j] += addC[t][j];
                        if (c[t][j] >= k) {
                            c[t][j] -= k;
                        }
                    }
                }
            }
            int[][] x = new int[k][n];
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < n; j++) {
                    for (int t = 0; t < k; t++) {
                        //c^{-1}(i,t) * Fr_{t,j}
                        mulAddTo(Fr[t], j, iaddC[i][t], buf[0]);
                    }
                    moduleInPlace(buf[0], phi);
                    assert constant(buf[0]);
                    x[i][j] = (int) ((long) buf[0][0] * invK % mod);
                    buf[0][0] = 0;
                }
            }
            int sum = Arrays.stream(C).sum();
            int bits = Log2.floorLog(sum) + 1;
            int highBits = bits / 2;
            int lowBits = bits - highBits;
            int[][] fastPowLowBit = new int[k][1 << lowBits];
            int[][] fastPowHighBit = new int[k][1 << highBits];
            int[][] F = new int[k][n];
            Arrays.fill(F[0], one);
            for (int i = 0; i < k; i++) {
                for (int t = 0; t < k; t++) {
                    Arrays.fill(fastPowLowBit[t], 0);
                    Arrays.fill(fastPowHighBit[t], 0);
                }
                fastPowLowBit[0][0] = one;
                for (int j = 1; j < 1 << lowBits; j++) {
                    mulAddTo(fastPowLowBit, j - 1, fastPowLowBit, j, i);
                }
                fastPowHighBit[0][0] = one;
                if (highBits > 0) {
                    mulAddTo(fastPowLowBit, (1 << lowBits) - 1, fastPowHighBit, 1, i);
                    for (int j = 2; j < 1 << highBits; j++) {
                        mulAddTo(fastPowHighBit, j - 1, fastPowHighBit, 1);
                        for (int t = 0; t < k; t++) {
                            fastPowHighBit[t][j] = (int) accum[t];
                            accum[t] = 0;
                        }
                    }
                }

                for (int j = 0; j < n; j++) {
                    int p = x[i][j];
                    int l = p & ((1 << lowBits) - 1);
                    int h = p >>> lowBits;
                    if (l != 0) {
                        mulAddTo(fastPowLowBit, l, F, j);
                        for (int t = 0; t < k; t++) {
                            F[t][j] = (int) accum[t];
                            accum[t] = 0;
                        }
                    }
                    if (h != 0) {
                        mulAddTo(fastPowHighBit, h, F, j);
                        for (int t = 0; t < k; t++) {
                            F[t][j] = (int) accum[t];
                            accum[t] = 0;
                        }
                    }
                }
            }

            addIFWT(F, 0, n - 1);
            return F[0];
        }

        private int[] countSubsetWithG(int[] C) {
            int[][] c = new int[k][k];
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    c[i][j] = 1;
                }
            }
            int n = C.length;
            int[][] Fr = new int[k][n];
            for (int i = 0; i < k; i++) {
                System.arraycopy(C, 0, Fr[i], 0, n);
                addFWTWithRoot(Fr[i], 0, n - 1, c);
                for (int t = 0; t < k; t++) {
                    for (int j = 0; j < k; j++) {
                        c[t][j] = (int) ((long) c[t][j] * addCWithRoot[t][j] % mod);
                    }
                }
            }
            int[][] x = new int[k][n];
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < n; j++) {
                    long sum = 0;
                    for (int t = 0; t < k; t++) {
                        //c^{-1}(i,t) * Fr_{t,j}
                        sum += (long) Fr[t][j] * iaddC[i][t];
                        if (sum >= inf) {
                            sum %= mod;
                        }
                    }
                    x[i][j] = (int) (sum % mod * invK % mod);
                }
            }
            int sum = Arrays.stream(C).sum();
            int bits = Log2.floorLog(sum) + 1;
            int highBits = bits / 2;
            int lowBits = bits - highBits;
            int[] fastPowLowBit = new int[1 << lowBits];
            int[] fastPowHighBit = new int[1 << highBits];
            int[] F = new int[n];
            Arrays.fill(F, one);
            for (int i = 0; i < k; i++) {
                fastPowLowBit[0] = one;
                for (int j = 1; j < 1 << lowBits; j++) {
                    fastPowLowBit[j] = (int) ((long) fastPowLowBit[j - 1] * addCWithRoot[1][i] % mod);
                }
                fastPowHighBit[0] = one;
                if (highBits > 0) {
                    fastPowHighBit[1] = (int) ((long) fastPowLowBit[fastPowLowBit.length - 1] * addCWithRoot[1][i] % mod);
                    for (int j = 2; j < 1 << highBits; j++) {
                        fastPowHighBit[j] = (int) ((long) fastPowHighBit[j - 1] * fastPowHighBit[1] % mod);
                    }
                }

                for (int j = 0; j < n; j++) {
                    int p = x[i][j];
                    int l = p & ((1 << lowBits) - 1);
                    int h = p >>> lowBits;
                    if (l != 0) {
                        F[j] = (int) ((long) F[j] * fastPowLowBit[l] % mod);
                    }
                    if (h != 0) {
                        F[j] = (int) ((long) F[j] * fastPowHighBit[h] % mod);
                    }
                }
            }

            addIFWTWithRoot(F, 0, n - 1);
            return F;
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

        public int ri() {
            return readInt();
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

    static class Log2 {
        public static int ceilLog(int x) {
            if (x <= 0) {
                return 0;
            }
            return 32 - Integer.numberOfLeadingZeros(x - 1);
        }

        public static int floorLog(int x) {
            if (x <= 0) {
                throw new IllegalArgumentException();
            }
            return 31 - Integer.numberOfLeadingZeros(x);
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

        public static int[] allocIntPow2(int[] data) {
            int[] ans = allocIntPow2(data.length);
            System.arraycopy(data, 0, ans, 0, data.length);
            return ans;
        }

        public static int[] resize(int[] data, int want) {
            int log = Log2.ceilLog(want);
            if (data.length == 1 << log) {
                return data;
            }
            return replace(allocIntPow2(data, want), data);
        }

        public static int[] allocIntPow2(int[] data, int newLen) {
            int[] ans = allocIntPow2(newLen);
            System.arraycopy(data, 0, ans, 0, Math.min(data.length, newLen));
            return ans;
        }

        public static void release(int[] data) {
            intPow2Bufs[Log2.ceilLog(data.length)].release(data);
        }

        public static int[] replace(int[] a, int[] b) {
            release(b);
            return a;
        }

        public static void release(int[]... data) {
            for (int[] x : data) {
                release(x);
            }
        }

    }
}

