import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.AbstractCollection;
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
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            P3381 solver = new P3381();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P3381 {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int s = in.readInt();
            int t = in.readInt();
            List<LongCostFlowEdge>[] g = LongFlow.createCostFlow(n + 1);
            for (int i = 0; i < m; i++) {
                int u = in.readInt();
                int v = in.readInt();
                int c = in.readInt();
                int w = in.readInt();
                LongFlow.addEdge(g, u, v, c, w);
            }

            LongMinimumCostFlow mcf = new LongMinCostFlowPolynomial();
            long[] ans = mcf.apply(g, s, t, (long) 2e18);
            out.append(ans[0]).append(' ').append(ans[1]);
        }

    }

    static interface LongMinimumCostFlow {
        long[] apply(List<LongCostFlowEdge>[] net, int s, int t, long send);

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

    static class LongMinCostFlowPolynomial implements LongMinimumCostFlow {
        static void bellmanFord(List<LongCostFlowEdge>[] graph, int s, long[] dist) {
            int n = graph.length;
            Arrays.fill(dist, Integer.MAX_VALUE);
            dist[s] = 0;
            boolean[] inqueue = new boolean[n];
            int[] q = new int[n];
            int qt = 0;
            q[qt++] = s;
            for (int qh = 0; (qh - qt) % n != 0; qh++) {
                int u = q[qh % n];
                inqueue[u] = false;
                for (int i = 0; i < graph[u].size(); i++) {
                    LongCostFlowEdge e = graph[u].get(i);
                    if (e.rev.flow == 0)
                        continue;
                    int v = e.to;
                    long ndist = dist[u] + e.cost;
                    if (dist[v] > ndist) {
                        dist[v] = ndist;
                        if (!inqueue[v]) {
                            inqueue[v] = true;
                            q[qt++ % n] = v;
                        }
                    }
                }
            }
        }

        public static long[] minCostFlow(List<LongCostFlowEdge>[] graph, int s, int t, long maxf) {
            int n = graph.length;
            long[] prio = new long[n];
            long[] curflow = new long[n];
            int[] prevedge = new int[n];
            int[] prevnode = new int[n];
            long[] pot = new long[n];

            // bellmanFord invocation can be skipped if edges costs are non-negative
            bellmanFord(graph, s, pot);
            long flow = 0;
            long flowCost = 0;

            PriorityQueue<LongMinCostFlowPolynomial.State> q = new PriorityQueue<>();
            while (flow < maxf) {
                q.clear();
                q.add(new LongMinCostFlowPolynomial.State(0, s));
                Arrays.fill(prio, Integer.MAX_VALUE);
                prio[s] = 0;
                boolean[] finished = new boolean[n];
                curflow[s] = Integer.MAX_VALUE;
                while (!finished[t] && !q.isEmpty()) {
                    LongMinCostFlowPolynomial.State cur = q.remove();
                    int u = cur.u;
                    long priou = cur.priou;
                    if (priou != prio[u])
                        continue;
                    finished[u] = true;
                    for (int i = 0; i < graph[u].size(); i++) {
                        LongCostFlowEdge e = graph[u].get(i);
                        if (e.rev.flow == 0)
                            continue;
                        int v = e.to;
                        long nprio = prio[u] + e.cost + pot[u] - pot[v];
                        if (prio[v] > nprio) {
                            prio[v] = nprio;
                            q.add(new LongMinCostFlowPolynomial.State(nprio, v));
                            prevnode[v] = u;
                            prevedge[v] = i;
                            curflow[v] = Math.min(curflow[u], e.rev.flow);
                        }
                    }
                }
                if (prio[t] == Integer.MAX_VALUE)
                    break;
                for (int i = 0; i < n; i++)
                    if (finished[i])
                        pot[i] += prio[i] - prio[t];
                long df = Math.min(curflow[t], maxf - flow);
                flow += df;
                for (int v = t; v != s; v = prevnode[v]) {
                    LongCostFlowEdge e = graph[prevnode[v]].get(prevedge[v]);
                    LongFlow.send(e, df);
                    flowCost += df * e.cost;
                }
            }
            return new long[]{flow, flowCost};
        }

        public long[] apply(List<LongCostFlowEdge>[] net, int s, int t, long send) {
            return minCostFlow(net, s, t, send);
        }

        public static class State implements Comparable<LongMinCostFlowPolynomial.State> {
            long priou;
            int u;

            public State(long priou, int u) {
                this.priou = priou;
                this.u = u;
            }

            public int compareTo(LongMinCostFlowPolynomial.State o) {
                return priou == o.priou ? Integer.compare(u, o.u) : Long.compare(priou, o.priou);
            }

        }

    }

    static class LongCostFlowEdge extends LongFlowEdge<LongCostFlowEdge> {
        public long cost;

        public LongCostFlowEdge(int to, long flow, boolean real, long cost) {
            super(to, flow, real);
            this.cost = cost;
        }

    }

    static class LongFlowEdge<T extends LongFlowEdge> extends DirectedEdge {
        public long flow;
        public boolean real;
        public T rev;

        public LongFlowEdge(int to, long flow, boolean real) {
            super(to);
            this.flow = flow;
            this.real = real;
        }

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

    static class LongFlow {
        public static <T extends LongFlowEdge> void send(T edge, long flow) {
            edge.flow += flow;
            edge.rev.flow -= flow;
        }

        public static LongCostFlowEdge addEdge(List<LongCostFlowEdge>[] g, int s, int t, long cap, long cost) {
            LongCostFlowEdge real = new LongCostFlowEdge(t, 0, true, cost);
            LongCostFlowEdge virtual = new LongCostFlowEdge(s, cap, false, -cost);
            real.rev = virtual;
            virtual.rev = real;
            g[s].add(real);
            g[t].add(virtual);
            return real;
        }

        public static List<LongCostFlowEdge>[] createCostFlow(int n) {
            List<LongCostFlowEdge>[] g = new List[n];
            for (int i = 0; i < n; i++) {
                g[i] = new ArrayList<>();
            }
            return g;
        }

    }
}

