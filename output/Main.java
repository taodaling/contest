import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.util.Random;
import java.util.AbstractCollection;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
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
            EAquariumDecoration solver = new EAquariumDecoration();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class EAquariumDecoration {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int k = in.readInt();
            int[] cost = new int[n];
            int[] type = new int[n];
            in.populate(cost);
            for (int i = 0; i < 2; i++) {
                int t = in.readInt();
                while (t-- > 0) {
                    int j = in.readInt() - 1;
                    type[j] |= 1 << i;
                }
            }
            IntegerList[] classify = new IntegerList[4];
            for (int i = 0; i < 4; i++) {
                classify[i] = new IntegerList(n);
            }
            for (int i = 0; i < n; i++) {
                classify[type[i]].add(cost[i]);
            }
            for (int i = 0; i < 4; i++) {
                classify[i].sort();
            }

            LongPreSum[] lps = new LongPreSum[4];
            for (int i = 0; i < 4; i++) {
                int finalI = i;
                lps[i] = new LongPreSum(j -> classify[finalI].get(j), classify[finalI].size());
            }

            long limit = (long) 1e18;
            long ans = limit;
            Machine mac = new Machine(n);
            for (int i = 0; i < classify[0].size(); i++) {
                mac.addFree(classify[0].get(i));
            }
            for (int i = 0; i <= classify[3].size() && i <= m; i++) {
                int req = Math.max(0, k - i);
                if (req > classify[1].size() || req > classify[2].size()) {
                    continue;
                }

                while (classify[1].size() > req) {
                    mac.addFree(classify[1].pop());
                }
                while (classify[2].size() > req) {
                    mac.addFree(classify[2].pop());
                }

                if (m - i - req * 2 >= 0 && m - i - req * 2 <= mac.size()) {
                    long local = lps[3].prefix(i - 1) +
                            lps[1].prefix(req - 1) + lps[2].prefix(req - 1) + mac.presum(m - i - req * 2);
                    ans = Math.min(ans, local);
                }
            }

            out.println(ans == limit ? -1 : ans);
        }

    }

    static class Randomized {
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

    static class SequenceUtils {
        public static boolean equal(int[] a, int al, int ar, int[] b, int bl, int br) {
            if ((ar - al) != (br - bl)) {
                return false;
            }
            for (int i = al, j = bl; i <= ar; i++, j++) {
                if (a[i] != b[j]) {
                    return false;
                }
            }
            return true;
        }

    }

    static interface IntToLongFunction {
        long apply(int x);

    }

    static class LongPreSum {
        private long[] pre;
        private int n;

        public LongPreSum(int n) {
            pre = new long[n];
        }

        public void populate(IntToLongFunction a, int n) {
            this.n = n;
            if (n == 0) {
                return;
            }
            pre[0] = a.apply(0);
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] + a.apply(i);
            }
        }

        public LongPreSum(IntToLongFunction a, int n) {
            this(n);
            populate(a, n);
        }

        public long prefix(int i) {
            if (i < 0) {
                return 0;
            }
            return pre[Math.min(i, n - 1)];
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

    static class IntegerList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntegerList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntegerList(IntegerList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntegerList() {
            this(0);
        }

        public void ensureSpace(int req) {
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public int get(int i) {
            checkRange(i);
            return data[i];
        }

        public void add(int x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntegerList list) {
            addAll(list.data, 0, list.size);
        }

        public void sort() {
            if (size <= 1) {
                return;
            }
            Randomized.shuffle(data, 0, size);
            Arrays.sort(data, 0, size);
        }

        public int pop() {
            return data[--size];
        }

        public int size() {
            return size;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntegerList)) {
                return false;
            }
            IntegerList other = (IntegerList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Integer.hashCode(data[i]);
            }
            return h;
        }

        public IntegerList clone() {
            IntegerList ans = new IntegerList();
            ans.addAll(this);
            return ans;
        }

    }

    static class RandomWrapper {
        private Random random;
        public static RandomWrapper INSTANCE = new RandomWrapper(new Random());

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

    static class Machine {
        PriorityQueue<Integer> cur;
        PriorityQueue<Integer> cand;
        long sum = 0;

        public Machine(int n) {
            cand = new PriorityQueue<>(n);
            cur = new PriorityQueue<>(n, (a, b) -> -a.compareTo(b));
        }

        public void addFree(int x) {
            cand.add(x);
        }

        private void add(int x) {
            cur.add(x);
            sum += x;
        }

        private int remove() {
            int ans = cur.remove();
            sum -= ans;
            return ans;
        }

        public int size() {
            return cur.size() + cand.size();
        }

        public long presum(int n) {
            while (cur.size() < n) {
                add(cand.remove());
            }
            while (cur.size() > n) {
                remove();
            }
            while (!cur.isEmpty() && !cand.isEmpty() && cur.peek() > cand.peek()) {
                cand.add(remove());
                add(cand.remove());
            }
            return sum;
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
}

