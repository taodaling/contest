import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 29);
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
            CQuarantine solver = new CQuarantine();
            int testCount = Integer.parseInt(in.next());
            for (int i = 1; i <= testCount; i++)
                solver.solve(i, in, out);
            out.close();
        }
    }

    static class CQuarantine {
        Debug debug = new Debug(true);
        Tuple best;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            out.printf("Case #%d: ", testNumber);
            debug.debug("testNumber", testNumber);
            int n = in.readInt();
            int k = in.readInt();
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
                nodes[i].color = in.readChar() == '#' ? 0 : 1;
            }
            int[] p = new int[n + 1];
            for (int i = 2; i <= k + 1; i++) {
                p[i] = in.readInt();
            }
            long a = in.readInt();
            long b = in.readInt();
            long c = in.readInt();
            for (int i = k + 2; i <= n; i++) {
                p[i] = (int) ((a * p[i - 2] + b * p[i - 1] + c) % (i - 1) + 1);
            }
            for (int i = 2; i <= n; i++) {
                Node fa = nodes[p[i] - 1];
                Node child = nodes[i - 1];
                addEdge(fa, child);
            }

            long pairs = 0;
            long way = 0;
            int block = 0;
            for (Node node : nodes) {
                if (node.color == 0 || node.visited) {
                    continue;
                }
                block++;
                dfsForSize(node, null);
                dfsForCost(node, null, node.size);
                pairs += countPair(node.size);
            }

            if (block <= 1) {
                bruteForceForSize(nodes[0], null);
                way = bruteForceForWay(nodes[0], null, nodes[0].size, nodes[0].size1);
            } else {
                nodes[0].adj.add(null);
                dfsForTuple(nodes[0], null);
                best = new Tuple();
                upDown(nodes[0], null, 0, new Tuple());

                pairs += best.size;
                way = best.cnt;
            }
            out.append(pairs).append(' ').println(way);
        }

        public long bruteForceForWay(Node root, Edge p, long total, long total1) {
            long ans = 0;
            for (Edge e : root.adj) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                ans += bruteForceForWay(node, e, total, total1);
                if (e.a.color == 1 && e.b.color == 1) {
                    ans += node.size1 * (total1 - node.size1);
                } else {
                    ans += node.size * (total - node.size);
                }
            }
            return ans;
        }

        public void bruteForceForSize(Node root, Edge p) {
            root.size1 = root.color;
            root.size = 1;
            for (Edge e : root.adj) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                bruteForceForSize(node, e);
                root.size1 += node.size1;
                root.size += node.size;
            }
        }

        public void addEdge(Node a, Node b) {
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            a.adj.add(e);
            b.adj.add(e);
        }

        public long countPair(long n) {
            return n * (n - 1) / 2;
        }

        public void dfsForSize(Node root, Edge p) {
            root.visited = true;
            root.size = 1;
            for (Edge e : root.adj) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                if (node.color == 0) {
                    continue;
                }
                dfsForSize(node, e);
                root.size += node.size;
            }
        }

        public void dfsForCost(Node root, Edge p, int total) {
            for (Edge e : root.adj) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                if (node.color == 0) {
                    continue;
                }
                e.cost = countPair(total) - countPair(total - node.size) -
                        countPair(node.size);
                dfsForCost(node, e, total);
            }
        }

        public void dfsForTuple(Node root, Edge p) {
            root.curSize = root.color;
            for (Edge e : root.adj) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                dfsForTuple(node, e);
                root.tuple.update(node.tuple);
                if (root.color == 1) {
                    root.curSize += node.curSize;
                } else {
                    root.tuple.update(node.curSize, 1);
                }
            }
        }

        public void upDown(Node root, Edge p, int curSize, Tuple fromP) {
            //two way
            if (root.color == 1) {
                curSize += root.curSize;
                int n = root.adj.size();
                Tuple[] l2r = new Tuple[n];
                Tuple[] r2l = new Tuple[n];
                for (int i = 0; i < n; i++) {
                    Edge e = root.adj.get(i);
                    if (e == p) {
                        r2l[i] = l2r[i] = fromP;
                    } else {
                        r2l[i] = l2r[i] = e.other(root).tuple;
                    }
                }
                for (int i = 1; i < n; i++) {
                    l2r[i] = Tuple.merge(l2r[i - 1], l2r[i]);
                }
                for (int i = n - 2; i >= 0; i--) {
                    r2l[i] = Tuple.merge(r2l[i + 1], r2l[i]);
                }

                for (int i = 0; i < n; i++) {
                    Edge e = root.adj.get(i);
                    if (e == p) {
                        continue;
                    }
                    Node node = e.other(root);
                    //remove edge e
                    Tuple up = new Tuple();
                    if (i > 0) {
                        up.update(l2r[i - 1]);
                    }
                    if (i + 1 < n) {
                        up.update(r2l[i + 1]);
                    }
                    //recursive
                    upDown(node, e, curSize - node.curSize, up);

                    up.update(curSize - node.curSize, 1);
                    Tuple down = node.tuple.clone();
                    down.update(node.curSize, 1);

                    best.update(up.size * down.size - e.cost,
                            up.cnt * down.cnt * up.size * down.size);
                }
            } else {
                if (p != null) {
                    fromP = fromP.clone();
                    fromP.update(curSize, 1);
                }
                int n = root.adj.size();
                Tuple[] l2r = new Tuple[n];
                Tuple[] r2l = new Tuple[n];
                for (int i = 0; i < n; i++) {
                    Edge e = root.adj.get(i);
                    if (e == p) {
                        r2l[i] = l2r[i] = fromP;
                    } else {
                        r2l[i] = l2r[i] = e.other(root).tuple.clone();
                        r2l[i].update(e.other(root).curSize, 1);
                    }
                }
                for (int i = 1; i < n; i++) {
                    l2r[i] = Tuple.merge(l2r[i - 1], l2r[i]);
                }
                for (int i = n - 2; i >= 0; i--) {
                    r2l[i] = Tuple.merge(r2l[i + 1], r2l[i]);
                }

                for (int i = 0; i < n; i++) {
                    Edge e = root.adj.get(i);
                    if (e == p) {
                        continue;
                    }
                    Node node = e.other(root);
                    //remove edge e
                    Tuple up = new Tuple();
                    if (i > 0) {
                        up.update(l2r[i - 1]);
                    }
                    if (i + 1 < n) {
                        up.update(r2l[i + 1]);
                    }
                    //recursive
                    upDown(node, e, 0, up);

                    up.update(0, 1);
                    Tuple down = node.tuple.clone();
                    down.update(node.curSize, 1);

                    best.update(up.size * down.size - e.cost,
                            up.cnt * down.cnt * up.size * down.size);
                }
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
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

    static class Node {
        boolean visited;
        List<Edge> adj = new ArrayList<>();
        int color;
        int curSize;
        Tuple tuple = new Tuple();
        int size;
        int size1;
        int id;

        public String toString() {
            return "" + (id + 1);
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

    static class Edge {
        Node a;
        Node b;
        long cost;

        Node other(Node x) {
            return a == x ? b : a;
        }

    }

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
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

        public String next() {
            return readString();
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

        public char readChar() {
            skipBlank();
            char c = (char) next;
            next = read();
            return c;
        }

        public String readString(StringBuilder builder) {
            skipBlank();

            while (next > 32) {
                builder.append((char) next);
                next = read();
            }

            return builder.toString();
        }

        public String readString() {
            defaultStringBuf.setLength(0);
            return readString(defaultStringBuf);
        }

    }

    static class Tuple implements Cloneable {
        long size;
        long cnt;

        public static Tuple merge(Tuple a, Tuple b) {
            Tuple ans = new Tuple();
            ans.update(a);
            ans.update(b);
            return ans;
        }

        public void update(Tuple tuple) {
            update(tuple.size, tuple.cnt);
        }

        public void update(long size, long cnt) {
            if (this.size < size) {
                this.size = size;
                this.cnt = 0;
            }
            if (this.size == size) {
                this.cnt += cnt;
            }
        }

        public Tuple clone() {
            try {
                return (Tuple) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            return String.format("(%d, %d)", size, cnt);
        }

    }
}

