import java.util.Arrays;
import java.util.Random;

public class AllInclusiveString {
    boolean[][] edge;
    boolean[] visited;
    boolean[] instk;
    int n;
    Modular mod = new Modular(998244353);
    int[] mask = new int[32];
    DSU dsu = new DSU(32);
    Combination comb = new Combination(100, mod);
    LongList[] lists = new LongList[]{new LongList(1000000), new LongList(1000000)};

    public int[] shortest(String[] a) {
        int m = a.length;
        boolean[] used = new boolean['z' - 'a' + 1];
        for (String s : a) {
            for (int i = 0; i < 2; i++) {
                used[s.charAt(i) - 'a'] = true;
            }
        }

        IntegerPreSum ips = new IntegerPreSum(i -> used[i] ? 1 : 0, used.length);
        int[][] conn = new int[m][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                conn[i][j] = ips.prefix(a[i].charAt(j) - 'a') - 1;
            }
        }
        n = ips.prefix(used.length);
        edge = new boolean[n][n];
        visited = new boolean[n];
        instk = new boolean[n];


        boolean[] valid = new boolean[1 << n];
        for (int s = 0; s < (1 << n); s++) {
            SequenceUtils.deepFill(edge, false);
            SequenceUtils.deepFill(visited, false);
            SequenceUtils.deepFill(instk, false);

            for (int[] c : conn) {
                if (Bits.bitAt(s, c[0]) + Bits.bitAt(s, c[1]) == 0) {
                    edge[c[0]][c[1]] = true;
                }
            }

            valid[s] = true;
            for (int i = 0; i < n; i++) {
                if (Bits.bitAt(s, i) == 1) {
                    continue;
                }
                valid[s] = valid[s] && !dfs(i);
            }
        }

        int inf = (int) 1e9;
        int min = inf;
        for (int i = 0; i < valid.length; i++) {
            if (!valid[i]) {
                continue;
            }
            int cost = n + Integer.bitCount(i);
            min = Math.min(min, cost);
        }

        int cnt = 0;
        for (int i = 0; i < valid.length; i++) {
            if (!valid[i]) {
                continue;
            }
            int cost = n + Integer.bitCount(i);
            if (cost != min) {
                continue;
            }
            int way = solve(conn, i);
//            debug.debug("i", Integer.toBinaryString(i));
//            debug.debug("way", way);
            cnt = mod.plus(cnt, way);
        }

        return new int[]{min, cnt};
    }

    public int solve(int[][] conn, int s) {

        dsu.reset();
        Arrays.fill(mask, 0);
        for (int[] c : conn) {
            int a = c[0];
            int b = c[1];
            if (Bits.bitAt(s, b) == 1) {
                b += n;
            }
            mask[a] |= 1 << b;
            dsu.merge(a, b);
        }
        for (int i = 0; i < n; i++) {
            if (Bits.bitAt(s, i) == 1) {
                mask[i] |= 1 << (i + n);
                dsu.merge(i, i + n);
            }
        }

        int ans = 1;
        int remain = Integer.bitCount(s) + n;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            int x = dsu.mask[dsu.find(i)];
            int way = solve(mask, x);
            way = mod.mul(way, comb.combination(remain, Integer.bitCount(x)));
            remain -= Integer.bitCount(x);
            ans = mod.mul(ans, way);
        }

        return ans;
    }

    public int solve(int[] mask, int s) {
        int m = Integer.bitCount(s);
        LongList last = lists[0];
        LongList cur = lists[1];
        int log = Log2.floorLog(Integer.highestOneBit(s));
        last.clear();
        last.add(DigitUtils.asLong(s, 1));
        for (int i = m; i > 0; i--) {
            cur.clear();
            last.sort();
            long[] data = last.getData();
            int size = last.size();
            for (int j = 0; j < size; j++) {
                int high = DigitUtils.highBit(data[j]);
                int cnt = DigitUtils.lowBit(data[j]);
                int r = j;
                while (r + 1 < size && high == DigitUtils.highBit(data[r + 1])) {
                    r++;
                    cnt = mod.plus(cnt, DigitUtils.lowBit(data[r]));
                }
                j = r;
                for (int k = 0; k <= log; k++) {
                    if (Bits.bitAt(high, k) == 0 || (mask[k] & high) != 0) {
                        continue;
                    }
                    cur.add(DigitUtils.asLong(Bits.setBit(high, k, false), cnt));
                }
            }
            LongList tmp = cur;
            cur = last;
            last = tmp;
        }

        int ans = 0;
        for (int i = 0; i < last.size(); i++) {
            ans = mod.plus(ans, DigitUtils.lowBit(last.get(i)));
        }
        return ans;
    }

    public boolean dfs(int root) {
        if (visited[root]) {
            return instk[root];
        }
        visited[root] = instk[root] = true;

        for (int i = 0; i < n; i++) {
            if (!edge[root][i]) {
                continue;
            }
            if (dfs(i)) {
                return true;
            }
        }

        instk[root] = false;
        return false;
    }

}

