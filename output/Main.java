import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.util.Random;
import java.util.TreeSet;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.io.Writer;
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
            DFindingLines solver = new DFindingLines();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DFindingLines {
        int interval = 19000;
        int inf = (int) 1e8;
        Comparator<Interval> comp = (a, b) -> a.len == b.len ? Integer.compare(a.l, b.l) : -Integer.compare(a.len, b.len);
        PriorityQueue<Interval> ySet = new PriorityQueue<>((int) 1e4, comp);
        List<Integer> yLines = new ArrayList<>();
        TreeSet<Interval> xSet = new TreeSet<>(comp);
        List<Integer> xLines = new ArrayList<>();
        FastInput in;
        FastOutput out;
        RandomWrapper rw = new RandomWrapper(new Random());
        int limit = (int) 3e5;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            this.in = in;
            this.out = out;
            clear();
            for (int i = -inf; i <= inf; i += interval) {
                int l = i;
                int r = Math.min(inf, i + interval - 1);
                check(new Interval(l, r), false, 0);
            }
            xLines.sort(Comparator.naturalOrder());
            int[] xArrays = xLines.stream().filter(x -> exist(x, false, 2))
                    .mapToInt(Integer::intValue).toArray();

            clear();
            ySet.clear();
            int left = -inf;
            for (int x : xArrays) {
                Interval interval = new Interval(left, x - 1);
                if (!interval.isEmpty()) {
                    ySet.add(interval);
                }
                left = x + 1;
            }
            {
                Interval interval = new Interval(left, inf);
                if (!interval.isEmpty()) {
                    ySet.add(interval);
                }
            }

            for (int i = -inf; i <= inf; i += interval) {
                int l = i;
                int r = Math.min(inf, i + interval - 1);
                check(new Interval(l, r), true, 0);
            }
            xLines.sort(Comparator.naturalOrder());
            int[] yArrays = xLines.stream().filter(x -> exist(x, true, 2))
                    .mapToInt(Integer::intValue).toArray();

            out.printf("1 %d %d", xArrays.length, yArrays.length).println();
            for (int x : xArrays) {
                out.append(x).append(' ');
            }
            out.println();
            for (int y : yArrays) {
                out.append(y).append(' ');
            }
            out.println().flush();
        }

        public void clear() {
            ySet.clear();
            xSet.clear();
            yLines.clear();
            xLines.clear();
            ySet.add(new Interval(-inf, inf));
            xSet.add(new Interval(-inf, inf));
        }

        public void check(Interval x, boolean inv, int depth) {
            if (x.isEmpty()) {
                return;
            }

            Interval y = ySet.peek();
            int cx = x.middle();
            int cy = y.middle();
            int dist = ask(cx, cy, inv);

            if (!(cx - dist >= x.l || cx + dist <= x.r)) {
                return;
            }

            if (cx - dist >= x.l && exist(cx - dist, inv, 2)) {
                xLines.add(cx - dist);
                for (Interval interval : x.split(cx - dist)) {
                    check(interval, inv, depth + 1);
                }
                return;
            }

            if (cx + dist <= x.r && exist(cx + dist, inv, 2)) {
                xLines.add(cx + dist);
                for (Interval interval : x.split(cx + dist)) {
                    check(interval, inv, depth + 1);
                }
                return;
            }

            if (cy - dist >= y.l && exist(cy - dist, !inv, 2)) {
                ySet.remove();
                for (Interval interval : y.split(cy - dist)) {
                    if (!interval.isEmpty()) {
                        ySet.add(interval);
                    }
                }
            }

            if (cy + dist <= y.r && exist(cy + dist, !inv, 2)) {
                ySet.remove();
                for (Interval interval : y.split(cy + dist)) {
                    if (!interval.isEmpty()) {
                        ySet.add(interval);
                    }
                }
            }

            //check(x, inv, depth + 1);
        }

        public boolean exist(int x, boolean inv, int time) {
            for (int i = 0; i < time; i++) {
                int y1 = rw.nextInt(-inf, inf);
                if (ask(x, y1, inv) != 0) {
                    return false;
                }
            }
            return true;
        }

        public int ask(int x, int y, boolean inv) {
            if (inv) {
                int tmp = x;
                x = y;
                y = tmp;
            }

            if (limit-- == 0) {
                throw new RuntimeException();
            }

            out.append("0 ").append(x).append(' ').append(y).println().flush();
            return in.readInt();
        }

    }

    static class Interval {
        int l;
        int r;
        int len;

        public int middle() {
            return (r + l) / 2;
        }

        public Interval(int l, int r) {
            this.l = l;
            this.r = r;
            this.len = r - l + 1;
        }

        public boolean isEmpty() {
            return r < l;
        }

        public Interval[] split(int x) {
            return new Interval[]{new Interval(l, x - 1), new Interval(x + 1, r)};
        }

        public String toString() {
            return String.format("[%d, %d]", l, r);
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

        public FastOutput append(String c) {
            cache.append(c);
            return this;
        }

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
            return this;
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

    static class RandomWrapper {
        private Random random;

        public RandomWrapper() {
            this(new Random());
        }

        public RandomWrapper(Random random) {
            this.random = random;
        }

        public int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }
}

