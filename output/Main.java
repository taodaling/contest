import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            EWelcomeHomeChtholly solver = new EWelcomeHomeChtholly();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class EWelcomeHomeChtholly {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();

            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].val = in.readInt();
            }

            int B = 250;
            Block[] blocks = new Block[DigitUtils.ceilDiv(n, B)];
            for (int i = 0; i < blocks.length; i++) {
                blocks[i] = new Block(nodes, i * B, Math.min(n, (i + 1) * B) - 1);
            }

            for (int i = 0; i < m; i++) {
                int t = in.readInt();
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                int x = in.readInt();
                if (t == 1) {
                    for (Block b : blocks) {
                        b.update(l, r, x);
                    }
                } else {
                    int ans = 0;
                    for (Block b : blocks) {
                        ans += b.query(l, r, x);
                    }
                    out.println(ans);
                }
            }
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int floorDiv(int a, int b) {
            return a < 0 ? -ceilDiv(-a, b) : a / b;
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

    static class Node {
        Node next;
        Node prev;
        int val;

        public void reset() {
            next = prev = this;
        }

        public static void merge(Node a, Node b) {
            Node aTail = a.prev;
            Node bTail = b.prev;
            a.prev = bTail;
            bTail.next = a;
            aTail.next = b;
            b.prev = aTail;
        }

    }

    static class Block {
        Node[] level;
        int[] size;
        int high;
        Node[] nodes;
        int l;
        int r;
        int tag;

        public Block(Node[] nodes, int l, int r) {
            this.nodes = nodes;
            this.l = l;
            this.r = r;
            for (int i = l; i <= r; i++) {
                high = Math.max(nodes[i].val, high);
            }
            level = new Node[high + 1];
            size = new int[high + 1];
            install();
        }

        private void uninstall() {
            for (int i = l; i <= r; i++) {
                int val = nodes[i].val;
                if (level[val] == null) {
                    continue;
                }
                level[val].prev.next = null;
                for (Node head = level[val]; head != null; head = head.next) {
                    head.val = val + tag;
                }
                level[val] = null;
                size[val] = 0;
            }
            tag = 0;
        }

        private void install() {
            for (int i = l; i <= r; i++) {
                nodes[i].reset();
            }

            for (int i = l; i <= r; i++) {
                if (level[nodes[i].val] != null) {
                    Node.merge(level[nodes[i].val], nodes[i]);
                }
                level[nodes[i].val] = nodes[i];
                size[nodes[i].val]++;
            }
        }

        private void move(int i, int j) {
            if (level[i] == null) {
                return;
            }
            if (level[j] != null) {
                Node.merge(level[i], level[j]);
            }
            level[j] = level[i];
            level[i] = null;
            level[j].val = j;
            size[j] += size[i];
            size[i] = 0;
        }

        public void adjust() {
            while (level[high] == null) {
                high--;
            }
        }

        public void update(int ll, int rr, int x) {
            ll = Math.max(ll, l);
            rr = Math.min(rr, r);
            if (rr < ll) {
                return;
            }
            if (ll != l || rr != r) {
                //not cover
                uninstall();
                for (int i = ll; i <= rr; i++) {
                    if (nodes[i].val <= x) {
                        continue;
                    }
                    nodes[i].val -= x;
                }
                install();
                return;
            }

            adjust();
            //cover
            int mx = high + tag;
            if (mx <= x) {
                return;
            }

            if (x * 2 <= mx) {
                for (int i = 1 - tag; i + tag <= x; i++) {
                    move(i, i + x);
                }
                tag -= x;
            } else {
                for (int i = high; i + tag > x; i--) {
                    move(i, i - x);
                }
            }
        }

        public int query(int ll, int rr, int x) {
            ll = Math.max(ll, l);
            rr = Math.min(rr, r);
            if (rr < ll) {
                return 0;
            }
            if (ll != l || rr != r) {
                //not cover
                uninstall();
                int ans = 0;
                for (int i = ll; i <= rr; i++) {
                    if (nodes[i].val == x) {
                        ans++;
                    }
                }
                install();
                return ans;
            }

            adjust();
            //cover
            x -= tag;
            if (x >= 0 && x <= high) {
                return size[x];
            }
            return 0;
        }

    }
}

