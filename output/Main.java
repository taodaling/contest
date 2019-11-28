import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
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
            TaskE solver = new TaskE();
            int testCount = Integer.parseInt(in.next());
            for (int i = 1; i <= testCount; i++)
                solver.solve(i, in, out);
            out.close();
        }
    }

    static class TaskE {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int k = in.readInt();
            Node[][] nodes = new Node[n][2];
            BitOperator bo = new BitOperator();
            for (int i = 0; i < n; i++) {
                nodes[i][0] = new Node();
                nodes[i][0].id = i;
                nodes[i][1] = new Node();
                nodes[i][1].id = i;
                nodes[i][1].rev = 1;
                for (int j = 0; j < m; j++) {
                    boolean one = in.readChar() == '1';
                    nodes[i][0].set = bo.setBit(nodes[i][0].set, (m - 1 - j), one);
                    nodes[i][1].set = bo.setBit(nodes[i][1].set, j, one);
                }
                addEdge(nodes[i][0], nodes[i][1]);
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    for (int k1 = 0; k1 < 2; k1++) {
                        for (int k2 = 0; k2 < 2; k2++) {
                            if (m - Long.bitCount(nodes[i][k1].set ^ nodes[j][k2].set) < k) {
                                addEdge(nodes[i][k1], nodes[j][k2]);
                            }
                        }
                    }
                }
            }

            boolean valid = true;
            List<Node> reverse = new ArrayList<>();
            for (Node[] pair : nodes) {
                Node node = pair[0];
                if (node.color != 0) {
                    continue;
                }
                List<Node>[] lists = new List[]{
                        new ArrayList(), new ArrayList()
                };
                valid = valid && dfs(node, 1, lists);
                for (int i = 0; i < 2; i++) {
                    lists[i] = lists[i].stream().filter(x -> x.rev == 1).collect(Collectors.toList());
                }
                if (lists[0].size() > lists[1].size()) {
                    SequenceUtils.swap(lists, 0, 1);
                }
                reverse.addAll(lists[0]);
            }

            if (!valid) {
                out.println(-1);
                return;
            }
            out.println(reverse.size());
            for (Node node : reverse) {
                out.append(node.id + 1).append(' ');
            }
            out.println();
        }

        public void addEdge(Node a, Node b) {
            a.next.add(b);
            b.next.add(a);
        }

        public boolean dfs(Node root, int color, List<Node>[] lists) {
            if (root.color != 0) {
                return root.color == color;
            }
            root.color = color;
            lists[root.color - 1].add(root);
            for (Node node : root.next) {
                if (!dfs(node, 3 - color, lists)) {
                    return false;
                }
            }
            return true;
        }

    }

    static class Node {
        List<Node> next = new ArrayList<>();
        int color;
        long set;
        int id;
        int rev;

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

        public FastOutput println(int c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput println() {
            cache.append('\n');
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

    static class BitOperator {
        public long setBit(long x, int i, boolean v) {
            if (v) {
                x |= 1L << i;
            } else {
                x &= ~(1L << i);
            }
            return x;
        }

    }

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
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

        public String next() {
            return readString();
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

        public char readChar() {
            skipBlank();
            char c = (char) next;
            next = read();
            return c;
        }

        public String readString(StringBuilder builder) {
            skipBlank();

            while (next > 32) {
                builder.append((char) next);
                next = read();
            }

            return builder.toString();
        }

        public String readString() {
            defaultStringBuf.setLength(0);
            return readString(defaultStringBuf);
        }

    }

    static class SequenceUtils {
        public static <T> void swap(T[] data, int i, int j) {
            T tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

    }
}

