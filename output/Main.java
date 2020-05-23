import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
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
            GGearUp solver = new GGearUp();
            try {
                int testNumber = 1;
                while (true)
                    solver.solve(testNumber++, in, out);
            } catch (UnknownError e) {
                out.close();
            }
        }
    }

    static class GGearUp {
        double ln2 = Math.log(2);
        int order = 0;
        Node[] seq = new Node[100000 + 1];

        public void solve(int testNumber, FastInput in, FastOutput out) {
            if (!in.hasMore()) {
                //debug.debug("2^30", 1 << 31);
                throw new UnknownError();
            }

            order = 0;
            out.printf("Case #%d:", testNumber).println();
            int n = in.readInt();
            int m = in.readInt();
            int q = in.readInt();
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
                nodes[i].r = Log2.floorLog(in.readInt());
            }
            for (int i = 0; i < m; i++) {
                Edge e = new Edge();
                e.linear = in.readInt();
                e.a = nodes[in.readInt() - 1];
                e.b = nodes[in.readInt() - 1];
                e.a.adj.add(e);
                e.b.adj.add(e);
            }

            for (Node node : nodes) {
                if (node.visited) {
                    continue;
                }
                dfs(node, null, node, true, 0);
            }

            Segment seg = new Segment(1, order, i -> seq[i].ps);

            for (int i = 0; i < q; i++) {
                int a = in.readInt();
                Node x = nodes[in.readInt() - 1];
                int y = Log2.floorLog(in.readInt());
                int cur = seg.query(x.levelL, x.levelL, 1, order);
                if (a == 1) {
                    //replace
                    if (x.special) {
                        int delta = (-y) - (-cur);
                        seg.update(x.levelL, x.levelR, 1, order, delta);
                        seg.update(x.subL + 1, x.subR, 1, order, -delta);
                    } else {
                        int delta = y - cur;
                        seg.update(x.subL + 1, x.subR, 1, order, delta);
                    }
                } else {
                    //query
                    Node root = x.top;
                    int rootVelocity = y - cur;
                    int max = seg.query(root.levelL, root.levelR, 1, order);
                    max += rootVelocity;
                    out.printf("%.3f", max * ln2).println();
                }
            }
        }

        public void dfs(Node root, Node p, Node top, boolean type, int ps) {
            root.visited = true;
            root.special = type;
            root.top = top;
            root.adj.sort((a, b) -> a.linear - b.linear);
            root.ps = ps;
            root.levelR = root.subR = root.subL = root.levelL = ++order;
            seq[root.subL] = root;
            for (Edge e : root.adj) {
                Node node = e.other(root);
                if (node == p) {
                    continue;
                }
                if (e.linear == 1) {
                    dfs(node, root, top, true, ps + root.r - node.r);
                    root.subR = root.levelR = order;
                } else {
                    dfs(node, root, top, false, ps);
                    root.levelR = order;
                }
            }
        }

    }

    static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        int max;
        int plusTag;

        private void modify(int x) {
            plusTag += x;
            max += x;
        }

        public void pushUp() {
            max = Math.max(left.max, right.max);
        }

        public void pushDown() {
            if (plusTag != 0) {
                left.modify(plusTag);
                right.modify(plusTag);
                plusTag = 0;
            }
        }

        public Segment(int l, int r, IntToIntFunction func) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m, func);
                right = new Segment(m + 1, r, func);
                pushUp();
            } else {
                max = func.apply(l);
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

        public int query(int ll, int rr, int l, int r) {
            if (noIntersection(ll, rr, l, r)) {
                return (int) -1e8;
            }
            if (covered(ll, rr, l, r)) {
                return max;
            }
            pushDown();
            int m = (l + r) >> 1;
            return Math.max(left.query(ll, rr, l, m),
                    right.query(ll, rr, m + 1, r));
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
                builder.append(max).append(",");
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

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
            return this;
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

    static class Node {
        List<Edge> adj = new ArrayList<>();
        int r;
        Node top;
        boolean visited = false;
        int subL;
        int subR;
        int levelL;
        int levelR;
        boolean special;
        int ps;
        int id;

        public String toString() {
            return "" + id;
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

        public boolean hasMore() {
            skipBlank();
            return next != -1;
        }

    }

    static class Edge {
        Node a;
        Node b;
        int linear;

        Node other(Node x) {
            return a == x ? b : a;
        }

    }

    static class Log2 {
        public static int floorLog(int x) {
            return 31 - Integer.numberOfLeadingZeros(x);
        }

    }

    static interface IntToIntFunction {
        int apply(int x);

    }
}

