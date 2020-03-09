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
            LUOGU4195 solver = new LUOGU4195();
            try {
                int testNumber = 1;
                while (true)
                    solver.solve(testNumber++, in, out);
            } catch (UnknownError e) {
                out.close();
            }
        }
    }

    static class LUOGU4195 {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int a = in.readInt();
            int p = in.readInt();
            int b = in.readInt();
            if (a == 0 && b == 0 && p == 0) {
                throw new UnknownError();
            }
            GenericModLog log = new GenericModLog(a, p);
            int x = log.log(b);
            if (x == -1) {
                out.println("No Solution");
            } else {
                out.println(x);
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

        public FastOutput append(String c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
            return append(c).println();
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

        public String toString() {
            return "mod " + m;
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

    static class RelativePrimeModLog {
        Modular mod;
        Modular powMod;
        int x;
        int phi;
        IntegerHashMap map;
        int m;
        int invM;
        private static ExtGCD extGCD = new ExtGCD();

        public RelativePrimeModLog(int x, Modular mod) {
            this.x = x;
            this.mod = mod;
            phi = mod.getMod();
            powMod = new Modular(phi);
            if (extGCD.extgcd(x, mod.getMod()) != 1) {
                throw new IllegalArgumentException();
            }
            m = (int) Math.ceil(Math.sqrt(phi));
            map = new IntegerHashMap(m, false);

            int inv = mod.valueOf(extGCD.getX());
            invM = new Power(mod).pow(inv, m);

            int prod = mod.valueOf(1);
            for (int i = 0; i < m; i++) {
                map.putIfNotExist(prod, i);
                prod = mod.mul(prod, x);
            }
        }

        public int log(int y) {
            y = mod.valueOf(y);
            int start = y;
            for (int i = 0; i * m < phi; start = mod.mul(start, invM), i++) {
                int val = map.getOrDefault(start, -1);
                if (val >= 0) {
                    return powMod.valueOf(val + i * m);
                }
            }
            return -1;
        }

    }

    static class Hasher {
        private long time = System.nanoTime() + System.currentTimeMillis();

        private int shuffle(long x) {
            x += time;
            x += 0x9e3779b97f4a7c15L;
            x = (x ^ (x >>> 30)) * 0xbf58476d1ce4e5b9L;
            x = (x ^ (x >>> 27)) * 0x94d049bb133111ebL;
            return (int) (x ^ (x >>> 31));
        }

        public int hash(int x) {
            return shuffle(x);
        }

    }

    static interface IntegerEntryIterator {
        boolean hasNext();

        void next();

        int getEntryKey();

        int getEntryValue();

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

    static class IntegerHashMap {
        private int[] slot;
        private int[] next;
        private int[] keys;
        private int[] values;
        private int alloc;
        private boolean[] removed;
        private int mask;
        private int size;
        private boolean rehash;
        private Hasher hasher = new Hasher();

        public IntegerHashMap(int cap, boolean rehash) {
            this.mask = (1 << (32 - Integer.numberOfLeadingZeros(cap - 1))) - 1;
            slot = new int[mask + 1];
            next = new int[cap + 1];
            keys = new int[cap + 1];
            values = new int[cap + 1];
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

        private int hash(int x) {
            return hasher.hash(x);
        }

        public void putIfNotExist(int x, int y) {
            put(x, y, false);
        }

        public void put(int x, int y, boolean cover) {
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
                } else if (cover) {
                    values[index] = y;
                }
            }
            if (rehash && size >= slot.length) {
                rehash();
            }
        }

        public int getOrDefault(int x, int def) {
            int h = hash(x);
            int s = h & mask;
            if (slot[s] == 0) {
                return def;
            }
            int index = findIndexOrLastEntry(s, x);
            return keys[index] == x ? values[index] : def;
        }

        private int findIndexOrLastEntry(int s, int x) {
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

        public IntegerEntryIterator iterator() {
            return new IntegerEntryIterator() {
                int index = 1;
                int readIndex = -1;


                public boolean hasNext() {
                    while (index <= alloc && removed[index]) {
                        index++;
                    }
                    return index <= alloc;
                }


                public int getEntryKey() {
                    return keys[readIndex];
                }


                public int getEntryValue() {
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
            IntegerEntryIterator iterator = iterator();
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

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int mod(int x, int mod) {
            x %= mod;
            if (x < 0) {
                x += mod;
            }
            return x;
        }

    }

    static class Power {
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

    }

    static class GenericModLog {
        private RelativePrimeModLog log;
        private int mul = 1;
        private int div = 1;
        private int k = 0;
        private int a;
        private Modular original;
        private Modular modular;
        private static ExtGCD extGCD = new ExtGCD();

        public GenericModLog(int a, int p) {
            a = DigitUtils.mod(a, p);
            original = new Modular(p);
            int g;
            while ((g = GCDs.gcd(a, p)) != 1) {
                mul = original.mul(mul, a / g);
                div *= g;
                p /= g;
                k++;
            }
            this.a = a;
            this.modular = new Modular(p);
            log = new RelativePrimeModLog(a, modular);
            extGCD.extgcd(mul, p);
            mul = modular.valueOf(extGCD.getX());
        }

        public int log(int y) {
            y = original.valueOf(y);
            int prod = original.valueOf(1);
            for (int i = 0; i < k; i++) {
                if (prod == y) {
                    return i;
                }
                prod = original.mul(prod, a);
            }
            if (y % div != 0) {
                return -1;
            }

            y = modular.mul(y / div, mul);
            int ans = log.log(y);
            if (ans >= 0) {
                ans += k;
            }
            return ans;
        }

    }
}

