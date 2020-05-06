import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            DRsumReview solver = new DRsumReview();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DRsumReview {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            long k = in.readLong();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.readInt();
            }
            int[] top = new int[n];

            long topSum = 0;
            for (int i = 0; i < n; i++) {
                double pos = Math.sqrt(a[i] / 3.0);
                int up = (int) Math.ceil(pos);
                int bot = (int) Math.floor(pos);
                top[i] = up;
                if (apply(a[i], top[i]) < apply(a[i], bot)) {
                    top[i] = bot;
                }

                topSum += top[i];
            }

            int[] cur;

            if (topSum >= k) {
                LongBinarySearch lbs = new LongBinarySearch() {

                    public boolean check(long mid) {
                        int cnt = 0;
                        for (int i = 0; i < n; i++) {
                            int index = bs(a[i], mid, 0, top[i]);
                            if (diff(a[i], index - 1) < mid) {
                                index--;
                            }
                            cnt += index;
                        }
                        return cnt >= k;
                    }
                };

                long mid = lbs.binarySearch((long) -4e18, (long) 4e18);
                cur = new int[n];
                for (int i = 0; i < n; i++) {
                    int index = bs(a[i], mid, 0, top[i]);
                    long d = diff(a[i], index - 1);
                    if (diff(a[i], index - 1) < mid) {
                        index--;
                    }
                    cur[i] = index;
                }

                fix(a, cur, k, mid);
            } else {
                LongBinarySearch lbs = new LongBinarySearch() {

                    public boolean check(long mid) {
                        mid = -mid;
                        int cnt = 0;
                        for (int i = 0; i < n; i++) {
                            int index = bs(a[i], mid, top[i], a[i]);
                            if (diff(a[i], index - 1) < mid) {
                                index--;
                            }
                            cnt += index;
                        }
                        return cnt >= k;
                    }
                };

                long mid = -lbs.binarySearch((long) -4e18, (long) 4e18);
                cur = new int[n];
                for (int i = 0; i < n; i++) {
                    int index = bs(a[i], mid, top[i], a[i]);
                    if (diff(a[i], index - 1) < mid) {
                        index--;
                    }
                    cur[i] = index;
                }

                fix(a, cur, k, mid);
            }

            for (int i = 0; i < n; i++) {
                out.append(cur[i]).append(' ');
            }
        }

        public void fix(int[] a, int[] cur, long k, long d) {
            int n = cur.length;
            long sum = 0;
            for (int i = 0; i < n; i++) {
                sum += cur[i];
            }

            for (int i = 0; i < n && sum > k; i++) {
                while (sum > k && diff(a[i], cur[i] - 1) == d) {
                    sum--;
                    cur[i]--;
                }
            }
        }

        public long apply(long a, long b) {
            return b * (a - b * b);
        }

        public long diff(long a, long b) {
            //b * (a - (b+1)^2)
            return a - 3 * b * b - 3 * b - 1;
        }

        public int bs(int a, long step, int l, int r) {
            while (l < r) {
                int m = (l + r + 1) >>> 1;
                if (diff(a, m - 1) >= step) {
                    l = m;
                } else {
                    r = m - 1;
                }
            }

            return l;
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

        public long readLong() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            long val = 0;
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

    static abstract class LongBinarySearch {
        public abstract boolean check(long mid);

        public long binarySearch(long l, long r) {
            if (l > r) {
                throw new IllegalArgumentException();
            }
            while (l < r) {
                long mid = DigitUtils.average(l, r);
                if (check(mid)) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            return l;
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(1 << 20);
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

    static class DigitUtils {
        private DigitUtils() {
        }

        public static long average(long a, long b) {
            if (Long.signum(a) != Long.signum(b)) {
                return a + b;
            }
            if (a >= 0) {
                return (a + b) >>> 1;
            } else {
                return -(((-a) + (-b)) >>> 1);
            }
        }

    }
}

