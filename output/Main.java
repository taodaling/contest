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
import java.util.Comparator;
import java.util.Collections;
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
            FAlmostSameDistance solver = new FAlmostSameDistance();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FAlmostSameDistance {
        Node[] nodes;
        int[] ans;
        Segment segment;
        int n;
        IntList list = new IntList(500000);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            ans = new int[n + 1];
            nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }

            MultiWayIntStack edges = new MultiWayIntStack(n + 1, n * 2);
            for (int i = 1; i < n; i++) {
                int a = in.readInt();
                int b = in.readInt();
                nodes[a].next.add(nodes[b]);
                nodes[b].next.add(nodes[a]);
                edges.addLast(a - 1, b - 1);
                edges.addLast(b - 1, a - 1);
            }
            TreeDiameter diameter = new TreeDiameter(edges, n);
            ans[diameter.getDiameter()] = 2;
            ans[n] = 1;

            for (int i = 1; i <= n; i++) {
                nodes[i].next.sort(Node.sortById);
            }

            segment = new Segment(0, 2 * n);
            dfsForMaxDepth(nodes[1], null, 0);
            prepareForMax(nodes[1], null, 0);
            type1(nodes[1], null);
            type2(nodes[1], null);
            for (int i = n - 1; i >= 1; i--) {
                ans[i] = Math.max(ans[i], ans[i + 1]);
            }

            for (int i = 1; i <= n; i++) {
                out.println(ans[i]);
            }
        }

        public void type2(Node root, Node p) {
            list.clear();
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                list.add(node.maxDepth - root.depth);
            }
            ans[1] = Math.max(ans[1], 2);
            list.sort();
            int m = list.size();
            for (int i = m - 1; i >= 0; i--) {
                int cur = list.get(i);
                ans[cur * 2] = Math.max(ans[cur * 2], segment.query(cur + n - root.depth, 2 * n, 0, 2 * n) + m - i);
            }
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                int x = node.maxDepth - root.depth + n - root.depth;
                segment.update(x, x, 0, 2 * n, 1);
            }

            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                int x = node.maxDepth - root.depth + n - root.depth;
                segment.update(x, x, 0, 2 * n, -1);
                type2(node, root);
                segment.update(x, x, 0, 2 * n, 1);
            }

            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                int x = node.maxDepth - root.depth + n - root.depth;
                segment.update(x, x, 0, 2 * n, -1);
            }
        }

        public void type1(Node root, Node p) {
            int m = root.next.size();
            list.clear();
            for (Node node : root.next) {
                int depth = maxDepthRegardless(node, root) + 1;
                list.add(depth);
            }
            list.sort();
            for (int i = m - 1; i >= 0; i--) {
                int cur = list.get(i);
                int cnt = m - i;
                if (cnt > 1) {
                    ans[cur * 2] = Math.max(ans[cur * 2], cnt);
                }
            }

            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                type1(node, root);
            }

            ans[1] = Math.max(ans[1], m + 1);
        }

        public static int maxDepthRegardless(Node root, Node node) {
            int index = Collections.binarySearch(root.next, node, Node.sortById);
            return maxRegardless(root, index);
        }

        public static void prepareForMax(Node root, Node p, int maxDepth) {
            int m = root.next.size();
            root.preMax = new int[m];
            root.postMax = new int[m];
            for (int i = 0; i < m; i++) {
                Node node = root.next.get(i);
                root.preMax[i] = node == p ? maxDepth : (node.maxDepth - root.depth);
                if (i > 0) {
                    root.preMax[i] = Math.max(root.preMax[i], root.preMax[i - 1]);
                }
            }
            for (int i = m - 1; i >= 0; i--) {
                Node node = root.next.get(i);
                root.postMax[i] = node == p ? maxDepth : (node.maxDepth - root.depth);
                if (i + 1 < m) {
                    root.postMax[i] = Math.max(root.postMax[i], root.postMax[i + 1]);
                }
            }

            for (int i = 0; i < m; i++) {
                Node node = root.next.get(i);
                if (node == p) {
                    continue;
                }
                prepareForMax(node, root, maxRegardless(root, i) + 1);
            }
        }

        public static int maxRegardless(Node root, int i) {
            int max = 0;
            if (i > 0) {
                max = Math.max(max, root.preMax[i - 1]);
            }
            if (i + 1 < root.next.size()) {
                max = Math.max(max, root.postMax[i + 1]);
            }
            return max;
        }

        public static void dfsForMaxDepth(Node root, Node p, int depth) {
            root.depth = root.maxDepth = depth;
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                dfsForMaxDepth(node, root, depth + 1);
                root.maxDepth = Math.max(root.maxDepth, node.maxDepth);
            }
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

    static interface IntIterator {
        boolean hasNext();

        int next();

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

    static class Node {
        List<Node> next = new ArrayList<>();
        int[] preMax;
        int[] postMax;
        int maxDepth;
        int depth;
        int id;
        static Comparator<Node> sortById = (a, b) -> a.id - b.id;

    }

    static class MultiWayIntStack {
        private int[] values;
        private int[] next;
        private int[] heads;
        private int alloc;
        private int stackNum;

        public IntIterator iterator(final int queue) {
            return new IntIterator() {
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

        public MultiWayIntStack(int qNum, int totalCapacity) {
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
                for (IntIterator iterator = iterator(i); iterator.hasNext(); ) {
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

    static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private int cnt;

        public void pushUp() {
            cnt = left.cnt + right.cnt;
        }

        public void pushDown() {
        }

        public Segment(int l, int r) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m);
                right = new Segment(m + 1, r);
                pushUp();
            } else {

            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, int x) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                cnt += x;
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        public int query(int ll, int rr, int l, int r) {
            if (noIntersection(ll, rr, l, r)) {
                return 0;
            }
            if (covered(ll, rr, l, r)) {
                return cnt;
            }
            pushDown();
            int m = (l + r) >> 1;
            return left.query(ll, rr, l, m) +
                    right.query(ll, rr, m + 1, r);
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

    static class TreeDiameter {
        private MultiWayIntStack edges;
        private int[] depth;
        private int[] parents;
        private int diameter;
        private IntList centers;
        private IntList ends;

        public int getDiameter() {
            return diameter;
        }

        public TreeDiameter(MultiWayIntStack edges, int n) {
            this.edges = edges;
            depth = new int[n];
            centers = new IntList(2);
            ends = new IntList(2);
            parents = new int[n];

            dfsForDepth(0, -1);
            int end = 0;
            for (int i = 0; i < n; i++) {
                if (depth[i] > depth[end]) {
                    end = i;
                }
            }
            dfsForDepth(end, -1);
            int another = 0;
            for (int i = 0; i < n; i++) {
                if (depth[i] > depth[another]) {
                    another = i;
                }
            }

            ends.add(end);
            ends.add(another);

            diameter = depth[another];
            for (int i = another; i != -1; i = parents[i]) {
                if (depth[i] == DigitUtils.ceilDiv(diameter, 2) ||
                        depth[i] == DigitUtils.floorDiv(diameter, 2)) {
                    centers.add(i);
                }
            }

            ends.unique();
            centers.unique();
        }

        private void dfsForDepth(int root, int p) {
            parents[root] = p;
            depth[root] = p != -1 ? depth[p] + 1 : 0;
            for (IntIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
                int node = iterator.next();
                if (node == p) {
                    continue;
                }
                dfsForDepth(node, root);
            }
        }

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

    static class IntList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntList(IntList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntList() {
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

        public void addAll(IntList list) {
            addAll(list.data, 0, list.size);
        }

        public void sort() {
            if (size <= 1) {
                return;
            }
            Randomized.randomizedArray(data, 0, size);
            Arrays.sort(data, 0, size);
        }

        public void unique() {
            if (size <= 1) {
                return;
            }

            sort();
            int wpos = 1;
            for (int i = 1; i < size; i++) {
                if (data[i] != data[wpos - 1]) {
                    data[wpos++] = data[i];
                }
            }
            size = wpos;
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
            if (!(obj instanceof IntList)) {
                return false;
            }
            IntList other = (IntList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + data[i];
            }
            return h;
        }

        public IntList clone() {
            IntList ans = new IntList();
            ans.addAll(this);
            return ans;
        }

    }
}

