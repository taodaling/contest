import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
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
            TaskC solver = new TaskC();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskC {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int[][] boards = new int[n + 1][m + 1];
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    if (in.readChar() == '#') {
                        boards[i][j] = 1;
                    }
                }
            }

            int ans = Math.max(n, m);

            int[][] upRight = new int[n + 1][m + 1];
            for (int j = 1; j <= m; j++) {
                upRight[1][j] = m - j + 1;
            }
            for (int i = 2; i <= n; i++) {
                upRight[i][m] = 1;
                for (int j = m - 1; j >= 1; j--) {
                    upRight[i][j] = DigitUtils
                                    .isEven(boards[i][j] + boards[i - 1][j] + boards[i][j + 1] + boards[i - 1][j + 1])
                                                    ? upRight[i][j + 1] + 1
                                                    : 1;
                }
            }


            int[][] botRight = new int[n + 1][m + 1];
            for (int j = 1; j <= m; j++) {
                botRight[n][j] = m - j + 1;
            }
            for (int i = 1; i < n; i++) {
                botRight[i][m] = 1;
                for (int j = m - 1; j >= 1; j--) {
                    botRight[i][j] = DigitUtils
                                    .isEven(boards[i][j] + boards[i + 1][j] + boards[i][j + 1] + boards[i + 1][j + 1])
                                                    ? botRight[i][j + 1] + 1
                                                    : 1;
                }
            }

            int[][] upUntil = new int[n + 1][m + 1];
            int[][] botUntil = new int[n + 1][m + 1];

            IntDeque deque = new IntDeque(n);
            for (int j = 1; j <= m; j++) {
                deque.clear();
                for (int i = 1; i <= n; i++) {
                    while (!deque.isEmpty() && upRight[deque.peekLast()][j] >= upRight[i][j]) {
                        deque.removeLast();
                    }
                    if (deque.isEmpty()) {
                        upUntil[i][j] = 1;
                    } else {
                        upUntil[i][j] = deque.peekLast();
                    }
                    deque.addLast(i);
                }
            }

            for (int j = 1; j <= m; j++) {
                deque.clear();
                for (int i = n; i >= 1; i--) {
                    while (!deque.isEmpty() && botRight[deque.peekFirst()][j] >= botRight[i][j]) {
                        deque.removeFirst();
                    }
                    if (deque.isEmpty()) {
                        botUntil[i][j] = n;
                    } else {
                        botUntil[i][j] = deque.peekFirst();
                    }
                    deque.addFirst(i);
                }
            }

            for (int i = 2; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    int area1 = (botUntil[i - 1][j] - upUntil[i][j] + 1) * upRight[i][j];
                    int area2 = (botUntil[i - 1][j] - (i - 1) + 1) * upRight[i][j];
                    int area3 = (i - upUntil[i][j] + 1) * upRight[i][j];

                    ans = Math.max(ans, area1);
                    ans = Math.max(ans, area2);
                    ans = Math.max(ans, area3);
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

        public char readChar() {
            skipBlank();
            char c = (char) next;
            next = read();
            return c;
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

        public FastOutput println(int c) {
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
    static class DigitUtils {
        private static final long[] DIGIT_VALUES = new long[19];
        static {
            DIGIT_VALUES[0] = 1;
            for (int i = 1; i < 19; i++) {
                DIGIT_VALUES[i] = DIGIT_VALUES[i - 1] * 10;
            }
        }

        private DigitUtils() {}

        public static boolean isEven(int x) {
            return (x & 1) == 0;
        }

    }
    static class IntDeque {
        int[] data;
        int bpos;
        int epos;
        int cap;

        public IntDeque(int cap) {
            this.cap = cap + 1;
            this.data = new int[this.cap];
        }

        public boolean isEmpty() {
            return epos == bpos;
        }

        public int peekFirst() {
            return data[bpos];
        }

        private int last(int i) {
            return (i == 0 ? cap : i) - 1;
        }

        private int next(int i) {
            int n = i + 1;
            return n == cap ? 0 : n;
        }

        public int peekLast() {
            return data[last(epos)];
        }

        public int removeFirst() {
            int t = bpos;
            bpos = next(bpos);
            return data[t];
        }

        public int removeLast() {
            return data[epos = last(epos)];
        }

        public void addLast(int val) {
            data[epos] = val;
            epos = next(epos);
        }

        public void addFirst(int val) {
            data[bpos = last(bpos)] = val;
        }

        public void clear() {
            bpos = epos = 0;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = bpos; i != epos; i = next(i)) {
                builder.append(data[i]).append(' ');
            }
            return builder.toString();
        }

    }
}

