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
            TaskD solver = new TaskD();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskD {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
                nodes[i].price = in.readInt();
            }

            for (int i = 1; i < n; i++) {
                Node a = nodes[in.readInt()];
                Node b = nodes[in.readInt()];
                a.next.add(b);
                b.next.add(a);
            }

            dfsForDp(nodes[1], null);
            out.println(nodes[1].dp);
            LeftSideTree<Node> pq = nodes[1].lst;
            while (!pq.isEmpty()) {
                pq.peek().find().selected = true;
                pq = LeftSideTree.pop(pq, Node.sortByPrice);
            }

            List<Integer> ans = new ArrayList<>(n);
            for (int i = 1; i <= n; i++) {
                if (nodes[i].find().selected) {
                    ans.add(i);
                }
            }

            out.println(ans.size());
            for (Integer node : ans) {
                out.append(node).append(' ');
            }
        }

        public void dfsForDp(Node root, Node father) {
            root.next.remove(father);
            if (root.next.isEmpty()) {
                root.dp = root.price;
                return;
            }
            LeftSideTree<Node> pq = LeftSideTree.NIL;
            for (Node node : root.next) {
                dfsForDp(node, root);
                root.dp += node.dp;
                pq = LeftSideTree.merge(pq, node.lst, Node.sortByPrice);
            }
            Node delegate = pq.peek();
            if (delegate.price == root.price) {
                Node.merge(delegate, root);
            }
            if (delegate.price > root.price) {
                pq = LeftSideTree.pop(pq, Node.sortByPrice);
                pq = LeftSideTree.merge(pq, root.lst, Node.sortByPrice);
                if (pq.peek().price == delegate.price) {
                    Node.merge(delegate, pq.peek());
                }
                root.dp -= delegate.price;
                root.dp += root.price;
            }
            root.lst = pq;
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

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(Object c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(int c) {
            cache.append(c).append('\n');
            return this;
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

    static class LeftSideTree<K> {
        public static final LeftSideTree NIL = new LeftSideTree<>(null);
        LeftSideTree<K> left = NIL;
        LeftSideTree<K> right = NIL;
        int dist;
        K key;

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.dist = -1;
        }

        public LeftSideTree(K key) {
            this.key = key;
        }

        public static <K> LeftSideTree<K> merge(LeftSideTree<K> a, LeftSideTree<K> b, Comparator<K> cmp) {
            if (a == NIL) {
                return b;
            } else if (b == NIL) {
                return a;
            }
            if (cmp.compare(a.key, b.key) > 0) {
                LeftSideTree<K> tmp = a;
                a = b;
                b = tmp;
            }
            a.right = merge(a.right, b, cmp);
            if (a.left.dist < a.right.dist) {
                LeftSideTree<K> tmp = a.left;
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

        public static <K> LeftSideTree<K> pop(LeftSideTree<K> root, Comparator<K> cmp) {
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

    static class Node {
        List<Node> next = new ArrayList<>();
        long dp;
        long price;
        Node p = this;
        int rank;
        boolean selected;
        static Comparator<Node> sortByPrice = (a, b) -> -Long.compare(a.price, b.price);
        LeftSideTree<Node> lst = new LeftSideTree<>(this);

        Node find() {
            return p == p.p ? p : (p = p.find());
        }

        static void merge(Node a, Node b) {
            a = a.find();
            b = b.find();
            if (a == b) {
                return;
            }
            if (a.rank == b.rank) {
                a.rank++;
            }
            if (a.rank > b.rank) {
                b.p = a;
            } else {
                a.p = b;
            }
        }

    }
}

