import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.TreeSet;
import java.nio.charset.StandardCharsets;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.util.Comparator;
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
            HRedAndBlueLamps solver = new HRedAndBlueLamps();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class HRedAndBlueLamps {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.ri();
            int r = in.ri();
            int[] a = in.ri(n - 1);
            r = Math.min(r, n - r);
            int even = n / 2;
            if (r >= even) {
                long ans = 0;
                for (int x : a) {
                    ans += x;
                }
                out.println(ans);
                return;
            }
            a = Arrays.copyOf(a, n);
            long[] weights = new long[n];
            for (int i = 0; i < n; i++) {
                weights[i] = a[i] + a[(i + 1) % n];
            }
            PlantTreeProblem pt = new PlantTreeProblem(weights, r);
            long ans = pt.getAnswer();
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

        public int ri() {
            return readInt();
        }

        public int[] ri(int n) {
            int[] ans = new int[n];
            populate(ans);
            return ans;
        }

        public int readInt() {
            boolean rev = false;

            skipBlank();
            if (next == '+' || next == '-') {
                rev = next == '-';
                next = read();
            }

            int val = 0;
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }

            return rev ? val : -val;
        }

    }

    static class IntegerBIT {
        private int[] data;
        private int n;

        public IntegerBIT(int n) {
            this.n = n;
            data = new int[n + 1];
        }

        public int query(int i) {
            i = Math.min(i, n);
            int sum = 0;
            for (; i > 0; i -= i & -i) {
                sum += data[i];
            }
            return sum;
        }

        public void update(int i, int mod) {
            if (i <= 0) {
                return;
            }
            for (; i <= n; i += i & -i) {
                data[i] += mod;
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                builder.append(query(i) - query(i - 1)).append(' ');
            }
            return builder.toString();
        }

    }

    static class PlantTreeProblem {
        IntegerBIT bit;
        long ans;
        int n;

        public PlantTreeProblem(long[] weights, int m) {
            n = weights.length;
            bit = new IntegerBIT(n + 1);
            TreeSet<PlantTreeProblem.Node> set = new TreeSet<>(PlantTreeProblem.Node.sortByW);
            PlantTreeProblem.Node[] nodes = new PlantTreeProblem.Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new PlantTreeProblem.Node();
                nodes[i].w = weights[i];
                nodes[i].l = nodes[i].r = i;
            }
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    nodes[i].prev = nodes[i - 1];
                } else {
                    nodes[i].prev = nodes[n - 1];
                }
                if (i + 1 < n) {
                    nodes[i].next = nodes[i + 1];
                } else {
                    nodes[i].next = nodes[0];
                }
            }
            set.addAll(Arrays.asList(nodes));

            for (int i = 0; i < m; i++) {
                PlantTreeProblem.Node last = set.pollLast();
                ans += last.w;
                if (last.l <= last.r) {
                    bit.update(last.l + 1, 1);
                    bit.update(last.r + 2, 1);
                } else {
                    bit.update(1, 1);
                    bit.update(last.r + 2, 1);
                    bit.update(last.l + 1, n + 1);
                    bit.update(n, 1);
                }

                if (last.next == last.prev) {
                    continue;
                }
                PlantTreeProblem.Node prev = last.prev;
                set.remove(last.next);
                set.remove(last.prev);
                prev.r = last.next.r;
                prev.w += last.next.w - last.w;
                prev.next = last.next.next;
                prev.next.prev = prev;
                set.add(prev);
            }
        }

        public long getAnswer() {
            return ans;
        }

        public boolean chooseOrNot(int i) {
            return bit.query(i + 1) % 2 == 1;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < n; i++) {
                builder.append(chooseOrNot(i)).append(',');
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }

        private static class Node {
            PlantTreeProblem.Node prev;
            PlantTreeProblem.Node next;
            long w;
            int l;
            int r;
            static Comparator<PlantTreeProblem.Node> sortByW = (a, b) -> a.w == b.w ? a.l - b.l : Long.compare(a.w, b.w);

            public String toString() {
                return String.format("[%d, %d] => %d", l, r, w);
            }

        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private static final int THRESHOLD = 32 << 10;
        private OutputStream writer;
        private StringBuilder cache = new StringBuilder(THRESHOLD * 2);
        private static Field stringBuilderValueField;
        private char[] charBuf = new char[THRESHOLD * 2];
        private byte[] byteBuf = new byte[THRESHOLD * 2];

        static {
            try {
                stringBuilderValueField = StringBuilder.class.getSuperclass().getDeclaredField("value");
                stringBuilderValueField.setAccessible(true);
            } catch (Exception e) {
                stringBuilderValueField = null;
            }
            stringBuilderValueField = null;
        }

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

        public FastOutput(OutputStream writer) {
            this.writer = writer;
        }

        public FastOutput append(char c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput append(long c) {
            cache.append(c);
            afterWrite();
            return this;
        }

        public FastOutput println(long c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append('\n');
        }

        public FastOutput flush() {
            try {
                if (stringBuilderValueField != null) {
                    try {
                        byte[] value = (byte[]) stringBuilderValueField.get(cache);
                        writer.write(value, 0, cache.length());
                    } catch (Exception e) {
                        stringBuilderValueField = null;
                    }
                }
                if (stringBuilderValueField == null) {
                    int n = cache.length();
                    if (n > byteBuf.length) {
                        //slow
                        writer.write(cache.toString().getBytes(StandardCharsets.UTF_8));
//                writer.append(cache);
                    } else {
                        cache.getChars(0, n, charBuf, 0);
                        for (int i = 0; i < n; i++) {
                            byteBuf[i] = (byte) charBuf[i];
                        }
                        writer.write(byteBuf, 0, n);
                    }
                }
                writer.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                writer.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }
}

