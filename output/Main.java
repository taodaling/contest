import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.Random;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in Actual solution is at the top
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
        int[] heights;
        int[][] f;
        int[] g;
        int n;
        DiscreteMap dm;
        int m;
        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        NumberTheory.Power pow = new NumberTheory.Power(mod);

        public int f(int i, int j) {
            if (f[i][j] == -1) {
                f[i][j] = 1;
                int r = dm.rankOf(heights[i]);
                if (j >= r) {
                    return f[i][j] = 0;
                }
                int lastR = dm.rankOf(heights[i - 1]);

                if (lastR >= r) {
                    // inherit
                    f[i][j] = mod.subtract(f(i - 1, j), f(i - 1, r));
                    return f[i][j];
                }

                if (j <= lastR) {
                    // inherit
                    f[i][j] = mod.mul(f(i - 1, j), pow.pow(2, heights[i] - heights[i - 1]));

                    if (j < lastR) {
                        int plus = mod.plus(g(i - 1), 2);
                        plus = mod.mul(plus, mod.subtract(pow.pow(2, heights[i] - heights[i - 1]), 1));
                        f[i][j] = mod.plus(plus, f[i][j]);
                    }
                } else {
                    // self built
                    f[i][j] = mod.mul(2, g(i - 1));
                    f[i][j] = mod.mul(f[i][j], mod.subtract(pow.pow(2, heights[i] - dm.iThElement(j)), 1));
                }
            }

            return f[i][j];
        }

        public int g(int i) {
            if (g[i] == -1) {
                int r = dm.rankOf(heights[i]);
                int lastR = dm.rankOf(heights[i - 1]);
                if (r >= lastR) {
                    g[i] = mod.mul(2, g(i - 1));
                } else {
                    g[i] = mod.mul(2, mod.plus(f(i - 1, r), g(i - 1)));
                }
            }
            return g[i];
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            heights = new int[n];
            for (int i = 0; i < n; i++) {
                heights[i] = in.readInt();
            }

            IntList list = new IntList();
            list.addAll(heights);
            list.add(0);
            dm = new DiscreteMap(list.toArray());
            m = dm.maxRank();

            f = new int[n][m + 1];
            g = new int[n];
            SequenceUtils.deepFill(f, -1);
            SequenceUtils.deepFill(g, -1);
            g[0] = 2;
            for (int i = dm.minRank(), r = dm.rankOf(heights[0]); i <= r; i++) {
                if (i == r) {
                    f[0][i] = 0;
                } else if (dm.iThElement(i) == 0) {
                    f[0][i] = mod.mul(1, mod.subtract(pow.pow(2, heights[0] - dm.iThElement(i)), 2));
                } else {
                    f[0][i] = mod.mul(2, mod.subtract(pow.pow(2, heights[0] - dm.iThElement(i)), 1));
                }
            }

            int ans = mod.plus(f(n - 1, 0), g(n - 1));
            out.println(ans);
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
    static class IntList {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntList(IntList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntList() {
            this(0);
        }

        public void ensureSpace(int need) {
            int req = size + need;
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        public void add(int x) {
            ensureSpace(1);
            data[size++] = x;
        }

        public void addAll(int[] x) {
            addAll(x, 0, x.length);
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntList)) {
                return false;
            }
            IntList other = (IntList) obj;
            return SequenceUtils.equal(data, other.data, 0, size - 1, 0, other.size - 1);
        }

    }
    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput println(int c) {
            cache.append(c).append('\n');
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
    static class NumberTheory {
        public static class Modular {
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

            public int subtract(int x, int y) {
                return valueOf(x - y);
            }

            public String toString() {
                return "mod " + m;
            }

        }

        public static class Power {
            final NumberTheory.Modular modular;

            public Power(NumberTheory.Modular modular) {
                this.modular = modular;
            }

            public int pow(int x, long n) {
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

        public static boolean equal(int[] a, int[] b, int al, int ar, int bl, int br) {
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
    static class DiscreteMap {
        int[] val;
        int f;
        int t;

        public DiscreteMap(int[] val) {
            this(val, 0, val.length);
        }

        public DiscreteMap(int[] val, int f, int t) {
            Randomized.randomizedArray(val, f, t);
            Arrays.sort(val, f, t);
            int wpos = f + 1;
            for (int i = f + 1; i < t; i++) {
                if (val[i] == val[i - 1]) {
                    continue;
                }
                val[wpos++] = val[i];
            }
            this.val = val;
            this.f = f;
            this.t = wpos;
        }

        public int rankOf(int x) {
            return Arrays.binarySearch(val, f, t, x) - f;
        }

        public int iThElement(int i) {
            return val[f + i];
        }

        public int minRank() {
            return 0;
        }

        public int maxRank() {
            return t - f - 1;
        }

        public String toString() {
            return Arrays.toString(Arrays.copyOfRange(val, f, t));
        }

    }
    static class Randomized {
        static Random random = new Random();

        public static void randomizedArray(int[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                int tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }
}

