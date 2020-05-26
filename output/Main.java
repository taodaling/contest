import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.OptionalInt;
import java.io.UncheckedIOException;
import java.util.TreeMap;
import java.util.Map;
import java.io.Closeable;
import java.util.Map.Entry;
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
            EChoosingCarrot solver = new EChoosingCarrot();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class EChoosingCarrot {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] a = new int[n];
            in.populate(a);

            int[] ans = new int[n];

            //odd
            TreeMap<Integer, Integer> odd = new TreeMap<>();
            TreeMap<Integer, Integer> even = new TreeMap<>();
            for (int i = 0; i < n; i++) {
                if (i > 0 && i + 1 < n) {
                    int len = Math.min(i - 1, n - 1 - (i + 1)) * 2 + 3;
                    int val = Math.max(Math.min(a[i], a[i - 1]), Math.min(a[i], a[i + 1]));
                    odd.put(val, Math.max(len, odd.getOrDefault(val, 0)));
                }
                if (i > 0) {
                    int max = Math.max(a[i - 1], a[i]);
                    int evenLen = Math.min(i - 1, n - 1 - i) * 2 + 2;
                    even.put(max, Math.max(evenLen, even.getOrDefault(max, 0)));
                }
            }

            ans[n - 1] = Arrays.stream(a).max().getAsInt();
            for (int i = n - 2; i >= 0; i--) {
                int remain = n - i;
                if (remain % 2 == 1) {
                    while (odd.lastEntry().getValue() < remain) {
                        odd.pollLastEntry();
                    }
                    ans[i] = odd.lastKey();
                } else {
                    while (even.lastEntry().getValue() < remain) {
                        even.pollLastEntry();
                    }
                    ans[i] = even.lastKey();
                }
            }

            for (int x : ans) {
                out.append(x).append(' ');
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

        public void populate(int[] data) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readInt();
            }
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

