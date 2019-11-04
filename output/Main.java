import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.io.IOException;
import java.util.Deque;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
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
            PrintWriter out = new PrintWriter(outputStream);
            AntsonaCircle solver = new AntsonaCircle();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class AntsonaCircle {
        public void solve(int testNumber, FastInput in, PrintWriter out) {
            int n = in.readInt();
            long l = in.readInt();
            long t = in.readInt();
            l *= 4;
            t = t * 4 + 1;

            Ant[] ants = new Ant[n];
            for (int i = 0; i < n; i++) {
                ants[i] = new Ant();
                ants[i].x = in.readLong() * 4;
                ants[i].w = in.readInt();
                ants[i].id = i;
                ants[i].label = i;
            }

            boolean allSameDirection = true;
            for (int i = 1; i < n; i++) {
                allSameDirection = allSameDirection && ants[i].w == ants[i - 1].w;
            }

            for (int i = 0; i < n; i++) {
                long x = ants[i].x;
                if (ants[i].w == 1) {
                    x += t;
                } else {
                    x -= t;
                }
                ants[i].y = DigitUtils.mod(x, l);
            }

            int diffIndex = n;
            for (int i = 1; i < n; i++) {
                if (ants[i].w == 2 && ants[i - 1].w == 1) {
                    diffIndex = i;
                    break;
                }
            }
            if (diffIndex == n) {
                diffIndex = 0;
            }

            Deque<Ant> cw = new ArrayDeque<>(2 * n);
            Deque<Ant> ccw = new ArrayDeque<>(2 * n);
            for (int i = 0; i < n; i++) {
                Ant ant = ants[DigitUtils.mod(diffIndex + i, n)];
                if (ant.w == 2) {
                    ccw.addLast(ant);
                }
            }

            for (int i = 0; i < n; i++) {
                Ant ant = ants[DigitUtils.mod(diffIndex - i, n)];
                if (ant.w == 1) {
                    cw.addLast(ant);
                }
            }

            int antId = antOn(new ArrayDeque<>(ccw), new ArrayDeque<>(cw), l, l).id;
            Ant mod = antOn(new ArrayDeque<>(ccw), new ArrayDeque<>(cw), t % l, l);
            Arrays.sort(ants, (a, b) -> Long.compare(a.y, b.y));
            int diffLabel = DigitUtils.mod(diffIndex + (diffIndex - antId) * (t / l), n);
            int index = SequenceUtils.indexOf(ants, 0, n - 1, mod);

            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                int label = DigitUtils.mod((i - index) + diffLabel, n);
                ans[label] = position(ants[i].y, ants[i].w, l);
            }

            for (int i = 0; i < n; i++) {
                out.println(ans[i]);
            }
        }

        public int position(long y, int w, long l) {
            if (w == 1) {
                y -= w;
            } else {
                y += w;
            }
            y /= 4;
            return (int) DigitUtils.mod(y, l / 4);
        }

        public Ant antOn(Deque<Ant> ccw, Deque<Ant> cw, long t, long l) {
            long totalMove = t * 2;
            while (!ccw.isEmpty() && !cw.isEmpty()) {
                long distBetween = DigitUtils.mod(ccw.peekFirst().x - (t * 2 - totalMove) - cw.peekFirst().x, l);
                if (distBetween == 0) {
                    distBetween = l;
                }
                if (distBetween > totalMove) {
                    return ccw.removeFirst();
                }
                totalMove -= distBetween;
                ccw.addLast(ccw.removeFirst());

                distBetween = DigitUtils.mod(ccw.peekFirst().x - (t * 2 - totalMove) - cw.peekFirst().x, l);
                if (distBetween == 0) {
                    distBetween = l;
                }
                if (distBetween > totalMove) {
                    return cw.removeFirst();
                }
                cw.addLast(cw.removeFirst());
                totalMove -= distBetween;
            }

            return ccw.isEmpty() ? cw.peekFirst() : ccw.peekFirst();
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

        public long readLong() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            long val = 0;
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

    static class DigitUtils {
        private static final long[] DIGIT_VALUES = new long[19];

        static {
            DIGIT_VALUES[0] = 1;
            for (int i = 1; i < 19; i++) {
                DIGIT_VALUES[i] = DIGIT_VALUES[i - 1] * 10;
            }
        }

        private DigitUtils() {
        }

        public static int mod(long x, int mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return (int) x;
        }

        public static int mod(int x, int mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return x;
        }

        public static long mod(long x, long mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return x;
        }

    }

    static class Ant {
        int id;
        long x;
        int w;
        int label;
        long y;

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

    static class SequenceUtils {
        public static <T> int indexOf(T[] array, int l, int r, T val) {
            for (int i = l; i <= r; i++) {
                if (array[i] == val) {
                    return i;
                }
            }
            return -1;
        }

    }
}

