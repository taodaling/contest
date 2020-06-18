import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Deque;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
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
            GraveyardInDeyja solver = new GraveyardInDeyja();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class GraveyardInDeyja {
        String charset;

        {
            StringBuilder builder = new StringBuilder();
            for (char i = 'a'; i <= 'z'; i++) {
                builder.append(i);
            }
            for (char i = 'A'; i <= 'Z'; i++) {
                builder.append(i);
            }
            charset = builder.toString();
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            char[] s = in.readString().toCharArray();
            char[] t = in.readString().toCharArray();
            int m = charset.length();
            ACAutomaton ac = new ACAutomaton(0, m - 1);
            ac.beginBuilding();
            for (char c : t) {
                ac.build((char) charset.indexOf(c));
            }
            ac.getBuildLast().increaseCnt();
            ac.endBuilding();
            ACAutomaton.Node[] nodes = ac.getAllNodes().toArray(new ACAutomaton.Node[0]);
            int[][] dp = new int[s.length + 1][nodes.length];
            int inf = (int) 1e8;
            SequenceUtils.deepFill(dp, -inf);
            dp[0][0] = 0;
            for (int i = 0; i < s.length; i++) {
                int l = charset.indexOf(s[i]);
                int r = l;
                if (l == -1) {
                    l = 0;
                    r = charset.length() - 1;
                }
                for (int k = l; k <= r; k++) {
                    for (int j = 0; j < nodes.length; j++) {
                        ACAutomaton.Node next = nodes[j].next[k];
                        dp[i + 1][next.id] = Math.max(dp[i + 1][next.id], dp[i][j] + next.getCnt());
                    }
                }
            }

            int ans = 0;
            for (int i = 0; i < nodes.length; i++) {
                ans = Math.max(ans, dp[s.length][i]);
            }

            out.println(ans);
        }

    }

    static class ACAutomaton {
        private final int minCharacter;
        private final int maxCharacter;
        private final int range;
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
            for (int i = 0; i < range; i++) {
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

        public static class Node {
            public ACAutomaton.Node[] next;
            ACAutomaton.Node fail;
            ACAutomaton.Node father;
            int index;
            public int id;
            int cnt;
            int preSum;

            public int getCnt() {
                return cnt;
            }

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

    static class SequenceUtils {
        public static void deepFill(Object array, int val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof int[]) {
                int[] intArray = (int[]) array;
                Arrays.fill(intArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
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
}

