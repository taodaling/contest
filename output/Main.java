import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.io.Serializable;
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
            BitSetTest solver = new BitSetTest();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class BitSetTest {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            BitSet[] seq = new BitSet[n + 1];
            seq[0] = new BitSet(m);
            for (int i = 1; i <= n; i++) {
                int t = in.readInt();
                seq[i] = seq[i - 1].clone();
                if (t == 0) {
                    //set one
                    int l = in.readInt();
                    seq[i].set(l);
                } else if (t == 1) {
                    //set bulk
                    int l = in.readInt();
                    int r = in.readInt();
                    seq[i].set(l, r);
                } else if (t == 2) {
                    //clear
                    int l = in.readInt();
                    seq[i].clear(l);
                } else if (t == 3) {
                    //clear bulk
                    int l = in.readInt();
                    int r = in.readInt();
                    seq[i].clear(l, r);
                } else if (t == 4) {
                    //flip
                    int l = in.readInt();
                    seq[i].flip(l);
                } else if (t == 5) {
                    //flip bulk
                    int l = in.readInt();
                    int r = in.readInt();
                    seq[i].flip(l, r);
                } else if (t == 6) {
                    //and
                    int prev = in.readInt();
                    seq[i].and(seq[prev]);
                } else if (t == 7) {
                    //or
                    int prev = in.readInt();
                    seq[i].or(seq[prev]);
                } else if (t == 8) {
                    //xor
                    int prev = in.readInt();
                    seq[i].xor(seq[prev]);
                } else if (t == 9) {
                    //left shift
                    int step = in.readInt();
                    seq[i].leftShift(step);
                } else if (t == 10) {
                    //right shift
                    int step = in.readInt();
                    seq[i].rightShift(step);
                } else if (t == 11) {
                    int l = in.readInt();
                    int r = in.readInt();
                    output(out, seq[i].interval(l, r), 0, r - l);
                } else if (t == 12) {
                    int l = in.readInt();
                    int r = in.readInt();
                    out.println(seq[i].size(l, r));
                }
                output(out, seq[i], 0, m - 1);
            }
        }

        public void output(FastOutput out, BitSet bs, int l, int r) {
            out.append(bs.size()).append(' ');
            for (int i = l; i <= r; i++) {
                if (bs.get(i)) {
                    out.append(i).append(' ');
                }
            }
            out.println();
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

    static final class BitSet implements Serializable, Cloneable {
        private long[] data;
        private long tailAvailable;
        private int capacity;
        private int m;
        private static final int SHIFT = 6;
        private static final int LOW = 63;
        private static final int BITS_FOR_EACH = 64;
        private static final long ALL_ONE = ~0L;
        private static final long ALL_ZERO = 0L;
        private static final int MAX_OFFSET = 63;
        private static final BitSet EMPTY = new BitSet(0);

        public BitSet(int n) {
            capacity = n;
            this.m = (capacity + 64 - 1) / 64;
            data = new long[m];
            tailAvailable = oneBetween(0, offset(capacity - 1));
        }

        public BitSet(BitSet bs) {
            this.data = bs.data.clone();
            this.tailAvailable = bs.tailAvailable;
            this.capacity = bs.capacity;
            this.m = bs.m;
        }

        public BitSet interval(int l, int r) {
            if (r < l) {
                return EMPTY;
            }
            return new BitSet(this, l, r);
        }

        private BitSet(BitSet bs, int l, int r) {
            capacity = r - l + 1;
            tailAvailable = oneBetween(0, offset(capacity - 1));
            data = Arrays.copyOfRange(bs.data, word(l), word(r) + 1);
            this.m = data.length;
            leftShift(offset(l));
            this.m = (capacity + 64 - 1) / 64;
            data[m - 1] &= tailAvailable;
            for (int i = m; i < data.length; i++) {
                data[i] = 0;
            }
        }

        public boolean get(int i) {
            return (data[word(i)] & (1L << offset(i))) != 0;
        }

        public void set(int i) {
            data[word(i)] |= (1L << offset(i));
        }

        private static int word(int i) {
            return i >>> SHIFT;
        }

        private static int offset(int i) {
            return i & LOW;
        }

        private long oneBetween(int l, int r) {
            if (r < l) {
                return 0;
            }
            long lBegin = 1L << offset(l);
            long rEnd = 1L << offset(r);
            return (ALL_ONE ^ (lBegin - 1)) & ((rEnd << 1) - 1);
        }

        public void set(int l, int r) {
            if (r < l) {
                return;
            }
            int lWord = l >>> SHIFT;
            int rWord = r >>> SHIFT;
            for (int i = lWord + 1; i < rWord; i++) {
                data[i] = ALL_ONE;
            }
            //lword
            if (lWord == rWord) {
                data[lWord] |= oneBetween(offset(l), offset(r));
            } else {
                data[lWord] |= oneBetween(offset(l), MAX_OFFSET);
                data[rWord] |= oneBetween(0, offset(r));
            }
        }

        public void clear(int i) {
            data[word(i)] &= ~(1L << offset(i));
        }

        public void clear(int l, int r) {
            if (r < l) {
                return;
            }
            int lWord = l >>> SHIFT;
            int rWord = r >>> SHIFT;
            for (int i = lWord + 1; i < rWord; i++) {
                data[i] = ALL_ZERO;
            }
            //lword
            if (lWord == rWord) {
                data[lWord] &= ~oneBetween(offset(l), offset(r));
            } else {
                data[lWord] &= ~oneBetween(offset(l), MAX_OFFSET);
                data[rWord] &= ~oneBetween(0, offset(r));
            }
        }

        public void flip(int i) {
            data[word(i)] ^= (1L << offset(i));
        }

        public void flip(int l, int r) {
            if (r < l) {
                return;
            }
            int lWord = l >>> SHIFT;
            int rWord = r >>> SHIFT;
            for (int i = lWord + 1; i < rWord; i++) {
                data[i] ^= ALL_ONE;
            }
            //lword
            if (lWord == rWord) {
                data[lWord] ^= oneBetween(offset(l), offset(r));
            } else {
                data[lWord] ^= oneBetween(offset(l), MAX_OFFSET);
                data[rWord] ^= oneBetween(0, offset(r));
            }
        }

        public int capacity() {
            return capacity;
        }

        public int size() {
            int ans = 0;
            for (long x : data) {
                ans += Long.bitCount(x);
            }
            return ans;
        }

        public int size(int l, int r) {
            if (r < l) {
                return 0;
            }
            int ans = 0;
            int lWord = l >>> SHIFT;
            int rWord = r >>> SHIFT;
            for (int i = lWord + 1; i < rWord; i++) {
                ans += Long.bitCount(data[i]);
            }
            //lword
            if (lWord == rWord) {
                ans += Long.bitCount(data[lWord] & oneBetween(offset(l), offset(r)));
            } else {
                ans += Long.bitCount(data[lWord] & oneBetween(offset(l), MAX_OFFSET));
                ans += Long.bitCount(data[rWord] & oneBetween(0, offset(r)));
            }
            return ans;
        }

        public void or(BitSet bs) {
            int n = Math.min(this.m, bs.m);
            for (int i = 0; i < n; i++) {
                data[i] |= bs.data[i];
            }
        }

        public void and(BitSet bs) {
            int n = Math.min(this.m, bs.m);
            for (int i = 0; i < n; i++) {
                data[i] &= bs.data[i];
            }
        }

        public void xor(BitSet bs) {
            int n = Math.min(this.m, bs.m);
            for (int i = 0; i < n; i++) {
                data[i] ^= bs.data[i];
            }
        }

        public int nextSetBit(int start) {
            int offset = offset(start);
            int w = word(start);
            if (offset != 0) {
                long mask = oneBetween(offset, 63);
                if ((data[w] & mask) != 0) {
                    return Long.numberOfTrailingZeros(data[w] & mask) + w * BITS_FOR_EACH;
                }
                w++;
            }

            while (w < m && data[w] == ALL_ZERO) {
                w++;
            }
            if (w >= m) {
                return capacity();
            }
            return Long.numberOfTrailingZeros(data[w]) + w * BITS_FOR_EACH;
        }

        public void leftShift(int n) {
            int wordMove = word(n);
            int offsetMove = offset(n);
            int rshift = 63 - (offsetMove - 1);

            if (offsetMove != 0) {
                //slightly
                for (int i = 0; i < m; i++) {
                    if (i > 0) {
                        data[i - 1] |= data[i] << rshift;
                    }
                    data[i] >>>= offsetMove;
                }
            }
            if (wordMove > 0) {
                for (int i = 0; i < m; i++) {
                    if (i >= wordMove) {
                        data[i - wordMove] = data[i];
                    }
                    data[i] = 0;
                }
            }
        }

        public void rightShift(int n) {
            int wordMove = word(n);
            int offsetMove = offset(n);
            int lShift = MAX_OFFSET + 1 - offsetMove;

            if (offsetMove != 0) {
                //slightly
                for (int i = m - 1; i >= 0; i--) {
                    if (i + 1 < m) {
                        data[i + 1] |= data[i] >>> lShift;
                    }
                    data[i] <<= offsetMove;
                }
            }
            if (wordMove > 0) {
                for (int i = m - 1; i >= 0; i--) {
                    if (i + wordMove < m) {
                        data[i + wordMove] = data[i];
                    }
                    data[i] = 0;
                }
            }

            data[m - 1] &= tailAvailable;
        }

        public BitSet clone() {
            return new BitSet(this);
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("{");
            for (int i = nextSetBit(0); i < capacity(); i++) {
                builder.append(i).append(',');
            }
            if (builder.length() > 1) {
                builder.setLength(builder.length() - 1);
            }
            builder.append("}");
            return builder.toString();
        }

        public int hashCode() {
            int ans = 1;
            for (int i = 0; i < m; i++) {
                ans = ans * 31 + Long.hashCode(data[i]);
            }
            return ans;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof BitSet)) {
                return false;
            }
            BitSet other = (BitSet) obj;
            if (other.capacity != capacity) {
                return false;
            }
            for (int i = 0; i < m; i++) {
                if (other.data[i] != data[i]) {
                    return false;
                }
            }
            return true;
        }

    }
}

