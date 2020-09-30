import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Deque;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.ArrayDeque;

public class LongPalindromes {
    int[][][][] bf;
    Modular mod = new Modular(1e9 + 7);
    int modVal = mod.getMod();
    ArrayIndex ai;
    int[][][][][] vectors;
    char[] sa;
    char[] sb;
    int[][] vectorWith;

    public int count(int repeats, String pattern) {
        int n = pattern.length();
        sa = pattern.toCharArray();
        sb = reverse(pattern).toCharArray();
        vectors = new int[2][2][n + 1][n + 1][];
        ai = new ArrayIndex(n + 1, 2, 2);
        vectorWith = new int[ai.totalSize()][];

        int[][] mat = new int[ai.totalSize()][];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                mat[ai.indexOf(i, j, 0)] = dp(j, 0, i, 0);
                mat[ai.indexOf(i, j, 1)] = dp(0, j, 0, i);
            }
        }
        //debug.elapse("mat");
        ModMatrix transfer = new ModMatrix(mat);
        //ModMatrix transferN = ModMatrix.pow(transfer, repeats - 1, mod);

        bf = new int[2][2][n][n];
        //debug.debug("size", ai.totalSize());
        //debug.elapse("pow");

        SequenceUtils.deepFill(bf, -1);
        //debug.debug("bf(0, 0, 0, n - 1)", bf(0, 0, 0, n - 1));

        int[] vec = new int[ai.totalSize()];
        ModMatrix initState = new ModMatrix(ai.totalSize(), 1);
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                //0
                initState.set(ai.indexOf(i, j, 0), 0, bf(j, 0, i, n - 1));
                //1
                initState.set(ai.indexOf(i, j, 1), 0, bf(0, j, 0, n - 1 - i));
                vec[ai.indexOf(i, j, 0)] = bf(j, 0, i, n - 1);
                vec[ai.indexOf(i, j, 1)] = bf(0, j, 0, n - 1 - i);
            }
        }

        ModVectorLinearRecurrenceSolver solver = new ModVectorLinearRecurrenceSolver(new ModSparseMatrix(transfer),
                vec, mod);
        //debug.elapse("init");
        //ModMatrix finalState = ModMatrix.mul(transferN, initState, mod);
        int[] finalState = solver.solve(repeats - 1);
        int ans = finalState[ai.indexOf(0, 0, 0)];//finalState.get(ai.indexOf(0, 0, 0), 0);

        return ans;
    }

    public int bf(int lLock, int rLock, int l, int r) {
        if (l > r) {
            return lLock == 0 && rLock == 0 ? 1 : 0;
        }
        if (l == r) {
            return lLock == 0 && rLock == 0 ? 2 : 1;
        }
        if (bf[lLock][rLock][l][r] == -1) {
            long ans = 0;
            if (lLock == 0 && rLock == 0) {
                //match both
                if (aChar(l) == aChar(r)) {
                    ans += bf(0, 0, l + 1, r - 1);
                }
                ans += bf(1, 0, l, r - 1);
                ans += bf(0, 1, l + 1, r);
                ans += bf(0, 0, l + 1, r - 1);
            } else if (lLock == 1) {
                if (aChar(l) == aChar(r)) {
                    ans += bf(0, 0, l + 1, r - 1);
                }
                ans += bf(1, 0, l, r - 1);
            } else if (rLock == 1) {
                if (aChar(l) == aChar(r)) {
                    ans += bf(0, 0, l + 1, r - 1);
                }
                ans += bf(0, 1, l + 1, r);
            }
            bf[lLock][rLock][l][r] = (int) (ans % modVal);
        }
        return bf[lLock][rLock][l][r];
    }

    public int[] vectorWith(int i) {
        if (vectorWith[i] == null) {
            vectorWith[i] = new int[ai.totalSize()];
            vectorWith[i][i] = 1;
        }
        return vectorWith[i];
    }

    public void plus(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] = (a[i] + b[i]) % modVal;
        }
    }

    public int aChar(int i) {
        return i >= sa.length ? '1' : sa[i];
    }

    public int bChar(int i) {
        return i >= sb.length ? '2' : sb[i];
    }

    public int[] dp(int aLock, int bLock, int a, int b) {
        if (a == sa.length) {
            return vectorWith(ai.indexOf(b, bLock, 1));
        }
        if (b == sb.length) {
            return vectorWith(ai.indexOf(a, aLock, 0));
        }
        if (vectors[aLock][bLock][a][b] == null) {
            int[] vec = vectors[aLock][bLock][a][b] = new int[ai.totalSize()];
            if (aLock == 0 && bLock == 0) {
                //lock both
                if (aChar(a) == bChar(b)) {
                    plus(vec, dp(0, 0, a + 1, b + 1));
                }
                //release both
                plus(vec, dp(0, 0, a + 1, b + 1));
                //lock one
                plus(vec, dp(1, 0, a, b + 1));
                plus(vec, dp(0, 1, a + 1, b));
            } else if (aLock == 1) {
                //lock right
                if (aChar(a) == bChar(b)) {
                    plus(vec, dp(0, 0, a + 1, b + 1));
                }
                //skip right
                plus(vec, dp(1, 0, a, b + 1));
            } else if (bLock == 1) {
                //lock right
                if (aChar(a) == bChar(b)) {
                    plus(vec, dp(0, 0, a + 1, b + 1));
                }
                //skip right
                plus(vec, dp(0, 1, a + 1, b));
            }
        }
        return vectors[aLock][bLock][a][b];
    }

    public String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

}

