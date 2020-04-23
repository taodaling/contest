import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.Random;
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
            CNastyaAndUnexpectedGuest solver = new CNastyaAndUnexpectedGuest();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CNastyaAndUnexpectedGuest {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int[] d = new int[m];
            for (int i = 0; i < m; i++) {
                d[i] = in.readInt();
            }
            Randomized.shuffle(d);
            Arrays.sort(d);

            int g = in.readInt();
            int r = in.readInt();

            boolean[][] dp = new boolean[m][g + 1];
            dp[0][g] = true;
            IntegerDeque pend = new IntegerDequeImpl(m);
            LongDeque dq = new LongDequeImpl(m * (g + 1));
            dq.addLast(merge(0, g));
            int round = 0;
            boolean valid = false;

            while (!dq.isEmpty()) {
                round++;
                while (!dq.isEmpty()) {
                    long head = dq.removeFirst();
                    int high = (int) (head >> 32);
                    int low = (int) head;
//                debug.debug("high", high);
//                debug.debug("low", low);
                    valid = valid || high == m - 1;

                    if (low == 0) {
                        pend.addLast(high);
                        continue;
                    }

                    //left
                    if (high > 0) {
                        int leftTime = low - (d[high] - d[high - 1]);
                        if (leftTime >= 0 && !dp[high - 1][leftTime]) {
                            dp[high - 1][leftTime] = true;
                            dq.addLast(merge(high - 1, leftTime));
                        }
                    }

                    if (high < m - 1) {
                        int rightTime = low - (d[high + 1] - d[high]);
                        if (rightTime >= 0 && !dp[high + 1][rightTime]) {
                            dp[high + 1][rightTime] = true;
                            dq.addLast(merge(high + 1, rightTime));
                        }
                    }
                }

                if (valid) {
                    int time = -1;
                    for (int i = 0; i <= g; i++) {
                        if (dp[m - 1][i]) {
                            time = i;
                        }
                    }

                    int cost = (round - 1) * (g + r) + g - time;
                    out.println(cost);
                    return;
                }

                while (!pend.isEmpty()) {
                    int high = pend.removeFirst();
                    int low = g;

                    if (high > 0) {
                        int leftTime = low - (d[high] - d[high - 1]);
                        if (leftTime >= 0 && !dp[high - 1][leftTime]) {
                            dp[high - 1][leftTime] = true;
                            dq.addLast(merge(high - 1, leftTime));
                        }
                    }

                    if (high < m - 1) {
                        int rightTime = low - (d[high + 1] - d[high]);
                        if (rightTime >= 0 && !dp[high + 1][rightTime]) {
                            dp[high + 1][rightTime] = true;
                            dq.addLast(merge(high + 1, rightTime));
                        }
                    }
                }
            }

            out.println(-1);
        }

        public long merge(long a, long b) {
            return (a << 32) | b;
        }

    }

    static interface IntegerDeque extends IntegerStack {
        int removeFirst();

    }

    static interface IntegerIterator {
        boolean hasNext();

        int next();

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

    static class RandomWrapper {
        private Random random;
        public static final RandomWrapper INSTANCE = new RandomWrapper(new Random());

        public RandomWrapper() {
            this(new Random());
        }

        public RandomWrapper(Random random) {
            this.random = random;
        }

        public int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }

    static interface LongDeque extends LongStack {
        long removeFirst();

    }

    static class IntegerDequeImpl implements IntegerDeque {
        private int[] data;
        private int bpos;
        private int epos;
        private static final int[] EMPTY = new int[0];
        private int n;

        public IntegerDequeImpl(int cap) {
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
            bpos = 0;
            epos = 0;
            n = cap;
        }

        private void expandSpace(int len) {
            while (n < len) {
                n = Math.max(n + 10, n * 2);
            }
            int[] newData = new int[n];
            if (bpos <= epos) {
                if (bpos < epos) {
                    System.arraycopy(data, bpos, newData, 0, epos - bpos);
                }
            } else {
                System.arraycopy(data, bpos, newData, 0, data.length - bpos);
                System.arraycopy(data, 0, newData, data.length - bpos, epos);
            }
            epos = size();
            bpos = 0;
            data = newData;
        }

        public IntegerIterator iterator() {
            return new IntegerIterator() {
                int index = bpos;


                public boolean hasNext() {
                    return index != epos;
                }


                public int next() {
                    int ans = data[index];
                    index = IntegerDequeImpl.this.next(index);
                    return ans;
                }
            };
        }

        public int removeFirst() {
            int ans = data[bpos];
            bpos = next(bpos);
            return ans;
        }

        public void addLast(int x) {
            ensureMore();
            data[epos] = x;
            epos = next(epos);
        }

        private int next(int x) {
            return x + 1 >= n ? 0 : x + 1;
        }

        private void ensureMore() {
            if (next(epos) == bpos) {
                expandSpace(n + 1);
            }
        }

        public int size() {
            int ans = epos - bpos;
            if (ans < 0) {
                ans += data.length;
            }
            return ans;
        }

        public boolean isEmpty() {
            return bpos == epos;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (IntegerIterator iterator = iterator(); iterator.hasNext(); ) {
                builder.append(iterator.next()).append(' ');
            }
            return builder.toString();
        }

    }

    static interface LongStack {
        void addLast(long x);

        boolean isEmpty();

    }

    static interface LongIterator {
        boolean hasNext();

        long next();

    }

    static class LongDequeImpl implements LongDeque {
        private long[] data;
        private int bpos;
        private int epos;
        private static final long[] EMPTY = new long[0];
        private int n;

        public LongDequeImpl(int cap) {
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new long[cap];
            }
            bpos = 0;
            epos = 0;
            n = cap;
        }

        private void expandSpace(int len) {
            while (n < len) {
                n = Math.max(n + 10, n * 2);
            }
            long[] newData = new long[n];
            if (bpos <= epos) {
                if (bpos < epos) {
                    System.arraycopy(data, bpos, newData, 0, epos - bpos);
                }
            } else {
                System.arraycopy(data, bpos, newData, 0, data.length - bpos);
                System.arraycopy(data, 0, newData, data.length - bpos, epos);
            }
            epos = size();
            bpos = 0;
            data = newData;
        }

        public LongIterator iterator() {
            return new LongIterator() {
                int index = bpos;


                public boolean hasNext() {
                    return index != epos;
                }


                public long next() {
                    long ans = data[index];
                    index = LongDequeImpl.this.next(index);
                    return ans;
                }
            };
        }

        public long removeFirst() {
            long ans = data[bpos];
            bpos = next(bpos);
            return ans;
        }

        public void addLast(long x) {
            ensureMore();
            data[epos] = x;
            epos = next(epos);
        }

        private int next(int x) {
            return x + 1 >= n ? 0 : x + 1;
        }

        private void ensureMore() {
            if (next(epos) == bpos) {
                expandSpace(n + 1);
            }
        }

        public int size() {
            int ans = epos - bpos;
            if (ans < 0) {
                ans += data.length;
            }
            return ans;
        }

        public boolean isEmpty() {
            return bpos == epos;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (LongIterator iterator = iterator(); iterator.hasNext(); ) {
                builder.append(iterator.next()).append(' ');
            }
            return builder.toString();
        }

    }

    static class Randomized {
        public static void shuffle(int[] data) {
            shuffle(data, 0, data.length - 1);
        }

        public static void shuffle(int[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                int tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return RandomWrapper.INSTANCE.nextInt(l, r);
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

    static interface IntegerStack {
        void addLast(int x);

        boolean isEmpty();

    }
}

