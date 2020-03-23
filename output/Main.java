import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.util.BitSet;
import java.util.ArrayDeque;
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
            GLettersAndQuestionMarks solver = new GLettersAndQuestionMarks();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class GLettersAndQuestionMarks {
        Debug debug = new Debug(true);
        char[] buf = new char[1000000];
        int charset = 14;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int k = in.readInt();
            ACAutomaton ac = new ACAutomaton('a', 'n');
            for (int i = 0; i < k; i++) {
                int n = in.readString(buf, 0);
                ac.beginBuilding();
                for (int j = 0; j < n; j++) {
                    ac.build(buf[j]);
                }
                ac.getBuildLast().weight += in.readInt();
            }
            ac.endBuilding();
            ACAutomaton.Node[] nodes = ac.getAllNodes().toArray(new ACAutomaton.Node[0]);
            int m = nodes.length;
            int n = in.readString(buf, 0);

            int mask = (1 << charset) - 1;
            long[][] cur = new long[m][1 + mask];
            long[][] last = new long[m][1 + mask];
            long inf = (long) 1e18;
            SequenceUtils.deepFill(last, -inf);
            last[0][0] = 0;

            int[] transfer = new int[m];
            long[] collect = new long[m];
            BitSet bs = new BitSet(1 << charset);
            bs.set(0);
            for (int i = 0; i < n; i++) {
                //debug.debug("last", last);
                int l = i;
                int r = nextQuest(buf, i, n);
                debug.debug("l", l);
                debug.debug("r", r);
                i = r;


                for (int j = 0; j < m; j++) {
                    transfer[j] = j;
                    collect[j] = 0;
                }
                for (int j = l; j < r; j++) {
                    int offset = buf[j] - 'a';
                    for (int t = 0; t < m; t++) {
                        transfer[t] = nodes[transfer[t]].next[offset].id;
                        collect[t] += nodes[transfer[t]].weight;
                    }
                }

                SequenceUtils.deepFill(cur, -inf);


                if (r != n) {
                    BitSet next = new BitSet(1 << charset);
                    for (int t = bs.nextSetBit(0); t != -1; t = bs.nextSetBit(t + 1)) {
                        for (int z = 0; z < charset; z++) {
                            if (Bits.bitAt(t, z) == 1) {
                                continue;
                            }
                            for (int j = 0; j < m; j++) {
                                int nid = nodes[transfer[j]].next[z].id;
                                int bit = Bits.setBit(t, z, true);
                                cur[nid][bit] = Math.max(cur[nid][bit], last[j][t] + collect[j] + nodes[nid].weight);
                                next.set(bit);
                            }
                        }
                    }

                    bs = next;
                } else {
                    for (int t = bs.nextSetBit(0); t != -1; t = bs.nextSetBit(t + 1)) {
                        for (int j = 0; j < m; j++) {
                            cur[transfer[j]][t] = Math.max(cur[transfer[j]][t], last[j][t] + collect[j]);
                        }
                    }
                }
                long[][] tmp = cur;
                cur = last;
                last = tmp;

                continue;
            }

            long max = -inf;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j <= mask; j++) {
                    max = Math.max(max, last[i][j]);
                }
            }

            out.println(max);
        }

        public int nextQuest(char[] buf, int i, int n) {
            for (int j = i; j < n; j++) {
                if (buf[j] == '?') {
                    return i;
                }
            }
            return n;
        }

    }

    static class ACAutomaton {
        private final int MIN_CHARACTER;
        private final int MAX_CHARACTER;
        private final int RANGE;
        private ACAutomaton.Node root;
        private ACAutomaton.Node buildLast;
        private List<ACAutomaton.Node> allNodes = new ArrayList();

        public ACAutomaton.Node getBuildLast() {
            return buildLast;
        }

        public List<ACAutomaton.Node> getAllNodes() {
            return allNodes;
        }

        private ACAutomaton.Node addNode() {
            ACAutomaton.Node node = new ACAutomaton.Node(RANGE);
            node.id = allNodes.size();
            allNodes.add(node);
            return node;
        }

        public ACAutomaton(int minCharacter, int maxCharacter) {
            MIN_CHARACTER = minCharacter;
            MAX_CHARACTER = maxCharacter;
            RANGE = maxCharacter - minCharacter + 1;
            root = addNode();
        }

        public void beginBuilding() {
            buildLast = root;
        }

        public void endBuilding() {
            Deque<ACAutomaton.Node> deque = new ArrayDeque(allNodes.size());
            for (int i = 0; i < RANGE; i++) {
                if (root.next[i] != null) {
                    deque.addLast(root.next[i]);
                }
            }

            while (!deque.isEmpty()) {
                ACAutomaton.Node head = deque.removeFirst();
                ACAutomaton.Node fail = visit(head.father.fail, head.index);
                if (fail == null) {
                    head.fail = root;
                } else {
                    head.fail = fail.next[head.index];
                }
                head.weight += head.fail.weight;
                head.preSum = head.cnt + head.fail.preSum;
                for (int i = 0; i < RANGE; i++) {
                    if (head.next[i] != null) {
                        deque.addLast(head.next[i]);
                    }
                }
            }

            for (int i = 0; i < RANGE; i++) {
                if (root.next[i] != null) {
                    deque.addLast(root.next[i]);
                } else {
                    root.next[i] = root;
                }
            }
            while (!deque.isEmpty()) {
                ACAutomaton.Node head = deque.removeFirst();
                for (int i = 0; i < RANGE; i++) {
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
            int index = c - MIN_CHARACTER;
            if (buildLast.next[index] == null) {
                ACAutomaton.Node node = addNode();
                node.father = buildLast;
                node.index = index;
                buildLast.next[index] = node;
            }
            buildLast = buildLast.next[index];
        }

        public static class Node {
            public ACAutomaton.Node[] next;
            ACAutomaton.Node fail;
            ACAutomaton.Node father;
            int index;
            int id;
            int cnt;
            int preSum;
            long weight;

            public Node(int range) {
                next = new ACAutomaton.Node[range];
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(long c) {
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

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, int x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
        }

    }

    static class SequenceUtils {
        public static void deepFill(Object array, long val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof long[]) {
                long[] longArray = (long[]) array;
                Arrays.fill(longArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
                }
            }
        }

    }

    static class Bits {
        private Bits() {
        }

        public static int bitAt(int x, int i) {
            return (x >>> i) & 1;
        }

        public static int setBit(int x, int i, boolean v) {
            if (v) {
                x |= 1 << i;
            } else {
                x &= ~(1 << i);
            }
            return x;
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

        public int readString(char[] data, int offset) {
            skipBlank();

            int originalOffset = offset;
            while (next > 32) {
                data[offset++] = (char) next;
                next = read();
            }

            return offset - originalOffset;
        }

    }
}

