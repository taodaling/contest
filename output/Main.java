import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.math.BigDecimal;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
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
            DDZYLovesGames solver = new DDZYLovesGames();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DDZYLovesGames {
        Debug debug = new Debug(false);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.ri();
            int m = in.ri();
            int k = in.ri();
            int[][] g = new int[n][n];
            int[] degs = new int[n];
            int[] trap = new int[n];
            IntegerArrayList trapPos = new IntegerArrayList(n);
            for (int i = 0; i < n; i++) {
                trap[i] = in.ri();
                if (trap[i] == 1) {
                    trapPos.add(i);
                }
            }
            int t = trapPos.size();
            int[] allTrapPos = trapPos.toArray();
            debug.debug("trapPos", trapPos);
            for (int i = 0; i < m; i++) {
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                g[a][b]++;
                g[b][a]++;
                degs[a]++;
                degs[b]++;
            }
            debug.debugArray("degs", degs);
            double[][] toTrap = new double[n][n];
            for (int i = 0; i < n; i++) {
                toTrap[i][i]++;
                if (trap[i] == 0) {
                    for (int j = 0; j < n; j++) {
                        toTrap[j][i] -= g[i][j] / (double) degs[i];
                    }
                }
            }
            debug.debugMatrix("toTrap", toTrap);
            Matrix toTrapMat = new Matrix(toTrap);
            Matrix invToTrapMat = Matrix.inverse(toTrapMat);
            debug.debug("invToTrapMat", invToTrapMat);
            double[][] invToTrap = invToTrapMat.toArray();
            Matrix probMatrix = new Matrix(t, 1);
            for (int i = 0; i < t; i++) {
                probMatrix.set(i, 0, invToTrapMat.get(allTrapPos[i], 0));
            }
            debug.debug("probMatrix", probMatrix);
            double[][] transfer = new double[t][t];

            for (int i = 0; i < t; i++) {
                int x = allTrapPos[i];
                double prob = 1d / degs[x];
                for (int j = 0; j < n; j++) {
                    if (g[x][j] == 0) {
                        continue;
                    }
                    for (int z = 0; z < t; z++) {
                        transfer[z][i] += g[x][j] * invToTrap[allTrapPos[z]][j] * prob;
                    }
                }
            }
            debug.debugMatrix("transfer", transfer);
            int remainBlood = k - 1 - 1;
            debug.debug("remainBlood", remainBlood);
            Matrix transferMat = new Matrix(transfer);
            Matrix multiTransferMat = Matrix.pow(transferMat, remainBlood);
            Matrix probAfterTransfer = Matrix.mul(multiTransferMat, probMatrix);
            debug.debug("multiTransferMat", multiTransferMat);
            debug.debug("probAfterTransfer", probAfterTransfer);
            double ans = probAfterTransfer.get(t - 1, 0);
            out.println(ans);
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 32 << 10;
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

        public FastOutput append(double c) {
            cache.append(new BigDecimal(c).toPlainString());
            afterWrite();
            return this;
        }

        public FastOutput println(double c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append('\n');
        }

        public FastOutput flush() {
            try {
//            boolean success = false;
//            if (stringBuilderValueField != null) {
//                try {
//                    char[] value = (char[]) stringBuilderValueField.get(cache);
//                    os.write(value, 0, cache.length());
//                    success = true;
//                } catch (Exception e) {
//                }
//            }
//            if (!success) {
                os.append(cache);
//            }
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

    static abstract class CloneSupportObject<T> implements Cloneable {
        public T clone() {
            try {
                return (T) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    static class SequenceUtils {
        public static void swap(double[] data, int i, int j) {
            double tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
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

    static class Matrix extends CloneSupportObject<Matrix> {
        double[] mat;
        int m;

        public int getHeight() {
            return mat.length / m;
        }

        public int getWidth() {
            return m;
        }

        public void set(int i, int j, double val) {
            mat[i * m + j] = val;
        }

        public double get(int i, int j) {
            return mat[i * m + j];
        }

        public Matrix(Matrix model) {
            m = model.m;
            mat = model.mat.clone();
        }

        public Matrix(double[] mat, int m) {
            assert mat.length % m == 0;
            this.mat = mat;
            this.m = m;
        }

        public Matrix(double[][] mat) {
            this.m = mat[0].length;
            this.mat = new double[mat.length * m];
            for (int i = 0; i < mat.length; i++) {
                for (int j = 0; j < m; j++) {
                    this.mat[i * m + j] = mat[i][j];
                }
            }
        }

        public double[][] toArray() {
            int n = mat.length / m;
            double[][] ans = new double[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    ans[i][j] = mat[i * m + j];
                }
            }
            return ans;
        }

        public Matrix(int n, int m) {
            this.m = m;
            mat = new double[n * m];
        }

        public void fill(int v) {
            Arrays.fill(mat, v);
        }

        public void asStandard() {
            fill(0);
            for (int i = 0; i < mat.length; i += m + 1) {
                mat[i] = 1;
            }
        }

        public static Matrix mul(Matrix a, Matrix b) {
            assert a.getWidth() == b.getWidth();
            int h = a.getHeight();
            int mid = a.getWidth();
            int w = b.getWidth();
            Matrix c = new Matrix(h, w);

            for (int i = 0; i < h; i++) {
                for (int k = 0; k < mid; k++) {
                    for (int j = 0; j < w; j++) {
                        c.mat[i * w + j] += a.mat[i * mid + k] * b.mat[k * w + j];
                    }
                }
            }
            return c;
        }

        public static Matrix pow(Matrix x, long n) {
            if (n == 0) {
                assert x.square();
                Matrix r = new Matrix(x.getHeight(), x.getWidth());
                r.asStandard();
                return r;
            }
            Matrix r = pow(x, n >> 1);
            r = Matrix.mul(r, r);
            if (n % 2 == 1) {
                r = Matrix.mul(r, x);
            }
            return r;
        }

        public static Matrix inverse(Matrix x) {
            assert x.getHeight() == x.getWidth();
            int n = x.getWidth();
            Matrix l = new Matrix(x);
            Matrix r = new Matrix(n, n);
            r.asStandard();
            for (int i = 0; i < n; i++) {
                int maxRow = i;
                for (int j = i; j < n; j++) {
                    if (Math.abs(l.mat[j * n + i]) > Math.abs(l.mat[maxRow * n + i])) {
                        maxRow = j;
                    }
                }

                if (l.mat[maxRow * n + i] == 0) {
                    throw new RuntimeException("Can't inverse singular matrix");
                }
                r.swapRow(i, maxRow);
                l.swapRow(i, maxRow);

                r.divideRow(i, l.mat[i * n + i]);
                l.divideRow(i, l.mat[i * n + i]);

                for (int j = 0; j < n; j++) {
                    if (j == i) {
                        continue;
                    }
                    if (l.mat[j * n + i] == 0) {
                        continue;
                    }
                    double f = l.mat[j * n + i];
                    r.subtractRow(j, i, f);
                    l.subtractRow(j, i, f);
                }
            }
            return r;
        }

        private void swapRow(int i, int j) {
            if (i == j) {
                return;
            }
            for (int ir = i * m, jr = j * m, k = 0; k < m; k++) {
                SequenceUtils.swap(mat, ir + k, jr + k);
            }
        }

        private void subtractRow(int i, int j, double f) {
            for (int ir = i * m, jr = j * m, k = 0; k < m; k++) {
                mat[ir + k] -= mat[jr + k] * f;
            }
        }

        private void divideRow(int i, double f) {
            for (int ir = i * m, k = 0; k < m; k++) {
                mat[ir + k] /= f;
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("\n");
            int h = getHeight();
            int w = getWidth();
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    builder.append(mat[i * w + j]).append(' ');
                }
                builder.append('\n');
            }
            return builder.toString();
        }

        public boolean square() {
            return getHeight() == getWidth();
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debugMatrix(String name, double[][] matrix) {
            if (offline) {
                StringBuilder content = new StringBuilder("\n");
                for (double[] row : matrix) {
                    for (double cell : row) {
                        content.append(cell).append(' ');
                    }
                    content.append(System.lineSeparator());
                }
                debug(name, content);
            }
            return this;
        }

        public Debug debugArray(String name, int[] matrix) {
            if (offline) {
                debug(name, Arrays.toString(matrix));
            }
            return this;
        }

        public Debug debug(String name, int x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
        }

        public Debug debug(String name, Object x) {
            return debug(name, x, empty);
        }

        public Debug debug(String name, Object x, int... indexes) {
            if (offline) {
                if (x == null || !x.getClass().isArray()) {
                    out.append(name);
                    for (int i : indexes) {
                        out.printf("[%d]", i);
                    }
                    out.append("=").append("" + x);
                    out.println();
                } else {
                    indexes = Arrays.copyOf(indexes, indexes.length + 1);
                    if (x instanceof byte[]) {
                        byte[] arr = (byte[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof short[]) {
                        short[] arr = (short[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof boolean[]) {
                        boolean[] arr = (boolean[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof char[]) {
                        char[] arr = (char[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof int[]) {
                        int[] arr = (int[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof float[]) {
                        float[] arr = (float[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof double[]) {
                        double[] arr = (double[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof long[]) {
                        long[] arr = (long[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else {
                        Object[] arr = (Object[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    }
                }
            }
            return this;
        }

    }
}

