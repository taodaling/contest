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
            DLinearCongruentialGenerator solver = new DLinearCongruentialGenerator();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DLinearCongruentialGenerator {
        public int log(int x, int y) {
            int ans = 0;
            while (y != 0 && y % x == 0) {
                ans++;
                y /= x;
            }
            return ans;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int limit = (int) 2e6;
            IntegerMultiWayStack primeFactors = Factorization.factorizeRangePrime(limit);
            int[] cnts = new int[limit + 1];
            int[] times = new int[limit + 1];
            int[] xs = new int[n];

            in.populate(xs);
            Randomized.shuffle(xs);
            Arrays.sort(xs);
            SequenceUtils.reverse(xs);
            boolean[] retain = new boolean[n];
            for (int i = 0; i < n; i++) {
                int x = xs[i];
                if (cnts[x] == 0) {
                    retain[i] = true;
                    cnts[x] = 1;
                    times[x] = 1;
                    continue;
                }

                for (IntegerIterator iterator = primeFactors.iterator(x - 1); iterator.hasNext(); ) {
                    int p = iterator.next();
                    int log = log(p, x - 1);
                    if (cnts[p] < log) {
                        cnts[p] = log;
                        times[p] = 0;
                    }
                    if (cnts[p] == log) {
                        times[p]++;
                    }
                }
            }

            Modular mod = new Modular(1e9 + 7);
            int prod = 1;
            for (int i = 1; i <= limit; i++) {
                for (int j = 0; j < cnts[i]; j++) {
                    prod = mod.mul(prod, i);
                }
            }

            for (int i = 0; i < n; i++) {
                int x = xs[i];
                boolean unique = false;
                if (retain[i]) {
                    if (cnts[x] == 1 && times[x] == 1) {
                        unique = true;
                    }
                } else {
                    for (IntegerIterator iterator = primeFactors.iterator(x - 1); iterator.hasNext(); ) {
                        int p = iterator.next();
                        int log = log(p, x - 1);
                        if (cnts[p] == log && times[p] == 1) {
                            unique = true;
                        }
                    }
                }
                if (!unique) {
                    prod = mod.plus(prod, 1);
                    break;
                }
            }

            out.println(prod);
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

    static class RandomWrapper {
        private Random random;
        public static RandomWrapper INSTANCE = new RandomWrapper(new Random());

        public RandomWrapper() {
            this(new Random());
        }

        public RandomWrapper(Random random) {
            this.random = random;
        }

        public int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }

    static class Randomized {
        public static void shuffle(int[] data) {
            shuffle(data, 0, data.length - 1);
        }

        public static void shuffle(int[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                int tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return RandomWrapper.INSTANCE.nextInt(l, r);
        }

    }

    static class MinimumNumberWithMaximumFactors {
        private static int[] primes = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53};

        public static long[] maximumPrimeFactor(long n) {
            long[] ans = new long[2];
            ans[0] = 1;
            for (int i = 0; i < primes.length; i++) {
                if (DigitUtils.isMultiplicationOverflow(ans[0], primes[i], n)) {
                    break;
                }
                ans[0] *= primes[i];
                ans[1]++;
            }
            return ans;
        }

    }

    static class IntegerMultiWayStack {
        private int[] values;
        private int[] next;
        private int[] heads;
        private int alloc;
        private int stackNum;

        public IntegerIterator iterator(final int queue) {
            return new IntegerIterator() {
                int ele = heads[queue];


                public boolean hasNext() {
                    return ele != 0;
                }


                public int next() {
                    int ans = values[ele];
                    ele = next[ele];
                    return ans;
                }
            };
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            values = Arrays.copyOf(values, newSize);
        }

        public void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
        }

        public boolean isEmpty(int qId) {
            return heads[qId] == 0;
        }

        public IntegerMultiWayStack(int qNum, int totalCapacity) {
            values = new int[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            heads = new int[qNum];
            stackNum = qNum;
        }

        public void addLast(int qId, int x) {
            alloc();
            values[alloc] = x;
            next[alloc] = heads[qId];
            heads[qId] = alloc;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stackNum; i++) {
                if (isEmpty(i)) {
                    continue;
                }
                builder.append(i).append(": ");
                for (IntegerIterator iterator = iterator(i); iterator.hasNext(); ) {
                    builder.append(iterator.next()).append(",");
                }
                if (builder.charAt(builder.length() - 1) == ',') {
                    builder.setLength(builder.length() - 1);
                }
                builder.append('\n');
            }
            return builder.toString();
        }

    }

    static class Factorization {
        public static IntegerMultiWayStack factorizeRangePrime(int n) {
            int maxFactorCnt = (int) MinimumNumberWithMaximumFactors.maximumPrimeFactor(n)[1];
            IntegerMultiWayStack stack = new IntegerMultiWayStack(n + 1, n * maxFactorCnt);
            boolean[] isComp = new boolean[n + 1];
            for (int i = 2; i <= n; i++) {
                if (isComp[i]) {
                    continue;
                }
                for (int j = i; j <= n; j += i) {
                    isComp[j] = true;
                    stack.addLast(j, i);
                }
            }
            return stack;
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

    static class SequenceUtils {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static void reverse(int[] data, int l, int r) {
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
        }

        public static void reverse(int[] data) {
            reverse(data, 0, data.length - 1);
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

        public void populate(int[] data) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readInt();
            }
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

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }
}

