import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.IntStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
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
            Day8B solver = new Day8B();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Day8B {
        int n;

        int idOfIn(int i) {
            return i;
        }

        int idOfOut(int i) {
            return n + i;
        }

        int idOfSrc() {
            return 2 * n;
        }

        int idOfDst() {
            return idOfSrc() + 1;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            int m = in.readInt();
            int w = in.readInt();
            int[] time = new int[n];
            int[] cost = new int[n];

            for (int i = 0; i < n; i++) {
                time[i] = in.readInt();
            }
            for (int i = 0; i < n; i++) {
                cost[i] = in.readInt();
            }
            MakeDistanceFarthest mdf = new MakeDistanceFarthest(idOfDst() + 1);
            for (int i = 0; i < n; i++) {
                mdf.addLimitedEdge(idOfSrc(), idOfIn(i), 0, 0, 0);
                mdf.addLimitedEdge(idOfOut(i), idOfDst(), 0, 0, 0);
                mdf.addLimitedEdge(idOfIn(i), idOfOut(i), -time[i], cost[i], time[i]);
            }
            for (int i = 0; i < m; i++) {
                int u = in.readInt() - 1;
                int v = in.readInt() - 1;
                mdf.addLimitedEdge(idOfOut(v), idOfIn(u), 0, 0, 0);
            }

            mdf.solve(new LongDijkstraV2MinimumCostFlow(idOfDst() + 1), idOfSrc(), idOfDst(),
                    w, (long) 1e18, (int) 1e9);


            long ans = (long) Math.floor(mdf.queryByExpense(w));
            out.println(-ans);
        }

    }

    static class MakeDistanceFarthest {
        private static long inf = Long.MAX_VALUE / 4;
        private List<LongCostFlowEdge>[] g;
        MakeDistanceFarthest.LinearFunction[] fs;

        public MakeDistanceFarthest(int n) {
            g = LongFlow.createCostFlow(n);
        }

        public void addLimitedEdge(int u, int v, long len, long cost, long limit) {
            LongFlow.addCostEdge(g, u, v, cost, len);
            LongFlow.addCostEdge(g, u, v, inf, len + limit);
        }

        public void solve(LongAugmentMinimumCostFlow mcf, int s, int t, long budgeLimit, long distLimit, long flowLimit) {
            List<MakeDistanceFarthest.LinearFunction> list = new ArrayList<>();
            LongAugmentCallback callback = new LongAugmentCallback() {
                long sumFlow = 0;
                long sumCost = 0;


                public boolean callback(long flow, long pathCost) {
                    sumFlow += flow;
                    sumCost += flow * pathCost;

                    if (!list.isEmpty() && list.get(list.size() - 1).l == pathCost) {
                        list.remove(list.size() - 1);
                    }
                    MakeDistanceFarthest.LinearFunction func = new MakeDistanceFarthest.LinearFunction(pathCost, sumFlow, -sumCost);
                    list.add(func);
                    return func.getL() <= budgeLimit && func.l <= distLimit;
                }
            };
            mcf.setCallback(callback);
            mcf.apply(g, s, t, flowLimit);
            fs = list.toArray(new MakeDistanceFarthest.LinearFunction[0]);
        }

        public double queryByExpense(long x) {
            int l = 0;
            int r = fs.length - 1;
            while (l < r) {
                int mid = (l + r) / 2;
                boolean valid = mid + 1 >= fs.length ||
                        fs[mid + 1].getL() > x;
                if (valid) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            return fs[l].inverse(x);
        }

        private static class LinearFunction {
            long l;
            long a;
            long b;

            public LinearFunction(long l, long a, long b) {
                this.l = l;
                this.a = a;
                this.b = b;
            }

            long getL() {
                return a * l + b;
            }

            double inverse(double y) {
                return (y - b) / a;
            }

        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static boolean equal(long a, long b) {
            return a == b;
        }

    }

    static interface LongAugmentCallback {
        public static LongAugmentCallback NIL = (a, b) -> {
            return true;
        };

        public boolean callback(long flow, long pathCost);

    }

    static class LongFlow {
        public static <T extends LongFlowEdge> void send(T edge, long flow) {
            edge.flow += flow;
            edge.rev.flow -= flow;
        }

        public static LongCostFlowEdge addCostEdge(List<LongCostFlowEdge>[] g, int s, int t, long cap, long cost) {
            LongCostFlowEdge real = new LongCostFlowEdge(t, 0, true, cost);
            LongCostFlowEdge virtual = new LongCostFlowEdge(s, cap, false, -cost);
            real.rev = virtual;
            virtual.rev = real;
            g[s].add(real);
            g[t].add(virtual);
            return real;
        }

        public static List<LongCostFlowEdge>[] createCostFlow(int n) {
            return createGraph(n);
        }

        private static List[] createGraph(int n) {
            return IntStream.range(0, n).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[i]);
        }

    }

    static interface IntegerStack {
        void addLast(int x);

        boolean isEmpty();

        void clear();

    }

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }

    static interface LongMinimumCostFlow {
        long[] apply(List<LongCostFlowEdge>[] net, int s, int t, long send);

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

    static class LongCostFlowEdge extends LongFlowEdge<LongCostFlowEdge> {
        public long cost;

        public LongCostFlowEdge(int to, long flow, boolean real, long cost) {
            super(to, flow, real);
            this.cost = cost;
        }

    }

    static class LongDijkstraV2MinimumCostFlow implements LongAugmentMinimumCostFlow {
        private int m;
        private long[] lastDist;
        private long[] curDist;
        private LongCostFlowEdge[] prev;
        private boolean[] inq;
        private IntegerDeque dq;
        private static final long INF = Long.MAX_VALUE / 4;
        private List<LongCostFlowEdge>[] g;
        private LongAugmentCallback callback = LongAugmentCallback.NIL;

        public void setCallback(LongAugmentCallback callback) {
            this.callback = callback;
        }

        public LongDijkstraV2MinimumCostFlow(int m) {
            this.m = m - 1;
            lastDist = new long[m];
            curDist = new long[m];
            prev = new LongCostFlowEdge[m];
            inq = new boolean[m];
            dq = new IntegerDequeImpl(m);
        }

        private void bf(int s) {
            int n = g.length;
            dq.clear();
            for (int i = 0; i < n; i++) {
                lastDist[i] = INF;
                inq[i] = false;
            }
            lastDist[s] = 0;
            inq[s] = true;
            dq.addLast(s);
            while (!dq.isEmpty()) {
                int head = dq.removeFirst();
                inq[head] = false;
                for (LongCostFlowEdge e : g[head]) {
                    if (DigitUtils.equal(e.rev.flow, 0) || lastDist[e.to] <= lastDist[head] + e.cost) {
                        continue;
                    }
                    lastDist[e.to] = lastDist[head] + e.cost;
                    if (!inq[e.to]) {
                        inq[e.to] = true;
                        dq.addLast(e.to);
                    }
                }
            }
        }

        private void dijkstra(int s) {
            int n = g.length;
            for (int i = 0; i < n; i++) {
                curDist[i] = INF;
                prev[i] = null;
                inq[i] = false;
            }
            curDist[s] = 0;

            for (int i = 0; i < n; i++) {
                int head = -1;
                for (int j = 0; j < n; j++) {
                    if (!inq[j] && (head == -1 || curDist[j] < curDist[head])) {
                        head = j;
                    }
                }
                if (curDist[head] >= INF) {
                    break;
                }
                inq[head] = true;
                for (LongCostFlowEdge e : g[head]) {
                    long dist;
                    if (e.rev.flow == 0 || curDist[e.to] <= (dist = curDist[head] + e.cost - lastDist[e.to] + lastDist[head])) {
                        continue;
                    }
                    prev[e.to] = e.rev;
                    curDist[e.to] = dist;
                }
            }

            for (int i = 0; i < n; i++) {
                lastDist[i] = Math.min(curDist[i] + lastDist[i], INF);
            }
        }

        public long[] apply(List<LongCostFlowEdge>[] net, int s, int t, long send) {
            this.g = net;
            bf(s);
            long flow = 0;
            long cost = 0;
            while (flow < send) {
                dijkstra(s);
                if (prev[t] == null) {
                    break;
                }
                long remain = send - flow;
                for (LongCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                    remain = Math.min(remain, trace.flow);
                }
                long sumOfCost = 0;
                for (LongCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                    sumOfCost -= trace.cost;
                    LongFlow.send(trace, -remain);
                }
                cost += sumOfCost * -remain;
                flow += remain;
                if (!callback.callback(remain, sumOfCost)) {
                    break;
                }
            }
            return new long[]{flow, cost};
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

    static interface LongAugmentMinimumCostFlow extends LongMinimumCostFlow {
        public void setCallback(LongAugmentCallback callback);

    }

    static class LongFlowEdge<T extends LongFlowEdge<T>> extends DirectedEdge {
        public long flow;
        public boolean real;
        public T rev;

        public LongFlowEdge(int to, long flow, boolean real) {
            super(to);
            this.flow = flow;
            this.real = real;
        }

        public String toString() {
            return rev.to + "-[" + flow + "/" + (flow + rev.flow) + "]->" + to;
        }

    }

    static interface IntegerDeque extends IntegerStack {
        int removeFirst();

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
}

