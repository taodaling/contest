import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
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
            EExpectationOfDivision solver = new EExpectationOfDivision();
            try {
                int testNumber = 1;
                while (true)
                    solver.solve(testNumber++, in, out);
            } catch (UnknownError e) {
                out.close();
            }
        }
    }

    static class EExpectationOfDivision {
        Modular mod = new Modular(1e9 + 7);
        Map<Seq, Integer> seqToId = new HashMap<>((int) 3e5);
        List<Seq> seqs = new ArrayList<>((int) 3e5);
        int[][] next = new int[50][];
        Power pow = new Power(mod);

        {
            for (int i = 0; i < 50; i++) {
                next[i] = new int[i];
            }
        }

        private int seqId(Seq seq) {
            Integer id = seqToId.get(seq);
            if (id == null) {
                seqs.add(seq);
                id = seqs.size() - 1;
                seqToId.put(seq, id);

                int m = seq.data.length;
                int[] transform = new int[m];
                for (int i = 0; i < m; i++) {
                    transform[i] = subtract(seq, i);
                }
                seq.transform = transform;
                seq.prefix = new int[m];
                Arrays.fill(seq.prefix, -1);
            }

            return id;
        }

        public Integer subtract(Seq seq, int i) {
            int m = seq.data.length;
            int[] next = this.next[m];
            System.arraycopy(seq.data, 0, next, 0, m);
            next[i]--;
            Seq nextSeq = new Seq(next);
            return seqId(nextSeq);
        }

        public int prefix(long key) {
            int seqId = DigitUtils.highBit(key);
            int fix = DigitUtils.lowBit(key);
            Seq seq = seqs.get(seqId);
            if (fix >= seq.data.length) {
                return dp(seqId);
            }
            if (seq.prefix[fix] == -1) {
                int ans = 0;
                ans = mod.plus(ans, prefix(DigitUtils.asLong(seq.transform[fix], fix)));
                ans = mod.plus(ans, prefix(DigitUtils.asLong(seqId, fix + 1)));
                seq.prefix[fix] = ans;
            }
            return seq.prefix[fix];
        }

        public int dp(int seqId) {
            Seq seq = seqs.get(seqId);
            if (seq.dp == -1) {
                int m = seq.data.length;
                if (m == 0) {
                    return seq.dp = 0;
                }
                int prod = 1;
                for (int i = 0; i < m; i++) {
                    prod = mod.mul(prod, 1 + seq.data[i]);
                }
                int prob = pow.inverse(prod);
                int factor = pow.inverse(mod.subtract(1, prob));
                int sum = 0;
                for (int i = 0; i < m; i++) {
                    sum = mod.plus(sum, prefix(DigitUtils.asLong(seq.transform[i], i)));
                }
                seq.dp = mod.plus(mod.mul(sum, prob), 1);
                seq.dp = mod.mul(seq.dp, factor);
            }
            return seq.dp;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            if (!in.hasMore()) {
                throw new UnknownError();
            }
            out.printf("Case #%d: ", testNumber);
            BigInteger n = new BigInteger(in.readString());
            int m = in.readInt();
            int[] data = this.next[m];
            Arrays.fill(data, 0);
            for (int i = 0; i < m; i++) {
                BigInteger p = BigInteger.valueOf(in.readInt());
                while (true) {
                    BigInteger[] dar = n.divideAndRemainder(p);
                    if (!dar[1].equals(BigInteger.ZERO)) {
                        break;
                    }
                    n = dar[0];
                    data[i]++;
                }
            }

            int seqId = seqId(new Seq(data));
            int dp = dp(seqId);
            out.println(dp);
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

        public boolean hasMore() {
            skipBlank();
            return next != -1;
        }

    }

    static class CompareUtils {
        private CompareUtils() {
        }

        public static void insertSort(int[] data, IntegerComparator cmp, int l, int r) {
            for (int i = l + 1; i <= r; i++) {
                int j = i;
                int val = data[i];
                while (j > l && cmp.compare(data[j - 1], val) > 0) {
                    data[j] = data[j - 1];
                    j--;
                }
                data[j] = val;
            }
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

    static interface InverseNumber {
    }

    static class DigitUtils {
        private static long LONG_TO_INT_MASK = (1L << 32) - 1;

        private DigitUtils() {
        }

        public static long asLong(int high, int low) {
            return (((long) high) << 32) | (((long) low) & LONG_TO_INT_MASK);
        }

        public static int highBit(long x) {
            return (int) (x >> 32);
        }

        public static int lowBit(long x) {
            return (int) x;
        }

    }

    static class Power implements InverseNumber {
        static ExtGCD extGCD = new ExtGCD();
        final Modular modular;

        public Power(Modular modular) {
            this.modular = modular;
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

    static interface IntegerComparator {
        public static final IntegerComparator REVERSE_ORDER = (a, b) -> Integer.compare(b, a);

        public int compare(int a, int b);

    }

    static class Seq {
        int[] data;
        int[] transform;
        int[] prefix;
        int dp = -1;

        public Seq(int[] seq) {
            CompareUtils.insertSort(seq, IntegerComparator.REVERSE_ORDER, 0, seq.length - 1);
            int suffix = seq.length;
            while (suffix > 0 && seq[suffix - 1] == 0) {
                suffix--;
            }
            data = Arrays.copyOfRange(seq, 0, suffix);
        }

        public int hashCode() {
            return Arrays.hashCode(data);
        }

        public boolean equals(Object obj) {
            return Arrays.equals(data, ((Seq) obj).data);
        }

        public String toString() {
            return Arrays.toString(data);
        }

    }
}

