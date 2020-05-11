import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.io.IOException;
import java.util.TreeSet;
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
            DDynamicShortestPath solver = new DDynamicShortestPath();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DDynamicShortestPath {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int q = in.readInt();
            Node[] nodes = new Node[n];

            long inf = (long) 1e18;
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
                nodes[i].dist = inf;
            }
            Edge[] edges = new Edge[m];
            for (int i = 0; i < m; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                int c = in.readInt();
                edges[i] = new Edge(a, b, c);
                a.next.add(edges[i]);
            }

            TreeSet<Node> pq = new TreeSet<>((a, b) -> a.dist == b.dist ? a.id - b.id : Long.compare(a.dist, b.dist));
            Node root = nodes[0];
            root.dist = 0;
            pq.add(root);
            while (!pq.isEmpty()) {
                Node head = pq.pollFirst();
                for (Edge e : head.next) {
                    Node node = e.to;
                    if (node.dist > head.dist + e.w) {
                        pq.remove(node);
                        node.dist = head.dist + e.w;
                        pq.add(node);
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                nodes[i].last = nodes[i].dist;
                nodes[i].dist = inf;
            }

            MultiWayStack<Node> stack = new MultiWayStack<>(1000001, 1000000);
            while (q-- > 0) {
                int op = 0;

                int t = in.readInt();
                if (t == 1) {
                    Node v = nodes[in.readInt() - 1];
                    out.println(v.last >= 1e12 ? -1 : v.last);
                    continue;
                }

                int c = in.readInt();
                //stack.expandStackNum(c + 1);
                stack.danger();
                for (int j = 0; j < c; j++) {
                    op++;
                    int x = in.readInt();
                    Edge e = edges[x - 1];
                    e.reserve++;
                }
                for (Edge e : edges) {
                    op++;
                    e.w = e.reserve + e.from.last - e.to.last;
                }
                root.dist = 0;
                stack.addLast(0, root);
                for (int i = 0; i <= c; i++) {
                    op++;
                    while (!stack.isEmpty(i)) {
                        op++;
                        Node head = stack.removeLast(i);
                        if (head.dist != i) {
                            continue;
                        }
                        for (Edge e : head.next) {
                            op++;
                            Node node = e.to;
                            if (node.dist > head.dist + e.w) {
                                node.dist = head.dist + e.w;
                                if (node.dist <= c) {
                                    stack.addLast((int) node.dist, node);
                                }
                            }
                        }
                    }
                }

                //restore
                for (int i = 0; i < n; i++) {
                    nodes[i].last = Math.min(nodes[i].dist + nodes[i].last, inf);
                    nodes[i].dist = inf;
                }

                //debug.debug("q", q);
                //debug.debug("op", op);
                //debug.debug("nodes", nodes);
            }
        }

    }

    static interface RevokeIterator<E> extends Iterator<E> {
    }

    static class Edge {
        Node to;
        long w;
        long reserve;
        Node from;

        public Edge(Node from, Node to, long w) {
            this.from = from;
            this.to = to;
            this.reserve = this.w = w;
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

    static class MultiWayStack<T> {
        private Object[] values;
        private int[] next;
        private int[] heads;
        private int alloc;
        private int stackNum;

        public RevokeIterator<T> iterator(final int queue) {
            return new RevokeIterator<T>() {
                int ele = heads[queue];
                int pre = 0;


                public boolean hasNext() {
                    return ele != 0;
                }


                public T next() {
                    T ans = (T) values[ele];
                    pre = ele;
                    ele = next[ele];
                    return ans;
                }


                public void revoke() {
                    ele = pre;
                    pre = 0;
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

        public void danger() {
            alloc = 0;
        }

        public boolean isEmpty(int qId) {
            return heads[qId] == 0;
        }

        public MultiWayStack(int qNum, int totalCapacity) {
            values = new Object[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            heads = new int[qNum];
            stackNum = qNum;
        }

        public void addLast(int qId, T x) {
            alloc();
            values[alloc] = x;
            next[alloc] = heads[qId];
            heads[qId] = alloc;
        }

        public T removeLast(int qId) {
            T ans = (T) values[heads[qId]];
            heads[qId] = next[heads[qId]];
            return ans;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stackNum; i++) {
                builder.append(i).append(": ");
                for (Iterator iterator = iterator(i); iterator.hasNext(); ) {
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

    static class Node {
        long dist;
        long last;
        int id;
        List<Edge> next = new ArrayList<>();

        public String toString() {
            return "" + id + ":" + dist;
        }

    }
}

