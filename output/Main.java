import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.Deque;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.io.InputStream;

/**
 * Built using CHelper plug-in Actual solution is at the top
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
            TaskF solver = new TaskF();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskF {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int p = in.readInt();
            int M = in.readInt();
            int m = in.readInt();

            TwoSat twoSat = new TwoSat(p + M + 1);

            for (int i = 1; i <= M; i++) {
                twoSat.deduce(twoSat.getElement(p + i + 1), twoSat.getElement(p + i));
            }

            for (int i = 0; i < n; i++) {
                int x = in.readInt();
                int y = in.readInt();
                twoSat.or(twoSat.getElement(x), twoSat.getElement(y));
            }

            for (int i = 1; i <= p; i++) {
                int l = in.readInt();
                int r = in.readInt();
                twoSat.deduce(twoSat.getElement(i), twoSat.getElement(p + l));
                twoSat.deduce(twoSat.getElement(i), twoSat.getNotElement(p + r + 1));
            }

            for (int i = 1; i <= m; i++) {
                int u = in.readInt();
                int v = in.readInt();
                twoSat.atLeastOneIsFalse(twoSat.getElement(u), twoSat.getElement(v));
            }

            if (!twoSat.solve(true)) {
                out.append(-1);
                return;
            }
            int k = 0;
            int f = 0;
            for (int i = 1; i <= p; i++) {
                if (twoSat.valueOf(i)) {
                    k++;
                }
            }

            for (int i = 1; i <= M; i++) {
                if (twoSat.valueOf(p + i) && !twoSat.valueOf(p + i + 1)) {
                    f = i;
                    break;
                }
            }

            out.append(k).append(' ').append(f).append('\n');
            for (int i = 1; i <= p; i++) {
                if (twoSat.valueOf(i)) {
                    out.append(i).append(' ');
                }
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

        public FastOutput append(int c) {
            cache.append(c);
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
    static class TwoSat {
        TwoSat.Node[][] nodes;
        Deque<TwoSat.Node> deque;
        int n;
        int order;

        public TwoSat(int n) {
            this.n = n;
            deque = new ArrayDeque(2 * n);
            nodes = new TwoSat.Node[2][n + 1];
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= n; j++) {
                    nodes[i][j] = new TwoSat.Node();
                    nodes[i][j].id = i == 0 ? -j : j;
                }
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= n; j++) {
                    nodes[i][j].inverse = nodes[1 - i][j];
                }
            }
            reset(n);
        }

        void reset(int n) {
            this.n = n;
            order = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= n; j++) {
                    nodes[i][j].dfn = -1;
                    nodes[i][j].outEdge.clear();
                    nodes[i][j].inEdge.clear();
                    nodes[i][j].head = null;
                    nodes[i][j].value = -1;
                    nodes[i][j].next = null;
                    nodes[i][j].relyOn = 0;
                }
            }
        }

        public TwoSat.Node getElement(int i) {
            return nodes[1][i];
        }

        public TwoSat.Node getNotElement(int i) {
            return nodes[0][i];
        }

        private void addEdge(TwoSat.Node a, TwoSat.Node b) {
            a.outEdge.add(b);
            b.inEdge.add(a);
        }

        public void or(TwoSat.Node a, TwoSat.Node b) {
            addEdge(a.inverse, b);
            addEdge(b.inverse, a);
        }

        public void deduce(TwoSat.Node a, TwoSat.Node b) {
            or(a.inverse, b);
        }

        public void atLeastOneIsFalse(TwoSat.Node a, TwoSat.Node b) {
            or(a.inverse, b.inverse);
        }

        public boolean valueOf(int i) {
            return nodes[1][i].value == 1;
        }

        public boolean solve(boolean fetchValue) {
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= n; j++) {
                    tarjan(nodes[i][j]);
                }
            }
            for (int i = 1; i <= n; i++) {
                if (nodes[0][i].head == nodes[1][i].head) {
                    return false;
                }
            }

            if (!fetchValue) {
                return true;
            }

            // Topological sort
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= n; j++) {
                    for (TwoSat.Node node : nodes[i][j].outEdge) {
                        if (node.head != nodes[i][j].head) {
                            nodes[i][j].head.relyOn++;
                        }
                    }
                }
            }

            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= n; j++) {
                    if (nodes[i][j].head == nodes[i][j] && nodes[i][j].relyOn == 0) {
                        deque.addLast(nodes[i][j]);
                    }
                }
            }

            while (!deque.isEmpty()) {
                TwoSat.Node head = deque.removeFirst();
                if (head.inverse.value != -1) {
                    head.value = 0;
                } else {
                    head.value = 1;
                }
                for (TwoSat.Node trace = head; trace != null; trace = trace.next) {
                    trace.value = head.value;
                    for (TwoSat.Node node : trace.inEdge) {
                        if (node.head == head) {
                            continue;
                        }
                        node.head.relyOn--;
                        if (node.head.relyOn == 0) {
                            deque.addLast(node.head);
                        }
                    }
                }
            }

            return true;
        }

        private void tarjan(TwoSat.Node root) {
            if (root.dfn >= 0) {
                return;
            }
            root.low = root.dfn = order++;
            deque.addLast(root);
            root.instack = true;
            for (TwoSat.Node node : root.outEdge) {
                tarjan(node);
                if (node.instack) {
                    root.low = Math.min(root.low, node.low);
                }
            }
            if (root.dfn == root.low) {
                while (true) {
                    TwoSat.Node head = deque.removeLast();
                    head.instack = false;
                    head.head = root;
                    if (head == root) {
                        break;
                    }
                    head.next = root.next;
                    root.next = head;
                }
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                builder.append(valueOf(i)).append(' ');
            }
            return builder.toString();
        }

        public static class Node {
            List<TwoSat.Node> outEdge = new LinkedList();
            List<TwoSat.Node> inEdge = new LinkedList<>();
            int id;
            TwoSat.Node inverse;
            TwoSat.Node head;
            TwoSat.Node next;
            int dfn;
            int low;
            boolean instack;
            int value;
            int relyOn;

            public String toString() {
                return "" + id;
            }

        }

    }
}

