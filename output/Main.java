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
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            Robot solver = new Robot();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Robot {
        public LCSegment.Line inverse(LCSegment.Line line) {
            return new LCSegment.Line(-line.a, -line.b);
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();

            LCSegment.Line[] pos = new LCSegment.Line[n];
            int[] modifyTime = new int[n];
            for (int i = 0; i < n; i++) {
                pos[i] = new LCSegment.Line(0, in.readInt());
            }

            List<Update> updates = new ArrayList<>(m);
            IntegerList qs = new IntegerList(m);
            IntegerList times = new IntegerList(m + 1);
            times.add(0);
            char[] cmd = new char[100];
            for (int i = 0; i < m; i++) {
                int t = in.readInt();
                times.add(t);
                in.readString(cmd, 0);
                if (cmd[0] == 'c') {
                    Update update = new Update();
                    update.time = t;
                    update.k = in.readInt() - 1;
                    update.x = in.readInt();
                    updates.add(update);
                } else {
                    qs.add(t);
                }
            }

            IntegerDiscreteMap dm = new IntegerDiscreteMap(times.getData(), 0, times.size());
            IntToLongFunction func = i -> dm.iThElement(i);
            LCSegment top = new LCSegment(dm.minRank(), dm.maxRank());
            LCSegment bot = new LCSegment(dm.minRank(), dm.maxRank());

            for (Update u : updates) {
                int l = dm.rankOf(modifyTime[u.k]);
                int r = dm.rankOf(u.time);
                top.update(l, r, dm.minRank(), dm.maxRank(), pos[u.k], func);
                bot.update(l, r, dm.minRank(), dm.maxRank(), inverse(pos[u.k]), func);
                modifyTime[u.k] = u.time;
                pos[u.k] = new LCSegment.Line(u.x, pos[u.k].apply(modifyTime[u.k]) - u.x * (long) modifyTime[u.k]);
            }

            for (int i = 0; i < n; i++) {
                int l = dm.rankOf(modifyTime[i]);
                int r = dm.maxRank();
                top.update(l, r, dm.minRank(), dm.maxRank(), pos[i], func);
                bot.update(l, r, dm.minRank(), dm.maxRank(), inverse(pos[i]), func);
            }

            for (int i = 0; i < qs.size(); i++) {
                int t = qs.get(i);
                int r = dm.rankOf(t);
                long cand1 = top.query(r, r, dm.minRank(), dm.maxRank(), t);
                long cand2 = bot.query(r, r, dm.minRank(), dm.maxRank(), t);
                out.println(Math.max(cand1, cand2));
            }
        }

    }

    static interface IntToLongFunction {
        long apply(int x);

    }

    static class IntegerDiscreteMap {
        int[] val;
        int f;
        int t;

        public IntegerDiscreteMap(int[] val, int f, int t) {
            Randomized.shuffle(val, f, t);
            Arrays.sort(val, f, t);
            int wpos = f + 1;
            for (int i = f + 1; i < t; i++) {
                if (val[i] == val[i - 1]) {
                    continue;
                }
                val[wpos++] = val[i];
            }
            this.val = val;
            this.f = f;
            this.t = wpos;
        }

        public int rankOf(int x) {
            return Arrays.binarySearch(val, f, t, x) - f;
        }

        public int iThElement(int i) {
            return val[f + i];
        }

        public int minRank() {
            return 0;
        }

        public int maxRank() {
            return t - f - 1;
        }

        public String toString() {
            return Arrays.toString(Arrays.copyOfRange(val, f, t));
        }

    }

    static class IntegerList implements Cloneable {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public int[] getData() {
            return data;
        }

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
                throw new ArrayIndexOutOfBoundsException("invalid index " + i);
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

    static class LCSegment {
        static final long INF = Long.MAX_VALUE / 2;
        private static final LCSegment.Line BOTTOM = new LCSegment.Line(0, -INF);
        LCSegment left;
        LCSegment right;
        LCSegment.Line line = BOTTOM;

        public void pushUp() {
        }

        public void pushDown() {
        }

        public LCSegment(int l, int r) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new LCSegment(l, m);
                right = new LCSegment(m + 1, r);
                pushUp();
            } else {
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, LCSegment.Line line, IntToLongFunction func) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }

            int m = (l + r) >> 1;
            if (covered(ll, rr, l, r)) {
                pushDown();
                LCSegment.Line line1 = this.line;
                LCSegment.Line line2 = line;
                if (line1.a > line2.a) {
                    LCSegment.Line tmp = line1;
                    line1 = line2;
                    line2 = tmp;
                }
                if (line1.a == line2.a) {
                    this.line = line1.b >= line2.b ? line1 : line2;
                    return;
                }
                double intersect = LCSegment.Line.intersectAt(line1, line2);
                long mid = func.apply(m);
                if (mid >= intersect) {
                    this.line = line2;
                    if (left != null) {
                        left.update(ll, rr, l, m, line1, func);
                    }
                } else {
                    this.line = line1;
                    if (right != null) {
                        right.update(ll, rr, m + 1, r, line2, func);
                    }
                }
                pushUp();
                return;
            }

            pushDown();
            left.update(ll, rr, l, m, line, func);
            right.update(ll, rr, m + 1, r, line, func);
            pushUp();
        }

        public long query(int ll, int rr, int l, int r, long x) {
            if (noIntersection(ll, rr, l, r)) {
                return -INF;
            }
            if (covered(ll, rr, l, r)) {
                return line.apply(x);
            }
            int m = (l + r) >> 1;
            long ans = Math.max(left.query(ll, rr, l, m, x),
                    right.query(ll, rr, m + 1, r, x));
            ans = Math.max(ans, line.apply(x));
            return ans;
        }

        public static class Line {
            public long a;
            public long b;

            public Line(long a, long b) {
                this.a = a;
                this.b = b;
            }

            public long apply(long x) {
                return a * x + b;
            }

            public boolean equals(Object obj) {
                LCSegment.Line line = (LCSegment.Line) obj;
                return line.a == a && line.b == b;
            }

            public static double intersectAt(LCSegment.Line a, LCSegment.Line b) {
                //a1 x + b1 = a2 x + b2
                return (double) (b.b - a.b) / (a.a - b.a);
            }

        }

    }

    static class Update {
        int time;
        int k;
        int x;

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

    static class Randomized {
        private static Random random = new Random();

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
            return random.nextInt(r - l + 1) + l;
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
}

