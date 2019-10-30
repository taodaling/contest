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
            TreeBurning solver = new TreeBurning();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TreeBurning {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int l = in.readInt();
            int n = in.readInt();
            int[] x = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                x[i] = in.readInt();
            }

            long[] clockwisePreSum = new long[n + 2];
            long[] countClockwisePreSum = new long[n + 2];
            for (int i = 1; i <= n; i++) {
                clockwisePreSum[i] = clockwisePreSum[i - 1] + x[i];
            }
            for (int i = n; i >= 1; i--) {
                countClockwisePreSum[i] = countClockwisePreSum[i + 1] + (l - x[i]);
            }

            long ans = 0;
            for (int i = 1; i <= n; i++) {
                int lCnt = i - 1;
                int rCnt = n - i;
                int oneCnt = Math.min(lCnt, rCnt);

                // same
                ans = Math.max(ans,
                                Math.max(x[i], l - x[i]) + (clockwisePreSum[i - 1] - clockwisePreSum[i - 1 - oneCnt]
                                                + countClockwisePreSum[i + 1] - countClockwisePreSum[i + 1 + oneCnt])
                                                * 2);

                // leftMore
                if (lCnt > 0) {
                    int rOneCnt = Math.min(lCnt - 1, rCnt);
                    int lOneCnt = rOneCnt + 1;
                    ans = Math.max(ans, l - x[i] + (clockwisePreSum[i - 1] - clockwisePreSum[i - 1 - lOneCnt]
                                    + countClockwisePreSum[i + 1] - countClockwisePreSum[i + 1 + rOneCnt]) * 2);
                }

                // rightMore
                if (rCnt > 0) {
                    int lOneCnt = Math.min(lCnt, rCnt - 1);
                    int rOneCnt = lOneCnt + 1;
                    ans = Math.max(ans, x[i] + (clockwisePreSum[i - 1] - clockwisePreSum[i - 1 - lOneCnt]
                                    + countClockwisePreSum[i + 1] - countClockwisePreSum[i + 1 + rOneCnt]) * 2);
                }
            }

            out.println(ans);
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

        public FastOutput println(long c) {
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

    }
}

