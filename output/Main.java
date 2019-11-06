import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.LongStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.Stream;
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
        Exam[] exams;
        long x;
        int n;
        PreSum ps;
        long debt;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            x = in.readInt();
            exams = new Exam[n];
            debt = 0;
            for (int i = 0; i < n; i++) {
                exams[i] = new Exam();
                exams[i].b = in.readInt();
                exams[i].l = in.readInt();
                exams[i].r = in.readInt();
                exams[i].profit = exams[i].r * x - exams[i].b * (exams[i].r - exams[i].l);
                debt += exams[i].l * exams[i].b;
            }

            Arrays.sort(exams, (a, b) -> -Long.compare(a.profit, b.profit));
            ps = new PreSum(Arrays.stream(exams).mapToLong(x -> x.profit).toArray());


            check(2540);
            long l = 0;
            long r = x * n;
            while (l < r) {
                long m = (l + r) >> 1;
                if (check(m)) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }

            out.println(l);
        }

        public boolean check(long time) {
            long remain = time % x;
            long block = time / x;
            if (block >= n) {
                return true;
            }

            for (int i = 0; i < n; i++) {
                long debtRemain = debt;
                if (i < block) {
                    debtRemain -= (ps.prefix((int) block) - exams[i].profit);
                } else if (block - 1 >= 0) {
                    debtRemain -= ps.prefix((int) block - 1);
                }
                if (debtRemain <= 0 || debtRemain - (remain - exams[i].b) * exams[i].r - exams[i].b * exams[i].l <= 0
                                || debtRemain - remain * exams[i].l <= 0) {
                    return true;
                }
            }
            return false;
        }

    }
    static class Exam {
        long b;
        long l;
        long r;
        long profit;

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

        public FastOutput println(long c) {
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

        public String toString() {
            return cache.toString();
        }

    }
    static class PreSum {
        private long[] pre;

        public PreSum(long[] a) {
            int n = a.length;
            pre = new long[n];
            pre[0] = a[0];
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] + a[i];
            }
        }

        public PreSum(int[] a) {
            int n = a.length;
            pre = new long[n];
            pre[0] = a[0];
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] + a[i];
            }
        }

        public long prefix(int i) {
            return pre[i];
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
}

