import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.AbstractCollection;
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
            ETrainTracks solver = new ETrainTracks();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class ETrainTracks {
        {
            LCTNode.dq.clear();
            LCTNode.time = -1;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();

            LCTNode[] nodes = new LCTNode[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new LCTNode();
                nodes[i].id = i;
            }
            for (int i = 0; i < n - 1; i++) {
                LCTNode a = nodes[in.readInt() - 1];
                LCTNode b = nodes[in.readInt() - 1];
                Edge edge = new Edge();
                edge.a = a;
                edge.b = b;
                edge.d = in.readInt();
                a.next.add(edge);
                b.next.add(edge);
            }

            dfs(nodes[0], null, 0);
            LCTNode.time = -1;
            for (int i = 0; i < m; i++) {
                LCTNode to = nodes[in.readInt() - 1];
                long time = in.readLong();
                LCTNode.time = time;
                LCTNode.access(to);
            }


            PriorityQueue<LCTNode> wait = new PriorityQueue<>(n, (a, b) -> Long.compare(a.begin, b.begin));
            PriorityQueue<LCTNode> pq = new PriorityQueue<>(n, (a, b) -> Long.compare(a.front, b.front));
            for (LCTNode node : nodes) {
                if (LCTNode.dq.isEmpty(node.id)) {
                    continue;
                }

                node.iterator = LCTNode.dq.iterator(node.id);
                node.distIterator = LCTNode.distDq.iterator(node.id);
                node.begin = 1;
                node.front = node.iterator.next();
                pq.add(node);
            }

            long inf = (long) 1e18;
            long time = inf;
            for (long i = 1; pq.size() + wait.size() > 0; i++) {
                if (pq.isEmpty()) {
                    i = Math.max(i, wait.peek().begin);
                }

                while (!wait.isEmpty() && wait.peek().begin <= i) {
                    pq.add(wait.remove());
                }

                LCTNode head = pq.remove();
                if (head.front < i) {
                    time = head.front;
                    break;
                }

                if (head.iterator.hasNext()) {
                    head.begin = head.front + head.distIterator.next();
                    head.front = head.iterator.next();
                    wait.add(head);
                }
            }


            int req = 0;
            out.append(time == inf ? -1 : time).append(' ');
            for (int i = 0; i < n; i++) {
                for (LongIterator iterator = LCTNode.dq.iterator(i); iterator.hasNext(); ) {
                    long next = iterator.next();
                    if (next < time) {
                        req++;
                    }
                }
            }

            out.println(req);
        }

        public void dfs(LCTNode root, LCTNode p, long depth) {
            root.depth = depth;
            for (Edge e : root.next) {
                LCTNode node = e.other(root);
                if (node == p) {
                    continue;
                }
                dfs(node, root, depth + e.d);
                root.pushDown();
                root.right.father = LCTNode.NIL;
                root.right.treeFather = root;
                root.setRight(node);
                root.pushUp();
            }
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

        public long readLong() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            long val = 0;
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

    static class Edge {
        LCTNode a;
        LCTNode b;
        long d;

        LCTNode other(LCTNode x) {
            return x == a ? b : a;
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(1 << 20);
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(int c) {
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

    static interface LongIterator {
        boolean hasNext();

        long next();

    }

    static class LongMultiWayDeque {
        private long[] values;
        private int[] next;
        private int[] prev;
        private int[] heads;
        private int[] tails;
        private int alloc;
        private int queueNum;

        public LongIterator iterator(final int queue) {
            return new LongIterator() {
                int ele = heads[queue];


                public boolean hasNext() {
                    return ele != 0;
                }


                public long next() {
                    long ans = values[ele];
                    ele = next[ele];
                    return ans;
                }
            };
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            prev = Arrays.copyOf(prev, newSize);
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
            Arrays.fill(heads, 0, queueNum, 0);
            Arrays.fill(tails, 0, queueNum, 0);
        }

        public boolean isEmpty(int qId) {
            return heads[qId] == 0;
        }

        public LongMultiWayDeque(int qNum, int totalCapacity) {
            values = new long[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            prev = new int[totalCapacity + 1];
            heads = new int[qNum];
            tails = new int[qNum];
            queueNum = qNum;
        }

        public void addLast(int qId, long x) {
            alloc();
            values[alloc] = x;

            if (heads[qId] == 0) {
                heads[qId] = tails[qId] = alloc;
                return;
            }
            next[tails[qId]] = alloc;
            prev[alloc] = tails[qId];
            tails[qId] = alloc;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < queueNum; i++) {
                builder.append(i).append(": ");
                for (LongIterator iterator = iterator(i); iterator.hasNext(); ) {
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

    static class LCTNode {
        public static final LCTNode NIL = new LCTNode();
        LCTNode left = NIL;
        LCTNode right = NIL;
        LCTNode father = NIL;
        LCTNode treeFather = NIL;
        boolean reverse;
        int id;
        long front;
        long begin;
        LongIterator iterator;
        LongIterator distIterator;
        List<Edge> next = new ArrayList<>();
        long depth;
        static LongMultiWayDeque dq = new LongMultiWayDeque(100000, 4000000);
        static LongMultiWayDeque distDq = new LongMultiWayDeque(100000, 4000000);
        static long time = -1;

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.father = NIL;
            NIL.treeFather = NIL;
            NIL.id = -1;
        }

        public static void access(LCTNode x) {
            LCTNode last = NIL;
            while (x != NIL) {
                splay(x);
                x.right.father = NIL;
                x.right.treeFather = x;
                x.setRight(last);
                x.pushUp();

                last = x;
                x = x.treeFather;
                if (time != -1 && x != NIL) {
                    dq.addLast(x.id, time + x.depth);
                    distDq.addLast(x.id, last.depth - x.depth);
                }
            }
        }

        public static void splay(LCTNode x) {
            if (x == NIL) {
                return;
            }
            LCTNode y, z;
            while ((y = x.father) != NIL) {
                if ((z = y.father) == NIL) {
                    y.pushDown();
                    x.pushDown();
                    if (x == y.left) {
                        zig(x);
                    } else {
                        zag(x);
                    }
                } else {
                    z.pushDown();
                    y.pushDown();
                    x.pushDown();
                    if (x == y.left) {
                        if (y == z.left) {
                            zig(y);
                            zig(x);
                        } else {
                            zig(x);
                            zag(x);
                        }
                    } else {
                        if (y == z.left) {
                            zag(x);
                            zig(x);
                        } else {
                            zag(y);
                            zag(x);
                        }
                    }
                }
            }

            x.pushDown();
            x.pushUp();
        }

        public static void zig(LCTNode x) {
            LCTNode y = x.father;
            LCTNode z = y.father;
            LCTNode b = x.right;

            y.setLeft(b);
            x.setRight(y);
            z.changeChild(y, x);

            y.pushUp();
        }

        public static void zag(LCTNode x) {
            LCTNode y = x.father;
            LCTNode z = y.father;
            LCTNode b = x.left;

            y.setRight(b);
            x.setLeft(y);
            z.changeChild(y, x);

            y.pushUp();
        }

        public String toString() {
            return "" + id;
        }

        public void pushDown() {
            if (this == NIL) {
                return;
            }
            if (reverse) {
                reverse = false;

                LCTNode tmpNode = left;
                left = right;
                right = tmpNode;

                left.reverse();
                right.reverse();
            }

            left.treeFather = treeFather;
            right.treeFather = treeFather;
        }

        public void reverse() {
            reverse = !reverse;
        }

        public void setLeft(LCTNode x) {
            left = x;
            x.father = this;
        }

        public void setRight(LCTNode x) {
            right = x;
            x.father = this;
        }

        public void changeChild(LCTNode y, LCTNode x) {
            if (left == y) {
                setLeft(x);
            } else {
                setRight(x);
            }
        }

        public void pushUp() {
            if (this == NIL) {
                return;
            }
        }

    }
}