interface InverseNumber {
}

class IntegerArrayList implements Cloneable {
    private int size;
    private int cap;
    private int[] data;
    private static final int[] EMPTY = new int[0];

    public int[] getData() {
        return data;
    }

    public IntegerArrayList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new int[cap];
        }
    }

    public IntegerArrayList(IntegerArrayList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public IntegerArrayList() {
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

    public void addAll(int[] x, int offset, int len) {
        ensureSpace(size + len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(IntegerArrayList list) {
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
        if (!(obj instanceof IntegerArrayList)) {
            return false;
        }
        IntegerArrayList other = (IntegerArrayList) obj;
        return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
    }

    public int hashCode() {
        int h = 1;
        for (int i = 0; i < size; i++) {
            h = h * 31 + Integer.hashCode(data[i]);
        }
        return h;
    }

    public IntegerArrayList clone() {
        IntegerArrayList ans = new IntegerArrayList();
        ans.addAll(this);
        return ans;
    }

}

class Buffer<T> {
    private Deque<T> deque;
    private Supplier<T> supplier;
    private Consumer<T> cleaner;
    private int allocTime;
    private int releaseTime;

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
        allocTime++;
        return deque.isEmpty() ? supplier.get() : deque.removeFirst();
    }

    public void release(T e) {
        releaseTime++;
        cleaner.accept(e);
        deque.addLast(e);
    }

}

class ExtGCD {
    public static int extGCD(int a, int b, int[] xy) {
        if (a >= b) {
            return extGCD0(a, b, xy);
        }
        int ans = extGCD0(b, a, xy);
        SequenceUtils.swap(xy, 0, 1);
        return ans;
    }

    private static int extGCD0(int a, int b, int[] xy) {
        if (b == 0) {
            xy[0] = 1;
            xy[1] = 0;
            return a;
        }
        int ans = extGCD0(b, a % b, xy);
        int x = xy[0];
        int y = xy[1];
        xy[0] = y;
        xy[1] = x - a / b * y;
        return ans;
    }

}

class ModSparseMatrix {
    private int[] x;
    private int[] y;
    private int[] elements;
    private int n;

    public ModSparseMatrix(ModMatrix mat) {
        this.n = mat.n;
        int m = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat.mat[i][j] > 0) {
                    m++;
                }
            }
        }
        x = new int[m];
        y = new int[m];
        elements = new int[m];
        int cur = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat.mat[i][j] > 0) {
                    x[cur] = i;
                    y[cur] = j;
                    elements[cur] = mat.mat[i][j];
                    cur++;
                }
            }
        }
    }

    public ModSparseMatrix(int n, int m) {
        this.n = n;
        x = new int[m];
        y = new int[m];
        elements = new int[m];
    }

    public void rightMul(int[] v, int[] output, Modular mod) {
        Arrays.fill(output, 0);
        for (int j = 0; j < elements.length; j++) {
            output[x[j]] = mod.plus(output[x[j]], mod.mul(elements[j], v[y[j]]));
        }
    }

    public IntegerArrayList getMinimalPolynomialByRandom(Modular mod) {
        int modVal = mod.getMod();
        int[] u = new int[n];
        int[] v = new int[n];
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            u[i] = RandomWrapper.INSTANCE.nextInt(1, modVal - 1);
            v[i] = RandomWrapper.INSTANCE.nextInt(1, modVal - 1);
        }

        ModLinearFeedbackShiftRegister lfsr = new ModLinearFeedbackShiftRegister(mod, 2 * n);
        for (int i = 0; i < 2 * n; i++) {
            long ai = 0;
            for (int j = 0; j < n; j++) {
                ai += (long) u[j] * v[j] % modVal;
            }
            ai %= modVal;
            lfsr.add((int) ai);
            rightMul(v, next, mod);
            int[] tmp = next;
            next = v;
            v = tmp;
        }

        IntegerArrayList polynomials = new IntegerArrayList(lfsr.length() + 1);
        for (int i = lfsr.length(); i >= 1; i--) {
            polynomials.add(mod.valueOf(-lfsr.codeAt(i)));
        }
        polynomials.add(1);
        return polynomials;
    }

}

