import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
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
            MaximumBuildingII solver = new MaximumBuildingII();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class MaximumBuildingII {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            char[][] mat = new char[n][m];
            for (int i = 0; i < n; i++) {
                in.readString(mat[i], 0);
            }
            long[][] ans = RectOnGridProblem.countAvailableRect((i, j) -> mat[i][j] == '*' ? 1 : 0, n, m);
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    out.append(ans[i][j]).append(' ');
                }
                out.println();
            }
        }

    }

    static interface RevokeIterator<E> extends Iterator<E> {
    }

    static class RectOnGridProblem {
        public static long[][] countAvailableRect(Int2ToIntegerFunction mat, int n, int m) {
            long[][] tag = new long[n + 1][m + 1];
            int[] low = new int[m];
            boolean[] active = new boolean[m];
            int[] left = new int[m];
            int[] right = new int[m];
            Arrays.fill(low, n);
            LinkedListBeta<Integer> list = new LinkedListBeta<>();
            LinkedListBeta.Node<Integer>[] nodes = new LinkedListBeta.Node[m];
            for (int i = 0; i < m; i++) {
                nodes[i] = list.addLast(i);
            }
            for (int i = n - 1; i >= 0; i--) {
                for (int j = 0; j < m; j++) {
                    if (mat.apply(i, j) != 0) {
                        list.remove(nodes[j]);
                        list.addLast(nodes[j]);
                        low[j] = i;
                    }
                }
                Arrays.fill(active, false);
                for (int j : list) {
                    int row = low[j] - 1;
                    int high = row - i + 1;
                    active[j] = true;
                    int l = j;
                    int r = j;
                    tag[high][1]++;
                    while (l > 0 && active[l - 1]) {
                        //merge
                        int lr = l - 1;
                        int ll = left[lr];
                        tag[high][lr - ll + 1]--;
                        tag[high][r - l + 1]--;
                        l = ll;
                        tag[high][r - l + 1]++;
                    }
                    while (r + 1 < m && active[r + 1]) {
                        //merge
                        int rl = r + 1;
                        int rr = right[rl];
                        tag[high][rr - rl + 1]--;
                        tag[high][r - l + 1]--;
                        r = rr;
                        tag[high][r - l + 1]++;
                    }
                    left[l] = left[r] = l;
                    right[r] = right[l] = r;
                }
            }

            for (int i = n - 1; i >= 0; i--) {
                for (int j = 0; j <= m; j++) {
                    tag[i][j] += tag[i + 1][j];
                }
            }
            for (int i = 1; i <= n; i++) {
                long cc = 0;
                long last = 0;
                for (int j = m; j >= 0; j--) {
                    cc += tag[i][j];
                    last += cc;
                    tag[i][j] = last;
                }
            }

            for (int i = 0; i <= m; i++) {
                tag[0][i] = 0;
            }

            for (int i = 0; i <= n; i++) {
                tag[i][0] = 0;
            }

            return tag;
        }

    }

    static class CircularLinkedNode<T extends CircularLinkedNode<T>> {
        public T prev = (T) this;
        public T next = (T) this;

        public void attach(T node) {
            this.next = node.next;
            this.prev = node;
            this.next.prev = (T) this;
            this.prev.next = (T) this;
        }

        public void detach() {
            this.prev.next = next;
            this.next.prev = prev;
            this.next = this.prev = (T) this;
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

        public FastOutput append(long c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
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

    static class LinkedListBeta<E> implements Iterable<E> {
        public LinkedListBeta.Node<E> dummy = new LinkedListBeta.Node<>(null);
        protected int size;

        public LinkedListBeta.Node<E> addLast(E e) {
            LinkedListBeta.Node node = new LinkedListBeta.Node(e);
            return addLast(node);
        }

        public LinkedListBeta.Node<E> addLast(LinkedListBeta.Node node) {
            node.attach(dummy.prev);
            size++;
            return node;
        }

        public void remove(LinkedListBeta.Node<E> node) {
            node.detach();
            size--;
        }

        public RevokeIterator<E> iterator() {
            return new RevokeIterator<E>() {
                LinkedListBeta.Node<E> trace = dummy;


                public void revoke() {
                    trace = trace.prev;
                }


                public boolean hasNext() {
                    return trace.next != dummy;
                }


                public E next() {
                    return (trace = trace.next).val;
                }
            };
        }

        public static class Node<E> extends CircularLinkedNode<LinkedListBeta.Node<E>> {
            public E val;

            public Node(E val) {
                this.val = val;
            }

        }

    }

    static interface Int2ToIntegerFunction {
        int apply(int a, int b);

    }
}

