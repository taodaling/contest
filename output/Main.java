import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.Random;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.Comparator;
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
            Bolero solver = new Bolero();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Bolero {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            Good[] goods = new Good[n];
            for (int i = 0; i < n; i++) {
                goods[i] = new Good();
                goods[i].p = in.readInt();
                goods[i].d = 100 - in.readInt();
            }

            int[] discounts = new int[100 + 1];
            Arrays.fill(discounts, n + 1);
            discounts[100] = 0;
            for (int i = 0; i < m; i++) {
                int k = in.readInt();
                int p = 100 - in.readInt();
                discounts[p] = Math.min(discounts[p], k);
            }

            long min = (long) 1e18;
            for (int i = 0; i <= 100; i++) {
                if (discounts[i] > n) {
                    continue;
                }
                for (Good good : goods) {
                    good.profit = (good.d - i) * good.p;
                }
                int l = 0;
                int r = n - 1;
                while (l <= r) {
                    if (goods[r].profit < 0) {
                        r--;
                    } else {
                        SequenceUtils.swap(goods, l, r);
                        l++;
                    }
                }

                if (l < discounts[i]) {
                    //need more
                    int k = discounts[i] - l;
                    CompareUtils.theKthSmallestElement(goods, (a, b) -> -Integer.compare(a.profit, b.profit), l, n, k);
                    l += k;
                }

                long sum = 0;
                for (int j = 0; j < n; j++) {
                    if (j < l) {
                        sum += goods[j].p * i;
                    } else {
                        sum += goods[j].p * goods[j].d;
                    }
                }

                min = Math.min(sum, min);
            }

            out.println(min / 100D);
        }

    }

    static class SequenceUtils {
        public static <T> void swap(T[] data, int i, int j) {
            T tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
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

    static class CompareUtils {
        private static final int THRESHOLD = 4;

        private CompareUtils() {
        }

        public static <T> void insertSort(T[] data, Comparator<T> cmp, int l, int r) {
            for (int i = l + 1; i <= r; i++) {
                int j = i;
                T val = data[i];
                while (j > l && cmp.compare(data[j - 1], val) > 0) {
                    data[j] = data[j - 1];
                    j--;
                }
                data[j] = val;
            }
        }

        public static <T> T theKthSmallestElement(T[] data, Comparator<T> cmp, int f, int t, int k) {
            if (t - f <= THRESHOLD) {
                insertSort(data, cmp, f, t - 1);
                return data[f + k - 1];
            }
            SequenceUtils.swap(data, f, Randomized.nextInt(f, t - 1));
            int l = f;
            int r = t;
            int m = l + 1;
            while (m < r) {
                int c = cmp.compare(data[m], data[l]);
                if (c == 0) {
                    m++;
                } else if (c < 0) {
                    SequenceUtils.swap(data, l, m);
                    l++;
                    m++;
                } else {
                    SequenceUtils.swap(data, m, --r);
                }
            }
            if (l - f >= k) {
                return theKthSmallestElement(data, cmp, f, l, k);
            } else if (m - f >= k) {
                return data[l];
            }
            return theKthSmallestElement(data, cmp, m, t, k - (m - f));
        }

    }

    static class Good {
        int p;
        int d;
        int profit;

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

        public FastOutput append(double c) {
            cache.append(new BigDecimal(c).toPlainString());
            return this;
        }

        public FastOutput println(double c) {
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

    static class Randomized {
        public static int nextInt(int l, int r) {
            return RandomWrapper.INSTANCE.nextInt(l, r);
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
}