class Randomized {
    public static void shuffle(long[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            long tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static int nextInt(int l, int r) {
        return RandomWrapper.INSTANCE.nextInt(l, r);
    }

}

class IntegerPreSum {
    private int[] pre;
    private int n;

    public IntegerPreSum(int n) {
        pre = new int[n];
    }

    public void populate(IntToIntegerFunction a, int n) {
        this.n = n;
        if (n == 0) {
            return;
        }
        pre[0] = a.apply(0);
        for (int i = 1; i < n; i++) {
            pre[i] = pre[i - 1] + a.apply(i);
        }
    }

    public IntegerPreSum(IntToIntegerFunction a, int n) {
        this(n);
        populate(a, n);
    }

    public int prefix(int i) {
        if (i < 0) {
            return 0;
        }
        return pre[Math.min(i, n - 1)];
    }

}

class RandomWrapper {
    private Random random;
    public static RandomWrapper INSTANCE = new RandomWrapper(new Random());

    public RandomWrapper() {
        this(new Random());
    }

    public RandomWrapper(Random random) {
        this.random = random;
    }

    public int nextInt(int l, int r) {
        return random.nextInt(r - l + 1) + l;
    }

}

class DigitUtils {
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

class Bits {
    private Bits() {
    }

    public static int bitAt(int x, int i) {
        return (x >>> i) & 1;
    }

    public static int setBit(int x, int i, boolean v) {
        if (v) {
            x |= 1 << i;
        } else {
            x &= ~(1 << i);
        }
        return x;
    }

}

class DSU {
    protected int[] p;
    protected int[] rank;
    int[] mask;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        mask = new int[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            mask[i] = 1 << i;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        return p[a] = find(p[a]);
    }

    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        mask[a] |= mask[b];
        p[b] = a;
    }

}

class SequenceUtils {
    public static void deepFill(Object array, boolean val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof boolean[]) {
            boolean[] intArray = (boolean[]) array;
            Arrays.fill(intArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

    public static boolean equal(long[] a, int al, int ar, long[] b, int bl, int br) {
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

interface IntCombination {
}

interface IntToIntegerFunction {
    int apply(int x);

}

class LongList implements Cloneable {
    private int size;
    private int cap;
    private long[] data;
    private static final long[] EMPTY = new long[0];

    public long[] getData() {
        return data;
    }

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

    public long get(int i) {
        checkRange(i);
        return data[i];
    }

    public void add(long x) {
        ensureSpace(size + 1);
        data[size++] = x;
    }

    public void addAll(long[] x, int offset, int len) {
        ensureSpace(size + len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(LongList list) {
        addAll(list.data, 0, list.size);
    }

    public void sort() {
        if (size <= 1) {
            return;
        }
        Randomized.shuffle(data, 0, size);
        Arrays.sort(data, 0, size);
    }

    public int size() {
        return size;
    }

    public long[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public void clear() {
        size = 0;
    }

    public String toString() {
        return Arrays.toString(toArray());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LongList)) {
            return false;
        }
        LongList other = (LongList) obj;
        return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
    }

    public int hashCode() {
        int h = 1;
        for (int i = 0; i < size; i++) {
            h = h * 31 + Long.hashCode(data[i]);
        }
        return h;
    }

    public LongList clone() {
        LongList ans = new LongList();
        ans.addAll(this);
        return ans;
    }

}

class Modular {
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

    public String toString() {
        return "mod " + m;
    }

}

class ModPrimeInverseNumber implements InverseNumber {
    int[] inv;

    public ModPrimeInverseNumber(int[] inv, int limit, Modular modular) {
        this.inv = inv;
        inv[1] = 1;
        int p = modular.getMod();
        for (int i = 2; i <= limit; i++) {
            int k = p / i;
            int r = p % i;
            inv[i] = modular.mul(-k, inv[r]);
        }
    }

    public ModPrimeInverseNumber(int limit, Modular modular) {
        this(new int[limit + 1], limit, modular);
    }

    public int inverse(int x) {
        return inv[x];
    }

}

class Log2 {
    public static int floorLog(int x) {
        return 31 - Integer.numberOfLeadingZeros(x);
    }

}

class Factorial {
    int[] fact;
    int[] inv;
    Modular modular;

    public Modular getModular() {
        return modular;
    }

    public Factorial(int[] fact, int[] inv, InverseNumber in, int limit, Modular modular) {
        this.modular = modular;
        this.fact = fact;
        this.inv = inv;
        fact[0] = inv[0] = 1;
        for (int i = 1; i <= limit; i++) {
            fact[i] = modular.mul(fact[i - 1], i);
            inv[i] = modular.mul(inv[i - 1], in.inverse(i));
        }
    }

    public Factorial(int limit, Modular modular) {
        this(new int[limit + 1], new int[limit + 1], new ModPrimeInverseNumber(limit, modular), limit, modular);
    }

    public int fact(int n) {
        return fact[n];
    }

    public int invFact(int n) {
        return inv[n];
    }

}

class Combination implements IntCombination {
    final Factorial factorial;
    final Modular modular;

    public Combination(Factorial factorial) {
        this.factorial = factorial;
        this.modular = factorial.getModular();
    }

    public Combination(int limit, Modular modular) {
        this(new Factorial(limit, modular));
    }

    public int combination(int m, int n) {
        if (n > m) {
            return 0;
        }
        return modular.mul(modular.mul(factorial.fact(m), factorial.invFact(n)), factorial.invFact(m - n));
    }

}

interface InverseNumber {
    int inverse(int x);

}
