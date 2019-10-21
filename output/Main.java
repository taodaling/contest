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
 * Built using CHelper plug-in
 * Actual solution is at the top
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
            TaskD solver = new TaskD();
            int testCount = Integer.parseInt(in.next());
            for (int i = 1; i <= testCount; i++)
                solver.solve(i, in, out);
            out.close();
        }
    }

    static class TaskD {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();

            Node[] people = new Node[n + 1];
            Node[] cats = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                people[i] = new Node();
                people[i].id = i;
            }
            for (int i = 1; i <= n; i++) {
                cats[i] = new Node();
                cats[i].id = i;
            }
            for (int i = 0; i < m; i++) {
                Node a = people[in.readInt()];
                Node b = cats[in.readInt()];
                a.next.add(b);
                b.next.add(a);
            }

            List<List<Node>[]> groups = new ArrayList<>(n);
            for (int i = 1; i <= n; i++) {
                if (people[i].visited) {
                    continue;
                }
                List<Node>[] lists = new List[]{new ArrayList(), new ArrayList()};
                dfs(people[i], 0, lists);
                groups.add(lists);
            }

            if (groups.size() == 1) {
                out.println("No");
                return;
            }

            out.println("Yes");
            List<Node> selectedPeople = new ArrayList<>(n);
            List<Node> selectedCats = new ArrayList<>(n);
            selectedPeople.addAll(groups.get(0)[0]);
            for (int i = 1, until = groups.size(); i < until; i++) {
                selectedCats.addAll(groups.get(i)[1]);
            }

            out.append(selectedPeople.size()).append(' ').append(selectedCats.size())
                    .append('\n');
            for (Node p : selectedPeople) {
                out.append(p.id).append(' ');
            }
            out.println();
            for (Node c : selectedCats) {
                out.println(c.id).append(' ');
            }
            out.println();
        }

        public void dfs(Node root, int depth, List<Node>[] lists) {
            if (root.visited) {
                return;
            }
            root.visited = true;
            lists[depth % 2].add(root);
            for (Node node : root.next) {
                dfs(node, depth + 1, lists);
            }
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

    static class Node {
        List<Node> next = new ArrayList<>(2);
        boolean visited;
        int id;

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

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
            cache.append(c).append('\n');
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

