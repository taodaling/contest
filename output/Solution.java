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
public class Solution {
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
            Indicium solver = new Indicium();
            int testCount = Integer.parseInt(in.next());
            for (int i = 1; i <= testCount; i++)
                solver.solve(i, in, out);
            out.close();
        }
    }

    static class Indicium {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            out.printf("Case #%d: ", testNumber);
            int n = in.readInt();
            int k = in.readInt() - n;
            int[][] mat = new int[n][n];
            SequenceUtils.deepFill(mat, -1);
            for (int i = 0; i < n; i++) {
                mat[i][i] = 0;
            }

            if (n <= 3) {
                int[][] ans = solve(n, k + n);
                if (ans == null) {
                    out.println("IMPOSSIBLE");
                    return;
                }
                out.println("POSSIBLE");
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        out.append(ans[i][j]).append(' ');
                    }
                    out.println();
                }
                return;
            }

            int sum = 0;
            int trace = n;
            while (sum < k) {
                trace--;
                int plus = Math.min(n - 1, k - sum);
                mat[trace][trace] += plus;
                sum += plus;
            }
            //debug.debug("mat", Arrays.deepToString(mat));
            if (trace == n - 1 && mat[trace][trace] == 1 || trace == 0 && mat[trace][trace] == n - 2) {
                out.println("IMPOSSIBLE");
                return;
            }

            if (k == 0 || k == (n - 1) * n) {
            } else if (trace == n - 1) {
                mat[trace][trace]--;
                mat[trace - 1][trace - 1]++;
            } else if (trace == 0) {
                mat[trace][trace]++;
                mat[trace + 1][trace + 1]--;
            } else if (trace == 1 && mat[trace][trace] == n - 1) {
                mat[trace - 1][trace - 1]++;
                mat[trace][trace]--;
            }
            //debug.debug("mat", Arrays.deepToString(mat));
            //debug.debug("trace", trace);
            IntegerList list = new IntegerList();
            IntegerList index = new IntegerList();
            for (int i = 0; i < n; i++) {
                if (list.isEmpty() || list.tail() != mat[i][i]) {
                    list.push(mat[i][i]);
                    index.push(i);
                }
            }

            if (list.size() == 3) {
                int a = list.get(0);
                int b = list.get(1);
                int c = list.get(2);
                trace = index.get(1);
                //1 x n
                //n 1     x
                //  n x 1
                //x     n 1
                //    1 x n

                for (int j = trace + 1; j < n; j++) {
                    mat[j - 1][j] = a;
                }
                mat[n - 1][trace] = a;
                for (int j = trace - 1; j >= 0; j--) {
                    mat[j + 1][j] = c;
                }
                mat[0][trace] = c;
                for (int j = 1; j < trace; j++) {
                    mat[j - 1][j] = b;
                }
                for (int j = n - 2; j > trace; j--) {
                    mat[j + 1][j] = b;
                }
                mat[trace - 1][n - 1] = b;
                mat[trace + 1][0] = b;
            } else if (list.size() == 2) {
                int a = list.get(0);
                int b = list.get(1);
                trace = index.get(1);
                //1 n
                //  1 n
                //n   1
                //      n   1
                //      1 n
                //        1 n
                for (int j = trace + 1; j < n; j++) {
                    mat[j][j - 1] = a;
                }
                mat[trace][n - 1] = a;
                for (int j = 1; j < trace; j++) {
                    mat[j - 1][j] = b;
                }
                mat[trace - 1][0] = b;
            }

            //debug.debug("mat", Arrays.deepToString(mat));

            boolean[] used = new boolean[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (mat[i][j] != -1) {
                        used[mat[i][j]] = true;
                    }
                }
            }

            HungrayAlgoMatrixStyle ha = new HungrayAlgoMatrixStyle(n, n);
            for (int i = 0; i < n; i++) {
                if (used[i]) {
                    continue;
                }
                ha.reset();
                for (int j = 0; j < n; j++) {
                    for (int t = 0; t < n; t++) {
                        if (mat[j][t] == -1) {
                            ha.addEdge(j, t, true);
                        }
                    }
                }
                for (int j = 0; j < n; j++) {
                    ha.matchLeft(j);
                }
                for (int j = 0; j < n; j++) {
                    mat[j][ha.leftPartner(j)] = i;
                }
            }

            out.println("POSSIBLE");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    out.append(mat[i][j] + 1).append(' ');
                }
                out.println();
            }
        }

        public boolean dfs(int[][] mat, boolean[][] row, boolean[][] col, int i, int j, int t) {
            if (j >= mat.length) {
                return dfs(mat, row, col, i + 1, 0, t);
            }
            if (i == mat.length) {
                int trace = 0;
                for (int k = 0; k < mat.length; k++) {
                    trace += mat[k][k];
                }
                return trace == t;
            }
            for (int k = 1; k <= mat.length; k++) {
                if (row[i][k] || col[j][k]) {
                    continue;
                }
                row[i][k] = col[j][k] = true;
                mat[i][j] = k;
                if (dfs(mat, row, col, i, j + 1, t)) {
                    return true;
                }
                row[i][k] = col[j][k] = false;
            }
            return false;
        }

        private int[][] solve(int n, int k) {
            int[][] mat = new int[n][n];
            boolean[][] row = new boolean[n][n + 1];
            boolean[][] col = new boolean[n][n + 1];
            if (dfs(mat, row, col, 0, 0, k)) {
                return mat;
            }
            return null;
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

        public int tail() {
            checkRange(0);
            return data[size - 1];
        }

        public void push(int x) {
            add(x);
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

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
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

        public String next() {
            return readString();
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

    static class SequenceUtils {
        public static void deepFill(Object array, int val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof int[]) {
                int[] intArray = (int[]) array;
                Arrays.fill(intArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
                }
            }
        }

        public static void deepFill(Object array, boolean val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof boolean[]) {
                boolean[] intArray = (boolean[]) array;
                Arrays.fill(intArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
                }
            }
        }

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

    static class HungrayAlgoMatrixStyle {
        boolean[][] edges;
        int[][] visited;
        int[][] partner;
        int now;
        int n;
        int m;

        public HungrayAlgoMatrixStyle(int n, int m) {
            this.n = n;
            this.m = m;
            edges = new boolean[n][m];
            visited = new int[][]{new int[n], new int[m]};
            partner = new int[][]{new int[n], new int[m]};
        }

        public void reset() {
            SequenceUtils.deepFill(edges, false);
            SequenceUtils.deepFill(partner, -1);
        }

        public void addEdge(int l, int r, boolean greedy) {
            edges[l][r] = true;
            if (greedy && partner[0][l] == -1 && partner[1][r] == -1) {
                partner[0][l] = r;
                partner[1][r] = l;
            }
        }

        public boolean matchLeft(int l) {
            if (partner[0][l] != -1) {
                return true;
            }
            now++;
            return bind(l);
        }

        public int leftPartner(int l) {
            return partner[0][l];
        }

        private boolean bind(int l) {
            if (visited[0][l] == now) {
                return false;
            }
            visited[0][l] = now;
            for (int i = 0; i < m; i++) {
                if (!edges[l][i]) {
                    continue;
                }
                if (release(i)) {
                    partner[0][l] = i;
                    partner[1][i] = l;
                    return true;
                }
            }
            return false;
        }

        private boolean release(int r) {
            if (visited[1][r] == now) {
                return false;
            }
            visited[1][r] = now;
            if (partner[1][r] == -1) {
                return true;
            }
            return bind(partner[1][r]);
        }

        public String toString() {
            return Arrays.deepToString(partner);
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

        public FastOutput append(String c) {
            cache.append(c);
            return this;
        }

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
            return this;
        }

        public FastOutput println(String c) {
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

