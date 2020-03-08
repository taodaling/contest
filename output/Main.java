import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.TreeMap;
import java.io.Closeable;
import java.util.Map.Entry;
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
            DMangaMarket solver = new DMangaMarket();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DMangaMarket {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            long t = in.readInt();
            List<Store> nonZero = new ArrayList<>(n);
            List<Store> zero = new ArrayList<>(n);

            for (int i = 0; i < n; i++) {
                int a = in.readInt();
                int b = in.readInt();
                Store store = new Store();
                store.a = a;
                store.b = b;
                if (a == 0) {
                    zero.add(store);
                } else {
                    nonZero.add(store);
                }
            }

            zero.sort((a, b) -> Long.compare(a.b, b.b));
            nonZero.sort((a, b) -> Long.compare((a.b + 1) * b.a, (b.b + 1) * a.a));
            int m = nonZero.size();
            long[][] dp = new long[m + 1][30];
            long inf = (long) 1e18;
            SequenceUtils.deepFill(dp, inf);
            dp[0][0] = 0;
            for (int i = 1; i <= m; i++) {
                Store s = nonZero.get(i - 1);
                for (int j = 0; j < 30; j++) {
                    dp[i][j] = dp[i - 1][j];
                    if (j > 0) {
                        dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1] + 1 + DigitUtils.mul(dp[i - 1][j - 1] + 1, s.a, inf, inf) + s.b);
                    }
                }
            }

            long sum = 0;
            TreeMap<Long, Integer> map = new TreeMap<>();
            for (int i = 0; i < zero.size(); i++) {
                Store s = zero.get(i);
                sum += s.b + 1;
                map.put(sum, i + 1);
            }

            long ans = 0;
            for (int i = 0; i < 30; i++) {
                if (dp[m][i] > t) {
                    continue;
                }
                Map.Entry<Long, Integer> floor = map.floorEntry(t - dp[m][i]);
                ans = Math.max(ans, i + (floor == null ? 0 : floor.getValue()));
            }

            out.println(ans);
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

        public static long mul(long a, long b, long limit, long overflowVal) {
            return isMultiplicationOverflow(a, b, limit) ? overflowVal : a * b;
        }

    }

    static class Store {
        long a;
        long b;

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

    static class SequenceUtils {
        public static void deepFill(Object array, long val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof long[]) {
                long[] longArray = (long[]) array;
                Arrays.fill(longArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
                }
            }
        }

    }
}

