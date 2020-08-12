import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.io.Writer;
import java.util.ArrayDeque;
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
            P4747CERC2017IntrinsicInterval solver = new P4747CERC2017IntrinsicInterval();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P4747CERC2017IntrinsicInterval {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] p = new int[n];
            for (int i = 0; i < n; i++) {
                p[i] = in.readInt() - 1;
            }
            ContinuousIntervalProblem problem = new ContinuousIntervalProblem(p);
            int m = in.readInt();
            for (int i = 0; i < m; i++) {
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                int[] ans = problem.findMinContinuousIntervalContains(l, r);
                out.append(ans[0] + 1).append(' ').append(ans[1] + 1).println();
            }
        }

    }

    static class ContinuousIntervalProblem {
        ContinuousIntervalProblem.Node[] leaf;
        List<ContinuousIntervalProblem.Node> nodes;
        KthAncestorOnTree kthAncestorOnTree;
        LcaOnTree lcaOnTree;

        public ContinuousIntervalProblem(int[] perm) {
            int n = perm.length;
            int[] p = new int[n + 2];
            for (int i = 1; i <= n; i++) {
                p[i] = perm[i - 1] + 1;
            }
            p[n + 1] = n + 1;

            nodes = new ArrayList<>(2 * n);
            ContinuousIntervalProblem.Node root = PermutationNode.build(p, () -> {
                ContinuousIntervalProblem.Node node = new ContinuousIntervalProblem.Node();
                node.id = nodes.size();
                nodes.add(node);
                return node;
            });
            leaf = new ContinuousIntervalProblem.Node[n + 2];

            int m = nodes.size();
            List<DirectedEdge>[] g = IntStream.range(0, m).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[i]);
            for (int i = 0; i < m; i++) {
                ContinuousIntervalProblem.Node node = nodes.get(i);
                for (PermutationNode pn : node.adj) {
                    ContinuousIntervalProblem.Node next = (ContinuousIntervalProblem.Node) pn;
                    g[i].add(new DirectedEdge(next.id));
                }
            }
            kthAncestorOnTree = new KthAncestorOnTree(g, root.id);
            lcaOnTree = new LcaOnTree(g, root.id);

            dfs(root, null, -1);
            dfs2(root);
        }

        public int[] findMinContinuousIntervalContains(int lId, int rId) {
            ContinuousIntervalProblem.Node l = leaf[lId + 1];
            ContinuousIntervalProblem.Node r = leaf[rId + 1];
            if (l == r) {
                return new int[]{l.ll - 1, l.rr - 1};
            }
            int lcaId = lcaOnTree.lca(l.id, r.id);
            ContinuousIntervalProblem.Node ancestor = nodes.get(lcaId);
            if (!ancestor.join) {
                return new int[]{ancestor.ll - 1, ancestor.rr - 1};
            } else {
                l = nodes.get(kthAncestorOnTree.kthAncestor(l.id, l.depth - ancestor.depth - 1));
                r = nodes.get(kthAncestorOnTree.kthAncestor(r.id, r.depth - ancestor.depth - 1));
                return new int[]{l.ll - 1, r.rr - 1};
            }
        }

        private void dfs(ContinuousIntervalProblem.Node root, ContinuousIntervalProblem.Node p, int index) {
            if (root.length() == 1) {
                leaf[root.ll] = root;
            }
            root.depth = p == null ? 0 : p.depth + 1;
            root.p = p;
            root.index = index;
            root.c = 1;
            int n = root.adj.size();
            if (root.join && !root.adj.isEmpty()) {
                root.c += (long) n * (n - 1) / 2 - 1;
            }
            root.ps = new long[n];
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    root.ps[i] = root.ps[i - 1];
                }
                ContinuousIntervalProblem.Node node = (ContinuousIntervalProblem.Node) root.adj.get(i);
                dfs(node, root, i);
                root.c += node.c;
                root.ps[i] += node.c;
            }
        }

        private long interval(long[] ps, int l, int r) {
            r = Math.min(r, ps.length - 1);
            l = Math.max(l, 0);
            if (l > r) {
                return 0;
            }
            long ans = ps[r];
            if (l > 0) {
                ans -= ps[l - 1];
            }
            return ans;
        }

        private long choose2(int n) {
            return n * (n - 1) / 2;
        }

        private void dfs2(ContinuousIntervalProblem.Node root) {
            if (root.p != null) {
                root.a = interval(root.p.ps, root.index + 1, root.p.ps.length - 1);
                root.b = interval(root.p.ps, 0, root.index - 1);
                if (root.p.join) {
                    root.a += choose2(root.p.ps.length - root.index - 1);
                    root.b += choose2(root.index);
                }
                root.psA = root.p.psA + root.a;
                root.psB = root.p.psB + root.b;
            }
            for (PermutationNode node : root.adj) {
                dfs2((ContinuousIntervalProblem.Node) node);
            }
        }

        private static class Node extends PermutationNode {
            ContinuousIntervalProblem.Node p;
            int depth;
            long[] ps;
            int index;
            long a;
            long b;
            long c;
            int id;
            long psA;
            long psB;

        }

    }

    static class Log2 {
        public static int floorLog(int x) {
            return 31 - Integer.numberOfLeadingZeros(x);
        }

    }

    static class KthAncestorOnTree {
        int[] depths;
        int[] longest;
        int[] longestChild;
        int[] top;
        int[] up;
        int[] down;
        int[] mem;
        int memIndicator;
        int[][] jump;
        List<? extends DirectedEdge>[] g;

        public int alloc(int n) {
            int ans = memIndicator;
            memIndicator += n;
            if (memIndicator > mem.length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            return ans;
        }

        public KthAncestorOnTree(List<? extends DirectedEdge>[] g, int root) {
            this.g = g;
            int n = g.length;
            depths = new int[n];
            longest = new int[n];
            top = new int[n];
            up = new int[n];
            down = new int[n];
            mem = new int[n * 2];
            longestChild = new int[n];
            jump = new int[Log2.floorLog(n) + 2][n];
            SequenceUtils.deepFill(jump, -1);
            dfs(root, -1);
            dfsForUpAndDown(root, -1, false);
        }

        public int kthAncestor(int root, int k) {
            if (k == 0) {
                return root;
            }
            int targetDepth = depths[root] - k;
            int log = Log2.floorLog(k);
            root = top[jump[log][root]];
            if (targetDepth <= depths[root]) {
                return mem[up[root] + depths[root] - targetDepth];
            } else {
                return mem[down[root] + targetDepth - depths[root]];
            }
        }

        private void dfs(int root, int p) {
            depths[root] = p == -1 ? 0 : (depths[p] + 1);
            jump[0][root] = p;
            for (int i = 0; jump[i][root] != -1; i++) {
                jump[i + 1][root] = jump[i][jump[i][root]];
            }
            longestChild[root] = -1;
            for (DirectedEdge e : g[root]) {
                if (e.to == p) {
                    continue;
                }
                dfs(e.to, root);
                if (longest[root] < longest[e.to] + 1) {
                    longest[root] = longest[e.to] + 1;
                    longestChild[root] = e.to;
                }
            }
        }

        private void upRecord(int root, int i, int until) {
            if (root == -1 || i == until) {
                return;
            }
            mem[i] = root;
            upRecord(jump[0][root], i + 1, until);
        }

        private void downRecord(int root, int i, int until) {
            if (root == -1 || i == until) {
                return;
            }
            mem[i] = root;
            downRecord(longestChild[root], i + 1, until);
        }

        private void dfsForUpAndDown(int root, int p, boolean connect) {
            if (connect) {
                top[root] = top[p];
            } else {
                top[root] = root;
                int len = longest[root];
                up[root] = alloc(len + 1);
                down[root] = alloc(len + 1);
                upRecord(root, up[root], up[root] + len + 1);
                downRecord(root, down[root], down[root] + len + 1);
            }

            for (DirectedEdge e : g[root]) {
                if (e.to == p) {
                    continue;
                }
                dfsForUpAndDown(e.to, root, e.to == longestChild[root]);
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

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }

    static class PermutationNode {
        public List<PermutationNode> adj = new ArrayList<>();
        public int l;
        public int r;
        public boolean join;
        public int ll;
        public int rr;
        private PermutationNode fail;
        private int failMin;
        private int failMax;

        private boolean increment() {
            return adj.get(0).l == l;
        }

        public int length() {
            return r - l + 1;
        }

        public String toString() {
            return String.format("[%d, %d]", l, r);
        }

        public static <T extends PermutationNode> T build(int[] perm, Supplier<T> supplier) {
            int n = perm.length;
            int[] index = new int[n];
            for (int i = 0; i < n; i++) {
                index[perm[i]] = i;
            }
            RMQ rangeMax = new RMQ(n);
            rangeMax.reset(0, n - 1, (a, b) -> -Integer.compare(index[a], index[b]));

            Deque<PermutationNode> dq = new ArrayDeque<>(n);
            for (int k = 0; k < n; k++) {
                PermutationNode x = supplier.get();
                x.failMin = x.failMax = x.l = x.r = perm[k];
                x.ll = x.rr = k;
                x.join = true;

                while (!dq.isEmpty()) {
                    //step 1
                    PermutationNode y = dq.peekLast();
                    if (y.join && y.adj.size() >= 2 && (x.r == y.l - 1 && !y.increment() ||
                            x.l == y.r + 1 && y.increment())) {
                        dq.removeLast();
                        y.adj.add(x);
                        y.l = Math.min(y.l, x.l);
                        y.r = Math.max(y.r, x.r);
                        y.rr = x.rr;
                        x = y;
                        continue;
                    }

                    //step 2
                    if (y.r + 1 == x.l || x.r + 1 == y.l) {
                        dq.removeLast();
                        PermutationNode z = supplier.get();
                        z.adj.add(y);
                        z.adj.add(x);
                        z.join = true;
                        z.l = Math.min(y.l, x.l);
                        z.r = Math.max(y.r, x.r);
                        z.ll = y.ll;
                        z.rr = x.rr;
                        x = z;
                        continue;
                    }

                    //step 3
                    //cut node has at least 4 children
                    x.failMin = x.l;
                    x.failMax = x.r;
                    x.fail = y;
                    boolean find = false;

                    for (PermutationNode node = y; node != null; node = node.fail) {
                        int l = Math.min(x.failMin, node.l);
                        int r = Math.max(x.failMax, node.r);

                        if (index[rangeMax.query(l, r)] > k) {
                            //fail here
                            break;
                        }

                        int cnt = k - node.ll + 1;
                        if (cnt == r - l + 1) {
                            find = true;
                            //can be merged into a cut node
                            PermutationNode fa = supplier.get();
                            fa.join = false;
                            fa.adj.add(x);
                            fa.l = l;
                            fa.r = r;
                            fa.ll = node.ll;
                            fa.rr = k;
                            while (!dq.isEmpty()) {
                                PermutationNode tail = dq.removeLast();
                                fa.adj.add(tail);
                                if (tail == node) {
                                    break;
                                }
                            }
                            SequenceUtils.reverse(fa.adj);
                            x = fa;
                            break;
                        }

                        x.failMin = Math.min(x.failMin, node.failMin);
                        x.failMax = Math.max(x.failMax, node.failMax);
                        x.fail = node.fail;
                    }

                    if (!find) {
                        break;
                    }
                }

                if (dq.isEmpty()) {
                    x.failMin = x.l;
                    x.failMax = x.r;
                }
                dq.addLast(x);
            }

            if (dq.size() != 1) {
                throw new IllegalStateException();
            }

            return (T) dq.removeFirst();
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

    static class SequenceUtils {
        public static <T> void swap(List<T> data, int i, int j) {
            T tmp = data.get(i);
            data.set(i, data.get(j));
            data.set(j, tmp);
        }

        public static void deepFill(Object array, int val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof int[]) {
                int[] intArray = (int[]) array;
                Arrays.fill(intArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
                }
            }
        }

        public static <T> void reverse(List<T> data, int l, int r) {
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
        }

        public static <T> void reverse(List<T> data) {
            reverse(data, 0, data.size() - 1);
        }

    }

    static class RMQ {
        private IntegerMultiWayStack stack;
        private RMQ.LcaOnTree lca;
        private Deque deque;
        private RMQ.Node[] nodes;
        private int offset;

        public RMQ(RMQ model) {
            this.stack = model.stack;
            this.deque = model.deque;
            this.nodes = model.nodes;
            this.lca = new RMQ.LcaOnTree(this.nodes.length);
        }

        public RMQ(int n) {
            stack = new IntegerMultiWayStack(n, n - 1);
            lca = new RMQ.LcaOnTree(n);
            deque = new ArrayDeque(n);
            nodes = new RMQ.Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new RMQ.Node();
            }
        }

        public <T> void reset(int l, int r, IntegerComparator comp) {
            int len = r - l + 1;
            stack.expandStackNum(len);
            stack.clear();
            deque.clear();
            offset = l;

            for (int i = 0; i < len; i++) {
                nodes[i].index = i;
                nodes[i].left = nodes[i].right = null;
            }
            Deque<RMQ.Node> deque = new ArrayDeque<>(len);
            for (int i = 0; i < len; i++) {
                while (!deque.isEmpty() && comp.compare(deque.peekLast().index + offset,
                        nodes[i].index + offset) > 0) {
                    RMQ.Node tail = deque.removeLast();
                    tail.right = nodes[i].left;
                    nodes[i].left = tail;
                }
                deque.addLast(nodes[i]);
            }
            while (deque.size() > 1) {
                RMQ.Node tail = deque.removeLast();
                deque.peekLast().right = tail;
            }
            RMQ.Node root = deque.removeLast();
            for (int i = 0; i < len; i++) {
                if (nodes[i].left != null) {
                    stack.addLast(i, nodes[i].left.index);
                }
                if (nodes[i].right != null) {
                    stack.addLast(i, nodes[i].right.index);
                }
            }

            lca.reset(stack, root.index);
        }

        public int query(int l, int r) {
            l -= offset;
            r -= offset;
            return lca.lca(l, r) + offset;
        }

        private static class LcaOnTree {
            int[] parent;
            int[] preOrder;
            int[] i;
            int[] head;
            int[] a;
            int time;

            void dfs1(IntegerMultiWayStack tree, int u, int p) {
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

            void dfs2(IntegerMultiWayStack tree, int u, int p, int up) {
                a[u] = up | Integer.lowestOneBit(i[u]);
                for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
                    int v = iterator.next();
                    if (v == p) continue;
                    dfs2(tree, v, u, a[u]);
                }
            }

            public void reset(IntegerMultiWayStack tree, int root) {
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

        private static class Node {
            public int index;
            public RMQ.Node left;
            public RMQ.Node right;

            public String toString() {
                return "" + index;
            }

        }

    }

    static class IntegerMultiWayStack {
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

        public void clear() {
            alloc = 0;
            Arrays.fill(heads, 0, stackNum, 0);
        }

        public boolean isEmpty(int qId) {
            return heads[qId] == 0;
        }

        public void expandStackNum(int qNum) {
            if (qNum <= stackNum) {
            } else if (qNum <= heads.length) {
                Arrays.fill(heads, stackNum, qNum, 0);
            } else {
                Arrays.fill(heads, stackNum, heads.length, 0);
                heads = Arrays.copyOf(heads, qNum);
            }
            stackNum = qNum;
        }

        public IntegerMultiWayStack(int qNum, int totalCapacity) {
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
                if (isEmpty(i)) {
                    continue;
                }
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

    static class DirectedEdge {
        public int to;

        public DirectedEdge(int to) {
            this.to = to;
        }

        public String toString() {
            return "->" + to;
        }

    }
}

