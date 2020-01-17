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
            LongLongMessage solver = new LongLongMessage();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class LongLongMessage {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int[] a = new int[100000 + 1];
            int[] b = new int[100000 + 1];
            int end = 'z' + 1;
            int aLen = in.readString(a, 0);
            int bLen = in.readString(b, 0);

            SuffixTree st = new SuffixTree(aLen + bLen + 1, 'a', end);
            for (int i = 0; i < aLen; i++) {
                st.append(a[i]);
            }
            for (int i = 0; i < bLen; i++) {
                st.append(b[i]);
            }
            st.append(end);

            int ans = st.lcs(aLen - 1);
            out.println(ans);
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

        public int readString(int[] data, int offset) {
            skipBlank();

            int originalOffset = offset;
            while (next > 32) {
                data[offset++] = (char) next;
                next = read();
            }

            return offset - originalOffset;
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

        public FastOutput println(int c) {
            cache.append(c);
            println();
            return this;
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

    static class DigitUtils {
        private static long mask32 = (1L << 32) - 1;

        private DigitUtils() {
        }

        public static long asLong(int high, int low) {
            return ((((long) high)) << 32) | (((long) low) & mask32);
        }

        public static int highBit(long x) {
            return (int) (x >> 32);
        }

        public static int lowBit(long x) {
            return (int) x;
        }

    }

    static class SuffixTree {
        int minCharacter;
        int maxCharacter;
        int alphabet;
        Node root;
        int[] data;
        int len;
        Node activeNode;
        Node lastJump;
        int l = 0;
        int sequence = 0;

        public SuffixTree(int len, int minCharacter, int maxCharacter) {
            data = new int[len];
            alphabet = maxCharacter - minCharacter + 1;
            this.minCharacter = minCharacter;
            this.maxCharacter = maxCharacter;
            root = new Node(null, alphabet);
            root.l = 0;
            root.r = -1;
            root.suffixLink = root;
            activeNode = root;
        }

        public void append(int x) {
            x -= minCharacter;
            data[len++] = x;
            lastJump = null;
            insert();
        }

        private void jump() {
            activeNode = activeNode.suffixLink;
            if (activeNode == null) {
                activeNode = root;
            }
            insert();
        }

        private Node newNode(Node parent) {
            Node node = new Node(parent, alphabet);
            if (lastJump != null) {
                lastJump.suffixLink = node;
            }
            return node;
        }

        private void insert() {
            if (l == len) {
                return;
            }
            Node node = activeNode.next[data[l]];
            if (node == null) {
                node = new Node(activeNode, alphabet);
                node.l = l;
                node.r = data.length;
                node.suffixStartIndex = l;
                activeNode.next[data[l]] = node;
                l++;
                jump();
                return;
            }
            if (data[node.l + len - 1 - l] == data[len - 1]) {
                if (node.r - node.l + 1 == len - l) {
                    activeNode = node;
                    l = len;
                }
                return;
            }

            Node split = newNode(activeNode);
            split.l = node.l;
            split.r = node.l + len - l - 2;
            node.l = split.r + 1;
            activeNode.next[data[l]] = split;
            split.next[data[node.l]] = node;
            node.setParent(split);

            Node inserted = new Node(split, alphabet);
            inserted.l = l + split.r - split.l + 1;
            inserted.r = data.length;
            inserted.suffixStartIndex = l;
            split.next[data[inserted.l]] = inserted;
            l++;
            jump();
        }

        public int lcs(int leftEndIndex) {
            return DigitUtils.lowBit(lcs(root, leftEndIndex));
        }

        private long lcs(Node root, int leftEndIndex) {
            int mask = 0;
            int ans = 0;
            if (root.suffixStartIndex != -1) {
                if (root.suffixStartIndex <= leftEndIndex) {
                    return 1L << 32;
                } else {
                    return 1L << 33;
                }
            }
            for (int i = 0; i < alphabet; i++) {
                Node node = root.next[i];
                if (node == null) {
                    continue;
                }
                long sub = lcs(node, leftEndIndex);
                mask |= DigitUtils.highBit(sub);
                ans = Math.max(ans, DigitUtils.lowBit(sub));
            }
            if (mask == 3) {
                ans = Math.max(ans, root.parentDepth + root.size(len));
            }
            return DigitUtils.asLong(mask, ans);
        }

        private class Node {
            int id;
            Node[] next;
            Node suffixLink;
            int l;
            int r;
            int suffixStartIndex = -1;
            int parentDepth;

            public void setParent(Node parent) {
                parentDepth = parent == null ? 0 : (parent.parentDepth + parent.r - parent.l + 1);
            }

            public Node(Node parent, int cap) {
                id = sequence++;
                setParent(parent);
                this.next = new Node[cap];
            }

            public String toString() {
                return Arrays.toString(Arrays.copyOf(data, Math.min(r + 1, len)));
            }

            public int size(int len) {
                return Math.min(r, len - 1) - l + 1;
            }

        }

    }
}

