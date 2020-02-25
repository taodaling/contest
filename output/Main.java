import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
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
            BDoubleElimination solver = new BDoubleElimination();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class BDoubleElimination {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int k = in.readInt();
            if (k == 0) {
                out.println(0);
                return;
            }
//        if (k == 1) {
//            out.println(n * 2 + 2);
//            return;
//        }

            Node[] replacement = new Node[1 << (n - 1)];
            for (int i = 0; i < replacement.length; i++) {
                replacement[i] = new Node();
            }
            Node[] nodes = new Node[1 << n];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new Node();
                nodes[i].replacement = replacement[i / 2];
            }

            merge0(nodes);
            merge1(replacement);
            int sum = 1 + k;
            for (int i = 0; i < k; i++) {
                int id = in.readInt() - 1;
                nodes[id].repr = nodes[id];
            }

            for (int i = 0; i < nodes.length; i += 2) {
                if (nodes[i].repr != null && nodes[i + 1].repr != null) {
                    sum += update(nodes[i].replacement);
                    nodes[i].repr = null;
                }
            }

            Node[] parents = getP(nodes);
            sum += dpOnTree(parents, 0);
            out.println(sum);
        }

        public Node[] getP(Node[] nodes) {
            Node[] parents = new Node[nodes.length / 2];
            for (int i = 0; i < nodes.length; i += 2) {
                parents[i / 2] = nodes[i].parent;
            }
            return parents;
        }

        public int dpOnTree(Node[] nodes, int level) {
            int ans = 0;
            for (Node node : nodes) {
                if (node.children[0].repr != null &&
                        node.children[1].repr != null) {
                    if (test(node.children[0].repr.replacement) <
                            test(node.children[1].repr.replacement)) {
                        SequenceUtils.swap(node.children, 0, 1);
                    }
                    if (test(node.children[0].repr.replacement) > level) {
                        ans += update(node.children[0].repr.replacement) - level;
                        node.children[0].repr = null;
                    }
                }
                for (int i = 0; i < 2; i++) {
                    if (node.children[i].repr != null) {
                        node.repr = node.children[i].repr;
                    }
                }
            }

            if (nodes.length == 1) {
                if (test(nodes[0].repr.replacement) > level + 1) {
                    ans += update(nodes[0].repr.replacement) - level;
                }
            } else {

                ans += dpOnTree(getP(nodes), level + 1);
            }
            return ans;
        }

        public void merge0(Node[] nodes) {
            if (nodes.length == 1) {
                return;
            }
            Node[] parents = new Node[nodes.length / 2];
            for (int i = 0; i < parents.length; i++) {
                parents[i] = new Node();
            }
            for (int i = 0; i < nodes.length; i++) {
                nodes[i].parent = parents[i / 2];
                parents[i / 2].children[i % 2] = nodes[i];
            }
            merge0(parents);
        }

        public void merge1(Node[] nodes) {
            Node[] tmp = new Node[nodes.length];
            for (int i = 0; i < tmp.length; i++) {
                tmp[i] = new Node();
                nodes[i].parent = tmp[i];
            }
            if (nodes.length == 1) {
                return;
            }
            Node[] parents = new Node[nodes.length / 2];
            for (int i = 0; i < parents.length; i++) {
                parents[i] = new Node();
            }
            for (int i = 0; i < nodes.length; i++) {
                tmp[i].parent = parents[i / 2];
                parents[i / 2].children[i % 2] = tmp[i];
            }
            merge1(parents);

        }

        public int test(Node node) {
            return test0(node);// - 1;
        }

        public int test0(Node node) {
            if (node == null || node.paint) {
                return 0;
            }
            //node.paint = true;
            return 1 + test0(node.parent);
        }

        public int update(Node node) {
            return update0(node);// - 1;
        }

        public int update0(Node node) {
            if (node == null || node.paint) {
                return 0;
            }
            node.paint = true;
            return 1 + update0(node.parent);
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

        public FastOutput println(int c) {
            cache.append(c);
            println();
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

    static class SequenceUtils {
        public static <T> void swap(T[] data, int i, int j) {
            T tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

    }

    static class Node {
        Node parent;
        Node[] children = new Node[2];
        boolean paint;
        Node replacement;
        Node repr;

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
}

