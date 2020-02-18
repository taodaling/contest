import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
            EMUL solver = new EMUL();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class EMUL {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            long[] weights = new long[n];
            long sum = 0;
            for (int i = 0; i < n; i++) {
                weights[i] = -in.readInt();
                sum -= weights[i];
            }
            LongMaximumCloseSubGraph mcsg = new LongMaximumCloseSubGraphAdapter(
                    new LongISAP(n + 2));
            boolean[] picked = new boolean[n];
            List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
            for (int i = 0; i < n; i++) {
                for (int j = (i + 1) * 2; j <= n; j += i + 1) {
                    Graph.addEdge(g, i, j - 1);
                }
            }
            long ans = mcsg.maximumCloseSubGraph(g, weights, picked);
            out.println(sum + ans);
        }

    }

    static interface LongMaximumFlow {
        long apply(List<LongFlowEdge>[] g, int s, int t);

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

        public FastOutput println(long c) {
            cache.append(c);
            println();
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

    static interface IntegerDeque extends IntegerStack {
        int removeFirst();

        void clear();

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

    static class LongFlow {
        public static boolean[] findSetST(List<LongFlowEdge>[] g, int s) {
            boolean[] visited = new boolean[g.length];
            dfs(g, visited, s);
            return visited;
        }

        private static void dfs(List<LongFlowEdge>[] g, boolean[] visited, int root) {
            if (visited[root]) {
                return;
            }
            visited[root] = true;
            for (LongFlowEdge e : g[root]) {
                if (e.rev.flow > 0) {
                    dfs(g, visited, e.to);
                }
            }
        }

        public static <T extends LongFlowEdge> void send(List<T>[] g, T edge, long flow) {
            edge.flow += flow;
            edge.rev.flow -= flow;
        }

        public static LongFlowEdge addEdge(List<LongFlowEdge>[] g, int s, int t, long cap) {
            LongFlowEdge real = new LongFlowEdge(t, 0, true);
            LongFlowEdge virtual = new LongFlowEdge(s, cap, false);
            real.rev = virtual;
            virtual.rev = real;
            g[s].add(real);
            g[t].add(virtual);
            return real;
        }

        public static List<LongFlowEdge>[] createFlow(int n) {
            List<LongFlowEdge>[] g = new List[n];
            for (int i = 0; i < n; i++) {
                g[i] = new ArrayList<>();
            }
            return g;
        }

        public static <T extends LongFlowEdge> void bfsForFlow(List<T>[] g, int s, int[] dist, int inf, IntegerDeque deque) {
            Arrays.fill(dist, 0, g.length, inf);
            dist[s] = 0;
            deque.clear();
            deque.addLast(s);
            while (!deque.isEmpty()) {
                int head = deque.removeFirst();
                for (T e : g[head]) {
                    if (e.flow > 0 && dist[e.to] == inf) {
                        dist[e.to] = dist[head] + 1;
                        deque.addLast(e.to);
                    }
                }
            }
        }

    }

    static class LongMaximumCloseSubGraphAdapter implements LongMaximumCloseSubGraph {
        private final LongMaximumFlow mf;
        private static final long INF = (long) 2e18;

        public LongMaximumCloseSubGraphAdapter(LongMaximumFlow mf) {
            this.mf = mf;
        }

        public long maximumCloseSubGraph(List<DirectedEdge>[] g, long[] weights, boolean[] picked) {
            int n = g.length;
            int s = n;
            int t = n + 1;
            List<LongFlowEdge>[] net = LongFlow.createFlow(n + 2);
            long sumOfPositive = 0;
            for (int i = 0; i < n; i++) {
                if (weights[i] > 0) {
                    LongFlow.addEdge(net, s, i, weights[i]);
                    sumOfPositive += weights[i];
                }
                if (weights[i] < 0) {
                    LongFlow.addEdge(net, i, t, -weights[i]);
                }
            }
            for (int i = 0; i < n; i++) {
                for (DirectedEdge e : g[i]) {
                    LongFlow.addEdge(net, i, e.to, INF);
                }
            }
            long minCut = mf.apply(net, s, t);
            boolean[] belongToS = LongFlow.findSetST(net, s);
            System.arraycopy(belongToS, 0, picked, 0, n);
            return sumOfPositive - minCut;
        }

    }

    static class LongFlowEdge extends DirectedEdge {
        public long flow;
        public boolean real;
        public LongFlowEdge rev;

        public LongFlowEdge(int to, long flow, boolean real) {
            super(to);
            this.flow = flow;
            this.real = real;
        }

    }

    static class LongISAP implements LongMaximumFlow {
        private List<LongFlowEdge>[] net;
        private int s;
        private int t;
        private int[] dists;
        private int[] cnts;
        private int n;
        private boolean exit;
        private IntegerDeque deque;

        public LongISAP(int vertexNum) {
            dists = new int[vertexNum];
            cnts = new int[vertexNum + 2];
            deque = new IntegerDequeImpl(n);
        }

        private long send(int root, long flow) {
            if (root == t) {
                return flow;
            }
            long snapshot = flow;
            for (LongFlowEdge e : net[root]) {
                long remain;
                if (dists[e.to] + 1 != dists[root] || (remain = e.rev.flow) == 0) {
                    continue;
                }
                long sent = send(e.to, Math.min(flow, remain));
                flow -= sent;
                LongFlow.send(net, e, sent);
                if (flow == 0 || exit) {
                    break;
                }
            }
            if (flow == snapshot) {
                cnts[dists[root]]--;
                dists[root]++;
                cnts[dists[root]]++;
                if (cnts[dists[root] - 1] == 0) {
                    exit = true;
                }
            }
            return snapshot - flow;
        }

        public long apply(List<LongFlowEdge>[] g, int s, int t) {
            this.net = g;
            this.s = s;
            this.t = t;
            this.exit = false;
            n = g.length;
            LongFlow.bfsForFlow(g, t, dists, n + 1, deque);
            Arrays.fill(cnts, 0, n + 2, 0);
            for (int d : dists) {
                cnts[d]++;
            }
            long flow = 0;
            while (!exit && dists[s] < n) {
                flow += send(s, Long.MAX_VALUE);
            }
            return flow;
        }

    }

    static class Graph {
        public static void addEdge(List<DirectedEdge>[] g, int s, int t) {
            g[s].add(new DirectedEdge(t));
        }

        public static List<DirectedEdge>[] createDirectedGraph(int n) {
            List<DirectedEdge>[] ans = new List[n];
            for (int i = 0; i < n; i++) {
                ans[i] = new ArrayList<>();
            }
            return ans;
        }

    }

    static interface LongMaximumCloseSubGraph {
        long maximumCloseSubGraph(List<DirectedEdge>[] g, long[] weights, boolean[] picked);

    }

    static interface IntegerStack {
        void addLast(int x);

        boolean isEmpty();

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

    static class IntegerDequeImpl implements IntegerDeque {
        private int[] data;
        private int bpos;
        private int epos;
        private static final int[] EMPTY = new int[0];
        private int n;

        public IntegerDequeImpl(int cap) {
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
            bpos = 0;
            epos = 0;
            n = cap;
        }

        private void expandSpace(int len) {
            while (n < len) {
                n = Math.max(n + 10, n * 2);
            }
            int[] newData = new int[n];
            if (bpos <= epos) {
                if (bpos < epos) {
                    System.arraycopy(data, bpos, newData, 0, epos - bpos);
                }
            } else {
                System.arraycopy(data, bpos, newData, 0, data.length - bpos);
                System.arraycopy(data, 0, newData, data.length - bpos, epos);
            }
            epos = size();
            bpos = 0;
            data = newData;
        }

        public IntegerIterator iterator() {
            return new IntegerIterator() {
                int index = bpos;


                public boolean hasNext() {
                    return index != epos;
                }


                public int next() {
                    int ans = data[index];
                    index = IntegerDequeImpl.this.next(index);
                    return ans;
                }
            };
        }

        public int removeFirst() {
            int ans = data[bpos];
            bpos = next(bpos);
            return ans;
        }

        public void addLast(int x) {
            ensureMore();
            data[epos] = x;
            epos = next(epos);
        }

        public void clear() {
            bpos = epos = 0;
        }

        private int next(int x) {
            return x + 1 >= n ? 0 : x + 1;
        }

        private void ensureMore() {
            if (next(epos) == bpos) {
                expandSpace(n + 1);
            }
        }

        public int size() {
            int ans = epos - bpos;
            if (ans < 0) {
                ans += data.length;
            }
            return ans;
        }

        public boolean isEmpty() {
            return bpos == epos;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (IntegerIterator iterator = iterator(); iterator.hasNext(); ) {
                builder.append(iterator.next()).append(' ');
            }
            return builder.toString();
        }

    }

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }
}

