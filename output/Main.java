import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.IntStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 29);
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
            P5CrowdedTravels solver = new P5CrowdedTravels();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P5CrowdedTravels {
        List<IntegerWeightUndirectedEdge>[] g;
        long[] w1;
        long[] w2;
        int[] fa;

        public void dfs(int root, int p) {
            fa[root] = p;
            for (IntegerWeightUndirectedEdge e : g[root]) {
                w2[root] += e.weight;
                if (e.to == p) {
                    w1[root] = e.weight;
                    continue;
                }
                dfs(e.to, root);
            }
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.ri();
            int q = in.ri();
            fa = new int[n];
            w1 = new long[n];
            w2 = new long[n];
            g = Graph.createGraph(n);
            LCTNode[] nodes = new LCTNode[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new LCTNode();
                nodes[i].id = i;
            }
            for (int i = 0; i < n - 1; i++) {
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                int w = in.ri();
                IntegerWeightGraph.addUndirectedEdge(g, a, b, w);
            }
            dfs(0, -1);
            for (int i = 0; i < n; i++) {
                nodes[i].weight = w1[i];
                nodes[i].upgrade = false;
                nodes[i].pushUp();
            }
            for (List<IntegerWeightUndirectedEdge> E : g) {
                for (IntegerWeightUndirectedEdge e : E) {
                    if (e.to < e.rev.to) {
                        LCTNode.join(nodes[e.to], nodes[e.rev.to]);
                    }
                }
            }
            LCTNode.makeRoot(nodes[0]);
            for (int i = 0; i < q; i++) {
                int t = in.ri();
                int u = in.ri() - 1;
                LCTNode.access(nodes[u]);
                if (t == 1) {
                    long newB = w2[u] - w1[u];
                    for (IntegerWeightUndirectedEdge e : g[u]) {
                        if (e.to == fa[u]) {
                            continue;
                        }
                        LCTNode.splay(nodes[e.to]);
                        newB += nodes[e.to].weight;
                    }
                    LCTNode.splay(nodes[u]);
                    nodes[u].upgrade = true;
                    nodes[u].pushUp();
                    nodes[u].modify(newB);

                    LCTNode first = find(nodes[u]);
                    LCTNode.access(first);
                    LCTNode.splay(first);
                    first.modify(-newB);
                } else {
                    LCTNode.splay(nodes[0]);
                    out.println(nodes[0].sumOfWeight - nodes[0].weight);
                }
            }
        }

        LCTNode find(LCTNode root) {
            assert root != LCTNode.NIL;
            root.pushDown();
            if (root.sumOfUpgrade) {
                return LCTNode.NIL;
            }
            if (!root.right.sumOfUpgrade) {
                return find(root.right);
            } else {
                if (!root.upgrade) {
                    return root;
                } else {
                    return find(root.left);
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

        public int ri() {
            return readInt();
        }

        public int readInt() {
            boolean rev = false;

            skipBlank();
            if (next == '+' || next == '-') {
                rev = next == '-';
                next = read();
            }

            int val = 0;
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }

            return rev ? val : -val;
        }

    }

    static class IntegerWeightDirectedEdge extends DirectedEdge {
        public int weight;

        public IntegerWeightDirectedEdge(int to, int weight) {
            super(to);
            this.weight = weight;
        }

    }

    static class Graph {
        public static List[] createGraph(int n) {
            return IntStream.range(0, n).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[i]);
        }

    }

    static class LCTNode {
        public static final LCTNode NIL = new LCTNode();
        public LCTNode left = NIL;
        public LCTNode right = NIL;
        public LCTNode father = NIL;
        public LCTNode treeFather = NIL;
        public boolean reverse;
        public int id;
        int size;
        long sumOfWeight;
        long weight;
        long weightUpd;
        boolean upgrade = true;
        boolean sumOfUpgrade = true;

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.father = NIL;
            NIL.treeFather = NIL;
            NIL.id = -1;
        }

        public static void access(LCTNode x) {
            LCTNode last = NIL;
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

        public static void makeRoot(LCTNode x) {
            access(x);
            splay(x);
            x.reverse();
        }

        public static void join(LCTNode y, LCTNode x) {
            makeRoot(x);
            makeRoot(y);
            x.treeFather = y;
            y.pushUp();
        }

        public static void splay(LCTNode x) {
            if (x == NIL) {
                return;
            }
            LCTNode y, z;
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

        public static void zig(LCTNode x) {
            LCTNode y = x.father;
            LCTNode z = y.father;
            LCTNode b = x.right;

            y.setLeft(b);
            x.setRight(y);
            z.changeChild(y, x);

            y.pushUp();
        }

        public static void zag(LCTNode x) {
            LCTNode y = x.father;
            LCTNode z = y.father;
            LCTNode b = x.left;

            y.setRight(b);
            x.setLeft(y);
            z.changeChild(y, x);

            y.pushUp();
        }

        public String toString() {
            return "" + id;
        }

        public void pushDown() {
            if (this == NIL) {
                return;
            }
            if (reverse) {
                reverse = false;

                LCTNode tmpNode = left;
                left = right;
                right = tmpNode;

                left.reverse();
                right.reverse();
            }

            left.treeFather = treeFather;
            right.treeFather = treeFather;

            if (weightUpd != 0) {
                left.modify(weightUpd);
                right.modify(weightUpd);
                weightUpd = 0;
            }
        }

        public void modify(long x) {
            if (this == NIL) {
                return;
            }
            weightUpd += x;
            weight += x;
            sumOfWeight += x * size;
        }

        public void reverse() {
            reverse = !reverse;
        }

        public void setLeft(LCTNode x) {
            left = x;
            x.father = this;
        }

        public void setRight(LCTNode x) {
            right = x;
            x.father = this;
        }

        public void changeChild(LCTNode y, LCTNode x) {
            if (left == y) {
                setLeft(x);
            } else {
                setRight(x);
            }
        }

        public void pushUp() {
            if (this == NIL) {
                return;
            }
            size = left.size + right.size + 1;
            sumOfWeight = left.sumOfWeight + right.sumOfWeight + weight;
            sumOfUpgrade = left.sumOfUpgrade && right.sumOfUpgrade && upgrade;
        }

    }

    static class IntegerWeightUndirectedEdge extends IntegerWeightDirectedEdge {
        public IntegerWeightUndirectedEdge rev;

        public IntegerWeightUndirectedEdge(int to, int weight) {
            super(to, weight);
        }

    }

    static class IntegerWeightGraph {
        public static IntegerWeightUndirectedEdge addUndirectedEdge(List<IntegerWeightUndirectedEdge>[] g, int s, int t, int w) {
            IntegerWeightUndirectedEdge toT = new IntegerWeightUndirectedEdge(t, w);
            IntegerWeightUndirectedEdge toS = new IntegerWeightUndirectedEdge(s, w);
            toS.rev = toT;
            toT.rev = toS;
            g[s].add(toT);
            g[t].add(toS);
            return toT;
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 32 << 10;
        private OutputStream writer;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);
        private static Field stringBuilderValueField;
        private char[] charBuf = new char[THRESHOLD * 2];
        private byte[] byteBuf = new byte[THRESHOLD * 2];

        static {
            try {
                stringBuilderValueField = StringBuilder.class.getSuperclass().getDeclaredField("value");
                stringBuilderValueField.setAccessible(true);
            } catch (Exception e) {
                stringBuilderValueField = null;
            }
            stringBuilderValueField = null;
        }

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        private void afterWrite() {
            if (cache.length() < THRESHOLD) {
                return;
            }
            flush();
        }

        public FastOutput(OutputStream writer) {
            this.writer = writer;
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(long c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(long c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append('\n');
        }

        public FastOutput flush() {
            try {
                if (stringBuilderValueField != null) {
                    try {
                        byte[] value = (byte[]) stringBuilderValueField.get(cache);
                        writer.write(value, 0, cache.length());
                    } catch (Exception e) {
                        stringBuilderValueField = null;
                    }
                }
                if (stringBuilderValueField == null) {
                    int n = cache.length();
                    if (n > byteBuf.length) {
                        //slow
                        writer.write(cache.toString().getBytes(StandardCharsets.UTF_8));
//                writer.append(cache);
                    } else {
                        cache.getChars(0, n, charBuf, 0);
                        for (int i = 0; i < n; i++) {
                            byteBuf[i] = (byte) charBuf[i];
                        }
                        writer.write(byteBuf, 0, n);
                    }
                }
                writer.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                writer.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }

    static class DirectedEdge {
        public int to;

        public DirectedEdge(int to) {
            this.to = to;
        }

        public String toString() {
            return "->" + to;
        }

    }
}

