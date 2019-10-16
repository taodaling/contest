import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskE {
        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        NumberTheory.Factorial fact = new NumberTheory.Factorial(500000, mod);
        NumberTheory.Composite comp = new NumberTheory.Composite(fact);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] s = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                s[i] = in.readInt();
            }

            BIT specified = new BIT(n);
            for (int i = 1; i <= n; i++) {
                if (s[i] > 0) {
                    specified.update(s[i], 1);
                }
            }

            int k = n - specified.query(n);

            int ans1 = fact.fact(k);
            int ans2 = 0;
            int ans3 = 0;


            int notFixSum = 0;
            for (int i = 1; i <= n; i++) {
                notFixSum = mod.plus(notFixSum, i - 1);
            }


            for (int i = 1; i <= n; i++) {
                if (s[i] != 0) {
                    notFixSum = mod.subtract(notFixSum, s[i] - 1);
                }
            }

            for (int i = 1; i <= n; i++) {
                int cnt = 1;
                if (s[i] == 0) {
                    cnt = mod.mul(cnt, notFixSum);
                    cnt = mod.mul(cnt, fact.fact(k - 1));
                } else {
                    cnt = mod.mul(cnt, s[i] - 1);
                    cnt = mod.mul(cnt, fact.fact(k));
                }
                cnt = mod.mul(cnt, fact.fact(n - i));
                ans2 = mod.plus(ans2, cnt);
            }

            int[] prefixSubstitute = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                prefixSubstitute[i] = prefixSubstitute[i - 1];
                if (s[i] == 0) {
                    prefixSubstitute[i]++;
                }
            }

            BIT bit = new BIT(n);
            int freeGreaterThanCnt = 0;
            for (int i = 1; i <= n; i++) {
                int cnt = 0;
                int rep = prefixSubstitute[i - 1];
                int cnt1 = 0;
                if (s[i] > 0) {
                    int free = s[i] - 1 - bit.query(s[i] - 1);
                    if (k > 0) {
                        cnt1 = mod.mul(free, fact.fact(k - 1));
                    }
                    cnt = mod.plus(cnt, mod.mul(bit.query(s[i]), fact.fact(k)));
                } else {
                    if (k > 1) {
                        cnt1 = mod.mul(comp.composite(k, 2), fact.fact(k - 2));
                    }
                    cnt = mod.plus(cnt, mod.mul(freeGreaterThanCnt, fact.fact(k - 1)));
                }
                cnt1 = mod.mul(cnt1, rep);
                cnt = mod.plus(cnt, cnt1);
                cnt = mod.mul(cnt, fact.fact(n - i));
                ans3 = mod.plus(ans3, cnt);
                if (s[i] > 0) {
                    bit.update(s[i], 1);
                    int blank = specified.query(n) - specified.query(s[i]);
                    blank = (n - s[i]) - blank;
                    freeGreaterThanCnt = mod.plus(freeGreaterThanCnt, blank);
                }
            }

            int ans = 0;
            ans = mod.plus(ans, ans1);
            ans = mod.plus(ans, ans2);
            ans = mod.subtract(ans, ans3);

            out.println(ans);
        }

    }
    static class NumberTheory {
        public static class Modular {
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

        public static class InverseNumber {
            int[] inv;

            public InverseNumber(int[] inv, int limit, NumberTheory.Modular modular) {
                this.inv = inv;
                inv[1] = 1;
                int p = modular.m;
                for (int i = 2; i <= limit; i++) {
                    int k = p / i;
                    int r = p % i;
                    inv[i] = modular.mul(-k, inv[r]);
                }
            }

            public InverseNumber(int limit, NumberTheory.Modular modular) {
                this(new int[limit + 1], limit, modular);
            }

        }

        public static class Factorial {
            int[] fact;
            int[] inv;
            NumberTheory.Modular modular;

            public Factorial(int[] fact, int[] inv, NumberTheory.InverseNumber in, int limit,
                            NumberTheory.Modular modular) {
                this.modular = modular;
                this.fact = fact;
                this.inv = inv;
                fact[0] = inv[0] = 1;
                for (int i = 1; i <= limit; i++) {
                    fact[i] = modular.mul(fact[i - 1], i);
                    inv[i] = modular.mul(inv[i - 1], in.inv[i]);
                }
            }

            public Factorial(int limit, NumberTheory.Modular modular) {
                this(new int[limit + 1], new int[limit + 1], new NumberTheory.InverseNumber(limit, modular), limit,
                                modular);
            }

            public int fact(int n) {
                return fact[n];
            }

        }

        public static class Composite {
            final NumberTheory.Factorial factorial;
            final NumberTheory.Modular modular;

            public Composite(NumberTheory.Factorial factorial) {
                this.factorial = factorial;
                this.modular = factorial.modular;
            }

            public Composite(int limit, NumberTheory.Modular modular) {
                this(new NumberTheory.Factorial(limit, modular));
            }

            public int composite(int m, int n) {
                if (n > m) {
                    return 0;
                }
                return modular.mul(modular.mul(factorial.fact[m], factorial.inv[n]), factorial.inv[m - n]);
            }

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
                    throw new RuntimeException(e);
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
    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(1 << 20);
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

    }
    static class BIT {
        private int[] data;
        private int n;

        public BIT(int n) {
            this.n = n;
            data = new int[n + 1];
        }

        public int query(int i) {
            int sum = 0;
            for (; i > 0; i -= i & -i) {
                sum += data[i];
            }
            return sum;
        }

        public void update(int i, int mod) {
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
}

