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
            GOVInternship3 solver = new GOVInternship3();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class GOVInternship3 {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] a = new int[n];
            in.populate(a);
            int m = in.readInt();
            int[] b = new int[m];
            in.populate(b);

            Handler text = new Handler(a);
            Handler pattern = new Handler(b);

            //replace text
            for (int i = 0; i < n; i++) {
                if (a[i] != 0) {
                    continue;
                }

                //n - 1 <=> m - 1
                int l = Math.max(0, (m - 1) - (n - 1 - i));
                //0 <=> 0
                int r = Math.min(i, m - 1);
                pattern.move(l, r);
                a[i] = pattern.findMax();
            }

            //replace pattern
            for (int i = 0; i < m; i++) {
                if (b[i] != 0) {
                    continue;
                }
                int l = i;
                //n - 1 <=> m - 1
                //n - 1 - t <=> m - 1 - i
                int r = Math.min(n - 1, (n - 1) - (m - 1 - i));
                text.move(l, r);
                b[i] = text.findMax();
            }

            for (int x : a) {
                out.append(x).append(' ');
            }
            out.println();
            for (int x : b) {
                out.append(x).append(' ');
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

        public void populate(int[] data) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readInt();
            }
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
        int cnt;
        int id;

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

    static class Handler {
        int[] data;
        int l;
        int r;
        Node[] nodes;
        Node[] levels;
        static int limit = (int) 1e5;
        int top;

        private void detach(Node node) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                levels[node.cnt] = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            node.prev = node.next = null;
        }

        private void attach(Node node) {
            if (levels[node.cnt] != null) {
                levels[node.cnt].prev = node;
                node.next = levels[node.cnt];
            }
            levels[node.cnt] = node;
            top = Math.max(top, node.cnt);
        }

        private void modify(int i, int x) {
            detach(nodes[i]);
            nodes[i].cnt += x;
            attach(nodes[i]);
        }

        public int findMax() {
            while (levels[top] == null) {
                top--;
            }
            return levels[top].id;
        }

        public void move(int l, int r) {
            while (this.l > l) {
                this.l--;
                modify(data[this.l], 1);
            }
            while (this.r < r) {
                this.r++;
                modify(data[this.r], 1);
            }
            while (this.l < l) {
                modify(data[this.l], -1);
                this.l++;
            }
            while (this.r > r) {
                modify(data[this.r], -1);
                this.r--;
            }
        }

        public Handler(int[] data) {
            this.data = data;
            l = 0;
            r = -1;
            nodes = new Node[limit + 1];
            levels = new Node[limit + 1];
            for (int i = 1; i <= limit; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
                if (i > 1) {
                    nodes[i - 1].next = nodes[i];
                    nodes[i].prev = nodes[i - 1];
                }
            }
            top = 0;
            levels[0] = nodes[1];
        }

    }
}

