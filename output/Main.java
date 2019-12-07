import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.io.UncheckedIOException;
import java.util.Map;
import java.io.Closeable;
import java.util.Map.Entry;
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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskE {
        int[] primes;
        int[] primeExps;
        int k;
        Segment segs;
        int n;
        ExtGCD gcd = new ExtGCD();
        int[] buf1;
        int[] buf2;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readInt();
            int m = in.readInt();
            Map<Integer, Integer> factors = new PollardRho()
                    .findAllFactors(m);

            IntList primeList = new IntList();
            IntList primeExpList = new IntList();
            for (Map.Entry<Integer, Integer> e : factors.entrySet()) {
                primeList.add(e.getKey());
                primeExpList.add(e.getValue());
            }

            primes = primeList.toArray();
            primeExps = primeExpList.toArray();
            k = primes.length;

            Segment.allPrimes = primes;
            Segment.modulars = new Modular[k];
            Segment.powers = new CachedPow[k];
            Segment.k = k;
            for (int i = 0; i < k; i++) {
                Segment.modulars[i] = new Modular(primeExps[i]);
                Segment.powers[i] = new CachedPow(primes[i], 10000000, Segment.modulars[i]);
            }

            segs = new Segment(1, n);
            buf1 = new int[k];
            buf2 = new int[k];

            for (int i = 1; i <= n; i++) {
                int a = in.readInt();
                mul(i, i, a);
            }

            int q = in.readInt();
            for (int i = 0; i < q; i++) {
                int t = in.readInt();
                if (t == 1) {
                    mul(in.readInt(), in.readInt(), in.readInt());
                } else if (t == 2) {
                    divide(in.readInt(), in.readInt());
                } else {
                    int ans = query(in.readInt(), in.readInt());
                    out.println(ans);
                }
            }
        }

        public void mul(int l, int r, int x) {
            for (int i = 0; i < k; i++) {
                int y = x;
                int p = 0;
                while (y % primes[i] == 0) {
                    y /= primes[i];
                    p++;
                }
                buf1[i] = y;
                buf2[i] = p;
            }
            segs.update(l, r, 1, n, buf1, buf2);
        }

        public void divide(int pos, int x) {
            for (int i = 0; i < k; i++) {
                int y = x;
                int p = 0;
                while (y % primes[i] == 0) {
                    y /= primes[i];
                    p++;
                }
                gcd.extgcd(y, primeExps[i]);
                int invY = DigitUtils.mod(gcd.getX(), primeExps[i]);
                buf1[i] = invY;
                buf2[i] = -p;
            }
            segs.update(pos, pos, 1, n, buf1, buf2);
        }

        public int query(int l, int r) {
            IntExtCRT crt = new IntExtCRT();
            Arrays.fill(buf1, 0);
            segs.query(l, r, 1, n, buf1);
            for (int i = 0; i < k; i++) {
                crt.add(buf1[i], primeExps[i]);
            }
            return crt.getValue();
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

    static class CachedPow {
        private int[] first;
        private int[] second;
        private Modular mod;

        public CachedPow(int x, int maxExp, Modular mod) {
            this.mod = mod;
            int k = Math.max(1, (int) DigitUtils.round(Math.sqrt(maxExp)));
            first = new int[k];
            second = new int[maxExp / k + 1];
            first[0] = 1;
            for (int i = 1; i < k; i++) {
                first[i] = mod.mul(x, first[i - 1]);
            }
            second[0] = 1;
            int step = mod.mul(x, first[k - 1]);
            for (int i = 1; i < second.length; i++) {
                second[i] = mod.mul(second[i - 1], step);
            }
        }

        public int pow(int exp) {
            return mod.mul(first[exp % first.length], second[exp / first.length]);
        }

    }

    static class Segment implements Cloneable {
        public static int k;
        public static int[] allPrimes;
        public static Modular[] modulars;
        public static CachedPow[] powers;
        private Segment left;
        private Segment right;
        private int[] sum = new int[k];
        private int[] val = new int[k];
        private int[] pow = new int[k];

        private void mod(int[] mul, int[] p) {
            for (int i = 0; i < k; i++) {
                if (p[i] >= 0) {
                    sum[i] = modulars[i].mul(mul[i], sum[i]);
                    sum[i] = modulars[i].mul(sum[i], powers[i].pow(p[i]));
                    val[i] = modulars[i].mul(val[i], mul[i]);
                    pow[i] += p[i];
                } else {
                    sum[i] = val[i] = modulars[i].mul(val[i], mul[i]);
                    pow[i] += p[i];
                    sum[i] = modulars[i].mul(sum[i], powers[i].pow(pow[i]));
                }
            }
        }

        public void pushUp() {
            for (int i = 0; i < k; i++) {
                sum[i] = modulars[i].plus(left.sum[i], right.sum[i]);
            }
        }

        public void pushDown() {
            left.mod(val, pow);
            right.mod(val, pow);
            Arrays.fill(val, 1);
            Arrays.fill(pow, 0);

        }

        public Segment(int l, int r) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m);
                right = new Segment(m + 1, r);
                Arrays.fill(val, 1);
                pushUp();
            } else {
                Arrays.fill(val, 1);
                Arrays.fill(sum, 1);
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, int[] mul, int[] p) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                mod(mul, p);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, mul, p);
            right.update(ll, rr, m + 1, r, mul, p);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, int[] ans) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                for (int i = 0; i < k; i++) {
                    ans[i] = modulars[i].plus(sum[i], ans[i]);
                }
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.query(ll, rr, l, m, ans);
            right.query(ll, rr, m + 1, r, ans);
        }

    }

    static class PollardRho {
        MillerRabin mr = new MillerRabin();
        Gcd gcd = new Gcd();
        Random random = new Random();

        public int findFactor(int n) {
            if (mr.mr(n, 10)) {
                return n;
            }
            while (true) {
                int f = findFactor0(random.nextInt(n), random.nextInt(n), n);
                if (f != -1) {
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

        private int findFactor0(int x, int c, int n) {
            int xi = x;
            int xj = x;
            int j = 2;
            int i = 1;
            while (i < n) {
                i++;
                xi = (int) ((long) xi * xi + c) % n;
                int g = gcd.gcd(n, Math.abs(xi - xj));
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

    }

    static class IntList {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public IntList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new int[cap];
            }
        }

        public IntList(IntList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public IntList() {
            this(0);
        }

        public void ensureSpace(int req) {
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        public void add(int x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public String toString() {
            return Arrays.toString(toArray());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof IntList)) {
                return false;
            }
            IntList other = (IntList) obj;
            return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
        }

        public int hashCode() {
            int h = 1;
            for (int i = 0; i < size; i++) {
                h = h * 31 + data[i];
            }
            return h;
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

    static class SequenceUtils {
        public static boolean equal(int[] a, int al, int ar, int[] b, int bl, int br) {
            if ((ar - al) != (br - bl)) {
                return false;
            }
            for (int i = al, j = bl; i <= ar; i++, j++) {
                if (a[i] != b[j]) {
                    return false;
                }
            }
            return true;
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

        public String toString() {
            return "mod " + m;
        }

    }

    static class IntExtCRT {
        int r;
        int m;
        boolean valid = true;
        static final ExtGCD gcd = new ExtGCD();

        public IntExtCRT() {
            r = 0;
            m = 1;
        }

        public int getValue() {
            return r;
        }

        public boolean add(int r, int m) {
            int m1 = this.m;
            int x1 = this.r;
            int m2 = m;
            int x2 = DigitUtils.mod(r, m);
            int g = (int) gcd.extgcd(m1, m2);
            int a = (int) (gcd.getX() % m2);
            if ((x2 - x1) % g != 0) {
                return valid = false;
            }
            this.m = m1 / g * m2;
            this.r = DigitUtils.mod((long) a * ((x2 - x1) / g) % this.m * m1 % this.m + x1, this.m);
            return true;
        }

        public String toString() {
            return String.format("%d mod %d", r, m);
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

    static class MillerRabin {
        Modular modular;
        Power power;
        Random random = new Random();

        public boolean mr(int n, int s) {
            if (n == 2) {
                return true;
            }
            if (n % 2 == 0) {
                return false;
            }
            modular = new Modular(n);
            power = new Power(modular);
            for (int i = 0; i < s; i++) {
                int x = random.nextInt(n - 2) + 2;
                if (!mr0(x, n)) {
                    return false;
                }
            }
            return true;
        }

        private boolean mr0(int x, int n) {
            int exp = n - 1;
            while (true) {
                int y = power.pow(x, exp);
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

    }

    static class Gcd {
        public int gcd(int a, int b) {
            return a >= b ? gcd0(a, b) : gcd0(b, a);
        }

        private int gcd0(int a, int b) {
            return b == 0 ? a : gcd0(b, a % b);
        }

    }
}

