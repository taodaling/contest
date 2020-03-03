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
            FSonyaAndBitwiseOR solver = new FSonyaAndBitwiseOR();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FSonyaAndBitwiseOR {
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int x = in.readInt();
            int[] val = new int[n];
            for (int i = 0; i < n; i++) {
                val[i] = in.readInt();
            }
            int bSize = (int) Math.ceil(Math.sqrt(n));
            int split = DigitUtils.ceilDiv(n, bSize);
            Block[] blocks = new Block[split];
            for (int i = 0; i < split; i++) {
                blocks[i] = new Block();
                blocks[i].pre = new int[bSize];
            }

            debug.debug("val", val);
            PreXor xor = new PreXor(val);
            int limit = 19;
            for (int i = 0; i < n; i++) {
                blocks[i / bSize].pre[i % bSize] = (int) xor.prefix(i);
                blocks[i / bSize].bTree.add((int) xor.prefix(i), limit, 1);
            }

            for (int i = 0; i < m; i++) {
                int t = in.readInt();
                if (t == 1) {
                    int index = in.readInt() - 1;
                    int y = in.readInt();
                    int ll = y;
                    int rr = n - 1;
                    for (int j = index / bSize; j < blocks.length; j++) {
                        int l = j * bSize;
                        int r = l + bSize - 1;
                        blocks[j].xor(Math.max(l, ll) - l, Math.min(r, rr) - l, y ^ val[index]);
                    }
                    val[index] = y;
                } else {
                    int ll = in.readInt() - 1;
                    int rr = in.readInt() - 1;
                    int mask = 0;
                    if (ll > 0) {
                        int bId = (ll - 1) / bSize;
                        mask = blocks[bId].get(ll - 1 - bId * bSize);
                    }
                    int ans = 0;
                    for (int j = 0; j < blocks.length; j++) {
                        int l = j * bSize;
                        int r = l + bSize - 1;
                        ans += blocks[j].greaterThan(Math.max(l, ll) - l, Math.min(r, rr) - l, mask, x);
                    }
                    out.println(ans);
                }
            }
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, Object x) {
            return debug(name, x, empty);
        }

        public Debug debug(String name, Object x, int... indexes) {
            if (offline) {
                if (!x.getClass().isArray()) {
                    out.append(name);
                    for (int i : indexes) {
                        out.printf("[%d]", i);
                    }
                    out.append("=").append("" + x);
                    out.println();
                } else {
                    indexes = Arrays.copyOf(indexes, indexes.length + 1);
                    if (x instanceof byte[]) {
                        byte[] arr = (byte[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof short[]) {
                        short[] arr = (short[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof boolean[]) {
                        boolean[] arr = (boolean[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof char[]) {
                        char[] arr = (char[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof int[]) {
                        int[] arr = (int[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof float[]) {
                        float[] arr = (float[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof double[]) {
                        double[] arr = (double[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof long[]) {
                        long[] arr = (long[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else {
                        Object[] arr = (Object[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    }
                }
            }
            return this;
        }

    }

    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 20];
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

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int floorDiv(int a, int b) {
            return a < 0 ? -ceilDiv(-a, b) : a / b;
        }

        public static int ceilDiv(int a, int b) {
            if (a < 0) {
                return -floorDiv(-a, b);
            }
            int c = a / b;
            if (c * b < a) {
                return c + 1;
            }
            return c;
        }

    }

    static class Bits {
        private Bits() {
        }

        public static int bitAt(int x, int i) {
            return (x >> i) & 1;
        }

    }

    static class Block {
        BTree bTree = new BTree();
        int mask;
        int[] pre;

        public int greaterThan(int l, int r, int xor, int x) {
            if (l == 0 && r == pre.length - 1) {
                return bTree.greater(x, 19, xor ^ mask);
            } else {
                xor ^= mask;
                int cnt = 0;
                for (int i = l; i <= r; i++) {
                    if ((pre[i] ^ xor) >= x) {
                        cnt++;
                    }
                }
                return cnt;
            }
        }

        public void xor(int l, int r, int x) {
            if (l == 0 && r == pre.length - 1) {
                mask ^= x;
            }
            for (int i = l; i <= r; i++) {
                bTree.add(pre[i], 19, -1);
                pre[i] ^= x;
                bTree.add(pre[i], 19, 1);
            }
        }

        public int get(int i) {
            return pre[i] ^ mask;
        }

    }

    static class PreXor {
        private long[] pre;

        public PreXor(int n) {
            pre = new long[n];
        }

        public void populate(long[] a) {
            int n = a.length;
            pre[0] = a[0];
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] ^ a[i];
            }
        }

        public void populate(int[] a) {
            int n = a.length;
            pre[0] = a[0];
            for (int i = 1; i < n; i++) {
                pre[i] = pre[i - 1] ^ a[i];
            }
        }

        public PreXor(long[] a) {
            this(a.length);
            populate(a);
        }

        public PreXor(int[] a) {
            this(a.length);
            populate(a);
        }

        public long prefix(int i) {
            return pre[i];
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

    static class BTree {
        BTree[] next = new BTree[2];
        int size = 0;

        public void add(int x, int bit, int mod) {
            if (bit < 0) {
                size += mod;
                return;
            }
            size += mod;
            get(Bits.bitAt(x, bit)).add(x, bit - 1, mod);
        }

        public int greater(int x, int bit, int xor) {
            int y = Bits.bitAt(xor, bit);
            if (Bits.bitAt(x, bit) == 1) {
                if (getSize(1 ^ y) == 0) {
                    return 0;
                }
                return get(1 ^ y).greater(x, bit - 1, xor);
            }
            int ans = getSize(1 ^ y);
            if (getSize(y) > 0) {
                ans += get(y).greater(x, bit - 1, xor);
            }
            return ans;
        }

        public int getSize(int i) {
            return next[i] == null ? 0 : next[i].size;
        }

        public BTree get(int i) {
            if (next[i] == null) {
                next[i] = new BTree();
            }
            return next[i];
        }

    }
}

