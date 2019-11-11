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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskE {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            long d = in.readInt();
            long[] a = new long[n + 1];
            long[] leftCost = new long[n + 1];
            long[] rightCost = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                a[i] = in.readInt();
                leftCost[i] = a[i] - i * d;
                rightCost[i] = a[i] + i * d;
            }

            Segment left = new Segment(1, n, leftCost);
            Segment right = new Segment(1, n, rightCost);

            left.update(1, 1, 1, n, -leftCost[1]);


            long ans = 0;
            for (int i = 1; i <= n; i++) {
                Recorder forLeft = new Recorder();
                Recorder forRight = new Recorder();
                left.query(1, n, 1, n, forLeft);
                right.query(1, n, 1, n, forRight);

                if (forLeft.val > forRight.val) {
                    Recorder tmp = forLeft;
                    forLeft = forRight;
                    forRight = tmp;
                }

                ans += forLeft.val;
                left.kill(forLeft.index, forLeft.index, 1, n);
                right.kill(forLeft.index, forLeft.index, 1, n);
                left.update(1, forLeft.index - 1, 1, n, rightCost[forLeft.index]);
                right.update(forLeft.index + 1, n, 1, n, leftCost[forLeft.index]);
            }

            out.println(ans);
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

        public FastOutput println(long c) {
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
    static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private long a;
        private long b = (long) 1e18;
        private long min = a + b;
        private int index;
        private int minAIndex;

        private void kill() {
            a = (long) 1e18;
            min = a + b;
        }

        public void updateB(long x) {
            b = Math.min(b, x);
            if (a + b < min) {
                min = a + b;
                if (left != null) {
                    if (a == left.a) {
                        index = left.minAIndex;
                    } else {
                        index = right.minAIndex;
                    }
                }
            }
        }

        public void pushUp() {
            min = Math.min(left.min, right.min);
            a = Math.min(left.a, right.a);
            if (min == left.min) {
                index = left.index;
            } else {
                index = right.index;
            }
            if (a == left.a) {
                minAIndex = left.minAIndex;
            } else {
                minAIndex = right.minAIndex;
            }
        }

        public void pushDown() {
            left.updateB(b);
            right.updateB(b);
        }

        public Segment(int l, int r, long[] a) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m, a);
                right = new Segment(m + 1, r, a);
                pushUp();
            } else {
                this.a = a[l];
                this.min = this.a + b;
                minAIndex = index = l;
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, long x) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                updateB(x);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        public void kill(int ll, int rr, int l, int r) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                kill();
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.kill(ll, rr, l, m);
            right.kill(ll, rr, m + 1, r);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, Recorder ans) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                ans.add(min, index);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.query(ll, rr, l, m, ans);
            right.query(ll, rr, m + 1, r, ans);
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
    static class Recorder {
        long val;
        int index;
        {
            prepare();
        }

        public void prepare() {
            val = (long) 1e18;
            index = -1;
        }

        public void add(long v, int i) {
            if (v < val) {
                val = v;
                index = i;
            }
        }

        public String toString() {
            return String.format("%d=%d", index, val);
        }

    }
}

