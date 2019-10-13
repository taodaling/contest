import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.stream.IntStream;
import java.util.Collection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.Map;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author daltao
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "daltao", 1 << 27);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            PrintWriter out = new PrintWriter(outputStream);
            TaskF solver = new TaskF();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskF {
        NumberTheory.Modular mod = new NumberTheory.Modular(924844033);
        NumberTheory.Factorial fact = new NumberTheory.Factorial(500000, mod);
        NumberTheory.Log2 log2 = new NumberTheory.Log2();
        NumberTheoryTransform ntt = new NumberTheoryTransform(mod, 5);

        public void solve(int testNumber, FastInput in, PrintWriter out) {
            int n = in.readInt();
            Node[] nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
            }
            for (int i = 1; i < n; i++) {
                Node a = nodes[in.readInt()];
                Node b = nodes[in.readInt()];
                a.next.add(b);
                b.next.add(a);
            }

            int[] coe = new int[n + 1];
            coe[n] = 1;
            dfsForSize(nodes[1], null);
            dfsForCoe(nodes[1], nodes[1].size, coe);

            int m = log2.ceilLog(n + 1) + 1;
            int properLen = 1 << m;
            int[] f = new int[properLen];
            int[] g = new int[properLen];
            int[] r = new int[properLen];
            for (int i = 0; i <= n; i++) {
                f[i] = mod.mul(coe[i], fact.fact(i));
                g[i] = fact.invFact(i);
            }
            Memory.reverse(f, 0, n + 1);

            ntt.prepareReverse(r, m);

            ntt.dft(r, f, m);
            ntt.dft(r, g, m);
            ntt.dotMul(f, g, f, m);
            ntt.idft(r, f, m);
            Memory.reverse(f, 0, n + 1);

            for (int i = 1; i <= n; i++) {
                int c = mod.mul(f[i], fact.invFact(i));
                out.println(c);
            }
        }

        public void dfsForCoe(Node root, int total, int[] coe) {
            for (Node node : root.next) {
                dfsForCoe(node, total, coe);
                coe[node.size] = mod.subtract(coe[node.size], 1);
                coe[total - node.size] = mod.subtract(coe[total - node.size], 1);
                coe[total] = mod.plus(coe[total], 1);
            }
        }

        public void dfsForSize(Node root, Node fa) {
            root.next.remove(fa);
            root.size = 1;
            for (Node node : root.next) {
                dfsForSize(node, root);
                root.size += node.size;
            }
        }

    }

    static class NumberTheoryTransform {
        private NumberTheory.Modular MODULAR;
        private NumberTheory.Power POWER;
        private int G;
        private int[] wCache = new int[30];
        private int[] invCache = new int[30];

        public NumberTheoryTransform(NumberTheory.Modular mod) {
            this(mod, new NumberTheory.PrimitiveRoot(mod.getMod()).findMinPrimitiveRoot());
        }

        public NumberTheoryTransform(NumberTheory.Modular mod, int g) {
            this.MODULAR = mod;
            this.POWER = new NumberTheory.Power(mod);
            this.G = g;
            for (int i = 0, until = wCache.length; i < until; i++) {
                int s = 1 << i;
                wCache[i] = POWER.pow(G, (MODULAR.m - 1) / 2 / s);
                invCache[i] = POWER.inverse(s);
            }
        }

        public void dotMul(int[] a, int[] b, int[] c, int m) {
            for (int i = 0, n = 1 << m; i < n; i++) {
                c[i] = MODULAR.mul(a[i], b[i]);
            }
        }

        public void prepareReverse(int[] r, int b) {
            int n = 1 << b;
            r[0] = 0;
            for (int i = 1; i < n; i++) {
                r[i] = (r[i >> 1] >> 1) | ((1 & i) << (b - 1));
            }
        }

        public void dft(int[] r, int[] p, int m) {
            int n = 1 << m;

            for (int i = 0; i < n; i++) {
                if (r[i] > i) {
                    int tmp = p[i];
                    p[i] = p[r[i]];
                    p[r[i]] = tmp;
                }
            }

            int w = 0;
            int t = 0;
            for (int d = 0; d < m; d++) {
                int w1 = wCache[d];
                int s = 1 << d;
                int s2 = s << 1;
                for (int i = 0; i < n; i += s2) {
                    w = 1;
                    for (int j = 0; j < s; j++) {
                        int a = i + j;
                        int b = a + s;
                        t = MODULAR.mul(w, p[b]);
                        p[b] = MODULAR.plus(p[a], -t);
                        p[a] = MODULAR.plus(p[a], t);
                        w = MODULAR.mul(w, w1);
                    }
                }
            }
        }

        public void idft(int[] r, int[] p, int m) {
            dft(r, p, m);

            int n = 1 << m;
            int invN = invCache[m];

            p[0] = MODULAR.mul(p[0], invN);
            p[n / 2] = MODULAR.mul(p[n / 2], invN);
            for (int i = 1, until = n / 2; i < until; i++) {
                int a = p[n - i];
                p[n - i] = MODULAR.mul(p[i], invN);
                p[i] = MODULAR.mul(a, invN);
            }
        }

    }

    static class Memory {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static void reverse(int[] data, int f, int t) {
            int l = f, r = t - 1;
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
        }

    }

    static class NumberTheory {
        private static final Random RANDOM = new Random();

        public static class Gcd {
            public int gcd(int a, int b) {
                return a >= b ? gcd0(a, b) : gcd0(b, a);
            }

            private int gcd0(int a, int b) {
                return b == 0 ? a : gcd0(b, a % b);
            }

        }

        public static class Modular {
            int m;

            public int getMod() {
                return m;
            }

            public Modular(int m) {
                this.m = m;
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

        public static class Power {
            final NumberTheory.Modular modular;

            public Power(NumberTheory.Modular modular) {
                this.modular = modular;
            }

            public int pow(int x, long n) {
                if (n == 0) {
                    return 1;
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

        public static class Log2 {
            public int ceilLog(int x) {
                return 32 - Integer.numberOfLeadingZeros(x - 1);
            }

        }

        public static class InverseNumber {
            int[] inv;

            public InverseNumber(int[] inv, int limit, NumberTheory.Modular modular) {
                this.inv = inv;
                inv[1] = 1;
                int p = modular.m;
                for (int i = 2; i <= limit; i++) {
                    int k = p / i;
                    int r = p % i;
                    inv[i] = modular.mul(-k, inv[r]);
                }
            }

            public InverseNumber(int limit, NumberTheory.Modular modular) {
                this(new int[limit + 1], limit, modular);
            }

        }

        public static class Factorial {
            int[] fact;
            int[] inv;
            NumberTheory.Modular modular;

            public Factorial(int[] fact, int[] inv, NumberTheory.InverseNumber in, int limit, NumberTheory.Modular modular) {
                this.modular = modular;
                this.fact = fact;
                this.inv = inv;
                fact[0] = inv[0] = 1;
                for (int i = 1; i <= limit; i++) {
                    fact[i] = modular.mul(fact[i - 1], i);
                    inv[i] = modular.mul(inv[i - 1], in.inv[i]);
                }
            }

            public Factorial(int limit, NumberTheory.Modular modular) {
                this(new int[limit + 1], new int[limit + 1], new NumberTheory.InverseNumber(limit, modular), limit, modular);
            }

            public int fact(int n) {
                return fact[n];
            }

            public int invFact(int n) {
                return inv[n];
            }

        }

        public static class MillerRabin {
            NumberTheory.Modular modular;
            NumberTheory.Power power;

            public boolean mr(int n, int s) {
                if (n == 2) {
                    return true;
                }
                if (n % 2 == 0) {
                    return false;
                }
                modular = new NumberTheory.Modular(n);
                power = new NumberTheory.Power(modular);
                for (int i = 0; i < s; i++) {
                    int x = RANDOM.nextInt(n - 2) + 2;
                    if (!mr0(x, n)) {
                        return false;
                    }
                }
                return true;
            }

            private boolean mr0(int x, int n) {
                int exp = n - 1;
                while (true) {
                    int y = power.pow(x, exp);
                    if (y != 1 && y != n - 1) {
                        return false;
                    }
                    if (y != 1 || exp % 2 == 1) {
                        break;
                    }
                    exp = exp / 2;
                }
                return true;
            }

        }

        public static class PollardRho {
            NumberTheory.MillerRabin mr = new NumberTheory.MillerRabin();
            NumberTheory.Gcd gcd = new NumberTheory.Gcd();
            Random random = new Random();

            public int findFactor(int n) {
                if (mr.mr(n, 10)) {
                    return n;
                }
                while (true) {
                    int f = findFactor0(random.nextInt(n), random.nextInt(n), n);
                    if (f != -1) {
                        return f;
                    }
                }
            }

            public Map<Integer, Integer> findAllFactors(int n) {
                Map<Integer, Integer> map = new HashMap();
                findAllFactors(map, n);
                return map;
            }

            private void findAllFactors(Map<Integer, Integer> map, int n) {
                if (n == 1) {
                    return;
                }
                int f = findFactor(n);
                if (f == n) {
                    Integer value = map.get(f);
                    if (value == null) {
                        value = 1;
                    }
                    map.put(f, value * f);
                    return;
                }
                findAllFactors(map, f);
                findAllFactors(map, n / f);
            }

            private int findFactor0(int x, int c, int n) {
                int xi = x;
                int xj = x;
                int j = 2;
                int i = 1;
                while (i < n) {
                    i++;
                    xi = (int) ((long) xi * xi + c) % n;
                    int g = gcd.gcd(n, Math.abs(xi - xj));
                    if (g != 1 && g != n) {
                        return g;
                    }
                    if (i == j) {
                        j = j << 1;
                        xj = xi;
                    }
                }
                return -1;
            }

        }

        public static class PrimitiveRoot {
            private int[] factors;
            private NumberTheory.Modular mod;
            private NumberTheory.Power pow;
            int phi;
            private static NumberTheory.PollardRho rho = new NumberTheory.PollardRho();

            public PrimitiveRoot(int x) {
                phi = x - 1;
                mod = new NumberTheory.Modular(x);
                pow = new NumberTheory.Power(mod);
                factors = rho.findAllFactors(phi).keySet()
                        .stream().mapToInt(Integer::intValue).toArray();
            }

            public int findMinPrimitiveRoot() {
                return findMinPrimitiveRoot(2);
            }

            public int findMinPrimitiveRoot(int since) {
                for (int i = since; i < mod.m; i++) {
                    boolean flag = true;
                    for (int f : factors) {
                        if (pow.pow(i, phi / f) == 1) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        return i;
                    }
                }
                return -1;
            }

        }

    }

    static class Node {
        List<Node> next = new ArrayList<>(2);
        int size;

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
                    throw new RuntimeException(e);
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

