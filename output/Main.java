import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Random;
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
            FBattalionStrength solver = new FBattalionStrength();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FBattalionStrength {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            Modular mod = new Modular(1e9 + 7);
            Power power = new Power(mod);
            int inv2 = power.inverseByFermat(2);

            int n = in.readInt();
            Treap root = Treap.NIL;
            int[] val = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                int x = in.readInt();
                val[i] = x;
                root = add(root, create(x));
            }

            //debug.debug("1/2(1/4+13)", mod.mul(inv2, mod.plus(power.inverseByFermat(4), 13)));
            //debug.debug("11/2", mod.mul(inv2, 11));

            int q = in.readInt();
            out.println(mod.mul(inv2, root.exp));
            for (int i = 0; i < q; i++) {
                // debug.debug("i", i);
                int index = in.readInt();
                int x = in.readInt();
                root = remove(root, val[index]);
                val[index] = x;
                root = add(root, create(val[index]));
                out.println(mod.mul(inv2, root.exp));
            }
        }

        public Treap add(Treap a, Treap b) {
            Treap[] split = Treap.splitByKey(a, b.key);
            b.modify(split[0].size);
            split[1].modify(1);
            split[0] = Treap.merge(split[0], b);
            return Treap.merge(split[0], split[1]);
        }

        public Treap remove(Treap a, int x) {
            Treap[] split = Treap.splitByKey(a, x);
            split[1].modify(-1);
            split[0] = Treap.splitByRank(split[0], split[0].size - 1)[0];
            return Treap.merge(split[0], split[1]);
        }

        public Treap create(int x) {
            Treap t = new Treap();
            t.key = x;
            t.index = 0;
            t.pushUp();
            return t;
        }

    }

    static class Treap implements Cloneable {
        private static Random random = new Random(0);
        static Treap NIL = new Treap();
        static Modular mod = new Modular(1e9 + 7);
        static Modular powMod = mod.getModularForPowerComputation();
        static CachedPow pow = new CachedPow(2, mod);
        Treap left = NIL;
        Treap right = NIL;
        int size = 1;
        int key;
        int index;
        int dirty;
        int sumX;
        int sumY;
        int exp;

        static {
            NIL.left = NIL.right = NIL;
            NIL.size = 0;
        }

        public Treap clone() {
            try {
                return (Treap) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public void modify(int d) {
            if (this == NIL) {
                return;
            }
            dirty = powMod.plus(dirty, d);
            index = powMod.plus(index, d);
            sumX = mod.mul(sumX, pow.pow(d));
            sumY = mod.mul(sumY, pow.inverse(d));
        }

        public void pushDown() {
            if (this == NIL) {
                return;
            }
            if (dirty != 0) {
                left.modify(dirty);
                right.modify(dirty);
                dirty = 0;
            }
        }

        public void pushUp() {
            if (this == NIL) {
                return;
            }
            size = left.size + right.size + 1;

            int pos = mod.mul(key, pow.pow(index));
            int inv = mod.mul(key, pow.inverse(index));
            sumX = mod.plus(left.sumX, right.sumX);
            sumX = mod.plus(sumX, pos);

            sumY = mod.plus(left.sumY, right.sumY);
            sumY = mod.plus(sumY, inv);

            exp = mod.plus(left.exp, right.exp);
            exp = mod.plus(exp, mod.mul(left.sumX, mod.plus(inv, right.sumY)));
            exp = mod.plus(exp, mod.mul(pos, right.sumY));
        }

        public static Treap[] splitByRank(Treap root, int rank) {
            if (root == NIL) {
                return new Treap[]{NIL, NIL};
            }
            root.pushDown();
            Treap[] result;
            if (root.left.size >= rank) {
                result = splitByRank(root.left, rank);
                root.left = result[1];
                result[1] = root;
            } else {
                result = splitByRank(root.right, rank - (root.size - root.right.size));
                root.right = result[0];
                result[0] = root;
            }
            root.pushUp();
            return result;
        }

        public static Treap merge(Treap a, Treap b) {
            if (a == NIL) {
                return b;
            }
            if (b == NIL) {
                return a;
            }
            if (random.nextInt(a.size + b.size) < a.size) {
                a.pushDown();
                a.right = merge(a.right, b);
                a.pushUp();
                return a;
            } else {
                b.pushDown();
                b.left = merge(a, b.left);
                b.pushUp();
                return b;
            }
        }

        public static void toString(Treap root, StringBuilder builder) {
            if (root == NIL) {
                return;
            }
            root.pushDown();
            toString(root.left, builder);
            builder.append(root.key).append(',');
            toString(root.right, builder);
        }

        public static Treap clone(Treap root) {
            if (root == NIL) {
                return NIL;
            }
            Treap clone = root.clone();
            clone.left = clone(root.left);
            clone.right = clone(root.right);
            return clone;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder().append(key).append(":");
            toString(clone(this), builder);
            return builder.toString();
        }

        public static Treap[] splitByKey(Treap root, int key) {
            if (root == NIL) {
                return new Treap[]{NIL, NIL};
            }
            root.pushDown();
            Treap[] result;
            if (root.key > key) {
                result = splitByKey(root.left, key);
                root.left = result[1];
                result[1] = root;
            } else {
                result = splitByKey(root.right, key);
                root.right = result[0];
                result[0] = root;
            }
            root.pushUp();
            return result;
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

        public Modular getModularForPowerComputation() {
            return new Modular(m - 1);
        }

        public String toString() {
            return "mod " + m;
        }

    }

    static class CachedPow {
        private int[] first;
        private int[] second;
        private Modular mod;
        private Modular powMod;

        public CachedPow(int x, Modular mod) {
            this(x, mod.getMod(), mod);
        }

        public CachedPow(int x, int maxExp, Modular mod) {
            this.mod = mod;
            this.powMod = mod.getModularForPowerComputation();
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
            exp = powMod.valueOf(exp);
            return mod.mul(first[exp % first.length], second[exp / first.length]);
        }

        public int inverse(int exp) {
            return pow(-exp);
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

        public int inverseByFermat(int x) {
            return pow(x, modular.m - 2);
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
}

