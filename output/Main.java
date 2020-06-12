import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.Stream;
import java.io.Closeable;
import java.io.Writer;
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
            DDivisionGame solver = new DDivisionGame();
            try {
                int testNumber = 1;
                while (true)
                    solver.solve(testNumber++, in, out);
            } catch (UnknownError e) {
                out.close();
            }
        }
    }

    static class DDivisionGame {
        Modular mod = new Modular(985661441);
        Factorial fact = new Factorial(1000000, mod);
        Combination comb = new Combination(fact);
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
        int[] seqA = new int[1 << 20];
        int[] seqB = new int[1 << 20];
        int[] seqC = new int[1 << 20];

        public int way(int n, int m) {
            return comb.combination(n + m - 1, n);
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            if (!in.hasMore()) {
                throw new UnknownError();
            }
            out.printf("Case #%d: ", testNumber);
            int m = in.readInt();
            int k = in.readInt();
            int[] a = new int[m];
            int sum = 0;
            for (int i = 0; i < m; i++) {
                in.readInt();
                a[i] = in.readInt();
                sum += a[i];
            }
            int proper = Log2.ceilLog(sum + 1 + sum + 1 + 1);
            for (int i = 0; i <= sum + 1; i++) {
                seqA[i] = fact.invFact(i);
                if ((i & 1) == 1) {
                    seqA[i] = mod.valueOf(-seqA[i]);
                }
                seqB[i] = fact.invFact(i);
                for (int j = 0; j < m; j++) {
                    seqB[i] = mod.mul(seqB[i], way(a[j], i));
                }
            }
            for (int i = sum + 2; i < 1 << proper; i++) {
                seqA[i] = seqB[i] = 0;
            }

            ntt.dft(seqA, proper);
            ntt.dft(seqB, proper);
            ntt.dotMul(seqA, seqB, seqC, proper);
            ntt.idft(seqC, proper);

            int[] f = seqC;
            for (int i = 0; i <= sum; i++) {
                f[i] = mod.mul(f[i + 1], fact.fact(i + 1));
            }

            //debug.debug("f", Arrays.copyOf(f, sum + 1));
            int[][] powF = new int[2][k + 1];
            int[] ans = new int[k];
            for (int i = 0; i <= k; i++) {
                powF[0][i] = i == 0 ? 1 : mod.mul(powF[0][i - 1], f[0]);
            }
            for (int i = 1; i <= sum; i++) {
                for (int j = 0; j <= k; j++) {
                    powF[1][j] = j == 0 ? 1 : mod.mul(powF[1][j - 1], f[i]);
                }

                for (int j = 0; j < k; j++) {
                    int local = mod.mul(powF[1][j], powF[0][k - j]);
                    ans[j] = mod.plus(ans[j], local);
                }

                SequenceUtils.swap(powF, 0, 1);
            }

            for (int x : ans) {
                out.append(x).append(' ');
            }

            out.println();
        }

    }

    static class Modular {
        int m;

        public int getMod() {
            return m;
        }

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

        public String toString() {
            return "mod " + m;
        }

    }

    static interface IntCombination {
    }

    static class Power implements InverseNumber {
        static ExtGCD extGCD = new ExtGCD();
        final Modular modular;

        public Power(Modular modular) {
            this.modular = modular;
        }

        public int pow(int x, int n) {
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

        public int inverseByFermat(int x) {
            return pow(x, modular.m - 2);
        }

        public int inverse(int x) {
            return inverseExtGCD(x);
        }

        public int inverseExtGCD(int x) {
            if (extGCD.extgcd(x, modular.getMod()) != 1) {
                throw new IllegalArgumentException();
            }
            return modular.valueOf(extGCD.getX());
        }

    }

    static class SequenceUtils {
        public static <T> void swap(T[] data, int i, int j) {
            T tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

    }

    static class ModPrimeInverseNumber implements InverseNumber {
        int[] inv;

        public ModPrimeInverseNumber(int[] inv, int limit, Modular modular) {
            this.inv = inv;
            inv[1] = 1;
            int p = modular.getMod();
            for (int i = 2; i <= limit; i++) {
                int k = p / i;
                int r = p % i;
                inv[i] = modular.mul(-k, inv[r]);
            }
        }

        public ModPrimeInverseNumber(int limit, Modular modular) {
            this(new int[limit + 1], limit, modular);
        }

        public int inverse(int x) {
            return inv[x];
        }

    }

    static class Factorial {
        int[] fact;
        int[] inv;
        Modular modular;

        public Modular getModular() {
            return modular;
        }

        public Factorial(int[] fact, int[] inv, InverseNumber in, int limit, Modular modular) {
            this.modular = modular;
            this.fact = fact;
            this.inv = inv;
            fact[0] = inv[0] = 1;
            for (int i = 1; i <= limit; i++) {
                fact[i] = modular.mul(fact[i - 1], i);
                inv[i] = modular.mul(inv[i - 1], in.inverse(i));
            }
        }

        public Factorial(int limit, Modular modular) {
            this(new int[limit + 1], new int[limit + 1], new ModPrimeInverseNumber(limit, modular), limit, modular);
        }

        public int fact(int n) {
            return fact[n];
        }

        public int invFact(int n) {
            return inv[n];
        }

    }

    static class NumberTheoryTransform {
        private Modular modular;
        private Power power;
        private int g;
        private int[] wCache = new int[30];
        private int[] invCache = new int[30];

        public NumberTheoryTransform(Modular mod) {
            this(mod, mod.getMod() == 998244353 ? 3 : new PrimitiveRoot(mod.getMod()).findMinPrimitiveRoot());
        }

        public NumberTheoryTransform(Modular mod, int g) {
            this.modular = mod;
            this.power = new Power(mod);
            this.g = g;
            for (int i = 0, until = wCache.length; i < until; i++) {
                int s = 1 << i;
                wCache[i] = power.pow(this.g, (modular.getMod() - 1) / 2 / s);
                invCache[i] = power.inverseByFermat(s);
            }
        }

        public void dotMul(int[] a, int[] b, int[] c, int m) {
            for (int i = 0, n = 1 << m; i < n; i++) {
                c[i] = modular.mul(a[i], b[i]);
            }
        }

        public void dft(int[] p, int m) {
            int n = 1 << m;

            int shift = 32 - Integer.numberOfTrailingZeros(n);
            for (int i = 1; i < n; i++) {
                int j = Integer.reverse(i << shift);
                if (i < j) {
                    int temp = p[i];
                    p[i] = p[j];
                    p[j] = temp;
                }
            }

            int w = 0;
            int t = 0;
            for (int d = 0; d < m; d++) {
                int w1 = wCache[d];
                int s = 1 << d;
                int s2 = s << 1;
                for (int i = 0; i < n; i += s2) {
                    w = 1;
                    for (int j = 0; j < s; j++) {
                        int a = i + j;
                        int b = a + s;
                        t = modular.mul(w, p[b]);
                        p[b] = modular.plus(p[a], -t);
                        p[a] = modular.plus(p[a], t);
                        w = modular.mul(w, w1);
                    }
                }
            }
        }

        public void idft(int[] p, int m) {
            dft(p, m);

            int n = 1 << m;
            int invN = invCache[m];

            p[0] = modular.mul(p[0], invN);
            p[n / 2] = modular.mul(p[n / 2], invN);
            for (int i = 1, until = n / 2; i < until; i++) {
                int a = p[n - i];
                p[n - i] = modular.mul(p[i], invN);
                p[i] = modular.mul(a, invN);
            }
        }

    }

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int mod(long x, int mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return (int) x;
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

        public boolean hasMore() {
            skipBlank();
            return next != -1;
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

    static class ExtGCD {
        private long x;
        private long y;
        private long g;

        public long getX() {
            return x;
        }

        public long extgcd(long a, long b) {
            if (a >= b) {
                g = extgcd0(a, b);
            } else {
                g = extgcd0(b, a);
                long tmp = x;
                x = y;
                y = tmp;
            }
            return g;
        }

        private long extgcd0(long a, long b) {
            if (b == 0) {
                x = 1;
                y = 0;
                return a;
            }
            long g = extgcd0(b, a % b);
            long n = x;
            long m = y;
            x = m;
            y = n - m * (a / b);
            return g;
        }

    }

    static class Log2 {
        public static int ceilLog(int x) {
            return 32 - Integer.numberOfLeadingZeros(x - 1);
        }

    }

    static class GCDs {
        private GCDs() {
        }

        public static int gcd(int a, int b) {
            return a >= b ? gcd0(a, b) : gcd0(b, a);
        }

        private static int gcd0(int a, int b) {
            return b == 0 ? a : gcd0(b, a % b);
        }

    }

    static class PollardRho {
        MillerRabin mr = new MillerRabin();
        Random random = new Random(1);

        public int findFactor(int n) {
            if (mr.mr(n, 5)) {
                return n;
            }
            while (true) {
                int f = rho(n);
                if (f != n) {
                    return f;
                }
            }
        }

        public Map<Integer, Integer> findAllFactors(int n) {
            Map<Integer, Integer> map = new HashMap();
            findAllFactors(map, n);
            return map;
        }

        private void findAllFactors(Map<Integer, Integer> map, int n) {
            if (n == 1) {
                return;
            }
            int f = findFactor(n);
            if (f == n) {
                Integer value = map.get(f);
                if (value == null) {
                    value = 1;
                }
                map.put(f, value * f);
                return;
            }
            findAllFactors(map, f);
            findAllFactors(map, n / f);
        }

        private int rho(int n) {
            if (n % 2 == 0) {
                return 2;
            }
            if (n % 3 == 0) {
                return 3;
            }
            int x = 0, y = x, t, q = 1, c = random.nextInt(n - 1) + 1;
            for (int k = 2; ; k <<= 1, y = x, q = 1) {
                for (int i = 1; i <= k; ++i) {
                    x = DigitUtils.mod((long) x * x + c, n);
                    q = DigitUtils.mod((long) q * Math.abs(x - y), n);
                    if ((i & 127) == 0) {
                        t = GCDs.gcd(q, n);
                        if (t > 1) {
                            return t;
                        }
                    }
                }
                if ((t = GCDs.gcd(q, n)) > 1) {
                    return t;
                }
            }
        }

    }

    static class Combination implements IntCombination {
        final Factorial factorial;
        final Modular modular;

        public Combination(Factorial factorial) {
            this.factorial = factorial;
            this.modular = factorial.getModular();
        }

        public Combination(int limit, Modular modular) {
            this(new Factorial(limit, modular));
        }

        public int combination(int m, int n) {
            if (n > m) {
                return 0;
            }
            return modular.mul(modular.mul(factorial.fact(m), factorial.invFact(n)), factorial.invFact(m - n));
        }

    }

    static interface InverseNumber {
        int inverse(int x);

    }

    static class PrimitiveRoot {
        private int[] factors;
        private Modular mod;
        private Power pow;
        int phi;
        private static PollardRho rho = new PollardRho();

        public PrimitiveRoot(Modular x) {
            phi = x.getMod() - 1;
            mod = x;
            pow = new Power(mod);
            factors = rho.findAllFactors(phi).keySet().stream().mapToInt(Integer::intValue).toArray();
        }

        public PrimitiveRoot(int x) {
            this(new Modular(x));
        }

        public int findMinPrimitiveRoot() {
            if (mod.getMod() == 2) {
                return 1;
            }
            return findMinPrimitiveRoot(2);
        }

        public int findMinPrimitiveRoot(int since) {
            for (int i = since; i < mod.m; i++) {
                boolean flag = true;
                for (int f : factors) {
                    if (pow.pow(i, phi / f) == 1) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return i;
                }
            }
            return -1;
        }

    }

    static class MillerRabin {
        Modular modular;
        Power power;
        Random random = new Random();

        public boolean mr(int n, int s) {
            if (n <= 1) {
                return false;
            }
            if (n == 2) {
                return true;
            }
            if (n % 2 == 0) {
                return false;
            }
            int m = n - 1;
            while (m % 2 == 0) {
                m /= 2;
            }
            modular = new Modular(n);
            power = new Power(modular);
            for (int i = 0; i < s; i++) {
                int x = random.nextInt(n - 2) + 2;
                if (!mr0(x, n, m)) {
                    return false;
                }
            }
            return true;
        }

        private boolean mr0(int x, int n, int m) {
            return test(power.pow(x, m), m, n);
        }

        private boolean test(int y, int exp, int n) {
            int y2 = modular.mul(y, y);
            if (!(exp == n - 1 || test(y2, exp * 2, n))) {
                return false;
            }
            if (exp != n - 1 && y2 != 1) {
                return true;
            }
            if (y != 1 && y != n - 1) {
                return false;
            }
            return true;
        }

    }
}

