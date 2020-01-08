import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
            ESegmentsOnTheLine solver = new ESegmentsOnTheLine();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class ESegmentsOnTheLine {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int s = in.readInt();
            int m = in.readInt();
            int k = in.readInt();

            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.readInt();
            }
            Interval[] intervals = new Interval[s];

            int total = 0;
            for (int i = 0; i < s; i++) {
                intervals[i] = new Interval();
                intervals[i].l = in.readInt() - 1;
                intervals[i].r = in.readInt() - 1;
                total += intervals[i].r - intervals[i].l + 1;
            }

            if (total < k) {
                out.println(-1);
                return;
            }

            IntBinarySearch ibs = new IntBinarySearch() {

                public boolean check(int mid) {
                    for (Interval interval : intervals) {
                        interval.w = 0;
                        for (int i = interval.l; i <= interval.r; i++) {
                            if (a[i] <= mid) {
                                interval.w++;
                            }
                        }
                    }
                    Arrays.sort(intervals, (a, b) -> -Integer.compare(a.w, b.w));
                    int count = 0;
                    for (int i = 0; i < m; i++) {
                        count += intervals[i].w;
                    }

                    return count >= k;
                }
            };

            int ans = ibs.binarySearch(1, (int) 1e9);
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

    static class Interval {
        int l;
        int r;
        int w;

    }

    static abstract class IntBinarySearch {
        public abstract boolean check(int mid);

        public int binarySearch(int l, int r) {
            if (l > r) {
                throw new IllegalArgumentException();
            }
            while (l < r) {
                int mid = (l + r) >>> 1;
                if (check(mid)) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            return l;
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
}

