import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.TreeSet;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            BZOJ1150 solver = new BZOJ1150();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class BZOJ1150 {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int k = in.readInt();
            int[] xs = new int[n];
            for (int i = 0; i < n; i++) {
                xs[i] = in.readInt();
            }
            long[] ws = new long[n];
            for (int i = 0; i < n - 1; i++) {
                ws[i] = xs[i] - xs[i + 1];
            }
            ws[n - 1] = (long) -1e18;
            PlantTreeProblem problem = new PlantTreeProblem(ws, k);
            out.println(-problem.getAnswer());
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

        public FastOutput println(long c) {
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

        public String toString() {
            return cache.toString();
        }

    }

    static class IntegerBIT {
        private int[] data;
        private int n;

        public IntegerBIT(int n) {
            this.n = n;
            data = new int[n + 1];
        }

        public int query(int i) {
            int sum = 0;
            for (; i > 0; i -= i & -i) {
                sum += data[i];
            }
            return sum;
        }

        public void update(int i, int mod) {
            if (i <= 0) {
                return;
            }
            for (; i <= n; i += i & -i) {
                data[i] += mod;
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                builder.append(query(i) - query(i - 1)).append(' ');
            }
            return builder.toString();
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

    static class PlantTreeProblem {
        IntegerBIT bit;
        long ans;
        int n;

        public PlantTreeProblem(long[] weights, int m) {
            n = weights.length;
            bit = new IntegerBIT(n + 1);
            TreeSet<PlantTreeProblem.Node> set = new TreeSet<>(PlantTreeProblem.Node.sortByW);
            PlantTreeProblem.Node[] nodes = new PlantTreeProblem.Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new PlantTreeProblem.Node();
                nodes[i].w = weights[i];
                nodes[i].l = nodes[i].r = i;
            }
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    nodes[i].prev = nodes[i - 1];
                } else {
                    nodes[i].prev = nodes[n - 1];
                }
                if (i + 1 < n) {
                    nodes[i].next = nodes[i + 1];
                } else {
                    nodes[i].next = nodes[0];
                }
            }
            set.addAll(Arrays.asList(nodes));

            for (int i = 0; i < m; i++) {
                PlantTreeProblem.Node last = set.pollLast();
                ans += last.w;
                if (last.l <= last.r) {
                    bit.update(last.l + 1, 1);
                    bit.update(last.r + 2, 1);
                } else {
                    bit.update(1, 1);
                    bit.update(last.r + 2, 1);
                    bit.update(last.l + 1, 1);
                    bit.update(n, 1);
                }

                if (last.next == last.prev) {
                    continue;
                }
                PlantTreeProblem.Node prev = last.prev;
                set.remove(last.next);
                set.remove(last.prev);
                prev.r = last.next.r;
                prev.w += last.next.w - last.w;
                prev.next = last.next.next;
                prev.next.prev = prev;
                set.add(prev);
            }
        }

        public long getAnswer() {
            return ans;
        }

        public boolean chooseOrNot(int i) {
            return bit.query(i + 1) % 2 == 1;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < n; i++) {
                builder.append(chooseOrNot(i)).append(',');
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }

        private static class Node {
            PlantTreeProblem.Node prev;
            PlantTreeProblem.Node next;
            long w;
            int l;
            int r;
            static Comparator<PlantTreeProblem.Node> sortByW = (a, b) -> a.w == b.w ? a.l - b.l : Long.compare(a.w, b.w);

            public String toString() {
                return String.format("[%d, %d] => %d", l, r, w);
            }

        }

    }
}

