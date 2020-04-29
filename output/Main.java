import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.util.Map;
import java.io.Writer;
import java.util.Map.Entry;
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
            CColorfulTree solver = new CColorfulTree();
            try {
                int testNumber = 1;
                while (true)
                    solver.solve(testNumber++, in, out);
            } catch (UnknownError e) {
                out.close();
            }
        }
    }

    static class CColorfulTree {
        long subtract = 0;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            if (!in.hasMore()) {
                throw new UnknownError();
            }

            subtract = 0;

            int n = in.readInt();
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].color = in.readInt();
            }
            for (int i = 0; i < n - 1; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                a.next.add(b);
                b.next.add(a);
            }

            dfs(nodes[0], null);
            for (int i = 1; i <= n; i++) {
                int value = nodes[0].cnts.get(i);
                remove(value);
            }

            long ans = pick2(n) * n - subtract;
            out.printf("Case #%d: %d", testNumber, ans).println();
        }

        public long pick2(long x) {
            return x * (x - 1) / 2;
        }

        public void remove(long b) {
            subtract += pick2(b);
        }

        public void dfs(Node root, Node p) {
            root.cnts.modAll(1);
            root.cnts.modOne(root.color, -1);
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                dfs(node, root);
                int value = node.cnts.get(root.color);
                node.cnts.modOne(root.color, -value);
                remove(value);
                root.cnts = MapHolder.merge(root.cnts, node.cnts);
            }
        }

    }

    static class Node {
        int color;
        List<Node> next = new ArrayList<>();
        MapHolder cnts = new MapHolder();

    }

    static class MapHolder {
        Map<Integer, Integer> cnt;
        int all;

        public MapHolder(Map<Integer, Integer> cnt) {
            this.cnt = cnt;
        }

        public MapHolder() {
            this(new HashMap<>());
        }

        public void modAll(int x) {
            all += x;
        }

        public void modOne(Integer c, int x) {
            cnt.put(c, cnt.getOrDefault(c, 0) + x);
        }

        public int get(Integer c) {
            return cnt.getOrDefault(c, 0) + all;
        }

        public static MapHolder merge(MapHolder a, MapHolder b) {
            if (a.cnt.size() > b.cnt.size()) {
                MapHolder holder = a;
                a = b;
                b = holder;
            }
            b.all += a.all;
            for (Map.Entry<Integer, Integer> entry : a.cnt.entrySet()) {
                b.modOne(entry.getKey(), entry.getValue());
            }
            return b;
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

        public boolean hasMore() {
            skipBlank();
            return next != -1;
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

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
            return this;
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
}

