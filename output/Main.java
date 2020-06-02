import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
            CPuttingBoxesTogether solver = new CPuttingBoxesTogether();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CPuttingBoxesTogether {
        static Modular mod = new Modular(1e9 + 7);
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int q = in.readInt();
            int[] a = new int[n + 1];
            int[] w = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                a[i] = in.readInt();
            }
            for (int i = 1; i <= n; i++) {
                w[i] = in.readInt();
            }

            Query query = new Query();
            State state = new State();
            Segment seg = new Segment(1, n, i -> w[i], i -> a[i]);
            for (int i = 0; i < q; i++) {
                int x = in.readInt();
                int y = in.readInt();
                if (x < 0) {
                    seg.update(-x, -x, 1, n, y);
                } else {
                    int l = x;
                    int r = y;
                    state.clear(0);
                    seg.query(l, r, 1, n, state);
                    long sumOfW = state.w;
                    query.reset(0, sumOfW);
                    seg.binarySearch(l, r, 1, n, query);
                    int index = query.index;
                    debug.debug("index", index);
                    long ans = 0;
                    state.clear(a[index]);
                    seg.query(l, index, 1, n, state);
                    ans += state.moveR;
                    state.clear(a[index]);
                    seg.query(index, r, 1, n, state);
                    ans += state.moveL;
                    out.println(mod.valueOf(ans));
                }
            }
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

    static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        State state = new State();

        private void modify(int w) {
            state.init(w);
        }

        public void pushUp() {
            state.merge(left.state, right.state);
        }

        public void pushDown() {
        }

        public Segment(int l, int r, IntToIntFunction func, IntToIntFunction index) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m, func, index);
                right = new Segment(m + 1, r, func, index);
                pushUp();
            } else {
                modify(func.apply(l));
                state.l = state.r = index.apply(l);
                state.size = 1;
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, int x) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                modify(x);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, State ans) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                ans.mergeRight(state);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.query(ll, rr, l, m, ans);
            right.query(ll, rr, m + 1, r, ans);
        }

        public void binarySearch(int ll, int rr, int l, int r, Query q) {
            if (noIntersection(ll, rr, l, r) || q.now * 2 >= q.total) {
                return;
            }
            if (covered(ll, rr, l, r) && (q.now + state.w) * 2 < q.total) {
                q.now += state.w;
                return;
            }
            if (l == r) {
                q.now += state.w;
                q.index = l;
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.binarySearch(ll, rr, l, m, q);
            right.binarySearch(ll, rr, m + 1, r, q);
        }

        private Segment deepClone() {
            Segment seg = clone();
            if (seg.left != null) {
                seg.left = seg.left.deepClone();
            }
            if (seg.right != null) {
                seg.right = seg.right.deepClone();
            }
            return seg;
        }

        protected Segment clone() {
            try {
                return (Segment) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        private void toString(StringBuilder builder) {
            if (left == null && right == null) {
                builder.append(state.w).append(",");
                return;
            }
            pushDown();
            left.toString(builder);
            right.toString(builder);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            deepClone().toString(builder);
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, int x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
        }

    }

    static class State {
        static Modular mod = new Modular(1e9 + 7);
        int l;
        int r;
        long w;
        int moveL;
        int moveR;
        int size;
        static State tmp = new State();

        public void clear(int i) {
            l = r = i;
            w = 0;
            moveL = moveR = 0;
            size = 0;
        }

        public void init(int w) {
            this.w = w;
            moveL = moveR = 0;
        }

        public void copy(State s) {
            l = s.l;
            r = s.r;
            w = s.w;
            moveL = s.moveL;
            moveR = s.moveR;
            size = s.size;
        }

        public void mergeRight(State r) {
            tmp.copy(this);
            merge(tmp, r);
        }

        public void merge(State a, State b) {
            l = a.l;
            r = b.r;
            w = a.w + b.w;
            size = a.size + b.size;
            moveL = mod.plus(a.moveL, b.moveL);
            moveL = mod.plus(moveL, mod.mul(mod.valueOf(b.w), b.l - a.l - a.size));
            moveR = mod.plus(a.moveR, b.moveR);
            moveR = mod.plus(moveR, mod.mul(mod.valueOf(a.w), b.r - a.r - b.size));
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

    static class Query {
        long now;
        long total;
        int index;

        public void reset(long now, long total) {
            this.now = now;
            this.total = total;
            index = -1;
        }

    }

    static interface IntToIntFunction {
        int apply(int x);

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
}

