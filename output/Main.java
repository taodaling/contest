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
            StrangeNim solver = new StrangeNim();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class StrangeNim {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();


            int xor = 0;
            for (int i = 0; i < n; i++) {
                int stones = in.readInt();
                int k = in.readInt();

                int group = stones / k + 1;
                int lastPosition = k - 1;
                int curPosition = DigitUtils.mod(lastPosition - stones % k, group);
                int dieRound = JosephusCircle.dieTime(group, k, curPosition);
                int numberOnIt = group - dieRound;

                xor ^= numberOnIt;
            }

            if (xor == 0) {
                out.println("Aoki");
            } else {
                out.println("Takahashi");
            }
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
    static class JosephusCircle {
        public static int dieTimeBF(int n, int k, int who) {
            if ((k - 1) % n == who) {
                return 1;
            }
            return dieTimeBF(n - 1, k, DigitUtils.mod(who - k, n)) + 1;
        }

        public static int dieTime(int n, int k, int who) {
            if ((who + 1) % k == 0) {
                return (who + 1) / k;
            }
            int turn = n / k;
            if (turn <= 1) {
                return dieTimeBF(n, k, who);
            }
            int next;
            if (who >= turn * k) {
                next = who - turn * k;
            } else {
                next = n + who - (who + 1) / k - turn * k;
            }
            return dieTime(n - turn, k, DigitUtils.mod(next, n)) + turn;
        }

    }
    static class DigitUtils {
        private static final long[] DIGIT_VALUES = new long[19];
        static {
            DIGIT_VALUES[0] = 1;
            for (int i = 1; i < 19; i++) {
                DIGIT_VALUES[i] = DIGIT_VALUES[i - 1] * 10;
            }
        }

        private DigitUtils() {}

        public static int mod(int x, int mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return x;
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

