import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
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
            FOpposition solver = new FOpposition();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FOpposition {
        FastInput in;
        FastOutput out;
        int remain;
        char[] s = new char[(int) 2e5];
        int n;
        List<Integer>[] goods = new List[4];
        String target = "LOVE";
        String[] pattern = new String[]{
                "?OVE", "L?VE", "LO?E", "LOV?"
        };
        int scan;

        {
            for (int i = 0; i < 4; i++) {
                goods[i] = new ArrayList<>();
            }
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readString(s, 0);
            for (int i = 0; i < n; i++) {
                if (s[i] != '?') {
                    continue;
                }
                remain++;
                for (int j = 0; j < 4; j++) {
                    if (equal(i - j, pattern[j])) {
                        goods[j].add(i);
                    }
                }
            }
            int t = in.readInt();
            this.in = in;
            this.out = out;

            if (t == 1) {
                first();
            } else {
                second();
            }
        }

        public boolean equal(int i, String cand) {
            for (int j = 0; j < cand.length(); j++) {
                if (i + j < 0 || i + j >= n || cand.charAt(j) != s[i + j]) {
                    return false;
                }
            }
            return true;
        }

        public int next() {
            while (s[scan] != '?') {
                scan++;
            }
            return scan++;
        }

        public void apply(int p, char x) {
            assert s[p] == '?';
            s[p] = x;
            remain--;

            for (int i = p - 3; i <= p + 3; i++) {
                if (i < 0 || i >= n || s[i] != '?') {
                    continue;
                }
                for (int j = 0; j < 4; j++) {
                    if (equal(i - j, pattern[j])) {
                        goods[j].add(i);
                    }
                }
            }

        }

        public void operate(int p, char x) {
            apply(p, x);
            out.append(p + 1).append(' ').append(x).println().flush();
            respond();
        }

        public void respond() {
            if (remain > 0) {
                int t = in.ri() - 1;
                char v = in.rc();
                apply(t, v);
            }
        }

        public void first() {
            while (remain > 0) {
                boolean find = false;
                for (int i = 0; i < 4; i++) {
                    while (!goods[i].isEmpty() && s[CollectionUtils.peek(goods[i])] != '?') {
                        CollectionUtils.pop(goods[i]);
                    }
                    if (goods[i].isEmpty()) {
                        continue;
                    }
                    find = true;
                    operate(CollectionUtils.pop(goods[i]), target.charAt(i));
                    break;
                }
                if (!find) {
                    operate(next(), 'L');
                }
            }

        }

        public void second() {
            respond();
            while (remain > 0) {
                boolean find = false;
                for (int i = 0; i < 4; i++) {
                    while (!goods[i].isEmpty() && s[CollectionUtils.peek(goods[i])] != '?') {
                        CollectionUtils.pop(goods[i]);
                    }
                    if (goods[i].isEmpty()) {
                        continue;
                    }
                    find = true;
                    operate(CollectionUtils.pop(goods[i]), target.charAt(i) == 'V' ? 'E' : 'V');
                    break;
                }
                if (!find) {
                    operate(next(), 'E');
                }
            }
        }

    }

    static class CollectionUtils {
        public static <T> T pop(List<T> list) {
            return list.remove(list.size() - 1);
        }

        public static <T> T peek(List<T> list) {
            return list.get(list.size() - 1);
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 1 << 13;
        private final Writer os;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        private void afterWrite() {
            if (cache.length() < THRESHOLD) {
                return;
            }
            flush();
        }

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(String c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println() {
            return append(System.lineSeparator());
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

        public int ri() {
            return readInt();
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

        public char rc() {
            return readChar();
        }

        public char readChar() {
            skipBlank();
            char c = (char) next;
            next = read();
            return c;
        }

        public int readString(char[] data, int offset) {
            skipBlank();

            int originalOffset = offset;
            while (next > 32) {
                data[offset++] = (char) next;
                next = read();
            }

            return offset - originalOffset;
        }

    }
}

