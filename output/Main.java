import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Built using CHelper plug-in Actual solution is at the top
 * 
 * @author daltao
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "daltao", 1 << 27);
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
            TaskH solver = new TaskH();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskH {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] a = new int[n + 1];

            for (int i = 1; i < n; i++) {
                a[i] = in.readInt();
            }

            int preSum = 0;
            for (int i = 3; i < n; i++) {
                preSum += a[i];
            }

            int limit = 1_000_001;
            RotateArray ra = new RotateArray(limit);
            int[] buf = new int[limit];
            for (int j = 0; j < limit; j++) {
                ra.set(j, j);
            }
            for (int i = 3; i < n; i++) {
                for (int j = 1; j <= a[i]; j++) {
                    buf[j] = ra.get(j);
                }
                for (int j = 1; j <= a[i]; j++) {
                    ra.set(-j, buf[j]);
                }
                ra.rotate(-a[i]);
            }

            int m = in.readInt();
            for (int i = 0; i < m; i++) {
                int x = in.readInt();
                int ans;
                if (x >= limit) {
                    ans = x - preSum;
                } else {
                    ans = ra.get(x);
                }
                out.println(ans + a[1] - a[2]);
            }
        }

    }
    static class DigitUtils {
        private DigitUtils() {}

        public static int mod(int x, int mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return x;
        }

    }
    static class RotateArray {
        private int offset;
        private int[] data;
        private int n;

        public RotateArray(int cap) {
            data = new int[cap];
            n = cap;
        }

        public int get(int i) {
            return data[DigitUtils.mod(i + offset, n)];
        }

        public void set(int i, int v) {
            data[DigitUtils.mod(i + offset, n)] = v;
        }

        public void rotate(int x) {
            offset += x;
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
                    throw new RuntimeException(e);
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
        private StringBuilder cache = new StringBuilder(1 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput println(int c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput flush() {
            try {
                os.append(cache);
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

    }
}

