import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.AbstractCollection;
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
            UOJ0003 solver = new UOJ0003();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class UOJ0003 {
        Debug debug = new Debug(false);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int[][] edges = new int[m][4];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < 4; j++) {
                    edges[i][j] = in.readInt();
                }
            }

            int src = 1;
            int dst = n;

            Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
            OfflineConnectionChecker occ = new OfflineConnectionChecker(n + 1);
            PriorityQueue<Integer> pq = new PriorityQueue<>(m, (a, b) -> -Integer.compare(edges[a][3], edges[b][3]));
            debug.debug("edges", edges);
            int ans = (int) 1e8;
            int limit = 100000;
            for (int i = 0; i < m; i++) {
                int l = i;
                int r = i;
                while (r < m - 1 && edges[r + 1][2] == edges[l][2]) {
                    r++;
                }
                i = r;
                for (int j = l; j <= r; j++) {
                    occ.addEdge(edges[j][0], edges[j][1], time(edges[j][3] - 1));
                    pq.add(j);
                }

                while (!pq.isEmpty()) {
                    occ.elapse(time(edges[pq.peek()][3]));
                    if (!occ.check(src, dst)) {
                        break;
                    }
                    int index = pq.remove();
                    ans = Math.min(ans, edges[index][3] + edges[l][2]);
                }
            }

            if (ans == (int) 1e8) {
                out.println(-1);
                return;
            }
            out.println(ans);
        }

        public int time(int x) {
            return 100000 - x;
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

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, Object x) {
            return debug(name, x, empty);
        }

        public Debug debug(String name, Object x, int... indexes) {
            if (offline) {
                if (x == null || !x.getClass().isArray()) {
                    out.append(name);
                    for (int i : indexes) {
                        out.printf("[%d]", i);
                    }
                    out.append("=").append("" + x);
                    out.println();
                } else {
                    indexes = Arrays.copyOf(indexes, indexes.length + 1);
                    if (x instanceof byte[]) {
                        byte[] arr = (byte[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof short[]) {
                        short[] arr = (short[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof boolean[]) {
                        boolean[] arr = (boolean[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof char[]) {
                        char[] arr = (char[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof int[]) {
                        int[] arr = (int[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof float[]) {
                        float[] arr = (float[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof double[]) {
                        double[] arr = (double[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof long[]) {
                        long[] arr = (long[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else {
                        Object[] arr = (Object[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    }
                }
            }
            return this;
        }

    }

    static class OfflineConnectionChecker {
        private OfflineConnectionChecker.LCTNode[] nodes;
        private int time = -1;

        public OfflineConnectionChecker(int n) {
            nodes = new OfflineConnectionChecker.LCTNode[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new OfflineConnectionChecker.LCTNode();
                nodes[i].id = i;
                nodes[i].dieTime = Integer.MAX_VALUE;
                nodes[i].pushUp();
            }
            for (int i = 1; i < n; i++) {
                OfflineConnectionChecker.LCTNode node = new OfflineConnectionChecker.LCTNode();
                node.dieTime = time;
                node.a = nodes[i - 1];
                node.b = nodes[i];
                node.pushUp();
                OfflineConnectionChecker.LCTNode.join(node.a, node);
                OfflineConnectionChecker.LCTNode.join(node.b, node);
            }
        }

        public void addEdge(int aId, int bId, int dieTime) {
            OfflineConnectionChecker.LCTNode a = nodes[aId];
            OfflineConnectionChecker.LCTNode b = nodes[bId];
            OfflineConnectionChecker.LCTNode.findRoute(a, b);
            OfflineConnectionChecker.LCTNode.splay(a);
            if (a.eldest.dieTime >= dieTime) {
                return;
            }
            OfflineConnectionChecker.LCTNode eldest = a.eldest;
            OfflineConnectionChecker.LCTNode.splay(eldest);
            OfflineConnectionChecker.LCTNode.cut(eldest.a, eldest);
            OfflineConnectionChecker.LCTNode.cut(eldest.b, eldest);

            OfflineConnectionChecker.LCTNode node = new OfflineConnectionChecker.LCTNode();
            node.dieTime = dieTime;
            node.a = a;
            node.b = b;
            node.pushUp();
            OfflineConnectionChecker.LCTNode.join(node.a, node);
            OfflineConnectionChecker.LCTNode.join(node.b, node);
        }

        public boolean check(int aId, int bId) {
            OfflineConnectionChecker.LCTNode a = nodes[aId];
            OfflineConnectionChecker.LCTNode b = nodes[bId];
            OfflineConnectionChecker.LCTNode.findRoute(a, b);
            OfflineConnectionChecker.LCTNode.splay(b);
            return b.eldest.dieTime > time;
        }

        public void elapse(int t) {
            time = t;
        }

        private static class LCTNode {
            public static final OfflineConnectionChecker.LCTNode NIL = new OfflineConnectionChecker.LCTNode();
            OfflineConnectionChecker.LCTNode left = NIL;
            OfflineConnectionChecker.LCTNode right = NIL;
            OfflineConnectionChecker.LCTNode father = NIL;
            OfflineConnectionChecker.LCTNode treeFather = NIL;
            boolean reverse;
            int id;
            OfflineConnectionChecker.LCTNode a;
            OfflineConnectionChecker.LCTNode b;
            OfflineConnectionChecker.LCTNode eldest;
            int dieTime;

            static {
                NIL.left = NIL;
                NIL.right = NIL;
                NIL.father = NIL;
                NIL.treeFather = NIL;
                NIL.dieTime = Integer.MAX_VALUE;
                NIL.eldest = NIL;
            }

            public static OfflineConnectionChecker.LCTNode elder(OfflineConnectionChecker.LCTNode a, OfflineConnectionChecker.LCTNode b) {
                return a.dieTime < b.dieTime ? a : b;
            }

            public static void access(OfflineConnectionChecker.LCTNode x) {
                OfflineConnectionChecker.LCTNode last = NIL;
                while (x != NIL) {
                    splay(x);
                    x.right.father = NIL;
                    x.right.treeFather = x;
                    x.setRight(last);
                    x.pushUp();

                    last = x;
                    x = x.treeFather;
                }
            }

            public static void makeRoot(OfflineConnectionChecker.LCTNode x) {
                access(x);
                splay(x);
                x.reverse();
            }

            public static void cut(OfflineConnectionChecker.LCTNode y, OfflineConnectionChecker.LCTNode x) {
                makeRoot(y);
                access(x);
                splay(y);
                y.right.treeFather = NIL;
                y.right.father = NIL;
                y.setRight(NIL);
                y.pushUp();
            }

            public static void join(OfflineConnectionChecker.LCTNode y, OfflineConnectionChecker.LCTNode x) {
                makeRoot(x);
                x.treeFather = y;
            }

            public static void findRoute(OfflineConnectionChecker.LCTNode x, OfflineConnectionChecker.LCTNode y) {
                makeRoot(y);
                access(x);
            }

            public static void splay(OfflineConnectionChecker.LCTNode x) {
                if (x == NIL) {
                    return;
                }
                OfflineConnectionChecker.LCTNode y, z;
                while ((y = x.father) != NIL) {
                    if ((z = y.father) == NIL) {
                        y.pushDown();
                        x.pushDown();
                        if (x == y.left) {
                            zig(x);
                        } else {
                            zag(x);
                        }
                    } else {
                        z.pushDown();
                        y.pushDown();
                        x.pushDown();
                        if (x == y.left) {
                            if (y == z.left) {
                                zig(y);
                                zig(x);
                            } else {
                                zig(x);
                                zag(x);
                            }
                        } else {
                            if (y == z.left) {
                                zag(x);
                                zig(x);
                            } else {
                                zag(y);
                                zag(x);
                            }
                        }
                    }
                }

                x.pushDown();
                x.pushUp();
            }

            public static void zig(OfflineConnectionChecker.LCTNode x) {
                OfflineConnectionChecker.LCTNode y = x.father;
                OfflineConnectionChecker.LCTNode z = y.father;
                OfflineConnectionChecker.LCTNode b = x.right;

                y.setLeft(b);
                x.setRight(y);
                z.changeChild(y, x);

                y.pushUp();
            }

            public static void zag(OfflineConnectionChecker.LCTNode x) {
                OfflineConnectionChecker.LCTNode y = x.father;
                OfflineConnectionChecker.LCTNode z = y.father;
                OfflineConnectionChecker.LCTNode b = x.left;

                y.setRight(b);
                x.setLeft(y);
                z.changeChild(y, x);

                y.pushUp();
            }

            public String toString() {
                return "" + id;
            }

            public void pushDown() {
                if (reverse) {
                    reverse = false;

                    OfflineConnectionChecker.LCTNode tmpNode = left;
                    left = right;
                    right = tmpNode;

                    left.reverse();
                    right.reverse();
                }

                left.treeFather = treeFather;
                right.treeFather = treeFather;
            }

            public void reverse() {
                reverse = !reverse;
            }

            public void setLeft(OfflineConnectionChecker.LCTNode x) {
                left = x;
                x.father = this;
            }

            public void setRight(OfflineConnectionChecker.LCTNode x) {
                right = x;
                x.father = this;
            }

            public void changeChild(OfflineConnectionChecker.LCTNode y, OfflineConnectionChecker.LCTNode x) {
                if (left == y) {
                    setLeft(x);
                } else {
                    setRight(x);
                }
            }

            public void pushUp() {
                eldest = elder(this, left.eldest);
                eldest = elder(eldest, right.eldest);
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
}

