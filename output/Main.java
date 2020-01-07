import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
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
            C1MadhouseEasyVersion solver = new C1MadhouseEasyVersion();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class C1MadhouseEasyVersion {
        FastInput in;
        FastOutput out;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            this.in = in;
            this.out = out;

            int n = in.readInt();
            if (n == 1) {
                Map<String, Integer> map = read(1, 1);
                String s = map.keySet().iterator().next();
                answer(s);
                return;
            }

            Map<String, Integer> a = read(1, n);
            Map<String, Integer> b = read(2, n);

            for (String key : b.keySet()) {
                int remain = a.get(key) - b.get(key);
                if (remain == 0) {
                    a.remove(key);
                } else {
                    a.put(key, remain);
                }
            }

            List<String> prefix = new ArrayList<>(a.keySet());
            prefix.sort((x, y) -> x.length() - y.length());

            StringBuilder ans = new StringBuilder(n);
            String last = "";
            for (String s : prefix) {
                ans.append(differ(last, s));
                last = s;
            }

            answer(ans.toString());
        }

        public void answer(String s) {
            out.printf("! %s", s).println().flush();
        }

        public char differ(String shorter, String longer) {
            int differ = 0;
            while (shorter.length() > differ && shorter.charAt(differ) == longer.charAt(differ)) {
                differ++;
            }
            return longer.charAt(differ);
        }

        public Map<String, Integer> read(int l, int r) {
            int n = (r - l + 1);
            int m = (n + 1) * n / 2;
            Map<String, Integer> map = new HashMap<>(m);
            out.printf("? %d %d", l, r).println().flush();
            for (int i = 0; i < m; i++) {
                String s = arrange(in.readString());
                map.put(s, map.getOrDefault(s, 0) + 1);
            }
            return map;
        }

        String arrange(String x) {
            char[] s = x.toCharArray();
            Randomized.shuffle(s);
            Arrays.sort(s);
            return new String(s);
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

    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
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

    static class Randomized {
        private static Random random = new Random();

        public static void shuffle(char[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                char tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

        public static void shuffle(char[] data) {
            shuffle(data, 0, data.length - 1);
        }

    }
}

