import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.Deque;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.io.InputStream;

/**
 * Built using CHelper plug-in Actual solution is at the top
 * 
 * @author daltao
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "daltao", 1 << 27);
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
            TaskC solver = new TaskC();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskC {
        Deque<Node> deque = new ArrayDeque<>(200000);
        List<Node> circle = new ArrayList<>(200000);
        VersionArray va = new VersionArray(300000);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            for (int i = 1; i <= n; i++) {
                Node p = nodes[in.readInt()];
                p.next.add(nodes[i]);
            }

            for (int i = 1; i <= n; i++) {
                if (detectCircle(nodes[i])) {
                    break;
                }
            }

            if (circle.size() == 0) {
                possible(out);
                return;
            }

            // remove any edge and add back later
            circle.get(0).next.remove(circle.get(1));
            for (int i = 1; i <= n; i++) {
                dfsForMex(nodes[i]);
            }
            circle.get(0).next.add(circle.get(1));

            for (Node node : circle) {
                va.clear();
                for (Node next : node.next) {
                    if (next.incircle) {
                        continue;
                    }
                    va.set(next.mex, 1);
                }
                for (int i = 0;; i++) {
                    if (va.get(i) == 0) {
                        if (node.choice1 == -1) {
                            node.choice1 = i;
                        } else {
                            node.choice1 = i;
                            break;
                        }
                    }
                }
            }

            int m = circle.size();
            TwoSat twoSat = new TwoSat(m);
            for (int i = 0; i < m; i++) {
                Node node = circle.get(i);
                Node next = circle.get((i + 1) % m);
                int id = i + 1;
                int nid = i + 2;
                if (nid == m + 1) {
                    nid = 1;
                }
                if (node.choice1 == next.choice1) {
                    twoSat.deduce(twoSat.getElement(nid), twoSat.getNotElement(id));
                } else {
                    twoSat.deduce(twoSat.getElement(nid), twoSat.getElement(id));
                }
                if (node.choice1 == next.choice2) {
                    twoSat.deduce(twoSat.getNotElement(nid), twoSat.getNotElement(id));
                } else {
                    twoSat.deduce(twoSat.getNotElement(nid), twoSat.getElement(id));
                }
            }

            boolean ans = twoSat.solve(false);
            if (ans) {
                possible(out);
            } else {
                impossible(out);
            }
        }

        public void possible(FastOutput out) {
            out.println("POSSIBLE");
            return;
        }

        public void impossible(FastOutput out) {
            out.println("IMPOSSIBLE");
            return;
        }

        public boolean detectCircle(Node root) {
            if (root.visited) {
                if (!root.instk) {
                    return false;
                }
                // find a loop
                while (deque.peekFirst() != root) {
                    deque.removeFirst();
                }
                circle.addAll(deque);
                for (Node node : circle) {
                    node.incircle = true;
                }
                return true;
            }
            root.visited = root.instk = true;
            deque.addLast(root);

            for (Node node : root.next) {
                if (detectCircle(node)) {
                    return true;
                }
            }

            deque.removeLast();
            root.instk = false;
            return false;
        }

        public void dfsForMex(Node root) {
            if (root.mex != -1) {
                return;
            }
            for (Node node : root.next) {
                dfsForMex(node);
            }
            va.clear();
            for (Node node : root.next) {
                va.set(node.mex, 1);
            }
            for (int i = 0;; i++) {
                if (va.get(i) == 0) {
                    root.mex = i;
                    break;
                }
            }
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
    static class VersionArray {
        int[] data;
        int[] version;
        int now;
        int def;

        public VersionArray(int cap) {
            this(cap, 0);
        }

        public VersionArray(int cap, int def) {
            data = new int[cap];
            version = new int[cap];
            now = 0;
            this.def = def;
        }

        public void clear() {
            now++;
        }

        public void visit(int i) {
            if (version[i] < now) {
                version[i] = now;
                data[i] = def;
            }
        }

        public void set(int i, int v) {
            version[i] = now;
            data[i] = v;
        }

        public int get(int i) {
            visit(i);
            return data[i];
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
            List<TwoSat.Node> outEdge = new ArrayList(2);
            List<TwoSat.Node> inEdge = new ArrayList(2);
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
    static class Node {
        List<Node> next = new ArrayList<>();
        int id;
        boolean incircle;
        boolean visited;
        boolean instk;
        int mex = -1;
        int choice1 = -1;
        int choice2 = -1;

    }
}

