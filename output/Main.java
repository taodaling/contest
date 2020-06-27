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
            CBearAndCompany solver = new CBearAndCompany();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CBearAndCompany {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            char[] s = in.readString().toCharArray();
            int V = 0;
            int K = 0;
            for (int i = 0; i < n; i++) {
                if (s[i] == 'V') {
                    V++;
                }
                if (s[i] == 'K') {
                    K++;
                }
            }

            int[][][][] prev = new int[V + 1][K + 1][K + 1][V + 1];
            int[][][][] next = new int[V + 1][K + 1][K + 1][V + 1];

            int[] lastNoWord = new int[n];
            int[] nextNoWord = new int[n];
            for (int i = 0; i < n; i++) {
                lastNoWord[i] = -1;
                if (s[i] != 'V' && s[i] != 'K') {
                    lastNoWord[i] = i;
                } else if (i > 0) {
                    lastNoWord[i] = lastNoWord[i - 1];
                }
            }
            for (int i = n - 1; i >= 0; i--) {
                nextNoWord[i] = -1;
                if (s[i] != 'V' && s[i] != 'K') {
                    nextNoWord[i] = i;
                } else if (i + 1 < n) {
                    nextNoWord[i] = nextNoWord[i + 1];
                }
            }

            int inf = (int) 1e9;
            SequenceUtils.deepFill(prev, inf);
            prev[0][0][0][0] = 0;
            for (int i = 0; i < n; i++) {
                SequenceUtils.deepFill(next, inf);
                for (int j = 0; j <= V; j++) {
                    for (int k = 0; k <= K; k++) {
                        for (int t = 0; t <= K; t++) {
                            for (int a = 0; a <= V; a++) {
                                if (s[i] == 'V') {
                                    //do nothing
                                    if (j + 1 <= V) {
                                        next[j + 1][k][t][a] = Math.min(next[j + 1][k][t][a], prev[j][k][t][a] + t + a);
                                    }
                                    //move first
                                    if (lastNoWord[i] != -1) {
                                        next[j][k][t][a] = Math.min(next[j][k][t][a], prev[j][k][t][a] + j + k + t + a + 1);
                                    }
                                    //add to
                                    if (a + 1 <= V) {
                                        next[j][k][t][a + 1] = Math.min(next[j][k][t][a + 1], prev[j][k][t][a]);
                                    }
                                } else if (s[i] == 'K') {
                                    //move before all V
                                    if (k + 1 <= K) {
                                        next[j][k + 1][t][a] = Math.min(next[j][k + 1][t][a], prev[j][k][t][a] + j + a + t);
                                    }
                                    //move after
                                    if (t + 1 <= K) {
                                        next[j][k][t + 1][a] = Math.min(next[j][k][t + 1][a], prev[j][k][t][a] + a);
                                    }
                                } else {
                                    next[a][t][0][0] = Math.min(next[a][t][0][0], prev[j][k][t][a] + t + a);
                                }
                            }
                        }
                    }
                }

                int[][][][] tmp = next;
                next = prev;
                prev = tmp;
            }

            int ans = inf;
            for (int i = 0; i <= V; i++) {
                for (int j = 0; j <= K; j++) {
                    ans = Math.min(ans, prev[i][j][0][0]);
                }
            }

            out.println(ans);
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
}

