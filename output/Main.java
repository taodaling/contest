import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            P5308COCI2019Quiz solver = new P5308COCI2019Quiz();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P5308COCI2019Quiz {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int k = in.readInt();

            double[] dp = new double[n + 1];
            int[] times = new int[n + 1];
            DoubleGeqSlopeOptimizer optimizer = new DoubleGeqSlopeOptimizer(n + 1);

            WQSBinarySearch bs = new WQSBinarySearch() {
                double best;
                int time;


                public double getBest() {
                    return best;
                }


                public int getTime() {
                    return time;
                }


                public void check(double costPerOperation) {
                    time = 0;
                    times[0] = 0;
                    dp[0] = 0;
                    optimizer.clear();
                    optimizer.add(0, 0, 0);
                    for (int i = 1; i <= n; i++) {
                        int j = optimizer.getBestChoice(1D / i);
                        times[i] = times[j] + 1;
                        dp[i] = 1 + dp[j] - (double) j / i - costPerOperation;
                        optimizer.add(dp[i], i, i);
                    }
                    best = dp[n];
                    time = times[n];
                }
            };

            double ans = bs.search(0, 1, 60, k, true);
            out.printf("%.9f", ans);
        }

    }

    static class DoubleGeqSlopeOptimizer {
        Deque<DoubleGeqSlopeOptimizer.Point> deque;

        public DoubleGeqSlopeOptimizer() {
            this(0);
        }

        public DoubleGeqSlopeOptimizer(int exp) {
            deque = new ArrayDeque<>(exp);
        }

        private double slope(DoubleGeqSlopeOptimizer.Point a, DoubleGeqSlopeOptimizer.Point b) {
            if (b.x == a.x) {
                if (b.y == a.y) {
                    return 0;
                } else if (b.y > a.y) {
                    return 1e50;
                } else {
                    return 1e-50;
                }
            }
            return (b.y - a.y) / (b.x - a.x);
        }

        public DoubleGeqSlopeOptimizer.Point add(double y, double x, int id) {
            DoubleGeqSlopeOptimizer.Point t1 = new DoubleGeqSlopeOptimizer.Point(x, y, id);
            while (deque.size() >= 2) {
                DoubleGeqSlopeOptimizer.Point t2 = deque.removeLast();
                DoubleGeqSlopeOptimizer.Point t3 = deque.peekLast();
                if (slope(t3, t2) > slope(t2, t1)) {
                    deque.addLast(t2);
                    break;
                }
            }
            deque.addLast(t1);
            return t1;
        }

        public int getBestChoice(double s) {
            while (deque.size() >= 2) {
                DoubleGeqSlopeOptimizer.Point h1 = deque.removeFirst();
                DoubleGeqSlopeOptimizer.Point h2 = deque.peekFirst();
                if (slope(h2, h1) < s) {
                    deque.addFirst(h1);
                    break;
                }
            }
            return deque.peekFirst().id;
        }

        public void clear() {
            deque.clear();
        }

        private static class Point {
            final double x;
            final double y;
            final int id;

            private Point(double x, double y, int id) {
                this.x = x;
                this.y = y;
                this.id = id;
            }

        }

    }

    static abstract class WQSBinarySearch {
        protected abstract double getBest();

        protected abstract int getTime();

        protected abstract void check(double costPerOperation);

        public double search(double l, double r, int round, int k, boolean top) {
            if (l > r || round <= 0) {
                throw new IllegalArgumentException();
            }
            double m = 0;
            while (round-- > 0) {
                m = (l + r) / 2;
                check(m);
                if (getTime() == k) {
                    break;
                }
                if (getTime() > k == top) {
                    l = m;
                } else {
                    r = m;
                }
            }
            return getBest() + m * k;
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

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
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

