import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.IntStream;
import java.util.Arrays;
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
            CoinGrid solver = new CoinGrid();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CoinGrid {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            DinicBipartiteMatch dbm = new DinicBipartiteMatch(n, n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (in.readChar() == 'o') {
                        dbm.addEdge(i, j);
                    }
                }
            }
            dbm.solve();
            boolean[][] cover = dbm.minVertexCover();
            int ans = 0;
            for (boolean[] a : cover) {
                for (boolean x : a) {
                    ans += x ? 1 : 0;
                }
            }
            out.println(ans);
            for (int i = 0; i < n; i++) {
                if (cover[0][i]) {
                    out.append(1).append(' ').append(i + 1).println();
                }
            }
            for (int i = 0; i < n; i++) {
                if (cover[1][i]) {
                    out.append(2).append(' ').append(i + 1).println();
                }
            }
        }

    }

    static class IntegerFlow {
        public static <T extends IntegerFlowEdge> void send(T edge, int flow) {
            edge.flow += flow;
            edge.rev.flow -= flow;
        }

        public static IntegerFlowEdge addFlowEdge(List[] g, int s, int t, int cap) {
            IntegerFlowEdge real = new IntegerFlowEdge(t, 0, true);
            IntegerFlowEdge virtual = new IntegerFlowEdge(s, cap, false);
            real.rev = virtual;
            virtual.rev = real;
            g[s].add(real);
            g[t].add(virtual);
            return real;
        }

        public static <T extends IntegerFlowEdge> void bfsForFlow(List<T>[] g, int s, int[] dist, int inf, IntegerDeque deque) {
            Arrays.fill(dist, 0, g.length, inf);
            dist[s] = 0;
            deque.clear();
            deque.addLast(s);
            while (!deque.isEmpty()) {
                int head = deque.removeFirst();
                for (T e : g[head]) {
                    if (!DigitUtils.equal(e.flow, 0) && dist[e.to] == inf) {
                        dist[e.to] = dist[head] + 1;
                        deque.addLast(e.to);
                    }
                }
            }
        }

    }

    static class IntegerDinic implements IntegerMaximumFlow {
        List<IntegerFlowEdge>[] g;
        int s;
        int t;
        IntegerDeque deque;
        int[] dists;
        int[] iterators;

        public IntegerDinic() {
        }

        public void ensure(int vertexNum) {
            if (dists != null && dists.length >= vertexNum) {
                return;
            }
            deque = new IntegerDequeImpl(vertexNum);
            dists = new int[vertexNum];
            iterators = new int[vertexNum];
        }

        public int send(int root, int flow) {
            if (root == t) {
                return flow;
            }
            int snapshot = flow;
            while (iterators[root] >= 0 && flow > 0) {
                IntegerFlowEdge e = g[root].get(iterators[root]);
                if (dists[e.to] + 1 == dists[root] && e.rev.flow != 0) {
                    int sent = send(e.to, Math.min(flow, e.rev.flow));
                    if (sent > 0) {
                        flow -= sent;
                        IntegerFlow.send(e, sent);
                        continue;
                    }
                }
                iterators[root]--;
            }
            return snapshot - flow;
        }

        public int apply(List<IntegerFlowEdge>[] g, int s, int t, int send) {
            ensure(g.length);
            this.s = s;
            this.t = t;
            this.g = g;
            int flow = 0;
            while (flow < send) {
                IntegerFlow.bfsForFlow(g, t, dists, Integer.MAX_VALUE, deque);
                if (dists[s] == Integer.MAX_VALUE) {
                    break;
                }
                for (int i = 0; i < g.length; i++) {
                    iterators[i] = g[i].size() - 1;
                }
                flow += send(s, send - flow);
            }
            return flow;
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

    static class DirectedEdge {
        public int to;

        public DirectedEdge(int to) {
            this.to = to;
        }

        public String toString() {
            return "->" + to;
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

        public char readChar() {
            skipBlank();
            char c = (char) next;
            next = read();
            return c;
        }

    }

    static interface IntegerDeque extends IntegerStack {
        int removeFirst();

    }

    static interface IntegerMaximumFlow {
    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 1 << 13;
        private final Writer os;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        private void afterWrite() {
            if (cache.length() < THRESHOLD) {
                return;
            }
            flush();
        }

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append(System.lineSeparator());
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

    static class DigitUtils {
        private DigitUtils() {
        }

        public static boolean equal(int a, int b) {
            return a == b;
        }

    }

    static class DinicBipartiteMatch {
        private List<IntegerFlowEdge>[] g;
        private IntegerDinic dinic;
        private int[] lMates;
        private int[] rMates;
        int l;
        int r;

        private int idOfL(int i) {
            return i;
        }

        private int idOfR(int i) {
            return i + l;
        }

        private int idOfSrc() {
            return l + r;
        }

        private int idOfDst() {
            return idOfSrc() + 1;
        }

        public DinicBipartiteMatch(int l, int r) {
            this.l = l;
            this.r = r;
            lMates = new int[l];
            rMates = new int[r];
            g = Graph.createGraph(idOfDst() + 1);
            dinic = new IntegerDinic();
            for (int i = 0; i < l; i++) {
                IntegerFlow.addFlowEdge(g, idOfSrc(), idOfL(i), 1);
            }
            for (int i = 0; i < r; i++) {
                IntegerFlow.addFlowEdge(g, idOfR(i), idOfDst(), 1);
            }
        }

        public void addEdge(int u, int v) {
            IntegerFlow.addFlowEdge(g, idOfL(u), idOfR(v), 1);
        }

        public int solve() {
            Arrays.fill(lMates, -1);
            Arrays.fill(rMates, -1);
            dinic.apply(g, idOfSrc(), idOfDst(), l);
            int ans = 0;
            for (int i = 0; i < l; i++) {
                for (IntegerFlowEdge e : g[idOfL(i)]) {
                    if (e.real && e.flow == 1) {
                        ans++;
                        int to = e.to - idOfR(0);
                        lMates[i] = to;
                        rMates[to] = i;
                        break;
                    }
                }
            }
            return ans;
        }

        public boolean[][] minVertexCover() {
            boolean[] lVis = new boolean[l];
            boolean[] rVis = new boolean[r];
            for (int i = 0; i < r; i++) {
                if (rMates[i] == -1) {
                    dfsRight(i, lVis, rVis);
                }
            }

            boolean[] left = new boolean[l];
            boolean[] right = new boolean[r];
            for (int i = 0; i < l; i++) {
                left[i] = lVis[i];
            }
            for (int i = 0; i < r; i++) {
                right[i] = !rVis[i];
            }

            return new boolean[][]{left, right};
        }

        private void dfsRight(int i, boolean[] lVis, boolean[] rVis) {
            if (rVis[i]) {
                return;
            }
            rVis[i] = true;
            for (IntegerFlowEdge e : g[idOfR(i)]) {
                if (e.real) {
                    continue;
                }
                dfsLeft(e.to - idOfL(0), lVis, rVis);
            }
        }

        private void dfsLeft(int i, boolean[] lVis, boolean[] rVis) {
            if (lMates[i] == -1) {
                return;
            }
            if (lVis[i]) {
                return;
            }
            lVis[i] = true;
            dfsRight(lMates[i], lVis, rVis);
        }

    }

    static interface IntegerIterator {
        boolean hasNext();

        int next();

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

    static class Graph {
        public static List[] createGraph(int n) {
            return IntStream.range(0, n).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[i]);
        }

    }

    static interface IntegerStack {
        void addLast(int x);

        boolean isEmpty();

        void clear();

    }
}

