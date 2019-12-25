import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.Random;
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
            BZOJ1146 solver = new BZOJ1146();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class BZOJ1146 {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int q = in.readInt();

            int[] data = new int[n];
            for (int i = 0; i < n; i++) {
                data[i] = in.readInt();
            }

            IntegerList allTimes = new IntegerList(n + q);
            allTimes.addAll(data);

            MultiWayIntegerStack edges = new MultiWayIntegerStack(n, n * 2);
            for (int i = 1; i < n; i++) {
                int a = in.readInt() - 1;
                int b = in.readInt() - 1;
                edges.addLast(a, b);
                edges.addLast(b, a);
            }

            List<Modify> modifyList = new ArrayList<>(q);
            List<Query> queryList = new ArrayList<>(q);
            for (int i = 0; i < q; i++) {
                int k = in.readInt();
                int a = in.readInt();
                int b = in.readInt();
                if (k == 0) {
                    Modify modify = new Modify();
                    modify.x = a - 1;
                    modify.val = b;
                    modifyList.add(modify);
                    allTimes.add(modify.val);
                } else {
                    Query query = new Query();
                    query.u = a - 1;
                    query.v = b - 1;
                    query.version = modifyList.size();
                    query.k = k;
                    queryList.add(query);
                }
            }

            IntegerDiscreteMap dm = new IntegerDiscreteMap(allTimes.getData(), 0, allTimes.size());
            for (int i = 0; i < n; i++) {
                data[i] = dm.rankOf(data[i]);
            }
            for (Modify modify : modifyList) {
                modify.val = dm.rankOf(modify.val);
            }

            MoOnTreeBeta mo = new MoOnTreeBeta(edges);
            mo.handle(data, modifyList.toArray(new Modify[0]),
                    queryList.toArray(new Query[0]), new Handler(dm.maxRank()));

            for (Query query : queryList) {
                if (query.ans == -1) {
                    out.println("invalid request!");
                    continue;
                }
                out.println(dm.iThElement(query.ans));
            }
        }

    }

    static class Query implements MoOnTreeBeta.VersionQuery {
        int u;
        int v;
        int version;
        int ans;
        int k;

        public int getU() {
            return u;
        }

        public int getV() {
            return v;
        }

        public int getVersion() {
            return version;
        }

    }

    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput println(String c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput println(int c) {
            cache.append(c).append('\n');
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

    static class IntegerList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public int[] getData() {
            return data;
        }

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

        public void addAll(IntegerList list) {
            addAll(list.data, 0, list.size);
        }

        public int size() {
            return size;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
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

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }

    static class MoOnTreeBeta {
        private MultiWayIntegerStack edges;
        private boolean[] odd;
        private int[] eulerTour;
        private int eulerTourTail = 0;
        private int[] begin;
        private int[] end;
        private LcaOnTree lcaOnTree;

        public MoOnTreeBeta(MultiWayIntegerStack edges) {
            this.edges = edges;
            odd = new boolean[edges.stackNumber()];
            eulerTour = new int[edges.stackNumber() * 2];
            begin = new int[edges.stackNumber()];
            end = new int[edges.stackNumber()];
            dfs(0, -1);
            lcaOnTree = new LcaOnTree(edges, 0);
        }

        private void dfs(int root, int p) {
            begin[root] = eulerTourTail;
            eulerTour[eulerTourTail++] = root;

            for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
                int node = iterator.next();
                if (node == p) {
                    continue;
                }
                dfs(node, root);
            }

            end[root] = eulerTourTail;
            eulerTour[eulerTourTail++] = root;
        }

        private <Q extends MoOnTreeBeta.Query> void invert(int node, int x, MoOnTreeBeta.IntHandler<Q> handler) {
            odd[node] = !odd[node];
            if (odd[node]) {
                handler.add(node, x);
            } else {
                handler.remove(node, x);
            }
        }

        public <Q extends MoOnTreeBeta.VersionQuery> void handle(int[] data, MoOnTreeBeta.IntModify[] modifies, Q[] queries, MoOnTreeBeta.IntHandler<Q> handler) {
            handle(data, modifies, queries, handler, (int) Math.ceil(Math.pow(eulerTour.length, 2.0 / 3)));
        }

        public <Q extends MoOnTreeBeta.VersionQuery> void handle(int[] data, MoOnTreeBeta.IntModify[] modifies, Q[] queries, MoOnTreeBeta.IntHandler<Q> handler,
                                                                 int blockSize) {
            if (data.length == 0 || queries.length == 0) {
                return;
            }

            MoOnTreeBeta.QueryWrapper<Q>[] wrappers = new MoOnTreeBeta.QueryWrapper[queries.length];
            for (int i = 0; i < queries.length; i++) {
                Q q = queries[i];
                wrappers[i] = new MoOnTreeBeta.QueryWrapper<Q>();
                wrappers[i].q = q;
                int u = q.getU();
                int v = q.getV();
                int ul = begin[u];
                int ur = end[u];
                int vl = begin[v];
                int vr = end[v];

                if (ur > vr) {
                    int tmp = ul;
                    ul = vl;
                    vl = tmp;

                    tmp = ur;
                    ur = vr;
                    vr = tmp;
                }

                if (ur < vl) {
                    wrappers[i].l = ur;
                    wrappers[i].r = vl;
                    wrappers[i].extra = end[lcaOnTree.lca(u, v)];
                } else {
                    wrappers[i].l = ur;
                    wrappers[i].r = vr - 1;
                    wrappers[i].extra = vr;
                }
            }

            Arrays.fill(odd, false);
            Arrays.sort(wrappers, (a, b) -> {
                int ans = a.l / blockSize - b.l / blockSize;
                if (ans == 0) {
                    ans = a.q.getVersion() / blockSize - b.q.getVersion() / blockSize;
                }
                if (ans == 0) {
                    ans = a.r - b.r;
                }
                return ans;
            });

            int l = wrappers[0].l;
            int r = l - 1;
            int v = 0;
            for (MoOnTreeBeta.QueryWrapper<Q> wrapper : wrappers) {
                int ll = wrapper.l;
                int rr = wrapper.r;
                int vv = wrapper.q.getVersion();

                while (v < vv) {
                    modifies[v].apply(data, handler, odd);
                    v++;
                }
                while (v > vv) {
                    v--;
                    modifies[v].revoke(data, handler, odd);
                }
                while (l > ll) {
                    l--;
                    invert(eulerTour[l], data[eulerTour[l]], handler);
                }
                while (r < rr) {
                    r++;
                    invert(eulerTour[r], data[eulerTour[r]], handler);
                }
                while (l < ll) {
                    invert(eulerTour[l], data[eulerTour[l]], handler);
                    l++;
                }
                while (r > rr) {
                    invert(eulerTour[r], data[eulerTour[r]], handler);
                    r--;
                }
                invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
                handler.answer(wrapper.q);
                invert(eulerTour[wrapper.extra], data[eulerTour[wrapper.extra]], handler);
            }
        }

        private static class QueryWrapper<Q extends MoOnTreeBeta.Query> {
            int l;
            int r;
            int extra;
            Q q;

        }

        public interface Query {
            int getU();

            int getV();

        }

        public interface IntHandler<Q extends MoOnTreeBeta.Query> {
            void add(int node, int x);

            void remove(int node, int x);

            void answer(Q q);

        }

        public interface VersionQuery extends MoOnTreeBeta.Query {
            int getVersion();

        }

        public interface IntModify {
            <Q extends MoOnTreeBeta.VersionQuery> void apply(int[] data, MoOnTreeBeta.IntHandler<Q> handler, boolean[] exists);

            <Q extends MoOnTreeBeta.VersionQuery> void revoke(int[] data, MoOnTreeBeta.IntHandler<Q> handler, boolean[] exists);

        }

    }

    static class SequenceUtils {
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

    static class LcaOnTree {
        int[] parent;
        int[] preOrder;
        int[] i;
        int[] head;
        int[] a;
        int time;

        void dfs1(MultiWayIntegerStack tree, int u, int p) {
            parent[u] = p;
            i[u] = preOrder[u] = time++;
            for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
                int v = iterator.next();
                if (v == p) continue;
                dfs1(tree, v, u);
                if (Integer.lowestOneBit(i[u]) < Integer.lowestOneBit(i[v])) {
                    i[u] = i[v];
                }
            }
            head[i[u]] = u;
        }

        void dfs2(MultiWayIntegerStack tree, int u, int p, int up) {
            a[u] = up | Integer.lowestOneBit(i[u]);
            for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
                int v = iterator.next();
                if (v == p) continue;
                dfs2(tree, v, u, a[u]);
            }
        }

        public LcaOnTree(MultiWayIntegerStack tree, int root) {
            int n = tree.stackNumber();
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

    static class Randomized {
        static Random random = new Random();

        public static void randomizedArray(int[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                int tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }

    static class IntegerDiscreteMap {
        int[] val;
        int f;
        int t;

        public IntegerDiscreteMap(int[] val, int f, int t) {
            Randomized.randomizedArray(val, f, t);
            Arrays.sort(val, f, t);
            int wpos = f + 1;
            for (int i = f + 1; i < t; i++) {
                if (val[i] == val[i - 1]) {
                    continue;
                }
                val[wpos++] = val[i];
            }
            this.val = val;
            this.f = f;
            this.t = wpos;
        }

        public int rankOf(int x) {
            return Arrays.binarySearch(val, f, t, x) - f;
        }

        public int iThElement(int i) {
            return val[f + i];
        }

        public int maxRank() {
            return t - f - 1;
        }

        public String toString() {
            return Arrays.toString(Arrays.copyOfRange(val, f, t));
        }

    }

    static class MultiWayIntegerStack {
        private int[] values;
        private int[] next;
        private int[] heads;
        private int alloc;
        private int stackNum;

        public IntegerIterator iterator(final int queue) {
            return new IntegerIterator() {
                int ele = heads[queue];


                public boolean hasNext() {
                    return ele != 0;
                }


                public int next() {
                    int ans = values[ele];
                    ele = next[ele];
                    return ans;
                }
            };
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            values = Arrays.copyOf(values, newSize);
        }

        public void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
        }

        public int stackNumber() {
            return stackNum;
        }

        public MultiWayIntegerStack(int qNum, int totalCapacity) {
            values = new int[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            heads = new int[qNum];
            stackNum = qNum;
        }

        public void addLast(int qId, int x) {
            alloc();
            values[alloc] = x;
            next[alloc] = heads[qId];
            heads[qId] = alloc;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stackNum; i++) {
                builder.append(i).append(": ");
                for (IntegerIterator iterator = iterator(i); iterator.hasNext(); ) {
                    builder.append(iterator.next()).append(",");
                }
                if (builder.charAt(builder.length() - 1) == ',') {
                    builder.setLength(builder.length() - 1);
                }
                builder.append('\n');
            }
            return builder.toString();
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

    static class Handler implements MoOnTreeBeta.IntHandler<Query> {
        int[] cnts;
        int[] summary;
        int blockSize;

        public Handler(int n) {
            cnts = new int[n + 1];
            blockSize = (int) Math.ceil(Math.sqrt(n + 1));
            summary = new int[n / blockSize + 1];
        }

        public void add(int node, int x) {
            cnts[x]++;
            summary[x / blockSize]++;
        }

        public void remove(int node, int x) {
            cnts[x]--;
            summary[x / blockSize]--;
        }

        public void answer(Query query) {
            query.ans = -1;
            int k = query.k;
            for (int i = summary.length - 1; i >= 0; i--) {
                if (k > summary[i]) {
                    k -= summary[i];
                    continue;
                }

                for (int j = Math.min((i + 1) * blockSize, cnts.length) - 1; ; j--) {
                    if (k > cnts[j]) {
                        k -= cnts[j];
                        continue;
                    }
                    query.ans = j;
                    return;
                }
            }
        }

    }

    static class Modify implements MoOnTreeBeta.IntModify {
        int x;
        int val;

        public <Q extends MoOnTreeBeta.VersionQuery> void apply(int[] data, MoOnTreeBeta.IntHandler<Q> handler, boolean[] exists) {
            if (exists[x]) {
                handler.remove(x, data[x]);
            }
            int oldVal = data[x];
            data[x] = val;
            val = oldVal;
            if (exists[x]) {
                handler.add(x, data[x]);
            }
        }

        public <Q extends MoOnTreeBeta.VersionQuery> void revoke(int[] data, MoOnTreeBeta.IntHandler<Q> handler, boolean[] exists) {
            apply(data, handler, exists);
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
}

