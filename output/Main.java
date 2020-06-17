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
            IfritBomber solver = new IfritBomber();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class IfritBomber {
        int[] depth;
        int[] edge;
        boolean[] visited;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int r = in.readInt();

            IntegerPoint2[] pts = new IntegerPoint2[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new IntegerPoint2(in.readInt(), in.readInt());
            }

            int[] mask = new int[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (IntegerPoint2.dist2(pts[i], pts[j]) <= r * r) {
                        mask[i] |= 1 << j;
                    }
                }
            }

            IntegerList choose = new IntegerList(n);
            int ans = 0;
            for (int i = 0; i < n; i++) {
                if (Integer.bitCount(mask[i]) == 1) {
                    ans |= 1 << i;
                } else {
                    choose.add(i);
                }
            }

            int[] chooseMask = new int[choose.size()];
            for (int i = 0; i < choose.size(); i++) {
                chooseMask[i] = mask[choose.get(i)];
            }

            depth = new int[n];
            edge = mask;
            visited = new boolean[n];
            for (int i = 0; i < n; i++) {
                dfs(i, 0);
            }

            int[] sep = new int[2];
            for (int i = 0; i < choose.size(); i++) {
                int x = choose.get(i);
                sep[depth[x] % 2] |= 1 << i;
            }

            int greedy = Integer.bitCount(sep[0]) < Integer.bitCount(sep[1]) ? sep[0] : sep[1];

            int maskPick = solve(chooseMask, greedy);
            for (int i = 0; i < choose.size(); i++) {
                if (Bits.get(maskPick, i) == 1) {
                    ans |= 1 << choose.get(i);
                }
            }

            out.println(Integer.bitCount(ans));
            for (int i = 0; i < n; i++) {
                if (Bits.get(ans, i) == 1) {
                    out.append(i + 1).append(' ');
                }
            }


        }

        public int comb(int n, int m) {
            return m == 0 ? 1 : (comb(n - 1, m - 1) * n / m);
        }

        public void dfs(int root, int d) {
            if (visited[root]) {
                return;
            }
            visited[root] = true;
            depth[root] = d;
            for (int i = 0; i < 30; i++) {
                if (Bits.get(edge[root], i) == 1) {
                    dfs(i, d + 1);
                }
            }
        }

        public int solve(int[] masks, int greedy) {
            if (masks.length == 0) {
                return 0;
            }

            int sum = 0;
            for (int x : masks) {
                sum |= x;
            }
            int leftCnt = masks.length / 2;
            int rightCnt = masks.length - leftCnt;

            int[][] leftIndex = new int[leftCnt + 1][];
            int[] leftSize = new int[leftCnt + 1];
            for (int i = 0; i <= leftCnt; i++) {
                leftIndex[i] = new int[comb(leftCnt, i)];
            }
            int[] leftMasks = new int[1 << leftCnt];
            for (int i = 0; i < 1 << leftCnt; i++) {
                int bitCount = Integer.bitCount(i);
                leftIndex[bitCount][leftSize[bitCount]++] = i;
                if (i > 0) {
                    int log = Log2.floorLog(i);
                    leftMasks[i] = leftMasks[i - (1 << log)] | masks[log];
                }
            }

            int[] rightMasks = new int[1 << rightCnt];
            for (int i = 0; i < 1 << rightCnt; i++) {
                if (i > 0) {
                    int log = Log2.floorLog(i);
                    rightMasks[i] = rightMasks[i - (1 << log)] | masks[log + leftCnt];
                }
            }

            int ans = greedy;
            for (int i = 0; i < leftMasks.length; i++) {
                if (leftMasks[i] == sum && Integer.bitCount(ans) > Integer.bitCount(i)) {
                    ans = i;
                }
            }

            for (int i = 0; i < 1 << rightCnt; i++) {
                int cur = Integer.bitCount(ans) - Integer.bitCount(i) - 1;
                cur = Math.min(cur, leftCnt);
                while (cur >= 0) {
                    boolean find = false;
                    for (int index : leftIndex[cur]) {
                        if ((rightMasks[i] | leftMasks[index]) == sum) {
                            ans = (i << leftCnt) | index;
                            find = true;
                            cur--;
                            break;
                        }
                    }
                    if (!find) {
                        break;
                    }
                }
            }


            return ans;
        }

    }

    static class Log2 {
        public static int floorLog(int x) {
            return 31 - Integer.numberOfLeadingZeros(x);
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

    static class IntegerPoint2 {
        public final long x;
        public final long y;

        public IntegerPoint2(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public static long dist2(IntegerPoint2 a, IntegerPoint2 b) {
            long dx = a.x - b.x;
            long dy = a.y - b.y;
            return dx * dx + dy * dy;
        }

        public IntegerPoint2 clone() {
            try {
                return (IntegerPoint2) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            return String.format("(%d, %d)", x, y);
        }

    }

    static class Bits {
        private Bits() {
        }

        public static int get(int x, int i) {
            return (x >>> i) & 1;
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
}

