import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
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
            Domino solver = new Domino();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Domino {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            IntegerList[][] edges = new IntegerList[7][7];
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    edges[i][j] = new IntegerList();
                }
            }
            UndirectedEulerTrace trace = new UndirectedEulerTrace(7, n);
            for (int i = 0; i < n; i++) {
                int a = in.readInt();
                int b = in.readInt();
                edges[a][b].add(i + 1);
                trace.addEdge(a, b);
            }

            if (!trace.findTrace()) {
                out.println("No solution");
                return;
            }
            IntegerList et = trace.getEulerTrace();
            for (int i = 1; i < et.size(); i++) {
                int a = et.get(i - 1);
                int b = et.get(i);
                if (!edges[a][b].isEmpty()) {
                    out.append(edges[a][b].pop()).append(' ').append('+');
                } else {
                    out.append(edges[b][a].pop()).append(' ').append('-');
                }
                out.println();
            }
        }

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

    static class IntegerList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntegerList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntegerList(IntegerList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntegerList() {
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

        public void addAll(IntegerList list) {
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

        public boolean isEmpty() {
            return size == 0;
        }

        public void clear() {
            size = 0;
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntegerList)) {
                return false;
            }
            IntegerList other = (IntegerList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Integer.hashCode(data[i]);
            }
            return h;
        }

        public IntegerList clone() {
            IntegerList ans = new IntegerList();
            ans.addAll(this);
            return ans;
        }

    }

    static class UndirectedEulerTrace {
        UndirectedEulerTrace.Node[] nodes;
        List<UndirectedEulerTrace.Edge> edges;
        boolean containEulerTrace;
        boolean isEulerTraceClose;
        IntegerList eulerTrace;

        public UndirectedEulerTrace(int n, int m) {
            nodes = new UndirectedEulerTrace.Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new UndirectedEulerTrace.Node();
                nodes[i].id = i;
            }
            edges = new ArrayList<>(m);
            eulerTrace = new IntegerList(m);
        }

        public void addEdge(int a, int b) {
            UndirectedEulerTrace.Edge edge = new UndirectedEulerTrace.Edge();
            edge.a = nodes[a];
            edge.b = nodes[b];
            nodes[a].edges.add(edge);
            nodes[b].edges.add(edge);
            edges.add(edge);
        }

        public IntegerList getEulerTrace() {
            return eulerTrace;
        }

        private void dfs(UndirectedEulerTrace.Node root) {
            while (!root.deque.isEmpty()) {
                UndirectedEulerTrace.Edge tail = root.deque.removeFirst();
                if (tail.visited) {
                    continue;
                }
                tail.visited = true;
                dfs(tail.other(root));
            }
            eulerTrace.add(root.id);
        }

        public boolean findEulerTraceSince(UndirectedEulerTrace.Node root) {
            eulerTrace.clear();
            containEulerTrace = false;
            isEulerTraceClose = false;
            for (UndirectedEulerTrace.Node node : nodes) {
                node.deque.clear();
                node.deque.addAll(node.edges);
            }
            dfs(root);
            containEulerTrace = eulerTrace.size() == edges.size() + 1;
            if (!containEulerTrace) {
                return false;
            }
            isEulerTraceClose = eulerTrace.get(eulerTrace.size() - 1)
                    == eulerTrace.get(0);
            return true;
        }

        public boolean findTrace() {
            eulerTrace.clear();
            containEulerTrace = false;
            isEulerTraceClose = false;

            UndirectedEulerTrace.Node p1 = null;
            UndirectedEulerTrace.Node p2 = null;
            for (UndirectedEulerTrace.Node node : nodes) {
                if (node.edges.size() % 2 != 0) {
                    if (p1 == null) {
                        p1 = node;
                    } else if (p2 == null) {
                        p2 = node;
                    } else {
                        containEulerTrace = false;
                        return false;
                    }
                }
            }

            if (p1 != null && p2 != null) {
                if (findEulerTraceSince(p1)) {
                    return true;
                }
                if (findEulerTraceSince(p2)) {
                    return true;
                }
            }

            for (UndirectedEulerTrace.Node node : nodes) {
                if (node.edges.size() > 0) {
                    return findEulerTraceSince(node);
                }
            }

            return true;
        }

        private static class Node {
            List<UndirectedEulerTrace.Edge> edges = new ArrayList<>(2);
            Deque<UndirectedEulerTrace.Edge> deque = new ArrayDeque<>();
            int id;

            public String toString() {
                return "" + id;
            }

        }

        private static class Edge {
            UndirectedEulerTrace.Node a;
            UndirectedEulerTrace.Node b;
            boolean visited;

            public UndirectedEulerTrace.Node other(UndirectedEulerTrace.Node x) {
                return a == x ? b : a;
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
}

