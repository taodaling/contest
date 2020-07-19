import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
            DBacterialMelee solver = new DBacterialMelee();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DBacterialMelee {
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                char c = in.readChar();
                if (sb.length() == 0 || sb.charAt(sb.length() - 1) != c) {
                    sb.append(c);
                }
            }
            int m = sb.length();
            int[] seq = new int[sb.length() + 1];
            for (int i = 1; i <= sb.length(); i++) {
                seq[i] = sb.charAt(i - 1) - 'a';
            }
            seq[0] = -1;
            //debug.debug("seq", seq);
            int charset = 'z' - 'a' + 1;
            int[][] next = new int[m + 1][charset];
            Arrays.fill(next[m], m + 1);
            for (int i = m - 1; i >= 0; i--) {
                for (int j = 0; j < charset; j++) {
                    next[i][j] = next[i + 1][j];
                }
                next[i][seq[i + 1]] = i + 1;
            }
            debug.elapse("init");
            //debug.debug("next", next);

            int[][] dp = new int[m + 1][m + 1];
            dp[0][0] = 1;
            Modular mod = new Modular(1e9 + 7);
            int[][] sum = new int[charset][m + 1];
            int[] global = new int[m + 1];
            for (int i = 0; i <= m; i++) {
                int c = seq[i];
                if (i > 0) {
                    for (int j = 0; j <= i; j++) {
                        dp[i][j] = mod.plus(sum[c][j], global[j]);
                        sum[c][j] = mod.valueOf(-global[j]);
                    }
                }
                if (i == m) {
                    break;
                }
                for (int j = 0; j <= i; j++) {
                    global[j + 1] = mod.plus(global[j + 1], dp[i][j]);
                    if (c >= 0) {
                        sum[c][j + 1] = mod.subtract(sum[c][j + 1], dp[i][j]);
                    }
                }
            }
            debug.elapse("dp");

            int[] cnts = new int[m + 1];
            for (int i = 1; i <= m; i++) {
                for (int j = 0; j <= m; j++) {
                    cnts[j] = mod.plus(cnts[j], dp[i][j]);
                }
            }
            //debug.debug("cnts", cnts);

            int ans = 0;
            Power pow = new Power(mod);
            Combination comb = new Combination(n, pow);
            for (int i = 1; i <= m; i++) {
                int contrib = comb.combination(n - i + i - 1, i - 1);
                contrib = mod.mul(contrib, cnts[i]);
                ans = mod.plus(ans, contrib);
            }

            debug.elapse("calc ans");
            out.println(ans);
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        private long time = System.currentTimeMillis();

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug elapse(String name) {
            if (offline) {
                debug(name, System.currentTimeMillis() - time);
                time = System.currentTimeMillis();
            }
            return this;
        }

        public Debug debug(String name, long x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
        }

    }

    static class Combination implements IntCombination {
        final Factorial factorial;
        final Modular modular;

        public Combination(Factorial factorial) {
            this.factorial = factorial;
            this.modular = factorial.getMod();
        }

        public Combination(int limit, Power pow) {
            this(new Factorial(limit, pow));
        }

        public int combination(int m, int n) {
            if (n > m || n < 0) {
                return 0;
            }
            return modular.mul(modular.mul(factorial.fact(m), factorial.invFact(n)), factorial.invFact(m - n));
        }

    }

    static class IntExtGCDObject {
        private int[] xy = new int[2];

        public int extgcd(int a, int b) {
            return ExtGCD.extGCD(a, b, xy);
        }

        public int getX() {
            return xy[0];
        }

    }

    static class Factorial {
        int[] fact;
        int[] inv;
        Modular mod;

        public Modular getMod() {
            return mod;
        }

        public Factorial(int[] fact, int[] inv, Power pow) {
            this.mod = pow.getModular();
            this.fact = fact;
            this.inv = inv;
            fact[0] = inv[0] = 1;
            for (int i = 1; i < fact.length; i++) {
                fact[i] = i;
                fact[i] = mod.mul(fact[i], fact[i - 1]);
            }
            inv[inv.length - 1] = pow.inverse(fact[inv.length - 1]);
            for (int i = inv.length - 2; i >= 1; i--) {
                inv[i] = mod.mul(inv[i + 1], i + 1);
            }
        }

        public Factorial(int limit, Power pow) {
            this(new int[limit + 1], new int[limit + 1], pow);
        }

        public int fact(int n) {
            return fact[n];
        }

        public int invFact(int n) {
            return inv[n];
        }

    }

    static class SequenceUtils {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

    }

    static class Power implements InverseNumber {
        static IntExtGCDObject extGCD = new IntExtGCDObject();
        final Modular modular;

        public Modular getModular() {
            return modular;
        }

        public Power(Modular modular) {
            this.modular = modular;
        }

        public int inverse(int x) {
            int ans = inverseExtGCD(x);
//        if(modular.mul(ans, x) != 1){
//            throw new IllegalStateException();
//        }
            return ans;
        }

        public int inverseExtGCD(int x) {
            if (extGCD.extgcd(x, modular.getMod()) != 1) {
                throw new IllegalArgumentException();
            }
            return modular.valueOf(extGCD.getX());
        }

    }

    static class ExtGCD {
        public static int extGCD(int a, int b, int[] xy) {
            if (a >= b) {
                return extGCD0(a, b, xy);
            }
            int ans = extGCD0(b, a, xy);
            SequenceUtils.swap(xy, 0, 1);
            return ans;
        }

        private static int extGCD0(int a, int b, int[] xy) {
            if (b == 0) {
                xy[0] = 1;
                xy[1] = 0;
                return a;
            }
            int ans = extGCD0(b, a % b, xy);
            int x = xy[0];
            int y = xy[1];
            xy[0] = y;
            xy[1] = x - a / b * y;
            return ans;
        }

    }

    static interface InverseNumber {
    }

    static interface IntCombination {
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

        public char readChar() {
            skipBlank();
            char c = (char) next;
            next = read();
            return c;
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

        public FastOutput println(int c) {
            return append(c).println();
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
}

