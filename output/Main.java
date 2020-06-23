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
            DeterminantOfSparseMatrix solver = new DeterminantOfSparseMatrix();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DeterminantOfSparseMatrix {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int k = in.readInt();
            Modular mod = new Modular(998244353);
            ModSparseMatrix mat = new ModSparseMatrix(n, k);
            for (int i = 0; i < k; i++) {
                int a = in.readInt();
                int b = in.readInt();
                int v = in.readInt();
                mat.set(i, a, b, v);
            }

            int ans = mat.determinant(mod);
            out.println(ans);
        }

    }

    static class ModSparseMatrix {
        private int[] x;
        private int[] y;
        private int[] elements;
        private int n;

        public ModSparseMatrix(ModMatrix mat) {
            this.n = mat.n;
            int m = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (mat.mat[i][j] > 0) {
                        m++;
                    }
                }
            }
            x = new int[m];
            y = new int[m];
            elements = new int[m];
            int cur = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (mat.mat[i][j] > 0) {
                        x[cur] = i;
                        y[cur] = j;
                        elements[cur] = mat.mat[i][j];
                        cur++;
                    }
                }
            }
        }

        public ModSparseMatrix(int n, int m) {
            this.n = n;
            x = new int[m];
            y = new int[m];
            elements = new int[m];
        }

        public void set(int i, int x, int y, int element) {
            this.x[i] = x;
            this.y[i] = y;
            this.elements[i] = element;
        }

        public void rightMul(int[] v, int[] output, Modular mod) {
            Arrays.fill(output, 0);
            for (int j = 0; j < elements.length; j++) {
                output[x[j]] = mod.plus(output[x[j]], mod.mul(elements[j], v[y[j]]));
            }
        }

        public IntegerList getMinimalPolynomialByRandom(Modular mod) {
            int modVal = mod.getMod();
            int m = x.length;
            int[] u = new int[n];
            int[] v = new int[n];
            int[] next = new int[n];
            for (int i = 0; i < n; i++) {
                u[i] = RandomWrapper.INSTANCE.nextInt(1, modVal - 1);
                v[i] = RandomWrapper.INSTANCE.nextInt(1, modVal - 1);
            }

            ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, 2 * n);
            for (int i = 0; i < 2 * n; i++) {
                long ai = 0;
                for (int j = 0; j < n; j++) {
                    ai += (long) u[j] * v[j] % modVal;
                }
                ai %= modVal;
                lfsr.add((int) ai);
                rightMul(v, next, mod);
                int[] tmp = next;
                next = v;
                v = tmp;
            }

            IntegerList polynomials = new IntegerList(lfsr.length() + 1);
            for (int i = lfsr.length(); i >= 1; i--) {
                polynomials.add(mod.valueOf(-lfsr.codeAt(i)));
            }
            polynomials.add(1);
            return polynomials;
        }

        public int determinant(Modular mod) {
            IntegerList minPoly = getMinimalPolynomialByRandom(mod);
            int ans = minPoly.get(0);
            if (n % 2 == 1) {
                ans = mod.valueOf(-ans);
            }
            return ans;
        }

    }

    static class ModLinearFeedbackShiftRegister {
        private IntegerList cm;
        int m = -1;
        int dm;
        private IntegerList cn;
        private IntegerList buf;
        private IntegerList seq;
        private Modular mod;
        private Power pow;

        public ModLinearFeedbackShiftRegister(Modular mod, int cap) {
            cm = new IntegerList(cap + 1);
            cn = new IntegerList(cap + 1);
            seq = new IntegerList(cap + 1);
            buf = new IntegerList(cap + 1);
            cn.add(1);

            this.mod = mod;
            this.pow = new Power(mod);
        }

        public ModLinearFeedbackShiftRegister(Modular mod) {
            this(mod, 0);
        }

        private int estimateDelta() {
            int n = seq.size() - 1;
            int ans = 0;
            int[] cnData = cn.getData();
            int[] seqData = seq.getData();
            for (int i = 0, until = cn.size(); i < until; i++) {
                ans = mod.plus(ans, mod.mul(cnData[i], seqData[n - i]));
            }
            return ans;
        }

        public void add(int x) {
            x = mod.valueOf(x);
            int n = seq.size();

            seq.add(x);
            int dn = estimateDelta();
            if (dn == 0) {
                return;
            }

            if (m < 0) {
                cm.clear();
                cm.addAll(cn);
                dm = dn;
                m = n;

                cn.expandWith(0, n + 2);
                return;
            }

            int ln = cn.size() - 1;
            int len = Math.max(ln, n + 1 - ln);
            buf.clear();
            buf.addAll(cn);
            buf.expandWith(0, len + 1);

            int factor = mod.mul(dn, pow.inverseByFermat(dm));

            int[] bufData = buf.getData();
            int[] cmData = cm.getData();
            for (int i = n - m, until = n - m + cm.size(); i < until; i++) {
                bufData[i] = mod.subtract(bufData[i], mod.mul(factor, cmData[i - (n - m)]));
            }

            if (cn.size() < buf.size()) {
                IntegerList tmp = cm;
                cm = cn;
                cn = tmp;
                m = n;
                dm = dn;
            }
            {
                IntegerList tmp = cn;
                cn = buf;
                buf = tmp;
            }


        }

        public int length() {
            return cn.size() - 1;
        }

        public String toString() {
            return cn.toString();
        }

        public int codeAt(int i) {
            return mod.valueOf(-cn.get(i));
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

    static class ModMatrix {
        int[][] mat;
        int n;
        int m;

        public ModMatrix(ModMatrix model) {
            n = model.n;
            m = model.m;
            mat = new int[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    mat[i][j] = model.mat[i][j];
                }
            }
        }

        public ModMatrix(int n, int m) {
            this.n = n;
            this.m = m;
            mat = new int[n][m];
        }

        public ModMatrix(int[][] mat) {
            if (mat.length == 0 || mat[0].length == 0) {
                throw new IllegalArgumentException();
            }
            this.n = mat.length;
            this.m = mat[0].length;
            this.mat = mat;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    builder.append(mat[i][j]).append(' ');
                }
                builder.append('\n');
            }
            return builder.toString();
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

    static class Power implements InverseNumber {
        final Modular modular;

        public Power(Modular modular) {
            this.modular = modular;
        }

        public int pow(int x, int n) {
            if (n == 0) {
                return modular.valueOf(1);
            }
            long r = pow(x, n >> 1);
            r = modular.valueOf(r * r);
            if ((n & 1) == 1) {
                r = modular.valueOf(r * x);
            }
            return (int) r;
        }

        public int inverseByFermat(int x) {
            return pow(x, modular.m - 2);
        }

    }

    static interface InverseNumber {
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

        public void expandWith(int x, int len) {
            ensureSpace(len);
            while (size < len) {
                data[size++] = x;
            }
        }

        public int size() {
            return size;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public void clear() {
            size = 0;
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

    static class Modular {
        int m;

        public int getMod() {
            return m;
        }

        public Modular(int m) {
            this.m = m;
        }

        public Modular(long m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public Modular(double m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public int valueOf(int x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return x;
        }

        public int valueOf(long x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return (int) x;
        }

        public int mul(int x, int y) {
            return valueOf((long) x * y);
        }

        public int plus(int x, int y) {
            return valueOf(x + y);
        }

        public int subtract(int x, int y) {
            return valueOf(x - y);
        }

        public String toString() {
            return "mod " + m;
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
}