class IntExtGCDObject {
    private int[] xy = new int[2];

    public int extgcd(int a, int b) {
        return ExtGCD.extGCD(a, b, xy);
    }

    public int getX() {
        return xy[0];
    }

}

class ModLinearFeedbackShiftRegister {
    private IntegerArrayList cm;
    int m = -1;
    int dm;
    private IntegerArrayList cn;
    private IntegerArrayList buf;
    private IntegerArrayList seq;
    private Modular mod;
    private Power pow;

    public ModLinearFeedbackShiftRegister(Modular mod, int cap) {
        cm = new IntegerArrayList(cap + 1);
        cn = new IntegerArrayList(cap + 1);
        seq = new IntegerArrayList(cap + 1);
        buf = new IntegerArrayList(cap + 1);
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

        int factor = mod.mul(dn, pow.inverseByFermat(dm));

        int[] bufData = buf.getData();
        int[] cmData = cm.getData();
        for (int i = n - m, until = n - m + cm.size(); i < until; i++) {
            bufData[i] = mod.subtract(bufData[i], mod.mul(factor, cmData[i - (n - m)]));
        }

        if (cn.size() < buf.size()) {
            IntegerArrayList tmp = cm;
            cm = cn;
            cn = tmp;
            m = n;
            dm = dn;
        }
        {
            IntegerArrayList tmp = cn;
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

class ModVectorLinearRecurrenceSolver {
    Modular mod;
    int[][] a;
    IntegerArrayList p;
    IntegerArrayList remainder;
    Power pow;
    int n;
    int m;

    private void init(int[][] a, IntegerArrayList coe, Modular mod) {
        this.a = a;
        this.mod = mod;
        n = a[0].length;
        m = coe.size();
        pow = new Power(mod);
        remainder = new IntegerArrayList(coe.size());
        p = coe;
    }

    private int[] solve() {
        int[] ans = new int[n];
        remainder.expandWith(0, m);
        for (int i = 0; i < m; i++) {
            int r = remainder.get(i);
            for (int j = 0; j < n; j++) {
                ans[j] = mod.plus(ans[j], mod.mul(r, a[i][j]));
            }
        }
        return ans;
    }

    public int[] solve(long k) {
        Polynomials.module(k, p, remainder, pow);
        return solve();
    }

    public ModVectorLinearRecurrenceSolver(ModSparseMatrix mat, int[] vec, Modular mod) {
        IntegerArrayList coe = mat.getMinimalPolynomialByRandom(mod);
        int m = coe.size();
        int n = vec.length;
        int[][] lists = new int[m][];
        lists[0] = vec;
        for (int i = 1; i < m; i++) {
            lists[i] = new int[n];
            mat.rightMul(lists[i - 1], lists[i], mod);
        }
        init(lists, coe, mod);
    }

}

class Power implements InverseNumber {
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

    public int inverseByFermat(int x) {
        return pow(x, modular.m - 2);
    }

}

class SequenceUtils {
    public static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static void deepFill(Object array, int val) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException();
        }
        if (array instanceof int[]) {
            int[] intArray = (int[]) array;
            Arrays.fill(intArray, val);
        } else {
            Object[] objArray = (Object[]) array;
            for (Object obj : objArray) {
                deepFill(obj, val);
            }
        }
    }

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

class RandomWrapper {
    private Random random;
    public static final RandomWrapper INSTANCE = new RandomWrapper(new Random());

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

class ModMatrix {
    int[][] mat;
    int n;
    int m;

    public ModMatrix(ModMatrix model) {
        n = model.n;
        m = model.m;
        mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = model.mat[i][j];
            }
        }
    }

    public ModMatrix(int n, int m) {
        this.n = n;
        this.m = m;
        mat = new int[n][m];
    }

    public ModMatrix(int[][] mat) {
        if (mat.length == 0 || mat[0].length == 0) {
            throw new IllegalArgumentException();
        }
        this.n = mat.length;
        this.m = mat[0].length;
        this.mat = mat;
    }

    public void set(int i, int j, int val) {
        mat[i][j] = val;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                builder.append(mat[i][j]).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
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

    public int subtract(int x, int y) {
        return valueOf(x - y);
    }

    public String toString() {
        return "mod " + m;
    }

}

class Polynomials {
    public static Buffer<IntegerArrayList> listBuffer = new Buffer<>(IntegerArrayList::new, list -> list.clear());
    private static IntExtGCDObject extGCD = new IntExtGCDObject();

    public static int rankOf(IntegerArrayList p) {
        int[] data = p.getData();
        int r = p.size() - 1;
        while (r >= 0 && data[r] == 0) {
            r--;
        }
        return Math.max(0, r);
    }

    public static void normalize(IntegerArrayList list) {
        list.retain(rankOf(list) + 1);
    }

    public static void mul(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, Modular mod) {
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

    public static void divide(IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, IntegerArrayList remainder, Power pow) {
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

    public static void module(long k, IntegerArrayList p, IntegerArrayList remainder, Power pow) {
        int rP = rankOf(p);
        if (rP == 0) {
            remainder.clear();
            remainder.add(0);
            return;
        }

        IntegerArrayList a = listBuffer.alloc();
        IntegerArrayList c = listBuffer.alloc();

        module(k, a, p, c, remainder, rP, pow);

        listBuffer.release(a);
        listBuffer.release(c);
    }

    private static void module(long k, IntegerArrayList a, IntegerArrayList b, IntegerArrayList c, IntegerArrayList remainder, int rb, Power pow) {
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

class ArrayIndex {
    int[] dimensions;

    public ArrayIndex(int... dimensions) {
        this.dimensions = dimensions;
    }

    public int totalSize() {
        int ans = 1;
        for (int x : dimensions) {
            ans *= x;
        }
        return ans;
    }

    public int indexOf(int a, int b) {
        return a * dimensions[1] + b;
    }

    public int indexOf(int a, int b, int c) {
        return indexOf(a, b) * dimensions[2] + c;
    }

}
