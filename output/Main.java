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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskE {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            char[] s = new char[100000];
            int len = in.readString(s, 0);
            Node[] nodes = new Node[len];
            int mod = 'z' - 'a' + 1;
            for (int i = 0; i < len; i++) {
                nodes[i] = new Node();
                nodes[i].val = s[i] - 'a';
            }

            int n = in.readInt();
            Interval[] intervals = new Interval[n];
            for (int i = 0; i < n; i++) {
                intervals[i] = new Interval();
                intervals[i].id = i;
                intervals[i].l = in.readInt() - 1;
                intervals[i].r = in.readInt() - 1;
            }
            ModifiableHash h31 = new ModifiableHash(31, n);
            ModifiableHash h11 = new ModifiableHash(11, n);
            LongHashMap map = new LongHashMap(n * 2, true);

            Interval[] intervalsSortByL = intervals.clone();
            Interval[] intervalsSortByR = intervals.clone();
            Arrays.sort(intervalsSortByL, (a, b) -> a.l - b.l);
            Arrays.sort(intervalsSortByR, (a, b) -> a.r - b.r);
            int lHead = 0;
            int rHead = 0;
            for (int i = 0; i < len; i++) {
                while (lHead < n && intervalsSortByL[lHead].l <= i) {
                    h31.modify(intervalsSortByL[lHead].id, 1);
                    h11.modify(intervalsSortByL[lHead].id, 1);
                    lHead++;
                }
                while (rHead < n && intervalsSortByR[rHead].r < i) {
                    h31.modify(intervalsSortByR[rHead].id, -1);
                    h11.modify(intervalsSortByR[rHead].id, -1);
                    rHead++;
                }
                long state = DigitUtils.asLong(h11.hash(0, n - 1), h31.hash(0, n - 1));
                if (!map.containKey(state)) {
                    map.put(state, i);
                    continue;
                }
                int index = (int) map.get(state);
                Node.merge(nodes[index], nodes[i], nodes[i].val - nodes[index].val);
            }
            int l = 0;
            int r = len - 1;
            while (l < r) {
                Node a = nodes[l++];
                Node b = nodes[r--];
                if (a.find() == b.find()) {
                    int differ = a.dist - b.dist;
                    if (differ % mod != 0) {
                        out.println("NO");
                        return;
                    }
                    continue;
                }
                Node.merge(a.find(), b.find(), 0);
            }

            out.println("YES");
        }

    }

    static class Node {
        Node p = this;
        int rank = 0;
        int dist;
        int val;

        Node find() {
            if (p == p.p) {
                return p;
            }
            p.find();
            dist += p.dist;
            p = p.find();
            return p;
        }

        static void merge(Node a, Node b, int bSubA) {
            a.find();
            bSubA += a.dist;
            a = a.find();
            b.find();
            bSubA -= b.dist;
            b = b.find();
            if (a == b) {
                return;
            }
            if (a.rank == b.rank) {
                a.rank++;
            }
            if (a.rank < b.rank) {
                Node tmp = a;
                a = b;
                b = tmp;
                bSubA = -bSubA;
            }
            b.p = a;
            b.dist = bSubA;
        }

    }

    static class ModBIT {
        private int[] data;
        private int n;
        private Modular modular;

        public ModBIT(int n, Modular mod) {
            this.n = n;
            data = new int[n + 1];
            this.modular = mod;
        }

        public int query(int i) {
            long sum = 0;
            for (; i > 0; i -= i & -i) {
                sum += data[i];
            }
            return modular.valueOf(sum);
        }

        public void update(int i, int mod) {
            if (i <= 0) {
                return;
            }
            for (; i <= n; i += i & -i) {
                data[i] = modular.plus(data[i], mod);
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

    static class ModifiableHash {
        public static final Modular MOD = new Modular((int) (1e9 + 7));
        public static final Power POWER = new Power(MOD);
        private int[] inverse;
        private int[] xs;
        private ModBIT bit;

        public ModifiableHash(int x, int n) {
            xs = new int[n + 1];
            inverse = new int[n + 1];
            bit = new ModBIT(n, MOD);

            xs[0] = 1;
            inverse[0] = 1;
            int invX = POWER.inverse(x);
            for (int i = 1; i <= n; i++) {
                xs[i] = MOD.mul(xs[i - 1], x);
                inverse[i] = MOD.mul(inverse[i - 1], invX);
            }
        }

        public void modify(int i, int v) {
            bit.update(i + 1, MOD.mul(v, xs[i]));
        }

        public int hash(int l, int r) {
            int h = bit.query(r + 1);
            if (l > 0) {
                h = MOD.subtract(h, bit.query(l));
                h = MOD.mul(h, inverse[l]);
            }
            return h;
        }

    }

    static class LongHashMap {
        private int[] slot;
        private int[] next;
        private long[] keys;
        private long[] values;
        private int alloc;
        private boolean[] removed;
        private int mask;
        private int size;
        private boolean rehash;

        public LongHashMap(int cap, boolean rehash) {
            this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
            slot = new int[mask + 1];
            next = new int[cap + 1];
            keys = new long[cap + 1];
            values = new long[cap + 1];
            removed = new boolean[cap + 1];
            this.rehash = rehash;
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            keys = Arrays.copyOf(keys, newSize);
            values = Arrays.copyOf(values, newSize);
            removed = Arrays.copyOf(removed, newSize);
        }

        public void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
            removed[alloc] = false;
            size++;
        }

        private void rehash() {
            int[] newSlots = new int[Math.max(16, slot.length * 2)];
            int newMask = newSlots.length - 1;
            for (int i = 0; i < slot.length; i++) {
                if (slot[i] == 0) {
                    continue;
                }
                int head = slot[i];
                while (head != 0) {
                    int n = next[head];
                    int s = hash(keys[head]) & newMask;
                    next[head] = newSlots[s];
                    newSlots[s] = head;
                    head = n;
                }
            }
            this.slot = newSlots;
            this.mask = newMask;
        }

        private int hash(long x) {
            int h = Long.hashCode(x);
            return h ^ (h >>> 16);
        }

        public void put(long x, long y) {
            int h = hash(x);
            int s = h & mask;
            if (slot[s] == 0) {
                alloc();
                slot[s] = alloc;
                keys[alloc] = x;
                values[alloc] = y;
            } else {
                int index = findIndexOrLastEntry(s, x);
                if (keys[index] != x) {
                    alloc();
                    next[index] = alloc;
                    keys[alloc] = x;
                    values[alloc] = y;
                } else {
                    values[index] = y;
                }
            }
            if (rehash && size >= slot.length) {
                rehash();
            }
        }

        public boolean containKey(long x) {
            int h = hash(x);
            int s = h & mask;
            if (slot[s] == 0) {
                return false;
            }
            return keys[findIndexOrLastEntry(s, x)] == x;
        }

        public long getOrDefault(long x, long def) {
            int h = hash(x);
            int s = h & mask;
            if (slot[s] == 0) {
                return def;
            }
            int index = findIndexOrLastEntry(s, x);
            return keys[index] == x ? values[index] : def;
        }

        public long get(long x) {
            return getOrDefault(x, 0);
        }

        private int findIndexOrLastEntry(int s, long x) {
            int iter = slot[s];
            while (keys[iter] != x) {
                if (next[iter] != 0) {
                    iter = next[iter];
                } else {
                    return iter;
                }
            }
            return iter;
        }

        public LongEntryIterator iterator() {
            return new LongEntryIterator() {
                int index = 1;
                int readIndex = -1;


                public boolean hasNext() {
                    while (index <= alloc && removed[index]) {
                        index++;
                    }
                    return index <= alloc;
                }


                public long getEntryKey() {
                    return keys[readIndex];
                }


                public long getEntryValue() {
                    return values[readIndex];
                }


                public void next() {
                    if (!hasNext()) {
                        throw new IllegalStateException();
                    }
                    readIndex = index;
                    index++;
                }
            };
        }

        public String toString() {
            LongEntryIterator iterator = iterator();
            StringBuilder builder = new StringBuilder("{");
            while (iterator.hasNext()) {
                iterator.next();
                builder.append(iterator.getEntryKey()).append("->").append(iterator.getEntryValue()).append(',');
            }
            if (builder.charAt(builder.length() - 1) == ',') {
                builder.setLength(builder.length() - 1);
            }
            builder.append('}');
            return builder.toString();
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

    static class DigitUtils {
        private DigitUtils() {
        }

        public static long asLong(int high, int low) {
            return (((long) high) << 32) | low;
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

    static interface LongEntryIterator {
        boolean hasNext();

        void next();

        long getEntryKey();

        long getEntryValue();

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

        public int inverse(int x) {
            return pow(x, modular.m - 2);
        }

    }

    static class Interval {
        int l;
        int r;
        int id;

    }
}

