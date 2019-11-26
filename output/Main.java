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
            TaskD solver = new TaskD();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskD {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            long[][] xy = new long[n][2];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 2; j++) {
                    xy[i][j] = in.readInt();
                }
            }
            for (int i = 1; i < n; i++) {
                if (parity(xy[i]) != parity(xy[i - 1])) {
                    out.println(-1);
                    return;
                }
            }

            boolean odd = parity(xy[0]) == 1;
            if (!odd) {
                out.println(35 + 1);
            } else {
                out.println(35);
            }
            for (int i = 35 - 1; i >= 0; i--) {
                out.append((1L << i)).append(' ');
            }
            if (!odd) {
                out.append(1);
            }
            out.println();
            for (int i = 0; i < n; i++) {
                CharList trace = new CharList(36);
                long[] pos = xy[i].clone();
                if (!odd) {
                    pos[0]++;
                }
                solve(34, pos, trace);
                if (!odd) {
                    trace.add('L');
                }
                for (int j = 0; j < trace.size(); j++) {
                    out.append(trace.get(j));
                }
                out.println();
            }
        }

        public void solve(int k, long[] pos, CharList trace) {
            if (k == -1) {
                return;
            }
            long jump = 1L << k;
            long[] up = pos.clone();
            up[1] += jump;
            long[] left = pos.clone();
            left[0] -= jump;
            long[] right = pos.clone();
            right[0] += jump;
            long[] down = pos.clone();
            down[1] -= jump;

            if (distToSrc(up) < jump) {
                trace.add('D');
                solve(k - 1, up, trace);
                return;
            }
            if (distToSrc(down) < jump) {
                trace.add('U');
                solve(k - 1, down, trace);
                return;
            }
            if (distToSrc(left) < jump) {
                trace.add('R');
                solve(k - 1, left, trace);
                return;
            }
            if (distToSrc(right) < jump) {
                trace.add('L');
                solve(k - 1, right, trace);
                return;
            }
        }

        public long distToSrc(long[] xy) {
            return Math.abs(xy[0]) + Math.abs(xy[1]);
        }

        public long parity(long[] xy) {
            return (Math.abs(xy[0]) +
                    Math.abs(xy[1])) % 2;
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(int c) {
            cache.append(c).append('\n');
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

    static class CharList {
        private int size;
        private int cap;
        private char[] data;
        private static final char[] EMPTY = new char[0];

        public CharList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new char[cap];
            }
        }

        public CharList(CharList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public CharList() {
            this(0);
        }

        private void ensureSpace(int req) {
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

        public char get(int i) {
            checkRange(i);
            return data[i];
        }

        public void add(char x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public int size() {
            return size;
        }

        public String toString() {
            return Arrays.toString(Arrays.copyOf(data, size));
        }

    }
}

