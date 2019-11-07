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
            TaskD solver = new TaskD();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskD {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            long a = in.readLong();
            long b = in.readLong();

            out.println(IntervalBitwiseOrExpandGroup.expand(a, b));
        }

    }

    static class IntervalBitwiseOrExpandGroup {
        public static long expand(long l, long r) {
            if (l == r) {
                return 1;
            }

            DigitUtils.DigitBase base = new DigitUtils.DigitBase(2);
            int[] bitsOfA = base.decompose(l);
            int[] bitsOfB = base.decompose(r);

            int since = bitsOfA.length - 1;
            while (bitsOfA[since] == bitsOfB[since]) {
                bitsOfA[since] = bitsOfB[since] = 0;
                since--;
            }

            bitsOfB[since] = 0;
            int bBits = since;
            while (bBits > 0 && bitsOfB[bBits - 1] == 0) {
                bBits--;
            }


            l = base.compose(bitsOfA);
            if ((1L << bBits) >= l) {
                return (1L << since) * 2 - l;
            }

            long ans = (1L << since) - l;
            ans += (1L << bBits);
            ans += (1L << since) - l;

            return ans;
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

        public static boolean isMultiplicationOverflow(long a, long b, long limit) {
            if (limit < 0) {
                limit = -limit;
            }
            if (a < 0) {
                a = -a;
            }
            if (b < 0) {
                b = -b;
            }
            if (a == 0 || b == 0) {
                return false;
            }
            return a > limit / b;
        }

        public static class DigitBase {
            private long[] pow;
            private long base;

            public DigitBase(long base) {
                if (base <= 1) {
                    throw new IllegalArgumentException();
                }
                this.base = base;
                LongList ll = new LongList(64);
                ll.add(1);
                while (!isMultiplicationOverflow(ll.tail(), base, Long.MAX_VALUE)) {
                    ll.add(ll.tail() * base);
                }
                pow = ll.toArray();
            }

            public int bitCount() {
                return pow.length;
            }

            public int[] decompose(long x) {
                return decompose(x, new int[bitCount()]);
            }

            public int[] decompose(long x, int[] ans) {
                for (int i = 0; i < ans.length; i++) {
                    ans[i] = (int) (x % base);
                    x /= base;
                }
                return ans;
            }

            public long compose(int[] bits) {
                if (bits.length > bitCount()) {
                    throw new IllegalArgumentException();
                }
                long ans = 0;
                for (int i = bits.length - 1; i >= 0; i--) {
                    ans = ans * base + bits[i];
                }
                return ans;
            }

        }

    }

    static class LongList {
        private int size;
        private int cap;
        private long[] data;
        private static final long[] EMPTY = new long[0];

        public LongList(int cap) {
            this.cap = cap;
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new long[cap];
            }
        }

        public LongList(LongList list) {
            this.size = list.size;
            this.cap = list.cap;
            this.data = Arrays.copyOf(list.data, size);
        }

        public LongList() {
            this(0);
        }

        private void ensureSpace(int need) {
            int req = size + need;
            if (req > cap) {
                while (cap < req) {
                    cap = Math.max(cap + 10, 2 * cap);
                }
                data = Arrays.copyOf(data, cap);
            }
        }

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public long tail() {
            checkRange(0);
            return data[size - 1];
        }

        public void add(long x) {
            ensureSpace(1);
            data[size++] = x;
        }

        public long[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public String toString() {
            return Arrays.toString(Arrays.copyOf(data, size));
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
}

