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

            LongMinimumCostFlow mcf = new LongSpfaMinimumCostFlow(n + 1);
            long[] ans = mcf.apply(g, s, t, (long) 2e18);
            out.append(ans[0]).append(' ').append(ans[1]);
        }

    }

    static interface LongMinimumCostFlow {
        long[] apply(List<LongCostFlowEdge>[] net, int s, int t, long send);

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

    static class LongSpfaMinimumCostFlow implements LongMinimumCostFlow {
        IntegerDeque deque;
        long[] dists;
        boolean[] inque;
        LongCostFlowEdge[] prev;
        List<LongCostFlowEdge>[] net;
        private static final long INF = (long) 2e18;

        public LongSpfaMinimumCostFlow(int vertexNum) {
            deque = new IntegerDequeImpl(vertexNum);
            dists = new long[vertexNum];
            inque = new boolean[vertexNum];
            prev = new LongCostFlowEdge[vertexNum];
        }

        private void spfa(int s, long inf) {
            deque.clear();
            for (int i = 0; i < net.length; i++) {
                dists[i] = inf;
                inque[i] = false;
            }
            dists[s] = 0;
            prev[s] = null;
            deque.addLast(s);
            while (!deque.isEmpty()) {
                int head = deque.removeFirst();
                inque[head] = false;
                for (LongCostFlowEdge e : net[head]) {
                    if (e.flow > 0 && dists[e.to] > dists[head] - e.cost) {
                        dists[e.to] = dists[head] - e.cost;
                        prev[e.to] = e;
                        if (!inque[e.to]) {
                            inque[e.to] = true;
                            deque.addLast(e.to);
                        }
                    }
                }
            }
        }

        public long[] apply(List<LongCostFlowEdge>[] net, int s, int t, long send) {
            long cost = 0;
            long flow = 0;
            this.net = net;
            while (flow < send) {
                spfa(t, INF);
                if (dists[s] == INF) {
                    break;
                }
                int iter = s;
                long sent = send - flow;
                while (prev[iter] != null) {
                    sent = Math.min(sent, prev[iter].flow);
                    iter = prev[iter].rev.to;
                }
                iter = s;
                while (prev[iter] != null) {
                    LongFlow.send(prev[iter], -sent);
                    iter = prev[iter].rev.to;
                }
                cost += sent * dists[s];
                flow += sent;
            }
            return new long[]{flow, cost};
        }

    }

    static interface IntegerIterator {
        boolean hasNext();

        int next();

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

    static interface IntegerDeque extends IntegerStack {
        int removeFirst();

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

    static interface IntegerStack {
        void addLast(int x);

        boolean isEmpty();

        void clear();

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

