import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.io.UncheckedIOException;
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
            TaskF solver = new TaskF();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskF {
        int[] allPrimes;
        int k = 62;
        Modular mod = new Modular(1e9 + 7);
        Segment sumSeg;
        int n;
        Power power = new Power(mod);
        int[] buf = new int[62];

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            int q = in.readInt();
            EulerSieve sieve = new EulerSieve(300);
            allPrimes = new int[sieve.getPrimeCount()];
            for (int i = 0; i < allPrimes.length; i++) {
                allPrimes[i] = sieve.get(i);
            }
            k = allPrimes.length;
            sumSeg = new Segment(1, n);
            for (int i = 1; i <= n; i++) {
                mul(i, i, in.readInt());
            }

            char[] cmd = new char[100];
            for (int i = 0; i < q; i++) {
                in.readString(cmd, 0);
                if (cmd[0] == 'M') {
                    mul(in.readInt(), in.readInt(), in.readInt());
                } else {
                    int ans = query(in.readInt(), in.readInt());
                    out.println(ans);
                }
            }
        }

        private void prepareBuf() {
            Arrays.fill(buf, 0);
        }

        public void mul(int l, int r, int x) {
            prepareBuf();
            for (int i = 0; i < k; i++) {
                int y = x;
                while (y % allPrimes[i] == 0) {
                    buf[i]++;
                    y /= allPrimes[i];
                }
            }
            sumSeg.update(l, r, 1, n, buf);
        }

        public int query(int l, int r) {
            prepareBuf();
            sumSeg.query(l, r, 1, n, buf);
            int ans = 1;
            for (int i = 0; i < k; i++) {
                if (buf[i] == 0) {
                    continue;
                }
                ans = mod.mul(ans, power.pow(allPrimes[i], buf[i] - 1));
                ans = mod.mul(ans, mod.subtract(allPrimes[i], 1));
            }
            return ans;
        }

    }

    static class Modular {
        int m;

        public Modular(int m) {
            this.m = m;
        }

        public Modular(long m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public Modular(double m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public int valueOf(int x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return x;
        }

        public int valueOf(long x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return (int) x;
        }

        public int mul(int x, int y) {
            return valueOf((long) x * y);
        }

        public int plus(int x, int y) {
            return valueOf(x + y);
        }

        public int subtract(int x, int y) {
            return valueOf(x - y);
        }

        public String toString() {
            return "mod " + m;
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

    static class Segment implements Cloneable {
        private static final int K = 62;
        private static Modular mod = new Modular(1e9 + 7);
        private Segment left;
        private Segment right;
        private int size;
        private int[] sum = new int[K];
        private int[] dirty = new int[K];

        public void setDirty(int[] d) {
            for (int i = 0; i < K; i++) {
                if (d[i] == 0) {
                    continue;
                }
                dirty[i] = mod.plus(dirty[i], d[i]);
                sum[i] = mod.plus(sum[i], mod.mul(size, d[i]));
            }
        }

        public void pushUp() {
            size = left.size + right.size;
            for (int i = 0; i < K; i++) {
                sum[i] = mod.plus(left.sum[i], right.sum[i]);
            }
        }

        public void pushDown() {
            left.setDirty(dirty);
            right.setDirty(dirty);
            Arrays.fill(dirty, 0);
        }

        public Segment(int l, int r) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m);
                right = new Segment(m + 1, r);
                pushUp();
            } else {
                size = 1;
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, int[] d) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                setDirty(d);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, d);
            right.update(ll, rr, m + 1, r, d);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, int[] ans) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                for (int i = 0; i < K; i++) {
                    ans[i] = mod.plus(ans[i], sum[i]);
                }
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.query(ll, rr, l, m, ans);
            right.query(ll, rr, m + 1, r, ans);
        }

    }

    static class Power {
        final Modular modular;

        public Power(Modular modular) {
            this.modular = modular;
        }

        public int pow(int x, long n) {
            if (n == 0) {
                return modular.valueOf(1);
            }
            long r = pow(x, n >> 1);
            r = modular.valueOf(r * r);
            if ((n & 1) == 1) {
                r = modular.valueOf(r * x);
            }
            return (int) r;
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

        public String toString() {
            return cache.toString();
        }

    }

    static class EulerSieve {
        private int[] primes;
        private boolean[] isComp;
        private int primeLength;

        public int getPrimeCount() {
            return primeLength;
        }

        public int get(int k) {
            return primes[k];
        }

        public EulerSieve(int limit) {
            isComp = new boolean[limit + 1];
            primes = new int[limit + 1];
            primeLength = 0;
            for (int i = 2; i <= limit; i++) {
                if (!isComp[i]) {
                    primes[primeLength++] = i;
                }
                for (int j = 0, until = limit / i; j < primeLength && primes[j] <= until; j++) {
                    int pi = primes[j] * i;
                    isComp[pi] = true;
                    if (i % primes[j] == 0) {
                        break;
                    }
                }
            }
        }

    }
}

