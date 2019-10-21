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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskE {
        boolean flag;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            flag = true;

            int n = in.readInt();
            Node[] nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
            }
            for (int i = 1; i < n; i++) {
                Node a = nodes[in.readInt()];
                Node b = nodes[in.readInt()];
                a.next.add(b);
                b.next.add(a);
            }
            int k = in.readInt();
            Node root = nodes[1];
            for (int i = 0; i < k; i++) {
                Node v = nodes[in.readInt()];
                v.num = in.readInt();
                if (root.num > v.num) {
                    root = v;
                }
            }

            check(root, null, root.num);
            setRange(root, null);
            setValue(root, null, root.num, root.num);

            if (!flag) {
                out.println("No");
                return;
            }

            out.println("Yes");
            for (int i = 1; i <= n; i++) {
                out.println(nodes[i].num);
            }
        }

        public void setValue(Node root, Node fa, int l, int r) {
            l = Math.max(l, root.l);
            r = Math.min(r, root.r);
            root.num = r;
            for (Node node : root.next) {
                if (node == fa) {
                    continue;
                }
                setValue(node, root, root.num - 1, root.num + 1);
            }
        }

        public void setRange(Node root, Node fa) {
            root.l = -(int) 1e9;
            root.r = (int) 1e9;
            if (root.num != Node.unknow) {
                root.l = root.r = root.num;
            }

            for (Node node : root.next) {
                if (node == fa) {
                    continue;
                }
                setRange(node, root);
                if (root.r < node.l || root.l > node.r) {
                    flag = false;
                }
                root.l = Math.max(root.l, node.l - 1);
                root.r = Math.min(root.r, node.r + 1);
            }

            if (root.l > root.r) {
                flag = false;
            }
        }

        public void check(Node root, Node fa, int depth) {
            if (root.num != Node.unknow && root.num % 2 != depth % 2) {
                flag = false;
            }
            for (Node node : root.next) {
                if (node == fa) {
                    continue;
                }
                check(node, root, depth + 1);
            }
        }

    }
    static class Node {
        public static int unknow = (int) 1e9;
        List<Node> next = new ArrayList<>();
        int num = unknow;
        int l;
        int r;

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
                    throw new RuntimeException(e);
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
        private StringBuilder cache = new StringBuilder(1 << 20);
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

        public FastOutput println(int c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput flush() {
            try {
                os.append(cache);
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
}

