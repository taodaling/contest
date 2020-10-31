import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayList;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.AbstractMap;
import java.util.TreeMap;
import java.io.Closeable;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Comparator;
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
            GDeathDBMS solver = new GDeathDBMS();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class GDeathDBMS {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            ACAutomaton ac = new ACAutomaton('a', 'z');

            int[] values = new int[n];
            MultiSet<Integer>[] sets = new MultiSet[n];
            Map<String, MultiSet<Integer>> map = new HashMap<>();
            int[] end = new int[n];
            for (int i = 0; i < n; i++) {
                String s = in.readString();
                ac.beginBuilding();
                for (int j = 0; j < s.length(); j++) {
                    ac.build(s.charAt(j));
                }
                sets[i] = map.computeIfAbsent(s, x -> new MultiSet<>());
                sets[i].add(0);
                end[i] = ac.getBuildLast().getId();
            }

            ac.endBuilding();
            List<ACAutomaton.Node> list = ac.getAllNodes();
            int k = list.size();
            LCTNode[] nodes = new LCTNode[k];
            for (int i = 0; i < k; i++) {
                nodes[i] = new LCTNode();
            }
            for (int id : end) {
                nodes[id].val = 0;
                nodes[id].pushUp();
            }
            for (ACAutomaton.Node node : list) {
                if (node.fail != null) {
                    LCTNode.join(nodes[node.id], nodes[node.fail.id]);
                }
            }
            LCTNode.makeRoot(nodes[0]);
            for (int i = 0; i < m; i++) {
                int t = in.readInt();
                if (t == 1) {
                    int which = in.readInt() - 1;
                    int x = in.readInt();
                    sets[which].remove(values[which]);
                    values[which] = x;
                    sets[which].add(x);
                    LCTNode.splay(nodes[end[which]]);
                    nodes[end[which]].val = sets[which].last();
                    nodes[end[which]].pushUp();
                } else {
                    ac.beginMatching();
                    String q = in.readString();
                    int ans = -1;
                    for (char c : q.toCharArray()) {
                        ac.match(c);
                        LCTNode cur = nodes[ac.getMatchLast().id];
                        LCTNode.access(cur);
                        LCTNode.splay(cur);
                        ans = Math.max(ans, cur.max);
                    }
                    out.println(ans);
                }
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

    static class ACAutomaton {
        private final int minCharacter;
        private final int maxCharacter;
        private final int range;
        private ACAutomaton.Node root;
        private ACAutomaton.Node buildLast;
        private ACAutomaton.Node matchLast;
        private List<ACAutomaton.Node> allNodes = new ArrayList();

        public ACAutomaton.Node getBuildLast() {
            return buildLast;
        }

        public ACAutomaton.Node getMatchLast() {
            return matchLast;
        }

        public List<ACAutomaton.Node> getAllNodes() {
            return allNodes;
        }

        private ACAutomaton.Node addNode() {
            ACAutomaton.Node node = new ACAutomaton.Node(range);
            node.id = allNodes.size();
            allNodes.add(node);
            return node;
        }

        public ACAutomaton(int minCharacter, int maxCharacter) {
            this.minCharacter = minCharacter;
            this.maxCharacter = maxCharacter;
            range = maxCharacter - minCharacter + 1;
            root = addNode();
        }

        public void beginBuilding() {
            buildLast = root;
        }

        public void endBuilding() {
            //treeOrder = new ArrayList<>(allNodes.size());
            //treeOrder.add(root);
            Deque<ACAutomaton.Node> deque = new ArrayDeque(allNodes.size());
            for (int i = 0; i < range; i++) {
                if (root.next[i] != null) {
                    deque.addLast(root.next[i]);
                }
            }

            while (!deque.isEmpty()) {
                ACAutomaton.Node head = deque.removeFirst();
                //treeOrder.add(head);
                ACAutomaton.Node fail = visit(head.father.fail, head.index);
                if (fail == null) {
                    head.fail = root;
                } else {
                    head.fail = fail.next[head.index];
                }
                head.preSum = head.cnt + head.fail.preSum;
                for (int i = 0; i < range; i++) {
                    if (head.next[i] != null) {
                        deque.addLast(head.next[i]);
                    }
                }
            }

            for (int i = 0; i < range; i++) {
                if (root.next[i] != null) {
                    deque.addLast(root.next[i]);
                } else {
                    root.next[i] = root;
                }
            }
            while (!deque.isEmpty()) {
                ACAutomaton.Node head = deque.removeFirst();
                for (int i = 0; i < range; i++) {
                    if (head.next[i] != null) {
                        deque.addLast(head.next[i]);
                    } else {
                        head.next[i] = head.fail.next[i];
                    }
                }
            }
        }

        public ACAutomaton.Node visit(ACAutomaton.Node trace, int index) {
            while (trace != null && trace.next[index] == null) {
                trace = trace.fail;
            }
            return trace;
        }

        public void build(char c) {
            int index = c - minCharacter;
            if (buildLast.next[index] == null) {
                ACAutomaton.Node node = addNode();
                node.father = buildLast;
                node.index = index;
                buildLast.next[index] = node;
            }
            buildLast = buildLast.next[index];
        }

        public void beginMatching() {
            matchLast = root;
        }

        public void match(char c) {
            int index = c - minCharacter;
            matchLast = matchLast.next[index];
        }

        public static class Node {
            public ACAutomaton.Node[] next;
            public ACAutomaton.Node fail;
            public ACAutomaton.Node father;
            int index;
            public int id;
            int cnt;
            int preSum;

            public int getId() {
                return id;
            }

            public Node(int range) {
                next = new ACAutomaton.Node[range];
            }

            public String toString() {
                return father == null ? "" : (father.toString() + (char) ('a' + index));
            }

        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 1 << 13;
        private final Writer os;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);

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

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append(System.lineSeparator());
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

    static class LCTNode {
        public static final LCTNode NIL = new LCTNode();
        LCTNode left = NIL;
        LCTNode right = NIL;
        LCTNode father = NIL;
        LCTNode treeFather = NIL;
        boolean reverse;
        int id;
        int val = -1;
        int max = -1;

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.father = NIL;
            NIL.treeFather = NIL;
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
            x.treeFather = y;
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
            max = Math.max(left.max, right.max);
            max = Math.max(max, val);
        }

    }

    static class MultiSet<T> {
        private TreeMap<T, Integer> map;
        private int size;

        public MultiSet(Comparator<T> comp) {
            this.map = new TreeMap<>(comp);
        }

        public MultiSet() {
            this.map = new TreeMap<>();
        }

        public T last() {
            return map.lastKey();
        }

        public void add(T key) {
            size++;
            update(key, map.getOrDefault(key, 0) + 1);
        }

        public void remove(T key) {
            size--;
            update(key, map.getOrDefault(key, 0) - 1);
        }

        public void update(T key, int cnt) {
            if (cnt == 0) {
                map.remove(key);
            } else if (cnt > 0) {
                map.put(key, cnt);
            } else {
                size++;
            }
        }

        public String toString() {
            return map.toString();
        }

    }
}

