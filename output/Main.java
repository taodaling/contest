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
        int n;
        int[] preGcd;
        int[] sufGcd;
        NumberTheory.Gcd gcd = new NumberTheory.Gcd();

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            int[] a = new int[n];
            preGcd = new int[n];
            sufGcd = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = in.readInt();
            }
            if (n == 1) {
                out.println("Second");
                return;
            }

            boolean success = win(a);

            if (success) {
                out.println("First");
            } else {
                out.println("Second");
            }
        }

        public boolean win(int[] data) {
            for (int i = 0; i < n; i++) {
                if (i == 0) {
                    preGcd[i] = data[0];
                } else {
                    preGcd[i] = gcd.gcd(preGcd[i - 1], data[i]);
                }
            }
            for (int i = n - 1; i >= 0; i--) {
                if (i == n - 1) {
                    sufGcd[i] = data[n - 1];
                } else {
                    sufGcd[i] = gcd.gcd(sufGcd[i + 1], data[i]);
                }
            }
            long sum = 0;
            for (int i = 0; i < n; i++) {
                sum += data[i];
            }

            if ((sum - n) % 2 == 1) {
                return true;
            }

            for (int i = 0; i < n; i++) {
                if (data[i] == 1) {
                    continue;
                }
                int p = i == 0 ? 0 : preGcd[i - 1];
                int s = i == n - 1 ? 0 : sufGcd[i + 1];
                int g = gcd.gcd(data[i] - 1, gcd.gcd(p, s));
                if (g > 1) {
                    data[i]--;
                    for (int j = 0; j < n; j++) {
                        data[j] /= g;
                    }
                    return !win(data);
                }
            }

            return false;
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

        public FastOutput println(String c) {
            cache.append(c).append('\n');
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
    static class NumberTheory {
        public static class Gcd {
            public int gcd(int a, int b) {
                return a >= b ? gcd0(a, b) : gcd0(b, a);
            }

            private int gcd0(int a, int b) {
                return b == 0 ? a : gcd0(b, a % b);
            }

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

