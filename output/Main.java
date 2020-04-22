import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.util.Deque;
import java.util.AbstractCollection;
import java.io.OutputStreamWriter;
import java.util.function.IntFunction;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
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
            CEnvy solver = new CEnvy();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CEnvy {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            Edge[] edges = new Edge[m];
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            for (int i = 0; i < m; i++) {
                edges[i] = new Edge();
                edges[i].a = nodes[in.readInt() - 1];
                edges[i].b = nodes[in.readInt() - 1];
                edges[i].w = in.readInt();
            }
            Edge[] sortedByWeight = edges.clone();
            Arrays.sort(sortedByWeight, (a, b) -> Integer.compare(a.w, b.w));

            int q = in.readInt();
            Query[] qs = new Query[q];
            for (int i = 0; i < q; i++) {
                int k = in.readInt();
                Edge[] set = new Edge[k];
                qs[i] = new Query();
                for (int j = 0; j < k; j++) {
                    set[j] = edges[in.readInt() - 1];
                }
                Arrays.sort(set, (a, b) -> Integer.compare(a.w, b.w));
                qs[i].dq = new Range2DequeAdapter<>(j -> set[j], 0, k - 1);
            }


            PriorityQueue<Query> pq = new PriorityQueue<>(q, (a, b) -> Integer.compare(a.front, b.front));
            for (int i = 0; i < q; i++) {
                qs[i].front = qs[i].dq.peekFirst().w;
                pq.add(qs[i]);
            }
            Deque<Node> sp = new ArrayDeque<>(n);
            for (int i = 0; i < m && !pq.isEmpty(); i++) {
                int w = sortedByWeight[i].w;
                while (!pq.isEmpty() && pq.peek().dq.peekFirst().w <= w) {
                    Query top = pq.remove();
                    while (!top.dq.isEmpty() && top.dq.peekFirst().w <= w) {
                        Edge head = top.dq.removeFirst();
                        Node v = Node.merge(head.a, head.b);
                        if (v == null) {
                            top.valid = false;
                            break;
                        }
                        sp.addLast(v);
                    }
                    while (!sp.isEmpty()) {
                        Node.rollback(sp.removeLast());
                    }
                    if (top.valid && !top.dq.isEmpty()) {
                        top.front = top.dq.peekFirst().w;
                        pq.add(top);
                    }
                }
                Node.merge(sortedByWeight[i].a, sortedByWeight[i].b);
            }

            for (Query query : qs) {
                out.println(query.valid ? "YES" : "NO");
            }
        }

    }

    static class Node {
        Node p;
        int rank = 1;
        int id;

        public Node find() {
            Node root = this;
            while (root.p != null) {
                root = root.p;
            }
            return root;
        }

        public static Node merge(Node a, Node b) {
            a = a.find();
            b = b.find();
            if (a == b) {
                return null;
            }

            if (a.rank > b.rank) {
                Node tmp = a;
                a = b;
                b = tmp;
            }
            a.p = b;
            b.rank += a.rank;
            return a;
        }

        public static void rollback(Node node) {
            for (Node trace = node.p; trace != null; trace = trace.p) {
                trace.rank -= node.rank;
            }
            node.p = null;
        }

    }

    static class Range2DequeAdapter<T> implements SimplifiedDeque<T> {
        IntFunction<T> data;
        int l;
        int r;

        public Range2DequeAdapter(IntFunction<T> data, int l, int r) {
            this.data = data;
            this.l = l;
            this.r = r;
        }

        public boolean isEmpty() {
            return l > r;
        }

        public T peekFirst() {
            return data.apply(l);
        }

        public T removeFirst() {
            return data.apply(l++);
        }

        public Iterator<T> iterator() {
            return new Iterator<T>() {
                int iter = l;


                public boolean hasNext() {
                    return iter <= r;
                }


                public T next() {
                    return data.apply(iter++);
                }
            };
        }

    }

    static class Edge {
        Node a;
        Node b;
        int w;

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

        public FastOutput append(String c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
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

    static interface SimplifiedDeque<T> extends SimplifiedStack<T> {
        T peekFirst();

        T removeFirst();

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

    static interface SimplifiedStack<T> extends Iterable<T> {
        boolean isEmpty();

    }

    static class Query {
        SimplifiedDeque<Edge> dq;
        boolean valid = true;
        int front;

    }
}

