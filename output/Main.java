import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.io.UncheckedIOException;
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
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            LUOGU4118 solver = new LUOGU4118();
            int testCount = Integer.parseInt(in.next());
            for (int i = 1; i <= testCount; i++)
                solver.solve(i, in, out);
            out.close();
        }
    }

    static class LUOGU4118 {
        LongPollardRho pollardRho = new LongPollardRho();

        public void solve(int testNumber, FastInput in, FastOutput out) {
            System.err.println(testNumber);
            long n = in.readLong();
            Map<Long, Long> map = pollardRho.findAllFactors(n);
            long max = 1;
            for (long key : map.keySet()) {
                max = Math.max(max, key);
            }
            if (max == 1) {
                out.println(1);
                return;
            }
            if (max == n) {
                out.println("Prime");
                return;
            }
            out.println(max);
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

        public String next() {
            return readString();
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

        public FastOutput println(String c) {
            cache.append(c).append('\n');
            return this;
        }

        public FastOutput println(int c) {
            cache.append(c).append('\n');
            return this;
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

    static class LongMillerRabin {
        ILongModular modular;
        LongPower power;
        Random random = new Random();

        public boolean mr(long n, int s) {
            if (n <= 1) {
                return false;
            }
            if (n == 2) {
                return true;
            }
            if (n % 2 == 0) {
                return false;
            }
            modular = ILongModular.getInstance(n);
            power = new LongPower(modular);
            for (int i = 0; i < s; i++) {
                long x = (long) (random.nextDouble() * (n - 2) + 2);
                if (!mr0(x, n)) {
                    return false;
                }
            }
            return true;
        }

        private boolean mr0(long x, long n) {
            long exp = n - 1;
            while (true) {
                long y = power.pow(x, exp);
                if (y != 1 && y != n - 1) {
                    return false;
                }
                if (y != 1 || exp % 2 == 1) {
                    break;
                }
                exp = exp / 2;
            }
            return true;
        }

    }

    static class LongPollardRho {
        LongMillerRabin mr = new LongMillerRabin();
        ILongModular modular;
        Random random = new Random();

        public long findFactor(long n) {
            if (mr.mr(n, 3)) {
                return n;
            }
            modular = ILongModular.getInstance(n);
            while (true) {
                long f = findFactor0((long) (random.nextDouble() * n), (long) (random.nextDouble() * n), n);
                if (f != -1) {
                    return f;
                }
            }
        }

        private long findFactor0(long x, long c, long n) {
            long xi = x;
            long xj = x;
            int j = 2;
            int i = 1;
            while (i < n) {
                i++;
                xi = modular.plus(modular.mul(xi, xi), c);
                long g = GCDs.gcd(n, Math.abs(xi - xj));
                if (g != 1 && g != n) {
                    return g;
                }
                if (i == j) {
                    j = j << 1;
                    xj = xi;
                }
            }
            return -1;
        }

        public Map<Long, Long> findAllFactors(long n) {
            Map<Long, Long> map = new HashMap();
            findAllFactors(map, n);
            return map;
        }

        private void findAllFactors(Map<Long, Long> map, long n) {
            if (n == 1) {
                return;
            }
            long f = findFactor(n);
            if (f == n) {
                Long value = map.get(f);
                if (value == null) {
                    value = 1L;
                }
                map.put(f, value * f);
                return;
            }
            findAllFactors(map, f);
            findAllFactors(map, n / f);
        }

    }

    static interface ILongModular {
        long plus(long a, long b);

        long mul(long a, long b);

        static ILongModular getInstance(long mod) {
            //return new LongModularDanger(mod);
            return mod <= (1L << 54) ? new LongModularDanger(mod) : new LongModular(mod);
        }

    }

    static class GCDs {
        private GCDs() {
        }

        public static long gcd(long a, long b) {
            return a >= b ? gcd0(a, b) : gcd0(b, a);
        }

        private static long gcd0(long a, long b) {
            return b == 0 ? a : gcd0(b, a % b);
        }

    }

    static class LongModular implements ILongModular {
        final long m;

        public LongModular(long m) {
            this.m = m;
        }

        public long mul(long a, long b) {
            return b == 0 ? 0 : ((mul(a, b >> 1) << 1) % m + a * (b & 1)) % m;
        }

        public long plus(long a, long b) {
            return valueOf(a + b);
        }

        public long valueOf(long a) {
            a %= m;
            if (a < 0) {
                a += m;
            }
            return a;
        }

    }

    static class LongPower {
        final ILongModular modular;

        public LongPower(ILongModular modular) {
            this.modular = modular;
        }

        public long pow(long x, long n) {
            if (n == 0) {
                return 1;
            }
            long r = pow(x, n >> 1);
            r = modular.mul(r, r);
            if ((n & 1) == 1) {
                r = modular.mul(r, x);
            }
            return r;
        }

    }

    static class LongModularDanger implements ILongModular {
        final long m;

        public LongModularDanger(long m) {
            this.m = m;
        }

        public long mul(long a, long b) {
            return DigitUtils.mulMod(a, b, m);
        }

        public long plus(long a, long b) {
            return valueOf(a + b);
        }

        public long valueOf(long a) {
            a %= m;
            if (a < 0) {
                a += m;
            }
            return a;
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static long round(double x) {
            if (x >= 0) {
                return (long) (x + 0.5);
            } else {
                return (long) (x - 0.5);
            }
        }

        public static long mod(long x, long mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return x;
        }

        public static long mulMod(long a, long b, long mod) {
            long k = DigitUtils.round((double) a / mod * b);
            return DigitUtils.mod(a * b - k * mod, mod);
        }

    }
}

