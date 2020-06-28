import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.Stream;
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
            CBearAndCompany solver = new CBearAndCompany();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CBearAndCompany {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            char[] s = in.readString().toCharArray();
            int[] cnts = new int[3];
            IntegerList[] indices = IntStream.range(0, 3).mapToObj(i -> new IntegerList()).toArray(i -> new IntegerList[i]);
            for (int i = 0; i < n; i++) {
                s[i] = (char) (s[i] == 'V' ? 0 : s[i] == 'K' ? 1 : 2);
                cnts[s[i]]++;
                indices[s[i]].add(i);
            }

            int[][] prevCnt = new int[n][3];
            for (int i = 1; i < n; i++) {
                for (int j = 0; j < 3; j++) {
                    prevCnt[i][j] = prevCnt[i - 1][j];
                }
                prevCnt[i][s[i - 1]]++;
            }

            ArrayIndex ai = new ArrayIndex(cnts[0] + 1, cnts[1] + 1, cnts[2] + 1, 2);
            int[] dp = new int[ai.totalSize()];
            int[] next = new int[ai.totalSize()];
            int inf = (int) 1e9;
            Arrays.fill(dp, inf);
            dp[ai.indexOf(0, 0, 0, 0)] = 0;
            for (int i = 0; i < n; i++) {
                Arrays.fill(next, inf);
                for (int a = 0; a <= cnts[0]; a++) {
                    for (int b = 0; b <= cnts[1]; b++) {
                        for (int c = 0; c <= cnts[2]; c++) {
                            for (int t = 0; t < 2; t++) {
                                int val = dp[ai.indexOf(a, b, c, t)];
                                //put 0
                                if (a + 1 <= cnts[0]) {
                                    int[] prev = prevCnt[indices[0].get(a)];
                                    next[ai.indexOf(a + 1, b, c, 1)] = Math.min(next[ai.indexOf(a + 1, b, c, 1)], val + remain(prev[0], prev[1], prev[2], a, b, c));
                                }
                                //put 1
                                if (b + 1 <= cnts[1] && t == 0) {
                                    int[] prev = prevCnt[indices[1].get(b)];
                                    next[ai.indexOf(a, b + 1, c, 0)] = Math.min(next[ai.indexOf(a, b + 1, c, 0)], val + remain(prev[0], prev[1], prev[2], a, b, c));
                                }
                                //put 2
                                if (c + 1 <= cnts[2]) {
                                    int[] prev = prevCnt[indices[2].get(c)];
                                    next[ai.indexOf(a, b, c + 1, 0)] = Math.min(next[ai.indexOf(a, b, c + 1, 0)], val + remain(prev[0], prev[1], prev[2], a, b, c));
                                }
                            }
                        }
                    }
                }

                int[] tmp = next;
                next = dp;
                dp = tmp;
            }

            int ans = Math.min(dp[ai.indexOf(cnts[0], cnts[1], cnts[2], 0)], dp[ai.indexOf(cnts[0], cnts[1], cnts[2], 1)]);
            out.println(ans);
        }

        public int remain(int a, int b, int c, int usedA, int usedB, int usedC) {
            return Math.max(a - usedA, 0) + Math.max(b - usedB, 0) + Math.max(c - usedC, 0);
        }

    }

    static class ArrayIndex {
        int[] dimensions;

        public ArrayIndex(int... dimensions) {
            this.dimensions = dimensions;
        }

        public int totalSize() {
            int ans = 1;
            for (int x : dimensions) {
                ans *= x;
            }
            return ans;
        }

        public int indexOf(int a, int b) {
            return a * dimensions[1] + b;
        }

        public int indexOf(int a, int b, int c) {
            return indexOf(a, b) * dimensions[2] + c;
        }

        public int indexOf(int a, int b, int c, int d) {
            return indexOf(a, b, c) * dimensions[3] + d;
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
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
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

        public String readString(StringBuilder builder) {
            skipBlank();

            while (next > 32) {
                builder.append((char) next);
                next = read();
            }

            return builder.toString();
        }

        public String readString() {
            defaultStringBuf.setLength(0);
            return readString(defaultStringBuf);
        }

    }
}

