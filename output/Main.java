import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.Deque;
import java.util.OptionalInt;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
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
            FCube solver = new FCube();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FCube {
        int mod = 998244353;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            Group<Perm> g = new Group<>(false, new Perm(IntStream.range(0, 6).toArray()), (a, b) -> new Perm(merge(a.p, b.p)));
            g.add(new Perm(4, 5, 2, 3, 1, 0));
            g.add(new Perm(2, 3, 1, 0, 4, 5));

            long S = in.rl() - 6;
            long sum = 0;
            for (Perm p : g) {
                int[] info = extractParameter(p.p);
                long contrib = way(info, S);
                sum += contrib;
            }

            sum %= mod;
            long divG = DigitUtils.modInverse(g.size(), mod);
            long ans = sum * divG % mod;
            out.println(ans);
        }

        long way(int[] a, long m) {
            int max = Arrays.stream(a).max().getAsInt();
            int n = a.length;
            int[][] mat = new int[max * n][max * n];
            ArrayIndex ai = new ArrayIndex(max, n);
            for (int i = 1; i < max; i++) {
                for (int j = 0; j < n; j++) {
                    mat[ai.indexOf(i - 1, j)][ai.indexOf(i, j)] = 1;
                }
            }
            for (int j = 0; j < n; j++) {
                for (int state = 0; state <= j; state++) {
                    mat[ai.indexOf(max - 1, j)][ai.indexOf(max - 1 - a[j] + 1, state)] = 1;
                }
            }
            ModMatrix A = new ModMatrix((i, j) -> mat[i][j], max * n, max * n);
            int[] initVec = new int[max * n];
            initVec[ai.indexOf(max - 1, 0)] = 1;
            ModMatrix Am = ModMatrix.pow(A, m, mod);
            ModMatrix x = new ModMatrix(initVec, 1);
            ModMatrix Amx = ModMatrix.mul(Am, x, mod);

            long ans = 0;
            for (int i = 0; i < n; i++) {
                ans += Amx.get(ai.indexOf(max - 1, i), 0);
            }

            ans %= mod;
            return ans;
        }

        int[] extractParameter(int[] p) {
            IntegerArrayList res = new IntegerArrayList(p.length);
            int n = p.length;
            boolean[] visited = new boolean[n];
            for (int i = 0; i < n; i++) {
                if (visited[i]) {
                    continue;
                }
                int cnt = 0;
                int go = i;
                while (!visited[go]) {
                    visited[go] = true;
                    go = p[go];
                    cnt++;
                }
                res.add(cnt);
            }

            return res.toArray();
        }

        int[] merge(int[] a, int[] b) {
            int n = a.length;
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                ans[i] = b[a[i]];
            }
            return ans;
        }

    }

    static class SequenceUtils {
        public static void swap(long[] data, int i, int j) {
            long tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
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

        public long rl() {
            return readLong();
        }

        public long readLong() {
            boolean rev = false;

            skipBlank();
            if (next == '+' || next == '-') {
                rev = next == '-';
                next = read();
            }

            long val = 0;
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }

            return rev ? val : -val;
        }

    }

    static class ArrayIndex {
        int[] dimensions;

        public ArrayIndex(int... dimensions) {
            this.dimensions = dimensions;
        }

        public int indexOf(int a, int b) {
            return a * dimensions[1] + b;
        }

    }

    static class Barrett {
        private final int mod;
        private final long h;
        private final long l;
        private final long MAX = 1L << 62;
        private final int MASK = (1 << 31) - 1;

        public Barrett(final int mod) {
            this.mod = mod;
            final long t = MAX / mod;
            h = t >>> 31;
            l = t & MASK;
        }

        public int reduce(final long x) {
            final long xh = x >>> 31, xl = x & MASK;
            long z = xl * l;
            z = xl * h + xh * l + (z >>> 31);
            z = xh * h + (z >>> 31);
            final int ret = (int) (x - z * mod);
            return ret >= mod ? ret - mod : ret;
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

    static class ModMatrix {
        int[] mat;
        int m;

        public ModMatrix(ModMatrix model) {
            m = model.m;
            mat = model.mat.clone();
        }

        public ModMatrix(int n, int m) {
            this.m = m;
            mat = new int[n * m];
        }

        public ModMatrix(int[] mat, int m) {
            assert mat.length % m == 0;
            this.mat = mat;
            this.m = m;
        }

        public ModMatrix(Int2ToIntegerFunction func, int n, int m) {
            this.mat = new int[n * m];
            this.m = m;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    mat[i * m + j] = func.apply(i, j);
                }
            }
        }

        public int getHeight() {
            return mat.length / m;
        }

        public int getWidth() {
            return m;
        }

        public boolean square() {
            return getHeight() == getWidth();
        }

        public void fill(int v) {
            Arrays.fill(mat, v);
        }

        public void asStandard(int mod) {
            fill(0);
            if (1 % mod == 0) {
                return;
            }
            for (int i = 0; i < mat.length; i += m + 1) {
                mat[i] = 1;
            }
        }

        public int get(int i, int j) {
            return mat[i * m + j];
        }

        public static ModMatrix mul(ModMatrix a, ModMatrix b, int mod) {
            assert a.getWidth() == b.getHeight();
            int h = a.getHeight();
            int mid = a.getWidth();
            int w = b.getWidth();
            ModMatrix c = new ModMatrix(h, w);

            SumOfModMul sm = new SumOfModMul(mod);
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    sm.clear();
                    for (int k = 0; k < mid; k++) {
                        sm.add(a.mat[i * mid + k], b.mat[k * w + j]);
                    }
                    c.mat[i * w + j] = sm.sum();
                }
            }
            return c;
        }

        public static ModMatrix pow(ModMatrix x, long n, int mod) {
            if (n == 0) {
                assert x.square();
                ModMatrix r = new ModMatrix(x.getHeight(), x.getWidth());
                r.asStandard(mod);
                return r;
            }
            ModMatrix r = pow(x, n >> 1, mod);
            r = ModMatrix.mul(r, r, mod);
            if (n % 2 == 1) {
                r = ModMatrix.mul(r, x, mod);
            }
            return r;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < getHeight(); i++) {
                for (int j = 0; j < getWidth(); j++) {
                    builder.append(mat[i * m + j]).append(' ');
                }
                builder.append('\n');
            }
            return builder.toString();
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

    static class SumOfModMul {
        long sum;
        int mod;
        long limit;
        long sub;
        Barrett barrett;

        public SumOfModMul(int mod) {
            this.mod = mod;
            barrett = new Barrett(mod);
            limit = Long.MAX_VALUE - (long) (mod - 1) * (mod - 1);
            sub = limit - limit % mod;
        }

        public void clear() {
            sum = 0;
        }

        public void add(int a, int b) {
            sum += (long) a * b;
            if (sum >= limit) {
                sum -= sub;
            }
        }

        public int sum() {
            if (sum >= mod) {
                sum = barrett.reduce(sum);
            }
            return (int) sum;
        }

        public String toString() {
            return "" + sum;
        }

    }

    static interface Int2ToIntegerFunction {
        int apply(int a, int b);

    }

    static class Perm {
        int[] p;

        public Perm(int... p) {
            this.p = p;
        }

        public boolean equals(Object obj) {
            Perm operand = (Perm) obj;
            return Arrays.equals(p, operand.p);
        }

        public int hashCode() {
            return Arrays.hashCode(p);
        }

        public String toString() {
            return Arrays.toString(p);
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

        public FastOutput append(long c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(long c) {
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

    static class Group<E> implements Iterable<E> {
        BiFunction<E, E, E> add;
        E identity;
        Set<E> set = new HashSet<>();
        List<E> added = new ArrayList<>();
        Deque<E> cand = new ArrayDeque<>();
        boolean commutative;

        public Group(boolean commutative, E identity, BiFunction<E, E, E> add) {
            this.add = add;
            this.identity = identity;
            this.commutative = commutative;
            add(identity);
        }

        public void add(E v) {
            if (set.add(v)) {
                cand.add(v);
            }
            consume();
        }

        private void consume() {
            while (!cand.isEmpty()) {
                E first = cand.removeFirst();
                added.add(first);
                for (E x : added) {
                    E y = add.apply(x, first);
                    if (set.add(y)) {
                        cand.add(y);
                    }
                    if (!commutative) {
                        E z = add.apply(first, x);
                        if (set.add(z)) {
                            cand.add(z);
                        }
                    }
                }
            }
        }

        public Iterator<E> iterator() {
            return added.iterator();
        }

        public int size() {
            return added.size();
        }

        public String toString() {
            return added.toString();
        }

    }

    static class IntegerArrayList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntegerArrayList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntegerArrayList(int[] data) {
            this(0);
            addAll(data);
        }

        public IntegerArrayList(IntegerArrayList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntegerArrayList() {
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

        public void add(int x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public void addAll(int[] x) {
            addAll(x, 0, x.length);
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntegerArrayList list) {
            addAll(list.data, 0, list.size);
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntegerArrayList)) {
                return false;
            }
            IntegerArrayList other = (IntegerArrayList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Integer.hashCode(data[i]);
            }
            return h;
        }

        public IntegerArrayList clone() {
            IntegerArrayList ans = new IntegerArrayList();
            ans.addAll(this);
            return ans;
        }

    }

    static class DigitUtils {
        private static LongExtGCDObject longExtGCDObject = new LongExtGCDObject();

        private DigitUtils() {
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
}

