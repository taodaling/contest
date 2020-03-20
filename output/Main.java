import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.function.IntUnaryOperator;
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
            int testCount = Integer.parseInt(in.next());
            for (int i = 1; i <= testCount; i++)
                solver.solve(i, in, out);
            out.close();
        }
    }

    static class TaskD {
        Debug debug = new Debug(true);
        int limit = 1000000;
        char[] s = new char[limit];
        HashData hd31 = new HashData(limit, (int) 1e9 + 7, 31);
        HashData hd61 = new HashData(limit, (int) 1e9 + 7, 61);
        PartialHash ph31 = new PartialHash(hd31);
        PartialHash ph61 = new PartialHash(hd61);
        PartialHash revPh31 = new PartialHash(hd31);
        PartialHash revPh61 = new PartialHash(hd61);
        int n;
        int[] pre;
        int[] post;

        int mirror(int x) {
            return n - 1 - x;
        }

        boolean isP(int l, int r) {
            int revL = mirror(r);
            int revR = mirror(l);
            return ph31.hash(l, r, false) == revPh31.hash(revL, revR, false) &&
                    ph61.hash(l, r, false) == revPh61.hash(revL, revR, false);
        }

        public int dist(int l, int r) {
            return r - l + 1;
        }

        public void solve(int l, int r, int[] ht) {
            if (r < l) {
                ht[0] = l - 1;
                ht[1] = r + 1;
                return;
            }
            if (l == r) {
                ht[0] = l;
                ht[1] = l + 1;
                return;
            }
            ht[0] = l - 1;
            ht[1] = r + 1;
            if (s[l] == s[r]) {
                solve(l + 1, r - 1, ht);
            }
            int now = dist(l, ht[0]) + dist(ht[1], r);
            int front = Math.min(pre[l], r);
            if (dist(l, front) > now) {
                ht[0] = front;
                ht[1] = r + 1;
                now = dist(l, front);
            }
            int back = Math.max(l, post[r]);
            if (dist(back, r) > now) {
                ht[0] = l - 1;
                ht[1] = back;
                now = dist(back, r);
            }
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            n = in.readString(s, 0);
            pre = new int[n];
            post = new int[n];
            pre[n - 1] = n - 1;
            post[0] = 0;

            ph31.populate(i -> s[i], 0, n - 1);
            ph61.populate(i -> s[i], 0, n - 1);
            revPh31.populate(i -> s[n - 1 - i], 0, n - 1);
            revPh61.populate(i -> s[n - 1 - i], 0, n - 1);
            for (int i = n - 2; i >= 0; i--) {
                pre[i] = Math.min(n - 1, pre[i + 1] + 1);
                while (!isP(i, pre[i])) {
                    pre[i]--;
                }
            }
            for (int i = 1; i < n; i++) {
                post[i] = Math.max(0, post[i - 1] - 1);
                while (!isP(post[i], i)) {
                    post[i]++;
                }
            }

            debug.debug("pre", pre);
            debug.debug("post", post);

            int[] ans = new int[2];
            solve(0, n - 1, ans);
            for (int i = 0; i <= ans[0]; i++) {
                out.append(s[i]);
            }
            for (int i = ans[1]; i <= n - 1; i++) {
                out.append(s[i]);
            }
            out.println();
        }

    }

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
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

        public String next() {
            return readString();
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

    static class HashData {
        public Modular mod;
        public int[] inv;
        public int[] pow;

        public HashData(int n, int p, int x) {
            this.mod = new Modular(p);
            n = Math.max(n, 1);
            inv = new int[n + 1];
            pow = new int[n + 1];
            inv[0] = 1;
            pow[0] = 1;
            int invX = new Power(mod).inverseByFermat(x);
            for (int i = 1; i <= n; i++) {
                inv[i] = mod.mul(inv[i - 1], invX);
                pow[i] = mod.mul(pow[i - 1], x);
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
                if (x == null || !x.getClass().isArray()) {
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

    static class PartialHash {
        HashData hd;
        int[] hash;

        public PartialHash(HashData hd) {
            this.hd = hd;
            hash = new int[hd.pow.length];
        }

        public void populate(IntUnaryOperator function, int l, int r) {
            if (l > 0) {
                hash[l - 1] = 0;
            }
            hash[l] = hd.mod.mul(function.applyAsInt(l), hd.pow[l]);
            for (int i = l + 1; i <= r; i++) {
                hash[i] = hd.mod.valueOf(hash[i - 1] + hd.pow[i] * (long) function.applyAsInt(i));
            }
        }

        public int hash(int l, int r, boolean verbose) {
            long h = hash[r];
            if (l > 0) {
                h -= hash[l - 1];
                h *= hd.inv[l];
            }
            if (verbose) {
                h += hd.pow[r - l + 1];
            }
            return hd.mod.valueOf(h);
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
}

