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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskE {
        MultiWayIntStack edges;
        int[] a;
        long[][] negDp;
        long[] posDp;
        int[] sizes;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            a = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                a[i] = in.readInt();
            }
            edges = new MultiWayIntStack(n + 1, n * 2);
            for (int i = 1; i < n; i++) {
                int a = in.readInt();
                int b = in.readInt();
                edges.addLast(a, b);
                edges.addLast(b, a);
            }
            negDp = new long[n + 1][];
            posDp = new long[n + 1];
            sizes = new int[n + 1];

            dfsForSize(1, 0);
            dfs(1, 0);
            long ans = posDp[1];
            for (int i = 0; i < n; i++) {
                if (negDp[1][i] < 0) {
                    ans = Math.min(ans, i);
                    break;
                }
            }
            out.println(ans);
        }

        public void dfsForSize(int root, int p) {
            sizes[root] = 1;
            for (IntIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
                int node = iterator.next();
                if (node == p) {
                    continue;
                }
                dfsForSize(node, root);
                sizes[root] += sizes[node];
            }
        }

        public void dfs(int root, int p) {
            posDp[root] = a[root] > 0 ? 0 : (long) 1e18;
            int total = 1;

            long[] last = new long[sizes[root]];
            long[] next = new long[sizes[root]];
            last[0] = a[root];
            for (IntIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
                int node = iterator.next();
                if (node == p) {
                    continue;
                }
                dfs(node, root);
                long atLeastNeed = posDp[node];
                long cutAtLeast = posDp[node] + 1;
                for (int i = 0; i < sizes[node]; i++) {
                    if (negDp[node][i] < 0) {
                        atLeastNeed = Math.min(atLeastNeed, i + 1);
                        cutAtLeast = Math.min(cutAtLeast, i + 1);
                        break;
                    }
                }
                posDp[root] += atLeastNeed;
                for (int i = 0; i <= total + sizes[node] - 1; i++) {
                    next[i] = (long) 1e18;
                    if (i >= cutAtLeast) {
                        next[i] = Math.min(next[i], last[(int) (i - cutAtLeast)]);
                    }
                }
                for (int i = 0; i < total; i++) {
                    for (int j = 0; j < sizes[node]; j++) {
                        next[i + j] = Math.min(next[i + j], last[i] + negDp[node][j]);
                    }
                }

                total += sizes[node];
                {
                    long[] tmp = next;
                    next = last;
                    last = tmp;
                }
            }
            negDp[root] = last;
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

    }

    static interface IntIterator {
        boolean hasNext();

        int next();

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

        public FastOutput println(long c) {
            cache.append(c).append('\n');
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

    static class MultiWayIntStack {
        private int[] values;
        private int[] next;
        private int[] heads;
        private int alloc;
        private int stackNum;

        public IntIterator iterator(final int queue) {
            return new IntIterator() {
                int ele = heads[queue];


                public boolean hasNext() {
                    return ele != 0;
                }


                public int next() {
                    int ans = values[ele];
                    ele = next[ele];
                    return ans;
                }
            };
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            values = Arrays.copyOf(values, newSize);
        }

        public void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
        }

        public MultiWayIntStack(int qNum, int totalCapacity) {
            values = new int[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            heads = new int[qNum];
            stackNum = qNum;
        }

        public void addLast(int qId, int x) {
            alloc();
            values[alloc] = x;
            next[alloc] = heads[qId];
            heads[qId] = alloc;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stackNum; i++) {
                builder.append(i).append(": ");
                for (IntIterator iterator = iterator(i); iterator.hasNext(); ) {
                    builder.append(iterator.next()).append(",");
                }
                if (builder.charAt(builder.length() - 1) == ',') {
                    builder.setLength(builder.length() - 1);
                }
                builder.append('\n');
            }
            return builder.toString();
        }

    }
}

