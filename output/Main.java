import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.LongStream;
import java.io.IOException;
import java.util.function.Consumer;
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
            PrintWriter out = new PrintWriter(outputStream);
            ProblemGRainbowGraph solver = new ProblemGRainbowGraph();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class ProblemGRainbowGraph {
        public void solve(int testNumber, FastInput in, PrintWriter out) {
            int n = in.readInt();
            int m = in.readInt();
            int[][] ae = new int[2][m];
            int[][] be = new int[2][m];
            long[] w = new long[m];

            DSU ad = new DSU(n);
            DSU bd = new DSU(n);
            for (int i = 0; i < m; i++) {
                int u = in.readInt() - 1;
                int v = in.readInt() - 1;
                w[i] = in.readInt();
                char c = in.readChar();
                if (c != 'B') {
                    ae[0][i] = u;
                    ae[1][i] = v;
                    ad.merge(u, v);
                }
                if (c != 'R') {
                    be[0][i] = u;
                    be[1][i] = v;
                    bd.merge(u, v);
                }
            }

            long sum = Arrays.stream(w).sum();
            for (int i = 1; i < n; i++) {
                if (ad.find(i) != ad.find(0) ||
                        bd.find(i) != bd.find(0)) {
                    for (int j = 0; j < m; j++) {
                        out.println(-1);
                    }
                    return;
                }
            }

            IntegerArrayList ans = new IntegerArrayList(m);
            MaximumWeightMatroidIntersect mwmi = new MaximumWeightMatroidIntersect(m, w);
            mwmi.setCallback(added -> {
                long x = 0;
                for (int i = 0; i < m; i++) {
                    if (added[i]) {
                        x += w[i];
                    }
                }
                ans.add((int) (sum - x));
                //debug.debug("added", Arrays.toString(added));
            });
            mwmi.intersect(MatroidIndependentSet.ofDeletionOnGraphRetainConnected(n, ae),
                    MatroidIndependentSet.ofDeletionOnGraphRetainConnected(n, be));
            while (ans.size() < m) {
                ans.add(-1);
            }
            while (ans.size() > m) {
                ans.pop();
            }
            ans.reverse();
            for (int x : ans.toArray()) {
                out.println(x);
            }
        }

    }

    static class MaximumWeightMatroidIntersect extends MatroidIntersect {
        protected long[] weight;
        protected long[] pathWeight;
        protected static final long weightInf = (long) 1e18;
        protected boolean[] inq;
        protected long[] fixWeight;

        public MaximumWeightMatroidIntersect(int n, long[] weight) {
            super(n);
            this.weight = weight;
            pathWeight = new long[n];
            inq = new boolean[n];
            fixWeight = new long[n];
        }

        protected boolean expand(MatroidIndependentSet a, MatroidIndependentSet b, int round) {
            a.prepare(added);
            b.prepare(added);
            Arrays.fill(x1, false);
            Arrays.fill(x2, false);
            a.extend(added, x1);
            b.extend(added, x2);

            for (int i = 0; i < n; i++) {
                if (added[i]) {
                    Arrays.fill(adj1[i], false);
                    Arrays.fill(adj2[i], false);
                    fixWeight[i] = weight[i];
                } else {
                    fixWeight[i] = -weight[i];
                }
            }

            a.computeAdj(added, adj1);
            b.computeAdj(added, adj2);
            Arrays.fill(dists, MatroidIntersect.distInf);
            Arrays.fill(pathWeight, weightInf);
            Arrays.fill(pre, -1);
            dq.clear();
            for (int i = 0; i < n; i++) {
                if (added[i]) {
                    continue;
                }
                //right
                if (x1[i]) {
                    dists[i] = 0;
                    pathWeight[i] = fixWeight[i];
                    dq.addLast(i);
                    inq[i] = true;
                }
            }

            while (!dq.isEmpty()) {
                int head = dq.removeFirst();
                inq[head] = false;

                for (int j = 0; j < n; j++) {
                    if (added[head] != added[j] && adj(head, j)) {
                        int comp = Long.compare(pathWeight[j], pathWeight[head] + fixWeight[j]);
                        if (comp == 0) {
                            comp = Integer.compare(dists[j], dists[head] + 1);
                        }
                        if (comp <= 0) {
                            continue;
                        }
                        dists[j] = dists[head] + 1;
                        pathWeight[j] = pathWeight[head] + fixWeight[j];
                        pre[j] = head;
                        if (!inq[j]) {
                            inq[j] = true;
                            dq.addLast(j);
                        }
                    }
                }
            }

            int tail = -1;
            for (int i = 0; i < n; i++) {
                if (!x2[i] || !x1[i] && pre[i] == -1) {
                    continue;
                }
                if (tail == -1 || pathWeight[i] < pathWeight[tail] || pathWeight[i] == pathWeight[tail] &&
                        dists[i] < dists[tail]) {
                    tail = i;
                }
            }
            if (tail == -1) {
                return false;
            }

            xorPath(tail);
            return true;
        }

    }

    static class MatroidIntersect {
        protected IntegerDequeImpl dq;
        protected int[] dists;
        protected boolean[] added;
        protected boolean[][] adj1;
        protected boolean[][] adj2;
        protected int n;
        protected boolean[] x1;
        protected boolean[] x2;
        protected static int distInf = (int) 1e9;
        protected int[] pre;
        protected Consumer<boolean[]> callback;
        protected static Consumer<boolean[]> nilCallback = x -> {
        };

        public void setCallback(Consumer<boolean[]> callback) {
            if (callback == null) {
                callback = nilCallback;
            }
            this.callback = callback;
        }

        public MatroidIntersect(int n) {
            this.n = n;
            dq = new IntegerDequeImpl(n);
            dists = new int[n];
            added = new boolean[n];
            adj1 = new boolean[n][];
            adj2 = new boolean[n][];
            x1 = new boolean[n];
            x2 = new boolean[n];
            pre = new int[n];
            setCallback(nilCallback);
        }

        protected boolean adj(int i, int j) {
            if (added[i]) {
                return adj1[i][j];
            } else {
                return adj2[j][i];
            }
        }

        protected boolean expand(MatroidIndependentSet a, MatroidIndependentSet b, int round) {
            Arrays.fill(x1, false);
            Arrays.fill(x2, false);
            a.prepare(added);
            b.prepare(added);
            a.extend(added, x1);
            b.extend(added, x2);
            for (int i = 0; i < n; i++) {
                if (x1[i] && x2[i]) {
                    pre[i] = -1;
                    xorPath(i);
                    return true;
                }
            }

            for (int i = 0; i < n; i++) {
                if (added[i]) {
                    Arrays.fill(adj1[i], false);
                    Arrays.fill(adj2[i], false);
                }
            }

            a.computeAdj(added, adj1);
            b.computeAdj(added, adj2);
            Arrays.fill(dists, distInf);
            Arrays.fill(pre, -1);
            dq.clear();
            for (int i = 0; i < n; i++) {
                if (added[i]) {
                    continue;
                }
                //right
                if (x1[i]) {
                    dists[i] = 0;
                    dq.addLast(i);
                }
            }

            int tail = -1;
            while (!dq.isEmpty()) {
                int head = dq.removeFirst();
                if (x2[head]) {
                    tail = head;
                    break;
                }
                for (int j = 0; j < n; j++) {
                    if (added[head] != added[j] && adj(head, j) && dists[j] > dists[head] + 1) {
                        dists[j] = dists[head] + 1;
                        dq.addLast(j);
                        pre[j] = head;
                    }
                }
            }

            if (tail == -1) {
                return false;
            }

            xorPath(tail);
            return true;
        }

        protected void xorPath(int tail) {
            boolean[] last1 = new boolean[n];
            boolean[] last2 = new boolean[n];
            for (boolean add = true; tail != -1; tail = pre[tail], add = !add) {
                assert added[tail] != add;
                added[tail] = add;
                if (add) {
                    adj1[tail] = last1;
                    adj2[tail] = last2;
                } else {
                    last1 = adj1[tail];
                    last2 = adj2[tail];
                    adj1[tail] = null;
                    adj2[tail] = null;
                }
            }
        }

        public boolean[] intersect(MatroidIndependentSet a, MatroidIndependentSet b) {
            Arrays.fill(added, false);
            int round = 0;
            callback.accept(added);
            while (expand(a, b, round)) {
                round++;
                callback.accept(added);
            }
            return added;
        }

    }

    static class SequenceUtils {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static void reverse(int[] data, int l, int r) {
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
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

    static class IntegerArrayList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public int[] getData() {
            return data;
        }

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

        public void reverse(int l, int r) {
            SequenceUtils.reverse(data, l, r);
        }

        public void reverse() {
            reverse(0, size - 1);
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

        public void addAll(IntegerArrayList list) {
            addAll(list.data, 0, list.size);
        }

        public int pop() {
            return data[--size];
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

    static interface MatroidIndependentSet {
        void computeAdj(boolean[] added, boolean[][] adj);

        void extend(boolean[] added, boolean[] extendable);

        void prepare(boolean[] added);

        public static MatroidIndependentSet ofDeletionOnGraphRetainConnected(int n, int[][] edges) {
            return new MatroidIndependentSet() {
                int[] left = new int[n];
                int[] right = new int[n];
                int[] dfn = new int[n];
                int[] set = new int[n];
                int[] low = new int[n];
                boolean[] instk = new boolean[n];
                IntegerDequeImpl dq = new IntegerDequeImpl(n);
                IntegerMultiWayStack g = new IntegerMultiWayStack(n, edges.length);
                int dfnOrder = 0;
                int idOrder = 0;
                IntegerArrayList inset = new IntegerArrayList(edges[0].length);

                /**
                 * O(rn)
                 */

                public void computeAdj(boolean[] added, boolean[][] adj) {
                    int[] insetData = inset.getData();
                    int m = inset.size();
                    for (int i = 0; i < added.length; i++) {
                        if (added[i]) {
                            continue;
                        }
                        int a = set[edges[0][i]];
                        int b = set[edges[1][i]];
                        if (set[a] == set[b]) {
                            for (int j = 0; j < m; j++) {
                                adj[insetData[j]][i] = true;
                            }
                        } else {
                            int l = Math.max(left[a], left[b]);
                            int r = Math.min(right[a], right[b]);
                            for (int j = 0; j < m; j++) {
                                if (include(edges[0][insetData[j]], l, r) != include(edges[1][insetData[j]], l, r)) {
                                    adj[insetData[j]][i] = true;
                                }
                            }
                        }
                    }
                }

                public boolean include(int i, int l, int r) {
                    return left[set[i]] >= l && right[set[i]] <= r;
                }

                /**
                 * O(n)
                 * @param added
                 * @param extendable
                 */

                public void extend(boolean[] added, boolean[] extendable) {
                    for (int i = 0; i < added.length; i++) {
                        if (added[i]) {
                            continue;
                        }
                        extendable[i] = set[edges[0][i]] == set[edges[1][i]];
                    }
                }

                public int opponent(int i, int root) {
                    return edges[0][i] == root ? edges[1][i] : edges[0][i];
                }

                private void tarjan(int root, int p) {
                    if (dfn[root] != -1) {
                        return;
                    }
                    low[root] = dfn[root] = dfnOrder++;
                    dq.addLast(root);
                    instk[root] = true;
                    for (IntegerIterator iterator = g.iterator(root); iterator.hasNext(); ) {
                        int e = iterator.next();
                        if (e == p) {
                            continue;
                        }
                        int node = opponent(e, root);
                        tarjan(node, e);
                        if (instk[node]) {
                            low[root] = Math.min(low[root], low[node]);
                        }
                    }
                    if (dfn[root] == low[root]) {
                        while (true) {
                            int tail = dq.removeLast();
                            set[tail] = root;
                            instk[tail] = false;
                            if (tail == root) {
                                break;
                            }
                        }
                    }
                }

                private void allocId(int root, int p) {
                    if (instk[root]) {
                        return;
                    }
                    instk[root] = true;

                    int l, r;
                    l = r = idOrder++;
                    for (IntegerIterator iterator = g.iterator(root); iterator.hasNext(); ) {
                        int e = iterator.next();
                        if (e == p) {
                            continue;
                        }
                        int node = opponent(e, root);
                        allocId(node, e);
                        l = Math.min(left[set[node]], l);
                        r = Math.max(right[set[node]], r);
                    }
                    left[set[root]] = Math.min(left[set[root]], l);
                    right[set[root]] = Math.max(right[set[root]], r);
                }

                /**
                 * O(rn)
                 * @param added
                 */

                public void prepare(boolean[] added) {
                    dfnOrder = idOrder = 0;
                    g.clear();
                    inset.clear();
                    for (int i = 0; i < added.length; i++) {
                        if (!added[i]) {
                            g.addLast(edges[0][i], i);
                            g.addLast(edges[1][i], i);
                        } else {
                            inset.add(i);
                        }
                    }
                    Arrays.fill(instk, false);
                    Arrays.fill(dfn, -1);
                    Arrays.fill(left, Integer.MAX_VALUE);
                    Arrays.fill(right, Integer.MIN_VALUE);
                    tarjan(0, -1);
                    allocId(0, -1);
                }
            };
        }

    }

    static interface IntegerDeque extends IntegerStack {
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

    static interface IntegerStack {
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

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }

    static class DSU {
        protected int[] p;
        protected int[] rank;

        public DSU(int n) {
            p = new int[n];
            rank = new int[n];
        }

        public final int find(int a) {
            if (p[a] == p[p[a]]) {
                return p[a];
            }
            preAccess(a);
            return p[a] = find(p[a]);
        }

        protected void preAccess(int a) {

        }

        protected void preMerge(int a, int b) {

        }

        public final void merge(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) {
                return;
            }
            if (rank[a] == rank[b]) {
                rank[a]++;
            }

            if (rank[a] < rank[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }

            preMerge(a, b);
            p[b] = a;
        }

    }
}

