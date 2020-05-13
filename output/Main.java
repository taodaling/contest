import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
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
            DDestiny solver = new DDestiny();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DDestiny {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int q = in.readInt();
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.readInt() - 1;
            }

            Query[] qs = new Query[q];
            for (int i = 0; i < q; i++) {
                qs[i] = new Query();
                qs[i].l = in.readInt() - 1;
                qs[i].r = in.readInt() - 1;
                qs[i].atLeast = (int) DigitUtils.minimumIntegerGreaterThanDiv(qs[i].r - qs[i].l + 1, in.readInt());
            }

            int[] cnts = new int[n];
            for (int i = 0; i < n; i++) {
                cnts[a[i]]++;
            }
            Handler handler = new Handler(n, cnts);
            MoOnArray.handle(a, qs.clone(), handler, 600);

            for (Query query : qs) {
                if (query.ans == -1) {
                    out.println(-1);
                    continue;
                }
                out.println(query.ans + 1);
            }
        }

    }

    static class MoOnArray {
        public static <Q extends MoOnArray.Query> void handle(int[] data, Q[] queries, MoOnArray.IntHandler<Q> handler, int blockSize) {
            if (queries.length == 0 || data.length == 0) {
                return;
            }

            Arrays.sort(queries, (a, b) -> {
                int ans = a.getL() / blockSize - b.getL() / blockSize;
                if (ans == 0) {
                    ans = a.getR() - b.getR();
                }
                return ans;
            });

            int ll = queries[0].getL();
            int rr = ll - 1;
            for (Q q : queries) {
                int l = q.getL();
                int r = q.getR();
                while (l < ll) {
                    ll--;
                    handler.add(ll, data[ll]);
                }
                while (r > rr) {
                    rr++;
                    handler.add(rr, data[rr]);
                }
                while (l > ll) {
                    handler.remove(ll, data[ll]);
                    ll++;
                }
                while (r < rr) {
                    handler.remove(rr, data[rr]);
                    rr--;
                }
                handler.answer(q);
            }
        }

        public interface Query {
            int getL();

            int getR();

        }

        public interface IntHandler<Q> {
            void add(int i, int x);

            void remove(int i, int x);

            void answer(Q q);

        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static long minimumIntegerGreaterThanDiv(long a, long b) {
            return floorDiv(a, b) + 1;
        }

        public static long floorDiv(long a, long b) {
            return a < 0 ? -ceilDiv(-a, b) : a / b;
        }

        public static int floorDiv(int a, int b) {
            return a < 0 ? -ceilDiv(-a, b) : a / b;
        }

        public static long ceilDiv(long a, long b) {
            if (a < 0) {
                return -floorDiv(-a, b);
            }
            long c = a / b;
            if (c * b < a) {
                return c + 1;
            }
            return c;
        }

        public static int ceilDiv(int a, int b) {
            if (a < 0) {
                return -floorDiv(-a, b);
            }
            int c = a / b;
            if (c * b < a) {
                return c + 1;
            }
            return c;
        }

    }

    static class Query implements MoOnArray.Query {
        int l;
        int r;
        int atLeast;
        int ans;

        public int getL() {
            return l;
        }

        public int getR() {
            return r;
        }

    }

    static class Handler implements MoOnArray.IntHandler<Query> {
        Node[] nodes;
        Node[][] level;
        int[] max;
        int k = 600;

        public Handler(int n, int[] cnts) {
            nodes = new Node[n];
            max = new int[DigitUtils.ceilDiv(n, k)];
            level = new Node[DigitUtils.ceilDiv(n, k)][];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
            }

            for (int i = 0; i < level.length; i++) {
                int l = i * k;
                int r = Math.min(l + k - 1, n - 1);
                int max = 0;
                for (int j = l; j <= r; j++) {
                    max = Math.max(cnts[j], max);
                }
                level[i] = new Node[max + 1];

                for (int j = l; j <= r; j++) {
                    attach(i, nodes[j]);
                }
            }
        }

        public void attach(int i, Node node) {
            node.next = level[i][node.cnt];
            if (level[i][node.cnt] != null) {
                level[i][node.cnt].prev = node;
            }
            level[i][node.cnt] = node;
        }

        public void detach(int i, Node node) {
            if (node.prev == null) {
                level[i][node.cnt] = node.next;
            } else {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            node.prev = node.next = null;
        }

        public void add(int i, int x) {
            int g = x / k;
            detach(g, nodes[x]);
            nodes[x].cnt++;
            attach(g, nodes[x]);
            max[g] = Math.max(max[g], nodes[x].cnt);
        }

        public void remove(int i, int x) {
            int g = x / k;
            detach(g, nodes[x]);
            nodes[x].cnt--;
            attach(g, nodes[x]);
            if (max[g] == nodes[x].cnt + 1 && level[g][nodes[x].cnt + 1] == null) {
                max[g] = nodes[x].cnt;
            }
        }

        public void answer(Query query) {
            int index = -1;
            for (int i = 0; i < max.length; i++) {
                if (max[i] >= query.atLeast) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                query.ans = -1;
                return;
            }

            for (int i = index * k; ; i++) {
                if (nodes[i].cnt >= query.atLeast) {
                    query.ans = i;
                    return;
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

    static class Node {
        Node prev;
        Node next;
        int cnt;

    }
}

