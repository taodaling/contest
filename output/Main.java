import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.io.IOException;
import java.util.Deque;
import java.util.function.Supplier;
import java.io.UncheckedIOException;
import java.util.function.Consumer;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
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
            LUOGU3390 solver = new LUOGU3390();
            int testCount = Integer.parseInt(in.next());
            for (int i = 1; i <= testCount; i++)
                solver.solve(i, in, out);
            out.close();
        }
    }

    static class LUOGU3390 {
        IntList a = new IntList();
        Modular mod = new Modular(1e9 + 7);
        LinearRecurrenceSolver solver;

        {
            a.expandWith(1, 3);
            IntList seq = new IntList();
            seq.addAll(new int[]{1, 1, 1, 2, 3, 4});
            solver = LinearRecurrenceSolver.newSolverFromSequence(seq, mod);
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int ans = solver.solve(n - 1, a);
            out.println(ans);
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

    static class IntList {
        private int size;
        private int cap;
        private int[] data;
        private static final int[] EMPTY = new int[0];

        public int[] getData() {
            return data;
        }

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

        private void checkRange(int i) {
            if (i < 0 || i >= size) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }

        public int get(int i) {
            checkRange(i);
            return data[i];
        }

        public void add(int x) {
            ensureSpace(size + 1);
            data[size++] = x;
        }

        public void addAll(int[] x) {
            addAll(x, 0, x.length);
        }

        public void addAll(int[] x, int offset, int len) {
            ensureSpace(size + len);
            System.arraycopy(x, offset, data, size, len);
            size += len;
        }

        public void addAll(IntList list) {
            addAll(list.data, 0, list.size);
        }

        public void expandWith(int x, int len) {
            ensureSpace(len);
            while (size < len) {
                data[size++] = x;
            }
        }

        public void retain(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n >= size) {
                return;
            }
            size = n;
        }

        public void set(int i, int x) {
            checkRange(i);
            data[i] = x;
        }

        public int size() {
            return size;
        }

        public int[] toArray() {
            return Arrays.copyOf(data, size);
        }

        public void clear() {
            size = 0;
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

    static class ModLinearFeedbackShiftRegister {
        private IntList cm;
        int m = -1;
        int dm;
        private IntList cn;
        private IntList buf;
        private IntList seq;
        private Modular mod;
        private Power pow;

        public ModLinearFeedbackShiftRegister(Modular mod, int cap) {
            cm = new IntList(cap + 1);
            cn = new IntList(cap + 1);
            seq = new IntList(cap + 1);
            buf = new IntList(cap + 1);
            cn.add(1);

            this.mod = mod;
            this.pow = new Power(mod);
        }

        public ModLinearFeedbackShiftRegister(Modular mod) {
            this(mod, 0);
        }

        private int estimateDelta() {
            int n = seq.size() - 1;
            int ans = 0;
            int[] cnData = cn.getData();
            int[] seqData = seq.getData();
            for (int i = 0, until = cn.size(); i < until; i++) {
                ans = mod.plus(ans, mod.mul(cnData[i], seqData[n - i]));
            }
            return ans;
        }

        public void add(int x) {
            x = mod.valueOf(x);
            int n = seq.size();

            seq.add(x);
            int dn = estimateDelta();
            if (dn == 0) {
                return;
            }

            if (m < 0) {
                cm.clear();
                cm.addAll(cn);
                dm = dn;
                m = n;

                cn.expandWith(0, n + 2);
                return;
            }

            int ln = cn.size() - 1;
            int len = Math.max(ln, n + 1 - ln);
            buf.clear();
            buf.addAll(cn);
            buf.expandWith(0, len + 1);

            int factor = mod.mul(dn, pow.inverse(dm));

            int[] bufData = buf.getData();
            int[] cmData = cm.getData();
            for (int i = n - m, until = n - m + cm.size(); i < until; i++) {
                bufData[i] = mod.subtract(bufData[i], mod.mul(factor, cmData[i - (n - m)]));
            }

            if (cn.size() < buf.size()) {
                IntList tmp = cm;
                cm = cn;
                cn = tmp;
                m = n;
                dm = dn;
            }
            {
                IntList tmp = cn;
                cn = buf;
                buf = tmp;
            }


        }

        public int length() {
            return cn.size() - 1;
        }

        public String toString() {
            return cn.toString();
        }

        public int codeAt(int i) {
            return mod.valueOf(-cn.get(i));
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

        public String next() {
            return readString();
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

    static class LinearRecurrenceSolver {
        Modular mod;
        IntList coe;
        IntList p;
        IntList remainder;
        Power pow;
        int n;

        private LinearRecurrenceSolver(IntList coe, Modular mod) {
            this.coe = coe;
            this.mod = mod;
            n = coe.size();
            pow = new Power(mod);
            remainder = new IntList(coe.size());
            p = new IntList(coe.size() + 1);
            for (int i = n - 1; i >= 0; i--) {
                p.add(mod.valueOf(-coe.get(i)));
            }
            p.add(1);
        }

        public static LinearRecurrenceSolver newSolverFromLinearRecurrence(IntList coe, Modular mod) {
            return new LinearRecurrenceSolver(coe, mod);
        }

        public static LinearRecurrenceSolver newSolverFromSequence(IntList seq, Modular mod) {
            ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, seq.size());
            for (int i = 0; i < seq.size(); i++) {
                lfsr.add(seq.get(i));
            }
            IntList coes = new IntList(lfsr.length());
            for (int i = 1; i <= lfsr.length(); i++) {
                coes.add(lfsr.codeAt(i));
            }
            return newSolverFromLinearRecurrence(coes, mod);
        }

        private int solve(IntList a) {
            int ans = 0;
            remainder.expandWith(0, n);
            for (int i = 0; i < n; i++) {
                ans = mod.plus(ans, mod.mul(remainder.get(i), a.get(i)));
            }
            return ans;
        }

        public int solve(long k, IntList a) {
            if (k < a.size()) {
                return a.get((int) k);
            }
            Polynomials.module(k, p, remainder, pow);
            return solve(a);
        }

    }

    static class Buffer<T> {
        private Deque<T> deque;
        private Supplier<T> supplier;
        private Consumer<T> cleaner;

        public Buffer(Supplier<T> supplier) {
            this(supplier, (x) -> {
            });
        }

        public Buffer(Supplier<T> supplier, Consumer<T> cleaner) {
            this(supplier, cleaner, 0);
        }

        public Buffer(Supplier<T> supplier, Consumer<T> cleaner, int exp) {
            this.supplier = supplier;
            this.cleaner = cleaner;
            deque = new ArrayDeque<>(exp);
        }

        public T alloc() {
            return deque.isEmpty() ? supplier.get() : deque.removeFirst();
        }

        public void release(T e) {
            cleaner.accept(e);
            deque.addLast(e);
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

    static class Polynomials {
        public static Buffer<IntList> listBuffer = new Buffer<>(IntList::new, list -> list.clear());
        private static ExtGCD extGCD = new ExtGCD();

        public static int rankOf(IntList p) {
            int[] data = p.getData();
            int r = p.size() - 1;
            while (r >= 0 && data[r] == 0) {
                r--;
            }
            return Math.max(0, r);
        }

        public static void normalize(IntList list) {
            list.retain(rankOf(list) + 1);
        }

        public static void mul(IntList a, IntList b, IntList c, Modular mod) {
            int rA = rankOf(a);
            int rB = rankOf(b);
            c.clear();
            c.expandWith(0, rA + rB + 1);
            int[] aData = a.getData();
            int[] bData = b.getData();
            int[] cData = c.getData();
            for (int i = 0; i <= rA; i++) {
                for (int j = 0; j <= rB; j++) {
                    cData[i + j] = mod.plus(cData[i + j], mod.mul(aData[i], bData[j]));
                }
            }
        }

        public static void divide(IntList a, IntList b, IntList c, IntList remainder, Power pow) {
            Modular mod = pow.getModular();
            int rA = rankOf(a);
            int rB = rankOf(b);

            if (rA < rB) {
                c.clear();
                c.add(0);
                remainder.clear();
                remainder.addAll(a);
                return;
            }

            int rC = Math.max(0, rA - rB);
            c.clear();
            c.expandWith(0, rC + 1);
            remainder.clear();
            remainder.addAll(a);

            int[] bData = b.getData();
            int[] cData = c.getData();
            int[] rData = remainder.getData();

            if (extGCD.extgcd(b.get(rB), mod.getMod()) != 1) {
                throw new IllegalArgumentException();
            }
            int inv = mod.valueOf(extGCD.getX());
            for (int i = rA, j = rC; i >= rB; i--, j--) {
                if (rData[i] == 0) {
                    continue;
                }
                int factor = mod.mul(-rData[i], inv);
                cData[j] = mod.valueOf(-factor);
                for (int k = rB; k >= 0; k--) {
                    rData[k + j] = mod.plus(rData[k + j], mod.mul(factor, bData[k]));
                }
            }

            normalize(remainder);
        }

        public static void module(long k, IntList p, IntList remainder, Power pow) {
            int rP = rankOf(p);
            if (rP == 0) {
                remainder.clear();
                remainder.add(0);
                return;
            }

            IntList a = listBuffer.alloc();
            IntList c = listBuffer.alloc();

            module(k, a, p, c, remainder, rP, pow);

            listBuffer.release(a);
            listBuffer.release(c);
        }

        private static void module(long k, IntList a, IntList b, IntList c, IntList remainder, int rb, Power pow) {
            Modular mod = pow.getModular();
            if (k < rb) {
                remainder.clear();
                remainder.expandWith(0, (int) k + 1);
                remainder.set((int) k, 1);
                return;
            }
            module(k / 2, a, b, c, remainder, rb, pow);
            mul(remainder, remainder, a, mod);
            if (k % 2 == 1) {
                int ra = rankOf(a);
                a.expandWith(0, ra + 2);
                int[] aData = a.getData();
                for (int i = ra; i >= 0; i--) {
                    aData[i + 1] = aData[i];
                }
                aData[0] = 0;
            }
            divide(a, b, c, remainder, pow);
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

    static class Power {
        final Modular modular;

        public Modular getModular() {
            return modular;
        }

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

        public int inverse(int x) {
            return pow(x, modular.m - 2);
        }

    }
}

