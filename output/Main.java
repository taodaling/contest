import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.io.Closeable;
import java.io.Writer;
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
            P1578 solver = new P1578();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P1578 {
        Debug debug = new Debug(false);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int L = in.ri();
            int W = in.ri();
            int n = in.ri();
            long[] x = new long[n];
            long[] y = new long[n];
            for (int i = 0; i < n; i++) {
                x[i] = in.ri();
                y[i] = in.ri();
            }
            Pair<long[], Long> pair = MaximumArea.rectCover(0, L, 0, W, x, y);
            out.println(pair.b);
            debug.debug("x", pair.a);
        }

    }

    static strictfp class MersenneTwisterFast implements Serializable, Cloneable {
        private static final int N = 624;
        private static final int M = 397;
        private static final int MATRIX_A = 0x9908b0df;
        private static final int UPPER_MASK = 0x80000000;
        private static final int LOWER_MASK = 0x7fffffff;
        private static final int TEMPERING_MASK_B = 0x9d2c5680;
        private static final int TEMPERING_MASK_C = 0xefc60000;
        private int[] mt;
        private int mti;
        private int[] mag01;
        private boolean __haveNextNextGaussian;

        public Object clone() {
            try {
                MersenneTwisterFast f = (MersenneTwisterFast) (super.clone());
                f.mt = (int[]) (mt.clone());
                f.mag01 = (int[]) (mag01.clone());
                return f;
            } catch (CloneNotSupportedException e) {
                throw new InternalError();
            } // should never happen
        }

        public MersenneTwisterFast() {
            this(System.currentTimeMillis());
        }

        public MersenneTwisterFast(long seed) {
            setSeed(seed);
        }

        public MersenneTwisterFast(int[] array) {
            setSeed(array);
        }

        public void setSeed(long seed) {
            // Due to a bug in java.util.Random clear up to 1.2, we're
            // doing our own Gaussian variable.
            __haveNextNextGaussian = false;

            mt = new int[N];

            mag01 = new int[2];
            mag01[0] = 0x0;
            mag01[1] = MATRIX_A;

            mt[0] = (int) (seed & 0xffffffff);
            for (mti = 1; mti < N; mti++) {
                mt[mti] =
                        (1812433253 * (mt[mti - 1] ^ (mt[mti - 1] >>> 30)) + mti);
                /* See Knuth TAOCP Vol2. 3rd Ed. P.106 for multiplier. */
                /* In the previous versions, MSBs of the seed affect   */
                /* only MSBs of the array mt[].                        */
                /* 2002/01/09 modified by Makoto Matsumoto             */
                // mt[mti] &= 0xffffffff;
                /* for >32 bit machines */
            }
        }

        public void setSeed(int[] array) {
            if (array.length == 0)
                throw new IllegalArgumentException("Array length must be greater than zero");
            int i, j, k;
            setSeed(19650218);
            i = 1;
            j = 0;
            k = (N > array.length ? N : array.length);
            for (; k != 0; k--) {
                mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1664525)) + array[j] + j; /* non linear */
                // mt[i] &= 0xffffffff; /* for WORDSIZE > 32 machines */
                i++;
                j++;
                if (i >= N) {
                    mt[0] = mt[N - 1];
                    i = 1;
                }
                if (j >= array.length) j = 0;
            }
            for (k = N - 1; k != 0; k--) {
                mt[i] = (mt[i] ^ ((mt[i - 1] ^ (mt[i - 1] >>> 30)) * 1566083941)) - i; /* non linear */
                // mt[i] &= 0xffffffff; /* for WORDSIZE > 32 machines */
                i++;
                if (i >= N) {
                    mt[0] = mt[N - 1];
                    i = 1;
                }
            }
            mt[0] = 0x80000000; /* MSB is 1; assuring non-zero initial array */
        }

        public int nextInt(int n) {
            if (n <= 0)
                throw new IllegalArgumentException("n must be positive, got: " + n);

            if ((n & -n) == n)  // i.e., n is a power of 2
            {
                int y;

                if (mti >= N)   // generate N words at one time
                {
                    int kk;
                    final int[] mt = this.mt; // locals are slightly faster
                    final int[] mag01 = this.mag01; // locals are slightly faster

                    for (kk = 0; kk < N - M; kk++) {
                        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
                    }
                    for (; kk < N - 1; kk++) {
                        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
                    }
                    y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                    mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

                    mti = 0;
                }

                y = mt[mti++];
                y ^= y >>> 11;                          // TEMPERING_SHIFT_U(y)
                y ^= (y << 7) & TEMPERING_MASK_B;       // TEMPERING_SHIFT_S(y)
                y ^= (y << 15) & TEMPERING_MASK_C;      // TEMPERING_SHIFT_T(y)
                y ^= (y >>> 18);                        // TEMPERING_SHIFT_L(y)

                return (int) ((n * (long) (y >>> 1)) >> 31);
            }

            int bits, val;
            do {
                int y;

                if (mti >= N)   // generate N words at one time
                {
                    int kk;
                    final int[] mt = this.mt; // locals are slightly faster
                    final int[] mag01 = this.mag01; // locals are slightly faster

                    for (kk = 0; kk < N - M; kk++) {
                        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
                    }
                    for (; kk < N - 1; kk++) {
                        y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
                        mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
                    }
                    y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
                    mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];

                    mti = 0;
                }

                y = mt[mti++];
                y ^= y >>> 11;                          // TEMPERING_SHIFT_U(y)
                y ^= (y << 7) & TEMPERING_MASK_B;       // TEMPERING_SHIFT_S(y)
                y ^= (y << 15) & TEMPERING_MASK_C;      // TEMPERING_SHIFT_T(y)
                y ^= (y >>> 18);                        // TEMPERING_SHIFT_L(y)

                bits = (y >>> 1);
                val = bits % n;
            } while (bits - val + (n - 1) < 0);
            return val;
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 1 << 13;
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

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(Object c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(Object c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append(System.lineSeparator());
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

    static class MaximumArea {
        public static Pair<long[], Long> rectCover(long l, long r, long b, long t,
                                                   long[] xs, long[] ys) {
            int n = xs.length;
            for (int i = 0; i < n; i++) {
                xs[i] = Math.max(xs[i], l);
                xs[i] = Math.min(xs[i], r);
                ys[i] = Math.max(ys[i], b);
                ys[i] = Math.min(ys[i], t);
            }
            long[][] pts = new long[n + 2][2];
            LongArrayList allX = new LongArrayList(n + 2);
            allX.add(l);
            allX.add(r);
            allX.addAll(xs);
            allX.unique();
            long[] allXArray = allX.getData();
            for (int i = 0; i < n; i++) {
                pts[i][0] = allX.binarySearch(xs[i]);
                pts[i][1] = ys[i];
            }
            pts[n][0] = 0;
            pts[n][1] = b;
            pts[n + 1][0] = 0;
            pts[n + 1][1] = t;
            n += 2;

            Arrays.sort(pts, Comparator.comparingLong(x -> x[1]));
            int m = allX.size();
            int[] L = new int[m];
            int[] R = new int[m];
            int[] cnt = new int[m];
            long bestArea = 0;
            long[] ans = new long[4];
            for (int i = 0; i < n; i++) {
                int to = i;
                while (to + 1 < n && pts[to + 1][1] == pts[i][1]) {
                    to++;
                }
                i = to;
                for (int j = i + 1; j < n; j++) {
                    cnt[(int) pts[j][0]]++;
                }
                long ll = l;
                long rr = l;
                {
                    int left = 0;
                    for (int j = 1; j < m; j++) {
                        if (cnt[j] > 0 || j == m - 1) {
                            long cand = allXArray[j] - allXArray[left];
                            if (rr - ll < cand) {
                                rr = allXArray[j];
                                ll = allXArray[left];
                            }
                            L[j] = left;
                            R[left] = j;
                            left = j;
                        }
                    }
                }
                for (int j = n - 1; j > i; j--) {
                    int x = (int) pts[j][0];
                    cnt[x]--;
                    if (cnt[x] == 0) {
                        //merge
                        L[R[x]] = L[x];
                        R[L[x]] = R[x];
                        long cand = allXArray[R[x]] - allXArray[L[x]];
                        if (rr - ll < cand) {
                            ll = allXArray[L[x]];
                            rr = allXArray[R[x]];
                        }
                    }
                    long cand = (rr - ll) * (pts[j][1] - pts[i][1]);
                    if (cand > bestArea) {
                        bestArea = cand;
                        ans[0] = ll;
                        ans[1] = rr;
                        ans[2] = pts[i][1];
                        ans[3] = pts[j][1];
                    }
                }
            }

            return new Pair<>(ans, bestArea);
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

    static class Randomized {
        public static void shuffle(long[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                long tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return RandomWrapper.INSTANCE.nextInt(l, r);
        }

    }

    static class Pair<A, B> implements Cloneable {
        public A a;
        public B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public String toString() {
            return "a=" + a + ",b=" + b;
        }

        public int hashCode() {
            return a.hashCode() * 31 + b.hashCode();
        }

        public boolean equals(Object obj) {
            Pair<A, B> casted = (Pair<A, B>) obj;
            return Objects.equals(casted.a, a) && Objects.equals(casted.b, b);
        }

        public Pair<A, B> clone() {
            try {
                return (Pair<A, B>) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    static class RandomWrapper {
        private MersenneTwisterFast random;
        public static final RandomWrapper INSTANCE = new RandomWrapper();

        public RandomWrapper() {
            this(new MersenneTwisterFast());
        }

        public RandomWrapper(MersenneTwisterFast random) {
            this.random = random;
        }

        public RandomWrapper(long seed) {
            this(new MersenneTwisterFast(seed));
        }

        public int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }

    static class LongArrayList implements Cloneable {
        private int size;
        private int cap;
        private long[] data;
        private static final long[] EMPTY = new long[0];

        public long[] getData() {
            return data;
        }

        public LongArrayList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new long[cap];
            }
        }

        public LongArrayList(long[] data) {
            this(0);
            addAll(data);
        }

        public LongArrayList(LongArrayList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public LongArrayList() {
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

        public void addAll(long[] x) {
            addAll(x, 0, x.length);
        }

        public void addAll(long[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(LongArrayList list) {
            addAll(list.data, 0, list.size);
        }

        public void sort() {
            if (size <= 1) {
                return;
            }
            Randomized.shuffle(data, 0, size);
            Arrays.sort(data, 0, size);
        }

        public void unique() {
            if (size <= 1) {
                return;
            }

            sort();
            int wpos = 1;
            for (int i = 1; i < size; i++) {
                if (data[i] != data[wpos - 1]) {
                    data[wpos++] = data[i];
                }
            }
            size = wpos;
        }

        public int binarySearch(long x) {
            return Arrays.binarySearch(data, 0, size, x);
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
            if (!(obj instanceof LongArrayList)) {
                return false;
            }
            LongArrayList other = (LongArrayList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Long.hashCode(data[i]);
            }
            return h;
        }

        public LongArrayList clone() {
            LongArrayList ans = new LongArrayList();
            ans.addAll(this);
            return ans;
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
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

