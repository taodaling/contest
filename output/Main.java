import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.io.Writer;
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
            DCaptainAmerica solver = new DCaptainAmerica();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DCaptainAmerica {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int r = in.readInt();
            int b = in.readInt();
            char rChar = 'r';
            char bChar = 'b';
            if (r > b) {
                int tmp = r;
                r = b;
                b = tmp;

                char cTmp = rChar;
                rChar = bChar;
                bChar = cTmp;
            }

            Edge[] edges = new Edge[n];
            Map<Integer, Node> rows = new HashMap<>();
            Map<Integer, Node> cols = new HashMap<>();
            for (int i = 0; i < n; i++) {
                Node u = rows.computeIfAbsent(in.readInt(), x -> new Node());
                Node v = cols.computeIfAbsent(in.readInt(), x -> new Node());
                Edge e = new Edge();
                edges[i] = e;
                e.a = u;
                e.b = v;
                e.a.deg++;
                e.b.deg++;
            }

            for (int i = 0; i < m; i++) {
                int t = in.readInt();
                int l = in.readInt();
                int k = in.readInt();
                Node node;
                if (t == 1) {
                    node = rows.computeIfAbsent(l, x -> new Node());
                } else {
                    node = cols.computeIfAbsent(l, x -> new Node());
                }
                intersect(node.lr, bounds(node.deg, k));
                if (node.lr[0] > node.lr[1]) {
                    out.println(-1);
                    return;
                }
            }

            int idAlloc = 0;
            for (Node u : rows.values()) {
                u.id = idAlloc++;
            }
            for (Node v : cols.values()) {
                v.id = idAlloc++;
            }
            int src = idAlloc++;
            int dst = idAlloc++;
            List<IntegerLRFlowEdge>[] g = IntegerFlow.createLRFlow(dst + 1);

            for (Edge e : edges) {
                e.e = IntegerFlow.addLREdge(g, e.a.id, e.b.id, 1, 0);
            }

            for (Node node : rows.values()) {
                IntegerFlow.addLREdge(g, src, node.id, node.lr[1], node.lr[0]);
            }
            for (Node node : cols.values()) {
                IntegerFlow.addLREdge(g, node.id, dst, node.lr[1], node.lr[0]);
            }

            IntegerMaximumFlow flow = new IntegerHLPPBeta();
            boolean feasible = IntegerFlow.feasibleFlow(g, src, dst, flow);
            if (!feasible) {
                out.println(-1);
                return;
            }
            flow.apply((List[]) g, src, dst, (int) 1e9);
            long ans = 0;
            for (Edge e : edges) {
                if (e.e.flow == 1) {
                    ans += r;
                } else {
                    ans += b;
                }
            }

            out.println(ans);
            for (Edge e : edges) {
                if (e.e.flow == 1) {
                    out.append(rChar);
                } else {
                    out.append(bChar);
                }
            }
        }

        public void intersect(int[] a, int[] b) {
            a[0] = Math.max(a[0], b[0]);
            a[1] = Math.min(a[1], b[1]);
        }

        public int[] bounds(int n, int k) {
            //x-(n-x)<=k
            //(n-x)-x<=k
            //2x<=n+k
            //2x>=n-k
            return new int[]{DigitUtils.ceilDiv(n - k, 2),
                    DigitUtils.floorDiv(n + k, 2)};
        }

    }

    static interface IntegerMaximumFlow {
        int apply(List<IntegerFlowEdge>[] g, int s, int t, int send);

    }

    static class Node {
        int id;
        int[] lr = new int[]{0, (int) 1e8};
        int deg;

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

    static class Edge {
        Node a;
        Node b;
        IntegerFlowEdge e;

    }

    static class DirectedEdge {
        public int to;

        public DirectedEdge(int to) {
            this.to = to;
        }

        public String toString() {
            return "->" + to;
        }

    }

    static class IntegerFlowEdge<T extends IntegerFlowEdge<T>> extends DirectedEdge {
        public int flow;
        public boolean real;
        public T rev;

        public IntegerFlowEdge(int to, int flow, boolean real) {
            super(to);
            this.flow = flow;
            this.real = real;
        }

        public String toString() {
            return rev.to + "-[" + flow + "/" + (flow + rev.flow) + "]->" + to;
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
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

    static class IntegerLRFlowEdge extends IntegerFlowEdge<IntegerLRFlowEdge> {
        public int low;

        public IntegerLRFlowEdge(int to, int flow, boolean real, int low) {
            super(to, flow, real);
            this.low = low;
        }

    }

    static class IntegerHLPPBeta implements IntegerMaximumFlow {
        private List<IntegerFlowEdge>[] g;
        private ListIterator<IntegerFlowEdge>[] iterators;
        private int[] heights;
        private int[] excess;
        private int vertexNum;
        IntegerHLPPBeta.Node[] nodes;
        private int sink;
        private int source;

        public IntegerHLPPBeta() {
        }

        public void ensure(int vertexNum) {
            if (iterators != null && iterators.length >= vertexNum) {
                return;
            }
            iterators = new ListIterator[vertexNum];
            heights = new int[vertexNum];
            excess = new int[vertexNum];
            nodes = new IntegerHLPPBeta.Node[vertexNum];
            for (int i = 0; i < vertexNum; i++) {
                nodes[i] = new IntegerHLPPBeta.Node();
                nodes[i].val = i;
            }
        }

        public int send(int flow) {
            init(flow);
            relabelToFront();
            return excess[sink];
        }

        private void relabel(int root) {
            int minHeight = 2 * vertexNum;
            for (IntegerFlowEdge c : g[root]) {
                if (c.rev.flow > 0) {
                    minHeight = Math.min(minHeight, heights[c.to]);
                }
            }
            heights[root] = minHeight + 1;
        }

        private void discharge(int root) {
            while (excess[root] > 0) {
                if (!iterators[root].hasNext()) {
                    relabel(root);
                    iterators[root] = g[root].listIterator();
                    continue;
                }
                IntegerFlowEdge c = iterators[root].next();
                int remain;
                if ((remain = c.rev.flow) > 0 && heights[c.to] + 1 == heights[root]) {
                    int sent = Math.min(excess[root], remain);
                    excess[root] -= sent;
                    excess[c.to] += sent;
                    IntegerFlow.send(c, sent);
                    if (excess[root] == 0) {
                        iterators[root].previous();
                        return;
                    }
                }
            }
        }

        private void relabelToFront() {
            IntegerHLPPBeta.Node head = null;
            for (int i = 0; i < vertexNum; i++) {
                if (i == source || i == sink) {
                    continue;
                }
                nodes[i].next = head;
                head = nodes[i];
            }

            IntegerHLPPBeta.Node trace = head;
            IntegerHLPPBeta.Node pre = null;
            while (trace != null) {
                int i = trace.val;
                int oldHeight = heights[i];
                discharge(i);
                if (heights[i] != oldHeight) {
                    if (pre != null) {
                        pre.next = trace.next;
                        trace.next = head;
                        head = trace;
                    }
                    trace = head;
                }
                pre = trace;
                trace = trace.next;
            }
        }

        private void init(int flow) {
            for (int i = 0; i < vertexNum; i++) {
                nodes[i].next = null;
                heights[i] = 0;
                excess[i] = 0;
            }
            heights[source] = vertexNum;
            int sent = 0;
            for (IntegerFlowEdge c : g[source]) {
                int newFlow = Math.min(c.rev.flow, flow - sent);
                sent += newFlow;
                IntegerFlow.send(c, newFlow);
                excess[source] -= newFlow;
                excess[c.to] += newFlow;
            }

            for (int i = 0; i < vertexNum; i++) {
                iterators[i] = g[i].listIterator();
            }
        }

        public int apply(List<IntegerFlowEdge>[] g, int s, int t, int send) {
            ensure(g.length);
            vertexNum = g.length;
            this.g = g;
            this.source = s;
            this.sink = t;
            return send(send);
        }

        private static class Node {
            IntegerHLPPBeta.Node next;
            int val;

        }

    }

    static class IntegerFlow {
        public static <T extends IntegerFlowEdge> void send(T edge, int flow) {
            edge.flow += flow;
            edge.rev.flow -= flow;
        }

        public static IntegerFlowEdge addEdge(List<IntegerFlowEdge>[] g, int s, int t, int cap) {
            IntegerFlowEdge real = new IntegerFlowEdge(t, 0, true);
            IntegerFlowEdge virtual = new IntegerFlowEdge(s, cap, false);
            real.rev = virtual;
            virtual.rev = real;
            g[s].add(real);
            g[t].add(virtual);
            return real;
        }

        public static IntegerLRFlowEdge addLREdge(List<IntegerLRFlowEdge>[] g, int s, int t, int cap, int low) {
            IntegerLRFlowEdge real = new IntegerLRFlowEdge(t, 0, true, low);
            IntegerLRFlowEdge virtual = new IntegerLRFlowEdge(s, cap - low, false, low);
            real.rev = virtual;
            virtual.rev = real;
            g[s].add(real);
            g[t].add(virtual);
            return real;
        }

        public static List<IntegerLRFlowEdge>[] createLRFlow(int n) {
            return createGraph(n);
        }

        public static boolean feasibleFlow(List<IntegerLRFlowEdge>[] g, int s, int t, IntegerMaximumFlow mf) {
            addLREdge(g, t, s, Integer.MAX_VALUE / 4, 0);
            boolean ans = feasibleFlow(g, mf);
            g[s].remove(g[s].size() - 1);
            g[t].remove(g[t].size() - 1);
            return ans;
        }

        public static boolean feasibleFlow(List<IntegerLRFlowEdge>[] g, IntegerMaximumFlow mf) {
            int n = g.length;
            List<IntegerFlowEdge>[] expand = expand(g, n + 2);
            int src = n;
            int dst = n + 1;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < expand[i].size(); j++) {
                    IntegerFlowEdge fe = expand[i].get(j);
                    if (fe.to == src || fe.to == dst ||
                            !fe.real) {
                        continue;
                    }
                    IntegerLRFlowEdge e = (IntegerLRFlowEdge) fe;
                    addEdge(expand, src, e.to, e.low);
                    addEdge(expand, i, dst, e.low);
                }
            }

            mf.apply(expand, src, dst, Integer.MAX_VALUE / 4);

            boolean ans = true;
            for (IntegerFlowEdge e : expand[src]) {
                ans = ans && DigitUtils.equal(e.rev.flow, 0);
            }
            for (IntegerFlowEdge e : expand[dst]) {
                ans = ans && DigitUtils.equal(e.flow, 0);
            }

            for (int i = 0; i < n; i++) {
                while (g[i].size() > 0) {
                    IntegerFlowEdge tail = g[i].get(g[i].size() - 1);
                    if (tail.to == src || tail.to == dst) {
                        g[i].remove(g[i].size() - 1);
                    } else {
                        break;
                    }
                }
            }

            return ans;
        }

        private static List[] expand(List[] g, int n) {
            List[] ans = Arrays.copyOf(g, n);
            for (int i = g.length; i < n; i++) {
                ans[i] = new ArrayList();
            }
            return ans;
        }

        private static List[] createGraph(int n) {
            return IntStream.range(0, n).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[i]);
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int floorDiv(int a, int b) {
            return a < 0 ? -ceilDiv(-a, b) : a / b;
        }

        public static int ceilDiv(int a, int b) {
            if (a < 0) {
                return -floorDiv(-a, b);
            }
            int c = a / b;
            if (c * b < a) {
                return c + 1;
            }
            return c;
        }

        public static boolean equal(int a, int b) {
            return a == b;
        }

    }
}

