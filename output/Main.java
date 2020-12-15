import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
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
            IceCreamTycoon solver = new IceCreamTycoon();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class IceCreamTycoon {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            char[] cmd = new char[100];
            List<Req> reqs = new ArrayList<>((int) 1e5);
            IntegerArrayList list = new IntegerArrayList((int) 1e5);
            while (in.hasMore()) {
                in.rs(cmd, 0);
                if (cmd[0] == 'A') {
                    int n = in.ri();
                    int c = in.ri();
                    reqs.add(new Req(0, n, c));
                    list.add(c);
                } else {
                    int n = in.ri();
                    long t = in.rl();
                    reqs.add(new Req(1, n, t));
                }
            }
            if (list.isEmpty()) {
                list.add(0);
            }
            list.unique();
            int m = list.size();
            Segment seg = new Segment(0, m - 1, i -> list.get(i));
            long sum = 0;
            int kth = 0;
            for (Req req : reqs) {
                kth++;
                if (req.type == 0) {
                    int n = req.n;
                    int c = (int) req.cost;
                    sum += n;
                    c = list.binarySearch(c);
                    seg.update(c, c, 0, m - 1, n);
                } else {
                    int n = req.n;
                    long t = req.cost;
                    if (sum < n) {
                        out.println("UNHAPPY");
                        continue;
                    }
                    long cost = seg.query(0, m - 1, 0, m - 1, n);
                    if (cost <= t) {
                        sum -= n;
                        seg.consume(0, m - 1, 0, m - 1, n);
                        out.println("HAPPY");
                    } else {
                        out.println("UNHAPPY");
                    }
                }
            }
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

    static class IntegerArrayList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntegerArrayList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntegerArrayList(int[] data) {
            this(0);
            addAll(data);
        }

        public IntegerArrayList(IntegerArrayList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntegerArrayList() {
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
                throw new ArrayIndexOutOfBoundsException("Access [" + i + "]");
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

        public void addAll(int[] x) {
            addAll(x, 0, x.length);
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntegerArrayList list) {
            addAll(list.data, 0, list.size);
        }

        public void sort() {
            if (size <= 1) {
                return;
            }
            Randomized.shuffle(data, 0, size);
            Arrays.sort(data, 0, size);
        }

        public void unique() {
            if (size <= 1) {
                return;
            }

            sort();
            int wpos = 1;
            for (int i = 1; i < size; i++) {
                if (data[i] != data[wpos - 1]) {
                    data[wpos++] = data[i];
                }
            }
            size = wpos;
        }

        public int binarySearch(int x) {
            return Arrays.binarySearch(data, 0, size, x);
        }

        public int size() {
            return size;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntegerArrayList)) {
                return false;
            }
            IntegerArrayList other = (IntegerArrayList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + Integer.hashCode(data[i]);
            }
            return h;
        }

        public IntegerArrayList clone() {
            IntegerArrayList ans = new IntegerArrayList();
            ans.addAll(this);
            return ans;
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int floorAverage(int x, int y) {
            return (x & y) + ((x ^ y) >> 1);
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

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(String c) {
            return append(c).println();
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

    static interface IntToIntegerFunction {
        int apply(int x);

    }

    static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        long cnt;
        long cost;
        boolean clear;
        int weight;

        private void modify(long cnt) {
            this.cnt += cnt;
            this.cost = this.cnt * weight;
        }

        private void clear() {
            clear = true;
            cnt = 0;
            cost = 0;
        }

        public void pushUp() {
            cnt = left.cnt + right.cnt;
            cost = left.cost + right.cost;
        }

        public void pushDown() {
            if (clear) {
                left.clear();
                right.clear();
                clear = false;
            }
        }

        public Segment(int l, int r, IntToIntegerFunction func) {
            if (l < r) {
                int m = DigitUtils.floorAverage(l, r);
                left = new Segment(l, m, func);
                right = new Segment(m + 1, r, func);
                pushUp();
            } else {
                weight = func.apply(l);
            }
        }

        private boolean enter(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean leave(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, long cnt) {
            if (leave(ll, rr, l, r)) {
                return;
            }
            if (enter(ll, rr, l, r)) {
                modify(cnt);
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            left.update(ll, rr, l, m, cnt);
            right.update(ll, rr, m + 1, r, cnt);
            pushUp();
        }

        public void consume(int ll, int rr, int l, int r, long k) {
            if (leave(ll, rr, l, r) || k <= 0) {
                return;
            }
            if (enter(ll, rr, l, r) && cnt <= k) {
                clear();
                return;
            }
            if (l == r) {
                modify(-k);
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            long forRight = k - left.cnt;
            left.consume(ll, rr, l, m, k);
            right.consume(ll, rr, m + 1, r, forRight);
            pushUp();
        }

        public long query(int ll, int rr, int l, int r, long k) {
            if (leave(ll, rr, l, r) || k <= 0) {
                return 0;
            }
            if (enter(ll, rr, l, r) && cnt <= k) {
                return cost;
            }
            if (l == r) {
                return weight * k;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            long cost = left.query(ll, rr, l, m, k);
            cost += right.query(ll, rr, m + 1, r, k - left.cnt);
            return cost;
        }

        private Segment deepClone() {
            Segment seg = clone();
            if (seg.left != null) {
                seg.left = seg.left.deepClone();
            }
            if (seg.right != null) {
                seg.right = seg.right.deepClone();
            }
            return seg;
        }

        protected Segment clone() {
            try {
                return (Segment) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        private void toString(StringBuilder builder) {
            if (left == null && right == null) {
                builder.append(cnt).append(",");
                return;
            }
            pushDown();
            left.toString(builder);
            right.toString(builder);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            deepClone().toString(builder);
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
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

        public int ri() {
            return readInt();
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

        public long rl() {
            return readLong();
        }

        public long readLong() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            long val = 0;
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

        public int rs(char[] data, int offset) {
            return readString(data, offset);
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

        public boolean hasMore() {
            skipBlank();
            return next != -1;
        }

    }

    static class Req {
        int type;
        int n;
        long cost;

        public Req(int type, int n, long cost) {
            this.type = type;
            this.n = n;
            this.cost = cost;
        }

    }
}

