import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
            Task solver = new Task();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Task {
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            long[][][] dp = new long[41][10][1 << 10];
            for (int i = 1; i < 10; i++) {
                dp[1][i][1 << i] = 1;
            }

            for (int i = 1; i < 40; i++) {
                for (int j = 0; j < 10; j++) {
                    for (int k = 0; k < 1 << 10; k++) {
                        long way = dp[i][j][k];
                        if (way == 0) {
                            continue;
                        }
                        if (j + 1 < 10) {
                            dp[i + 1][j + 1][k | (1 << (j + 1))] += way;
                        }
                        if (j - 1 >= 0) {
                            dp[i + 1][j - 1][k | (1 << (j - 1))] += way;
                        }
                    }
                }
            }

            long ans = 0;
            for (int i = 1; i <= 40; i++) {
                for (int j = 0; j < 10; j++) {
                    long way = dp[i][j][(1 << 10) - 1];
                    ans += way;

                    if (way > 0) {
                        debug.debug("i", i);
                        debug.debug("j", j);
                        debug.debug("way", way);
                    }
                }
            }

            out.println(ans);
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, int x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, long x) {
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

    }

    static class FastInput {
        private final InputStream is;

        public FastInput(InputStream is) {
            this.is = is;
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(long c) {
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

