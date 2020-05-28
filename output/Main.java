import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.util.Deque;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
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
            P5357AC solver = new P5357AC();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P5357AC {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            char[] patterns = new char[(int) 2e6];
            ACAutomaton.Node[] nodes = new ACAutomaton.Node[n];
            ACAutomaton ac = new ACAutomaton('a', 'z');
            for (int i = 0; i < n; i++) {
                int m = in.readString(patterns, 0);
                ac.beginBuilding();
                for (int j = 0; j < m; j++) {
                    ac.build(patterns[j]);
                }
                nodes[i] = ac.getBuildLast();
            }
            ac.endBuilding();

            int m = in.readString(patterns, 0);
            ac.beginMatching();
            for (int i = 0; i < m; i++) {
                ac.match(patterns[i]);
                ac.getMatchLast().increaseCnt();
            }

            ac.pushUp();
            for (ACAutomaton.Node node : nodes) {
                out.println(node.cnt);
            }
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
        private List<ACAutomaton.Node> treeOrder;

        public ACAutomaton.Node getBuildLast() {
            return buildLast;
        }

        public ACAutomaton.Node getMatchLast() {
            return matchLast;
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
            Deque<ACAutomaton.Node> deque = new ArrayDeque(allNodes.size());
            treeOrder = new ArrayList<>(allNodes.size());
            treeOrder.add(root);
            for (int i = 0; i < range; i++) {
                if (root.next[i] != null) {
                    deque.addLast(root.next[i]);
                }
            }

            while (!deque.isEmpty()) {
                ACAutomaton.Node head = deque.removeFirst();
                treeOrder.add(head);
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

        public void pushUp() {
            for (int i = treeOrder.size() - 1; i >= 1; i--) {
                ACAutomaton.Node node = treeOrder.get(i);
                node.fail.cnt += node.cnt;
            }
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
            ACAutomaton.Node fail;
            ACAutomaton.Node father;
            int index;
            int id;
            int cnt;
            int preSum;

            public void increaseCnt() {
                cnt++;
            }

            public Node(int range) {
                next = new ACAutomaton.Node[range];
            }

            public String toString() {
                return father == null ? "" : (father.toString() + (char) ('a' + index));
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

