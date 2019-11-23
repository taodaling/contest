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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskE {
        FastInput in;
        FastOutput out;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            this.in = in;
            this.out = out;

            IntList question = new IntList(n);
            boolean state;
            for (int i = 1; i <= n; i++) {
                question.add(i);
            }
            state = redMore(question);
            int red = 1;
            int blue = 2;
            int[] color = new int[2 * n + 1];
            int sep;

            int l = 1;
            int r = n;

            while (l < r) {
                sep = (l + r) >>> 1;
                question.clear();
                for (int j = 1; j <= 2 * n; j++) {
                    if (j <= n - sep || j > n && j <= n + sep) {
                        question.add(j);
                    }
                }
                if (state != redMore(question)) {
                    r = sep;
                } else {
                    l = sep + 1;
                }
            }

            sep = r;
            color[n - sep + 1] = state ? red : blue;
            color[n + sep] = 3 - color[n - sep + 1];
            question.clear();
            for (int j = 1; j <= 2 * n; j++) {
                if (j <= n - sep || j > n && j <= n + sep) {
                    question.add(j);
                }
            }

            for (int i = 1; i <= n - sep; i++) {
                replace(question, i, n - sep + 1);
                if (state != redMore(question)) {
                    color[i] = state ? red : blue;
                } else {
                    color[i] = state ? blue : red;
                }
                replace(question, n - sep + 1, i);
            }
            for (int i = n + sep + 1; i <= 2 * n; i++) {
                replace(question, n + sep, i);
                if (state == redMore(question)) {
                    color[i] = state ? red : blue;
                } else {
                    color[i] = state ? blue : red;
                }
                replace(question, i, n + sep);
            }

            for (int i = n - sep + 2; i <= n; i++) {
                replace(question, n + sep, i);
                if (state == redMore(question)) {
                    color[i] = state ? red : blue;
                } else {
                    color[i] = state ? blue : red;
                }
                replace(question, i, n + sep);
            }

            for (int i = n + 1; i < n + sep; i++) {
                replace(question, i, n - sep + 1);
                if (state != redMore(question)) {
                    color[i] = state ? red : blue;
                } else {
                    color[i] = state ? blue : red;
                }
                replace(question, n - sep + 1, i);
            }

            out.append("! ");
            for (int i = 1; i <= 2 * n; i++) {
                out.append(color[i] == red ? "R" : "B");
            }
            out.flush();
        }

        public void replace(IntList x, int a, int b) {
            x.set(x.indexOf(a), b);
        }

        public boolean redMore(IntList a) {
            out.append("? ");
            for (int i = 0; i < a.size(); i++) {
                out.append(a.get(i)).append(' ');
            }
            out.println();
            out.flush();
            return in.readString().equals("Red");
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

    static class IntList {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntList(IntList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntList() {
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

        public int indexOf(int x) {
            for (int i = 0; i < size; i++) {
                if (x == data[i]) {
                    return i;
                }
            }
            return -1;
        }

        public void set(int i, int x) {
            checkRange(i);
            data[i] = x;
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
            if (!(obj instanceof IntList)) {
                return false;
            }
            IntList other = (IntList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
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

        public FastOutput append(String c) {
            cache.append(c);
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
}

