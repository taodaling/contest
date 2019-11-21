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
            TaskF solver = new TaskF();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskF {
        int p;
        int M;

        public int idOfStation(int x) {
            return x - 1;
        }

        public int idOfGE(int x) {
            return p + x - 1;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            p = in.readInt();
            M = in.readInt();
            int m = in.readInt();

            TwoSatBeta ts = new TwoSatBeta(p + M + 1, 0);
            for (int i = 0; i < n; i++) {
                int a = in.readInt();
                int b = in.readInt();
                ts.atLeastOneIsTrue(ts.elementId(idOfStation(a)), ts.elementId(idOfStation(b)));
            }

            for (int i = 2; i <= M + 1; i++) {
                ts.deduce(ts.elementId(idOfGE(i)), ts.elementId(idOfGE(i - 1)));
            }

            for (int i = 1; i <= p; i++) {
                int l = in.readInt();
                int r = in.readInt();
                ts.deduce(ts.elementId(idOfStation(i)),
                        ts.elementId(idOfGE(l)));
                ts.deduce(ts.elementId(idOfStation(i)),
                        ts.negateElementId(idOfGE(r + 1)));
            }

            for (int i = 0; i < m; i++) {
                int a = in.readInt();
                int b = in.readInt();
                ts.atLeastOneIsFalse(ts.elementId(idOfStation(a)), ts.elementId(idOfStation(b)));
            }

            boolean solvable = ts.solve(true);
            if (!solvable) {
                out.println(-1);
                return;
            }
            int k = 0;
            int f = 0;
            for (int i = 1; i <= p; i++) {
                if (ts.valueOf(idOfStation(i))) {
                    k++;
                }
            }
            for (int i = 1; i <= M + 1; i++) {
                if (ts.valueOf(idOfGE(i))) {
                    f = i;
                }
            }
            out.append(k).append(' ').append(f).println();
            for (int i = 1; i <= p; i++) {
                if (ts.valueOf(idOfStation(i))) {
                    out.append(i).append(' ');
                }
            }
        }

    }

    static class MultiWayIntDeque {
        private int[] vals;
        private int[] next;
        private int[] prev;
        private int[] heads;
        private int[] tails;
        private int alloc;
        private int queueNum;

        public IntIterator iterator(final int queue) {
            return new IntIterator() {
                int ele = heads[queue];


                public boolean hasNext() {
                    return ele != 0;
                }


                public int next() {
                    int ans = vals[ele];
                    ele = next[ele];
                    return ans;
                }
            };
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            prev = Arrays.copyOf(prev, newSize);
            vals = Arrays.copyOf(vals, newSize);
        }

        public void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
        }

        public MultiWayIntDeque(int qNum, int totalCapacity) {
            vals = new int[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            prev = new int[totalCapacity + 1];
            heads = new int[qNum];
            tails = new int[qNum];
            queueNum = qNum;
        }

        public void addLast(int qId, int x) {
            alloc();
            vals[alloc] = x;

            if (heads[qId] == 0) {
                heads[qId] = tails[qId] = alloc;
                return;
            }
            next[tails[qId]] = alloc;
            prev[alloc] = tails[qId];
            tails[qId] = alloc;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < queueNum; i++) {
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

    static class TwoSatBeta {
        private MultiWayIntDeque edges;
        private boolean[] values;
        private int[] sets;
        private int[] dfns;
        private int[] lows;
        private boolean[] instk;
        private IntDeque deque;
        private int n;
        private int dfn = 0;

        public TwoSatBeta(int n, int m) {
            values = new boolean[n * 2];
            sets = new int[n * 2];
            edges = new MultiWayIntDeque(n * 2, m * 2);
            dfns = new int[n * 2];
            lows = new int[n * 2];
            instk = new boolean[n * 2];
            deque = new IntDeque(n * 2);
            this.n = n;
        }

        public boolean valueOf(int x) {
            return values[sets[elementId(x)]];
        }

        public boolean solve(boolean fetchValue) {
            Arrays.fill(values, false);
            Arrays.fill(dfns, 0);
            deque.clear();
            dfn = 0;

            for (int i = 0; i < sets.length; i++) {
                tarjan(i);
            }
            for (int i = 0; i < n; i++) {
                if (sets[elementId(i)] == sets[negateElementId(i)]) {
                    return false;
                }
            }

            if (!fetchValue) {
                return true;
            }

            Arrays.fill(dfns, 0);
            for (int i = 0; i < sets.length; i++) {
                assign(i);
            }
            return true;
        }

        private void assign(int root) {
            if (dfns[root] > 0) {
                return;
            }
            dfns[root] = 1;
            for (IntIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
                int node = iterator.next();
                assign(node);
            }
            if (sets[root] == root) {
                values[root] = !values[sets[negate(root)]];
            }
        }

        private void tarjan(int root) {
            if (dfns[root] > 0) {
                return;
            }
            lows[root] = dfns[root] = ++dfn;
            instk[root] = true;
            deque.addLast(root);
            for (IntIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
                int node = iterator.next();
                tarjan(node);
                if (instk[node] && lows[node] < lows[root]) {
                    lows[root] = lows[node];
                }
            }
            if (lows[root] == dfns[root]) {
                int last;
                do {
                    last = deque.removeLast();
                    sets[last] = root;
                    instk[last] = false;
                } while (last != root);
            }
        }

        public int elementId(int x) {
            return x << 1;
        }

        public int negateElementId(int x) {
            return (x << 1) | 1;
        }

        private int negate(int x) {
            return x ^ 1;
        }

        public void deduce(int a, int b) {
            edges.addLast(a, b);
            edges.addLast(negate(b), negate(a));
        }

        public void or(int a, int b) {
            deduce(negate(a), b);
        }

        public void atLeastOneIsFalse(int a, int b) {
            deduce(a, negate(b));
        }

        public void atLeastOneIsTrue(int a, int b) {
            or(a, b);
        }

    }

    static class IntDeque {
        int[] data;
        int bpos;
        int epos;
        int cap;

        public IntDeque(int cap) {
            this.cap = cap + 1;
            this.data = new int[this.cap];
        }

        private int last(int i) {
            return (i == 0 ? cap : i) - 1;
        }

        private int next(int i) {
            int n = i + 1;
            return n == cap ? 0 : n;
        }

        public int removeLast() {
            return data[epos = last(epos)];
        }

        public void addLast(int val) {
            data[epos] = val;
            epos = next(epos);
        }

        public void clear() {
            bpos = epos = 0;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = bpos; i != epos; i = next(i)) {
                builder.append(data[i]).append(' ');
            }
            return builder.toString();
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

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(int c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput println() {
            cache.append('\n');
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
}

