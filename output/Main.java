import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.Deque;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.Comparator;
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
            TaskG solver = new TaskG();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskG {
        int n;
        Segment segL;
        Segment segR;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            int q = in.readInt();
            int[] p = new int[n];
            for (int i = 0; i < n; i++) {
                p[i] = in.readInt();
            }
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].val = p[i];
                nodes[i].id = i;
            }
            Deque<Node> deque = new ArrayDeque<>(n);
            for (int i = 0; i < n; i++) {
                Node node = nodes[i];
                while (!deque.isEmpty() && deque.peekLast().val < node.val) {
                    Node last = deque.removeLast();
                    last.r = node.l;
                    node.l = last;
                }
                deque.addLast(node);
            }
            while (deque.size() > 1) {
                Node last = deque.removeLast();
                deque.peekLast().r = last;
            }
            Node root = deque.removeFirst();
            Query[] qs = new Query[q];
            for (int i = 0; i < q; i++) {
                qs[i] = new Query();
            }
            for (int i = 0; i < q; i++) {
                qs[i].l = in.readInt() - 1;
            }
            for (int i = 0; i < q; i++) {
                qs[i].r = in.readInt() - 1;
            }
            for (int i = 0; i < q; i++) {
                nodes[qs[i].l].pq = LeftistTree.merge(nodes[qs[i].l].pq,
                        new LeftistTree<>(qs[i]), Query.sortByR);
            }
            segL = new Segment(0, n);
            segR = new Segment(0, n);
            dfs(root);

            for (Query query : qs) {
                out.println(query.ans);
            }
        }

        public void dfs(Node root) {
            root.rangeR = root.rangeL = root.id;
            if (root.l != null) {
                dfs(root.l);
                root.rangeL = root.l.rangeL;
                root.pq = LeftistTree.merge(root.pq, root.l.pq, Query.sortByR);
            }
            if (root.r != null) {
                dfs(root.r);
                root.rangeR = root.r.rangeR;
                root.pq = LeftistTree.merge(root.pq, root.r.pq, Query.sortByR);
            }
            while (!root.pq.isEmpty() && root.pq.peek().r <= root.rangeR) {
                Query head = root.pq.peek();
                root.pq = LeftistTree.pop(root.pq, Query.sortByR);
                head.ans = (head.r - head.l + 1) +
                        segR.query(head.l, head.l, 0, n)
                        + segL.query(head.r, head.r, 0, n);
            }
            long leftSum = segL.query(root.id - 1, root.id - 1, 0, n);
            segL.update(root.id, root.rangeR, 0, n, leftSum - root.rangeL + 1, 1);
            long rightSum = segR.query(root.id + 1, root.id + 1, 0, n);
            segR.update(root.rangeL, root.id, 0, n, rightSum + root.rangeR + 1, -1);
        }

    }

    static class Query {
        int l;
        int r;
        long ans;
        static Comparator<Query> sortByR = (a, b) -> a.r - b.r;

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

        public FastOutput println(long c) {
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

    static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        long val;
        int index;
        long indexTime;

        public void modify(long x, long incTime) {
            val += x;
            indexTime += incTime;
        }

        public void pushUp() {
        }

        public void pushDown() {
            left.modify(val, indexTime);
            right.modify(val, indexTime);
            val = 0;
            indexTime = 0;
        }

        public Segment(int l, int r) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m);
                right = new Segment(m + 1, r);
                pushUp();
            } else {
                index = l;
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, long x, long incTime) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                modify(x, incTime);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x, incTime);
            right.update(ll, rr, m + 1, r, x, incTime);
            pushUp();
        }

        public long query(int ll, int rr, int l, int r) {
            if (noIntersection(ll, rr, l, r)) {
                return 0;
            }
            if (covered(ll, rr, l, r)) {
                return val + indexTime * index;
            }
            pushDown();
            int m = (l + r) >> 1;
            return left.query(ll, rr, l, m) +
                    right.query(ll, rr, m + 1, r);
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

    static class Node {
        Node l;
        Node r;
        int val;
        int rangeL;
        int rangeR;
        int id;
        LeftistTree<Query> pq = LeftistTree.NIL;

    }

    static class LeftistTree<K> {
        public static final LeftistTree NIL = new LeftistTree<>(null);
        LeftistTree<K> left = NIL;
        LeftistTree<K> right = NIL;
        int dist;
        K key;

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.dist = -1;
        }

        public LeftistTree(K key) {
            this.key = key;
        }

        public static <K> LeftistTree<K> merge(LeftistTree<K> a, LeftistTree<K> b, Comparator<K> cmp) {
            if (a == NIL) {
                return b;
            } else if (b == NIL) {
                return a;
            }
            if (cmp.compare(a.key, b.key) > 0) {
                LeftistTree<K> tmp = a;
                a = b;
                b = tmp;
            }
            a.right = merge(a.right, b, cmp);
            if (a.left.dist < a.right.dist) {
                LeftistTree<K> tmp = a.left;
                a.left = a.right;
                a.right = tmp;
            }
            a.dist = a.right.dist + 1;
            return a;
        }

        public boolean isEmpty() {
            return this == NIL;
        }

        public K peek() {
            return key;
        }

        public static <K> LeftistTree<K> pop(LeftistTree<K> root, Comparator<K> cmp) {
            return merge(root.left, root.right, cmp);
        }

        private void toStringDfs(StringBuilder builder) {
            if (this == NIL) {
                return;
            }
            builder.append(key).append(' ');
            left.toStringDfs(builder);
            right.toStringDfs(builder);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            toStringDfs(builder);
            return builder.toString();
        }

    }
}

