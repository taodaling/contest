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
            FNRE solver = new FNRE();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FNRE {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            long[] val = new long[n + 1];
            int sum = 0;
            for (int i = 1; i <= n; i++) {
                int x = in.readInt();
                val[i] = x == 1 ? 1 : -1;
                sum += x;
            }
            int q = in.readInt();
            IntervalPickProblem.Interval[] intervals = new IntervalPickProblem.Interval[q];
            for (int i = 0; i < q; i++) {
                intervals[i] = new IntervalPickProblem.Interval(in.readInt(), in.readInt());
            }
            int minus = (int) IntervalPickProblem.solve(val, intervals);
            sum -= minus;
            out.println(sum);
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

    static class IntervalPickProblem {
        public static long solve(long[] data, IntervalPickProblem.Interval[] intervals) {
            LongPreSum lps = new LongPreSum(data);
            Arrays.sort(intervals, (a, b) -> a.l == b.l ? a.r - b.r : a.l - b.l);
            int n = intervals.length;
            long[] dp = new long[n];
            int[] last = new int[n];

            IntervalPickProblem.LongSegmentQuery query = new IntervalPickProblem.LongSegmentQuery();
            int m = data.length;
            IntervalPickProblem.LongSegment lower = new IntervalPickProblem.LongSegment(0, m);
            IntervalPickProblem.LongSegment upper = new IntervalPickProblem.LongSegment(0, m);

            for (int i = 0; i < n; i++) {
                IntervalPickProblem.Interval now = intervals[i];
                dp[i] = lps.intervalSum(now.l, now.r);
                last[i] = -1;

                query.reset();
                lower.query(0, now.l - 1, 0, m, query);
                if (query.val + lps.intervalSum(now.l, now.r) > dp[i]) {
                    dp[i] = query.val + lps.intervalSum(now.l, now.r);
                    last[i] = query.index;
                }
                query.reset();
                upper.query(now.l, now.r, 0, m, query);
                if (query.val + lps.prefix(now.r) > dp[i]) {
                    dp[i] = query.val + lps.prefix(now.r);
                    last[i] = query.index;
                }

                lower.update(now.r, now.r, 0, m, dp[i], i);
                upper.update(now.r, now.r, 0, m, dp[i] - lps.prefix(now.r), i);
            }

            int maxIndex = 0;
            for (int i = 0; i < n; i++) {
                if (dp[i] > dp[maxIndex]) {
                    maxIndex = i;
                }
            }

            if (dp[maxIndex] < 0) {
                return 0;
            }
            int trace = maxIndex;
            while (trace >= 0) {
                intervals[trace].used = true;
                trace = last[trace];
            }

            return dp[maxIndex];
        }

        public static class Interval {
            public final int l;
            public final int r;
            public boolean used;

            public Interval(int l, int r) {
                this.l = l;
                this.r = r;
            }

            public String toString() {
                return String.format("(%d, %d)", l, r);
            }

        }

        private static class LongSegmentQuery {
            int index;
            long val;

            public void reset() {
                index = -1;
                val = -IntervalPickProblem.LongSegment.inf;
            }

            public void update(int index, long val) {
                if (this.val < val) {
                    this.index = index;
                    this.val = val;
                }
            }

        }

        private static class LongSegment implements Cloneable {
            private static long inf = (long) 2e18;
            private IntervalPickProblem.LongSegment left;
            private IntervalPickProblem.LongSegment right;
            private long val = -inf;
            private int index = -1;

            public void pushUp() {
                val = Math.max(left.val, right.val);
                if (val == left.val) {
                    index = left.index;
                } else {
                    index = right.index;
                }
            }

            public void pushDown() {
            }

            public LongSegment(int l, int r) {
                if (l < r) {
                    int m = (l + r) >> 1;
                    left = new IntervalPickProblem.LongSegment(l, m);
                    right = new IntervalPickProblem.LongSegment(m + 1, r);
                    pushUp();
                } else {
                }
            }

            private boolean covered(int ll, int rr, int l, int r) {
                return ll <= l && rr >= r;
            }

            private boolean noIntersection(int ll, int rr, int l, int r) {
                return ll > r || rr < l;
            }

            public void update(int ll, int rr, int l, int r, long x, int index) {
                if (noIntersection(ll, rr, l, r)) {
                    return;
                }
                if (covered(ll, rr, l, r)) {
                    if (x > val) {
                        this.val = x;
                        this.index = index;
                    }
                    return;
                }
                pushDown();
                int m = (l + r) >> 1;
                left.update(ll, rr, l, m, x, index);
                right.update(ll, rr, m + 1, r, x, index);
                pushUp();
            }

            public void query(int ll, int rr, int l, int r, IntervalPickProblem.LongSegmentQuery query) {
                if (noIntersection(ll, rr, l, r)) {
                    return;
                }
                if (covered(ll, rr, l, r)) {
                    query.update(index, val);
                    return;
                }
                pushDown();
                int m = (l + r) >> 1;
                left.query(ll, rr, l, m, query);
                right.query(ll, rr, m + 1, r, query);
            }

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

    static class LongPreSum {
        private long[] pre;
        private int n;

        public LongPreSum(int n) {
            pre = new long[n];
        }

        public void populate(long[] a) {
            n = a.length;
            pre[0] = a[0];
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] + a[i];
            }
        }

        public LongPreSum(long[] a) {
            this(a.length);
            populate(a);
        }

        public long intervalSum(int l, int r) {
            return prefix(r) - prefix(l - 1);
        }

        public long prefix(int i) {
            if (i < 0) {
                return 0;
            }
            return pre[Math.min(i, n - 1)];
        }

    }
}

