import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.stream.LongStream;
import java.util.Iterator;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.util.Comparator;
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
            DStrangeLCS solver = new DStrangeLCS();
            int testCount = Integer.parseInt(in.next());
            for (int i = 1; i <= testCount; i++)
                solver.solve(i, in, out);
            out.close();
        }
    }

    static class DStrangeLCS {
        int charset = remake('Z') + 1;
        Debug debug = new Debug(true);
        Comparator<E> comp = Comparator.<E>comparingLong(x -> x.a).thenComparing(x -> x.b);
        E buf = new E();
        List<E> allList = new ArrayList<>((int) 1e5);
        int[][][] next;
        char[][] s;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.ri();
            s = new char[n][];
            next = new int[n][][];
            for (int i = 0; i < n; i++) {
                s[i] = in.rs().toCharArray();
                for (int j = 0; j < s[i].length; j++) {
                    s[i][j] = (char) remake(s[i][j]);
                }
                next[i] = next(s[i]);
            }

            allList.clear();
            for (int i = 0; i < charset; i++) {
                buf.init();
                dfs(0, n, i);
            }
            allList.sort(comp);
            E[] all = allList.toArray(new E[0]);
            long[] keys = Arrays.stream(all).mapToLong(E::hash).toArray();
            PerfectHashing<E> ph = new PerfectHashing<>(keys, all);
            for (int i = 0; i < all.length; i++) {
                all[i].index = i;
                all[i].score = 1;
                all[i].prev = null;
            }


            for (E e : all) {
                for (int j = 0; j < charset; j++) {
                    buf.init();
                    for (int k = 0; k < n; k++) {
                        int to = next[k][e.get(k) + 1][j];
                        buf.set(k, to);
                    }
                    E obj = ph.get(buf.hash());
                    if (obj == null) {
                        continue;
                    }
                    if (obj.score < e.score + 1) {
                        obj.score = e.score + 1;
                        obj.prev = e;
                    }
                }
            }

            if (all.length == 0) {
                out.println(0);
                out.println();
                return;
            }
            E best = null;
            for (E e : all) {
                if (best == null || e.score > best.score) {
                    best = e;
                }
            }
            StringBuilder sb = new StringBuilder(charset * 2);
            while (best != null) {
                int v = s[0][best.get(0)];
                sb.append(goback(v));
                best = best.prev;
            }
            sb = sb.reverse();
            out.println(sb.length());
            out.println(sb);

            debug.debug("secondLevel", FastUniversalHashFunction0.numberOfInstance);
            debug.debug("numberOfHighestLevel", UniversalHashFunction.numberOfInstance);
        }

        public void dfs(int cur, int n, int c) {
            if (cur == n) {
                allList.add(buf.clone());
                return;
            }
            for (int iter = next[cur][0][c]; iter < s[cur].length; iter = next[cur][iter + 1][c]) {
                buf.set(cur, iter);
                dfs(cur + 1, n, c);
            }
        }

        public int[][] next(char[] s) {
            int n = s.length;
            int[][] next = new int[n + 1][];
            int[] prev = new int[charset];
            Arrays.fill(prev, n);
            next[n] = prev.clone();
            for (int i = n - 1; i >= 0; i--) {
                prev[s[i]] = i;
                next[i] = prev.clone();
            }
            return next;
        }

        public int remake(int x) {
            if ('a' <= x && x <= 'z') {
                return x - 'a';
            }
            return x - 'A' + 'z' - 'a' + 1;
        }

        public char goback(int x) {
            if (x < 'z' - 'a' + 1) {
                return (char) (x + 'a');
            } else {
                return (char) (x + 'A' - 'z' + 'a' - 1);
            }
        }

    }

    static class PerfectHashing<V> implements Iterable<V> {
        HashFunction f1 = FastUniversalHashFunction0.getInstance();
        HashFunction[] f2;
        static final Object NIL = new Object();
        int[] starts;
        int[] masks;
        long[] K;
        Object[] V;
        int globalMask;

        private PerfectHashing(int m) {
            m = 1 << Log2.ceilLog(m);
            globalMask = m - 1;
            starts = new int[m];
            masks = new int[m];
            f2 = new HashFunction[m];
            for (int i = 0; i < m; i++) {
                f2[i] = FastUniversalHashFunction0.getInstance();
            }
        }

        public PerfectHashing(long[] keys, V[] values) {
            this(keys.length);
            init0(keys, values, new int[keys.length]);
        }

        private void init0(long[] keys, V[] values, int[] h1) {
            int n = keys.length;
            Arrays.fill(masks, 0);
            for (int i = 0; i < n; i++) {
                long k = keys[i];
                masks[h1[i] = ((int) f1.f(k) & globalMask)]++;
            }
            long total = 0;
            for (int i = 0; i < n; i++) {
                total += (1L << Log2.ceilLog((long) masks[i] * masks[i]));
            }
            //success hash
            if (total >= 5 * starts.length) {
                f1 = f1.upgrade();
                init0(keys, values, h1);
                return;
            }
            for (int i = 0; i < masks.length; i++) {
                starts[i] = masks[i];
                if (i > 0) {
                    starts[i] += starts[i - 1];
                }
            }
            int[] indices = new int[n];
            for (int i = 0; i < n; i++) {
                indices[--starts[h1[i]]] = i;
            }
            total = 0;
            for (int i = 0; i < starts.length; i++) {
                starts[i] = (int) total;
                masks[i] = (int) ((1L << Log2.ceilLog((long) masks[i] * masks[i])) - 1);
                total += masks[i] + 1;
            }
            K = new long[(int) total];
            V = new Object[(int) total];
            for (int i = 0; i < n; i++) {
                int l = i;
                int r = l;
                int h = h1[indices[l]];
                int L = starts[h];
                int M = masks[h];
                while (r + 1 < n && h == h1[indices[r + 1]]) {
                    r++;
                }
                i = r;
                while (true) {
                    Arrays.fill(V, L, L + M + 1, NIL);
                    boolean success = true;
                    HashFunction f = f2[h];
                    for (int j = l; j <= r; j++) {
                        int index = indices[j];
                        int h2 = L + ((int) f.f(keys[index]) & M);
                        if (V[h2] != NIL) {
                            success = false;
                            break;
                        }
                        V[h2] = values[index];
                        K[h2] = keys[index];
                    }

                    if (success) {
                        break;
                    }

                    f2[h] = f2[h].upgrade();
                }
            }
        }

        public Iterator<V> iterator() {
            return new Iterator<V>() {
                int cur = 0;


                public boolean hasNext() {
                    while (cur <= globalMask && V[cur] == NIL) {
                        cur++;
                    }
                    return cur <= globalMask;
                }


                public V next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return (V) V[cur];
                }
            };
        }

        public V getOrDefault(long key, V def) {
            int h1 = (int) f1.f(key) & globalMask;
            int h2 = masks[h1] == 0 ? 0 : ((int) f2[h1].f(key) & masks[h1]);
            int index = starts[h1] + h2;
            Object ans = V[index];
            return ans == NIL || K[index] != key ? def : (V) ans;
        }

        public V get(long key) {
            return getOrDefault(key, null);
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

    static class Log2 {
        public static int ceilLog(int x) {
            if (x <= 0) {
                return 0;
            }
            return 32 - Integer.numberOfLeadingZeros(x - 1);
        }

        public static int ceilLog(long x) {
            if (x <= 0) {
                return 0;
            }
            return 64 - Long.numberOfLeadingZeros(x - 1);
        }

    }

    static class FastUniversalHashFunction4 implements HashFunction {
        static final FastUniversalHashFunction4 INSTANCE = new FastUniversalHashFunction4();

        private FastUniversalHashFunction4() {
        }

        public long f(long x) {
            long ans = x * 2654435761L;
            return ans ^ (ans >>> 32);
        }

        public HashFunction upgrade() {
            return FastUniversalHashFunction5.INSTANCE;
            //        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
        }

    }

    static class RandomWrapper {
        private Random random;
        public static final RandomWrapper INSTANCE = new RandomWrapper();

        public RandomWrapper() {
            this(new Random());
        }

        public RandomWrapper(Random random) {
            this.random = random;
        }

        public RandomWrapper(long seed) {
            this(new Random(seed));
        }

        public long nextLong(long l, long r) {
            return nextLong(r - l + 1) + l;
        }

        public long nextLong(long n) {
            return Math.round(random.nextDouble() * (n - 1));
        }

    }

    static abstract class CloneSupportObject<T> implements Cloneable {
        public T clone() {
            try {
                return (T) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
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

        public FastOutput append(int c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(Object c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
        }

        public FastOutput println(Object c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append('\n');
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

    static class FastUniversalHashFunction3 implements HashFunction {
        static final FastUniversalHashFunction3 INSTANCE = new FastUniversalHashFunction3();

        private FastUniversalHashFunction3() {
        }

        public long f(long h) {
            h ^= h >>> 33;
            h *= 0xff51afd7ed558ccdL;
            h ^= h >>> 33;
            h *= 0xc4ceb9fe1a85ec53L;
            h ^= h >>> 33;
            return h;
        }

        public HashFunction upgrade() {
//        return FastUniversalHashFunction4.INSTANCE;
            FastUniversalHashFunction0.numberOfInstance--;
            return new UniversalHashFunction();
        }

    }

    static class FastUniversalHashFunction2 implements HashFunction {
        static final FastUniversalHashFunction2 INSTANCE = new FastUniversalHashFunction2();

        private FastUniversalHashFunction2() {
        }

        public long f(long x) {
            x = (x ^ (x >>> 30)) * 0xbf58476d1ce4e5b9L;
            x = (x ^ (x >>> 27)) * 0x94d049bb133111ebL;
            x = x ^ (x >>> 31);
            return x;
        }

        public HashFunction upgrade() {
            return FastUniversalHashFunction3.INSTANCE;
//        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
        }

    }

    static class UniversalHashFunction implements HashFunction {
        public static int numberOfInstance = 0;
        private static ILongModular modular = LongModular2305843009213693951.getInstance();
        private static long mod = modular.getMod();
        long a;
        long b;

        {
            numberOfInstance++;
            upgrade();
        }

        public long f(long x) {
            long ans = modular.mul(x, a);
            ans += b;
            if (ans >= mod) {
                ans -= mod;
            }
            return ans;
        }

        public HashFunction upgrade() {
            this.a = RandomWrapper.INSTANCE.nextLong(1, mod - 1);
            this.b = RandomWrapper.INSTANCE.nextLong(0, mod - 1);
            return this;
        }

    }

    static class FastUniversalHashFunction5 implements HashFunction {
        static final FastUniversalHashFunction5 INSTANCE = new FastUniversalHashFunction5();

        private FastUniversalHashFunction5() {
        }

        long xorshift(long n, int i) {
            return n ^ (n >>> i);
        }

        public long f(long x) {
            long p = 0x5555555555555555L; // pattern of alternating 0 and 1
            long c = 7316035218449499591L;// random uneven integer constant;
            return c * xorshift(p * xorshift(x, 32), 32);
        }

        public HashFunction upgrade() {
            return FastUniversalHashFunction1.INSTANCE;
            //        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
        }

    }

    static interface ILongModular {
        long getMod();

        long mul(long a, long b);

    }

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
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

        public String next() {
            return readString();
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

        public String rs() {
            return readString();
        }

        public String readString(StringBuilder builder) {
            skipBlank();

            while (next > 32) {
                builder.append((char) next);
                next = read();
            }

            return builder.toString();
        }

        public String readString() {
            defaultStringBuf.setLength(0);
            return readString(defaultStringBuf);
        }

    }

    static interface HashFunction {
        long f(long x);

        HashFunction upgrade();

    }

    static class FastLongHash {
        static ILongModular modular = LongModular2305843009213693951.getInstance();
        static long mod = modular.getMod();
        long x = RandomWrapper.INSTANCE.nextLong(1, mod - 1);

        public long hash(long x0, long x1) {
            long ans = modular.mul(x1, x) + x0;
            if (ans >= mod) {
                ans -= mod;
            }
            return ans;
        }

    }

    static class LongModular2305843009213693951 implements ILongModular {
        private static long mod = 2305843009213693951L;
        private static final LongModular2305843009213693951 INSTANCE = new LongModular2305843009213693951();
        private static long mask = (1L << 32) - 1;

        private LongModular2305843009213693951() {
        }

        public static final LongModular2305843009213693951 getInstance() {
            return INSTANCE;
        }

        public long getMod() {
            return mod;
        }

        public long mul(long a, long b) {
            long l1 = a & mask;
            long h1 = (a >> 32) & mask;
            long l2 = b & mask;
            long h2 = (b >> 32) & mask;
            long l = l1 * l2;
            long m = l1 * h2 + l2 * h1;
            long h = h1 * h2;
            long ret = (l & mod) + (l >>> 61) + (h << 3) + (m >>> 29) + ((m << 35) >>> 3) + 1;
            ret = (ret & mod) + (ret >>> 61);
            ret = (ret & mod) + (ret >>> 61);
            return ret - 1;
        }

    }

    static class E extends CloneSupportObject<E> {
        long a;
        long b;
        int index;
        int score;
        E prev;
        static FastLongHash fh = new FastLongHash();

        void init() {
            a = b = 0;
        }

        private int get(long x, int i) {
            return (int) ((x >>> (i << 3)) & 255);
        }

        private long set(long x, int i, long v) {
            i <<= 3;
            v &= 255;
            return (x & ~(255L << i)) | (v << i);
        }

        public int get(int i) {
            if (i < 6) {
                return get(a, i);
            }
            i -= 6;
            return get(b, i);
        }

        public void set(int i, int v) {
            if (i < 6) {
                a = set(a, i, v);
                return;
            }
            i -= 6;
            b = set(b, i, v);
        }

        long hash() {
            return fh.hash(a, b);
        }

    }

    static class FastUniversalHashFunction1 implements HashFunction {
        static final FastUniversalHashFunction1 INSTANCE = new FastUniversalHashFunction1();
        private final long time = System.nanoTime() + System.currentTimeMillis() * 31L;

        private FastUniversalHashFunction1() {
        }

        public long f(long z) {
            z += time;
            z = (z ^ (z >>> 33)) * 0x62a9d9ed799705f5L;
            z = (((z ^ (z >>> 28)) * 0xcb24d0a5c88c35b3L) >>> 32);
            return z;
        }

        public HashFunction upgrade() {
            return FastUniversalHashFunction2.INSTANCE;
//        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
        }

    }

    static class FastUniversalHashFunction0 implements HashFunction {
        public static long numberOfInstance = 0;
        static final FastUniversalHashFunction0 INSTANCE = new FastUniversalHashFunction0();

        public static FastUniversalHashFunction0 getInstance() {
            numberOfInstance++;
            return INSTANCE;
        }

        private FastUniversalHashFunction0() {
        }

        public long f(long x) {
            return x ^ (x >>> 32);
        }

        public HashFunction upgrade() {
            return FastUniversalHashFunction4.INSTANCE;
//        FastUniversalHashFunction0.numberOfInstance--;
//        return new UniversalHashFunction();
        }

    }
}

