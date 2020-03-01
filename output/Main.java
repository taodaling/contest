import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.util.Random;
import java.util.ArrayList;
import java.util.AbstractCollection;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
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
            ETreelandAndViruses solver = new ETreelandAndViruses();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class ETreelandAndViruses {
        List<UndirectedEdge>[] g;
        int[] depths;
        int[] dfn;
        LcaOnTree lcaOnTree;
        int[] endWith;
        IntegerList nodes;
        IntegerVersionArray appear;
        IntegerDeque deque;
        List<UndirectedEdge>[] vtree;
        int order = 0;
        PriorityQueue<Event> events;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            g = Graph.createUndirectedGraph(n);
            depths = new int[n];
            dfn = new int[n];
            for (int i = 1; i < n; i++) {
                int a = in.readInt() - 1;
                int b = in.readInt() - 1;
                Graph.addUndirectedEdge(g, a, b);
            }
            dfs(0, -1, 0);
            lcaOnTree = new LcaOnTree(g, 0);
            int q = in.readInt();
            appear = new IntegerVersionArray(n);
            nodes = new IntegerList(n);
            deque = new IntegerDequeImpl(n);
            vtree = Graph.createUndirectedGraph(n);
            events = new PriorityQueue<>(2 * n, (a, b) ->
                    a.turn == b.turn ? Integer.compare(a.virus, b.virus) :
                            Integer.compare(a.turn, b.turn));
            endWith = new int[n];
            for (int i = 0; i < q; i++) {
                solveOnce(in, out);
            }
        }

        public void addNode(int i) {
            if (appear.get(i) == 0) {
                appear.set(i, 1);
                nodes.add(i);
            }
        }

        public void solveOnce(FastInput in, FastOutput out) {
            int k = in.readInt();
            int m = in.readInt();
            appear.clear();
            int[][] virus = new int[k][2];
            nodes.clear();
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < 2; j++) {
                    virus[i][j] = in.readInt();
                }
                virus[i][0]--;
                addNode(virus[i][0]);
            }
            int[] importance = new int[m];
            for (int i = 0; i < m; i++) {
                importance[i] = in.readInt() - 1;
                addNode(importance[i]);
            }

            nodes.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
            for (int i = 1, end = nodes.size(); i < end; i++) {
                int last = nodes.get(i - 1);
                int cur = nodes.get(i);
                addNode(lcaOnTree.lca(last, cur));
            }
            nodes.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
            for (int i = 0, end = nodes.size(); i < end; i++) {
                int node = nodes.get(i);
                vtree[node].clear();
                endWith[node] = -1;
            }
            deque.clear();
            deque.addLast(nodes.get(0));
            for (int i = 1, end = this.nodes.size(); i < end; i++) {
                int node = this.nodes.get(i);
                while (!deque.isEmpty() && lcaOnTree.lca(deque.peekLast(), node) !=
                        deque.peekLast()) {
                    deque.removeLast();
                }
                Graph.addUndirectedEdge(vtree, deque.peekLast(), node);
                deque.addLast(node);
            }
            events.clear();
            for (int i = 0; i < k; i++) {
                publish(i, 0, virus[i][0]);
            }

            while (!events.isEmpty()) {
                Event event = events.remove();
                if (endWith[event.node] != -1) {
                    continue;
                }
                endWith[event.node] = event.virus;
                for (UndirectedEdge e : vtree[event.node]) {
                    if (endWith[e.to] != -1) {
                        continue;
                    }
                    int dist = distance(e.to, virus[event.virus][0]);
                    int turn = DigitUtils.ceilDiv(dist, virus[event.virus][1]);
                    publish(event.virus, turn, e.to);
                }
            }

            for (int i : importance) {
                out.append(endWith[i] + 1).append(' ');
            }
            out.println();
        }

        public int distance(int a, int b) {
            int c = lcaOnTree.lca(a, b);
            return depths[a] + depths[b] - depths[c] * 2;
        }

        public void publish(int virus, int turn, int node) {
            Event event = new Event();
            event.virus = virus;
            event.turn = turn;
            event.node = node;
            events.add(event);
        }

        public void dfs(int root, int p, int depth) {
            depths[root] = depth;
            dfn[root] = order++;
            for (UndirectedEdge e : g[root]) {
                if (e.to == p) {
                    continue;
                }
                dfs(e.to, root, depth + 1);
            }
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

    static interface IntegerStack {
        void addLast(int x);

        int removeLast();

        int peekLast();

        boolean isEmpty();

        void clear();

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

    }

    static class Event {
        int turn;
        int virus;
        int node;

    }

    static class LcaOnTree {
        int[] parent;
        int[] preOrder;
        int[] i;
        int[] head;
        int[] a;
        int time;

        void dfs1(List<UndirectedEdge>[] tree, int u, int p) {
            parent[u] = p;
            i[u] = preOrder[u] = time++;
            for (UndirectedEdge e : tree[u]) {
                int v = e.to;
                if (v == p) continue;
                dfs1(tree, v, u);
                if (Integer.lowestOneBit(i[u]) < Integer.lowestOneBit(i[v])) {
                    i[u] = i[v];
                }
            }
            head[i[u]] = u;
        }

        void dfs2(List<UndirectedEdge>[] tree, int u, int p, int up) {
            a[u] = up | Integer.lowestOneBit(i[u]);
            for (UndirectedEdge e : tree[u]) {
                int v = e.to;
                if (v == p) continue;
                dfs2(tree, v, u, a[u]);
            }
        }

        public LcaOnTree(List<UndirectedEdge>[] tree, int root) {
            int n = tree.length;
            preOrder = new int[n];
            i = new int[n];
            head = new int[n];
            a = new int[n];
            parent = new int[n];

            dfs1(tree, root, -1);
            dfs2(tree, root, -1, 0);
        }

        private int enterIntoStrip(int x, int hz) {
            if (Integer.lowestOneBit(i[x]) == hz)
                return x;
            int hw = 1 << CachedLog2.floorLog(a[x] & (hz - 1));
            return parent[head[i[x] & -hw | hw]];
        }

        public int lca(int x, int y) {
            int hb = i[x] == i[y] ? Integer.lowestOneBit(i[x]) : (1 << CachedLog2.floorLog(i[x] ^ i[y]));
            int hz = Integer.lowestOneBit(a[x] & a[y] & -hb);
            int ex = enterIntoStrip(x, hz);
            int ey = enterIntoStrip(y, hz);
            return preOrder[ex] < preOrder[ey] ? ex : ey;
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

        public int removeLast() {
            int ans = data[last(epos)];
            epos = last(epos);
            return ans;
        }

        public void addLast(int x) {
            ensureMore();
            data[epos] = x;
            epos = next(epos);
        }

        public int peekLast() {
            return data[last(epos)];
        }

        public void clear() {
            bpos = epos = 0;
        }

        private int last(int x) {
            return (x == 0 ? n : x) - 1;
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

    static class CompareUtils {
        private static final int THRESHOLD = 4;

        private CompareUtils() {
        }

        public static void insertSort(int[] data, IntegerComparator cmp, int l, int r) {
            for (int i = l + 1; i <= r; i++) {
                int j = i;
                int val = data[i];
                while (j > l && cmp.compare(data[j - 1], val) > 0) {
                    data[j] = data[j - 1];
                    j--;
                }
                data[j] = val;
            }
        }

        public static void quickSort(int[] data, IntegerComparator cmp, int f, int t) {
            if (t - f <= THRESHOLD) {
                insertSort(data, cmp, f, t - 1);
                return;
            }
            SequenceUtils.swap(data, f, Randomized.nextInt(f, t - 1));
            int l = f;
            int r = t;
            int m = l + 1;
            while (m < r) {
                int c = cmp.compare(data[m], data[l]);
                if (c == 0) {
                    m++;
                } else if (c < 0) {
                    SequenceUtils.swap(data, l, m);
                    l++;
                    m++;
                } else {
                    SequenceUtils.swap(data, m, --r);
                }
            }
            quickSort(data, cmp, f, l);
            quickSort(data, cmp, m, t);
        }

    }

    static class Graph {
        public static void addUndirectedEdge(List<UndirectedEdge>[] g, int s, int t) {
            UndirectedEdge toT = new UndirectedEdge(t);
            UndirectedEdge toS = new UndirectedEdge(s);
            toT.rev = toS;
            toS.rev = toT;
            g[s].add(toT);
            g[t].add(toS);
        }

        public static List<UndirectedEdge>[] createUndirectedGraph(int n) {
            List<UndirectedEdge>[] ans = new List[n];
            for (int i = 0; i < n; i++) {
                ans[i] = new ArrayList<>();
            }
            return ans;
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

    static class CachedLog2 {
        private static final int BITS = 16;
        private static final int LIMIT = 1 << BITS;
        private static final byte[] CACHE = new byte[LIMIT];

        static {
            int b = 0;
            for (int i = 0; i < LIMIT; i++) {
                while ((1 << (b + 1)) <= i) {
                    b++;
                }
                CACHE[i] = (byte) b;
            }
        }

        public static int floorLog(int x) {
            return x < LIMIT ? CACHE[x] : (BITS + CACHE[x >>> BITS]);
        }

    }

    static class SequenceUtils {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static boolean equal(int[] a, int al, int ar, int[] b, int bl, int br) {
            if ((ar - al) != (br - bl)) {
                return false;
            }
            for (int i = al, j = bl; i <= ar; i++, j++) {
                if (a[i] != b[j]) {
                    return false;
                }
            }
            return true;
        }

    }

    static interface IntegerDeque extends IntegerStack {
    }

    static class Randomized {
        private static Random random = new Random(0);

        public static int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }

    static class UndirectedEdge extends DirectedEdge {
        public UndirectedEdge rev;

        public UndirectedEdge(int to) {
            super(to);
        }

    }

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }

    static class IntegerVersionArray {
        int[] data;
        int[] version;
        int now;
        int def;

        public IntegerVersionArray(int cap) {
            this(cap, 0);
        }

        public IntegerVersionArray(int cap, int def) {
            data = new int[cap];
            version = new int[cap];
            now = 0;
            this.def = def;
        }

        public void clear() {
            now++;
        }

        public void visit(int i) {
            if (version[i] < now) {
                version[i] = now;
                data[i] = def;
            }
        }

        public void set(int i, int v) {
            version[i] = now;
            data[i] = v;
        }

        public int get(int i) {
            visit(i);
            return data[i];
        }

    }

    static interface IntegerComparator {
        public int compare(int a, int b);

    }

    static class IntegerList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntegerList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntegerList(IntegerList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntegerList() {
            this(0);
        }

        public void ensureSpace(int req) {
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public int get(int i) {
            checkRange(i);
            return data[i];
        }

        public void add(int x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntegerList list) {
            addAll(list.data, 0, list.size);
        }

        public void sort(IntegerComparator comparator) {
            CompareUtils.quickSort(data, comparator, 0, size);
        }

        public int size() {
            return size;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public void clear() {
            size = 0;
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntegerList)) {
                return false;
            }
            IntegerList other = (IntegerList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Integer.hashCode(data[i]);
            }
            return h;
        }

        public IntegerList clone() {
            IntegerList ans = new IntegerList();
            ans.addAll(this);
            return ans;
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

