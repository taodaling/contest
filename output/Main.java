import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
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
            DKingdomAndItsCities solver = new DKingdomAndItsCities();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DKingdomAndItsCities {
        List<UndirectedEdge>[] g;
        int[] depth;
        int[] imp;
        int round;
        VirtualTree vt;
        boolean valid = true;
        int cost;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            vt = new VirtualTree(n, 0);
            for (int i = 0; i < n - 1; i++) {
                int a = in.readInt() - 1;
                int b = in.readInt() - 1;
                vt.addEdge(a, b);
            }

            int q = in.readInt();
            imp = new int[n];
            depth = new int[n];
            g = vt.getTree();
            dfsForDepth(0, -1);
            for (int i = 0; i < q; i++) {
                round++;
                int k = in.readInt();
                vt.nextRound();
                for (int j = 0; j < k; j++) {
                    int x = in.readInt() - 1;
                    imp[x] = round;
                    vt.active(x);
                }
                vt.buildVirtualTree();
                valid = true;
                cost = 0;
                dfs(vt.getVirtualTop(), -1);
                if (!valid) {
                    out.println(-1);
                } else {
                    out.println(cost);
                }
            }
        }

        public boolean dfs(int root, int p) {
            int sub = imp[root] == round ? 1 : 0;
            for (int iter = vt.heads[root]; iter != 0; iter = vt.next[iter]) {
                int node = vt.vals[iter];
                boolean ans = dfs(node, root);
                sub += ans ? 1 : 0;
            }
            if (sub > 1) {
                sub = 0;
                cost++;
            }
            if (imp[root] == round && p != -1 && imp[p] == round
                    && depth[root] == depth[p] + 1) {
                valid = false;
            }
            if (sub > 0 && p != -1 && imp[p] == round) {
                sub = 0;
                cost++;
            }
            return sub > 0;
        }

        public void dfsForDepth(int root, int p) {
            depth[root] = p == -1 ? 0 : depth[p] + 1;
            for (UndirectedEdge e : g[root]) {
                if (e.to == p) {
                    continue;
                }
                dfsForDepth(e.to, root);
            }
        }

    }

    static class VirtualTree {
        public int[] heads;
        public int[] next;
        public int[] vals;
        int[] dfn;
        int[] dfnClose;
        public List<UndirectedEdge>[] g;
        int order = 0;
        int alloc = 0;
        int[] activeNodes;
        boolean prepared = false;
        IntegerArrayList pend;
        int[] version;
        int round;
        public LcaOnTree lca;
        int top;
        int virtualTop;
        IntegerDequeImpl dq;

        public VirtualTree(int n, int top) {
            heads = new int[n];
            next = new int[(n - 1) * 2 + 1];
            vals = new int[next.length];
            dfn = new int[n];
            g = Graph.createGraph(n);
            activeNodes = new int[n];
            pend = new IntegerArrayList(n);
            version = new int[n];
            this.top = top;
            dq = new IntegerDequeImpl(n);
            dfnClose = new int[n];
        }

        private void add(int i, int x) {
            next[alloc] = heads[i];
            vals[alloc] = x;
            heads[i] = alloc;
            alloc++;
        }

        public List<UndirectedEdge>[] getTree() {
            return g;
        }

        private void dfsForDfn(int root, int p) {
            dfn[root] = order++;
            for (UndirectedEdge e : g[root]) {
                if (e.to == p) {
                    continue;
                }
                dfsForDfn(e.to, root);
            }
            dfnClose[root] = order - 1;
        }

        private void prepare() {
            if (prepared) {
                return;
            }
            prepared = true;
            dfsForDfn(top, -1);
            lca = new LcaOnTree(g, 0);
        }

        public void nextRound() {
            prepare();
            pend.clear();
            round++;
            alloc = 1;
        }

        public void active(int x) {
            if (version[x] != round) {
                pend.add(x);
                version[x] = round;
                heads[x] = 0;
            }
        }

        private boolean isAncestor(int a, int b) {
            return dfn[a] <= dfn[b] && dfnClose[a] >= dfn[b];
        }

        public void buildVirtualTree() {
            assert !pend.isEmpty();
            pend.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
            for (int i = pend.size() - 1; i >= 1; i--) {
                int cur = pend.get(i);
                int last = pend.get(i - 1);
                active(lca.lca(cur, last));
            }
            pend.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
            dq.clear();
            dq.addLast(pend.get(0));
            for (int i = 1; i < pend.size(); i++) {
                int node = pend.get(i);
                while (!isAncestor(dq.peekLast(), node)) {
                    dq.removeLast();
                }
                add(dq.peekLast(), node);
                dq.addLast(node);
            }
            virtualTop = dq.peekFirst();
        }

        public int getVirtualTop() {
            return virtualTop;
        }

        public void addEdge(int u, int v) {
            Graph.addUndirectedEdge(g, u, v);
        }

        public String toString() {
            StringBuilder ans = new StringBuilder();
            for (int i = 0; i < heads.length; i++) {
                if (version[i] != round) {
                    continue;
                }
                ans.append(i).append(":\n\t");
                for (int iter = heads[i]; iter != 0; iter = next[iter]) {
                    int node = vals[iter];
                    ans.append(node).append(' ');
                }
                ans.append('\n');
            }
            return ans.toString();
        }

    }

    static interface IntegerStack {
    }

    static class LcaOnTree {
        int[] parent;
        int[] preOrder;
        int[] i;
        int[] head;
        int[] a;
        int time;

        void dfs1(List<? extends DirectedEdge>[] tree, int u, int p) {
            parent[u] = p;
            i[u] = preOrder[u] = time++;
            for (DirectedEdge e : tree[u]) {
                int v = e.to;
                if (v == p) continue;
                dfs1(tree, v, u);
                if (Integer.lowestOneBit(i[u]) < Integer.lowestOneBit(i[v])) {
                    i[u] = i[v];
                }
            }
            head[i[u]] = u;
        }

        void dfs2(List<? extends DirectedEdge>[] tree, int u, int p, int up) {
            a[u] = up | Integer.lowestOneBit(i[u]);
            for (DirectedEdge e : tree[u]) {
                int v = e.to;
                if (v == p) continue;
                dfs2(tree, v, u, a[u]);
            }
        }

        public void reset(List<? extends DirectedEdge>[] tree, int root) {
            time = 0;
            dfs1(tree, root, -1);
            dfs2(tree, root, -1, 0);
        }

        public LcaOnTree(int n) {
            preOrder = new int[n];
            i = new int[n];
            head = new int[n];
            a = new int[n];
            parent = new int[n];
        }

        public LcaOnTree(List<? extends DirectedEdge>[] tree, int root) {
            this(tree.length);
            reset(tree, root);
        }

        private int enterIntoStrip(int x, int hz) {
            if (Integer.lowestOneBit(i[x]) == hz)
                return x;
            int hw = 1 << Log2.floorLog(a[x] & (hz - 1));
            return parent[head[i[x] & -hw | hw]];
        }

        public int lca(int x, int y) {
            int hb = i[x] == i[y] ? Integer.lowestOneBit(i[x]) : (1 << Log2.floorLog(i[x] ^ i[y]));
            int hz = Integer.lowestOneBit(a[x] & a[y] & -hb);
            int ex = enterIntoStrip(x, hz);
            int ey = enterIntoStrip(y, hz);
            return preOrder[ex] < preOrder[ey] ? ex : ey;
        }

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

    static class RandomWrapper {
        private Random random;
        public static final RandomWrapper INSTANCE = new RandomWrapper(new Random());

        public RandomWrapper() {
            this(new Random());
        }

        public RandomWrapper(Random random) {
            this.random = random;
        }

        public int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
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

        public static List[] createGraph(int n) {
            return IntStream.range(0, n).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[i]);
        }

    }

    static interface IntegerIterator {
        boolean hasNext();

        int next();

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

        public int peekFirst() {
            return data[bpos];
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

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (IntegerIterator iterator = iterator(); iterator.hasNext(); ) {
                builder.append(iterator.next()).append(' ');
            }
            return builder.toString();
        }

    }

    static class UndirectedEdge extends DirectedEdge {
        public UndirectedEdge rev;

        public UndirectedEdge(int to) {
            super(to);
        }

    }

    static class IntegerArrayList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntegerArrayList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntegerArrayList(int[] data) {
            this(0);
            addAll(data);
        }

        public IntegerArrayList(IntegerArrayList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntegerArrayList() {
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

        public void addAll(int[] x) {
            addAll(x, 0, x.length);
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntegerArrayList list) {
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

        public boolean isEmpty() {
            return size == 0;
        }

        public void clear() {
            size = 0;
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntegerArrayList)) {
                return false;
            }
            IntegerArrayList other = (IntegerArrayList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Integer.hashCode(data[i]);
            }
            return h;
        }

        public IntegerArrayList clone() {
            IntegerArrayList ans = new IntegerArrayList();
            ans.addAll(this);
            return ans;
        }

    }

    static interface IntegerDeque extends IntegerStack {
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

    static interface IntegerComparator {
        public int compare(int a, int b);

    }

    static class Log2 {
        public static int floorLog(int x) {
            if (x <= 0) {
                throw new IllegalArgumentException();
            }
            return 31 - Integer.numberOfLeadingZeros(x);
        }

    }

    static class CompareUtils {
        private static final int THRESHOLD = 8;

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
            SequenceUtils.swap(data, f, RandomWrapper.INSTANCE.nextInt(f, t - 1));
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
}

