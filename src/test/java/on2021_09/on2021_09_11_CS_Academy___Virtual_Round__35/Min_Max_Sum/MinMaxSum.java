package on2021_09.on2021_09_11_CS_Academy___Virtual_Round__35.Min_Max_Sum;



import template.datastructure.RMQBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.FastPow2;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;

public class MinMaxSum {
    int mod = (int) 1e9 + 7;
    FastPow2 p2 = new FastPow2(2, mod);
    long inv2 = (mod + 1) / 2;
    long[] fp2;
    long[] fpInv2;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        a = in.ri(n);
        fp2 = new long[n + 1];
        fpInv2 = new long[n + 1];
        for (int i = 0; i <= n; i++) {
            fp2[i] = p2.pow(i);
            fpInv2[i] = p2.inverse(i);
        }
        rangeMax = new RMQBeta(n, (i, j) -> -Integer.compare(a[i], a[j]));
        rangeMin = new RMQBeta(n, (i, j) -> Integer.compare(a[i], a[j]));
        ps = new LongPreSum(i -> a[i], n);
        ps2L = new LongPreSum(i -> seq(i), n);
        ps2R = new LongPreSum(i -> seq(n - 1 - i), n);
//        psL = new LongPreSum(i -> a[i] * seq(i) % mod, n);
//        psR = new LongPreSum(i -> a[i] * seq(n - 1 - i) % mod, n);
        sl = new Segment(0, n - 1, i -> seq(n - 1 - i));
        sr = new Segment(0, n - 1, i -> seq(i));
        dfs(0, n - 1);
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    public int seq(int k) {
        if (k == 0) {
            return 1;
        }
        return (int) fp2[k - 1];
    }

    int n;
    LongPreSum ps2L;
    LongPreSum ps2R;
    RMQBeta rangeMax;
    RMQBeta rangeMin;
    LongPreSum ps;
    int[] a;
    long ans = 0;
    Segment sl;
    Segment sr;

    public int len(int l, int r) {
        return r - l + 1;
    }

    Debug debug = new Debug(false);

    public void dfs(int l, int r) {
        if (l > r) {
            return;
        }
        int m = rangeMin.query(l, r);
        dfs(l, m - 1);
        dfs(m + 1, r);
        long contrib = 0;
        if (len(l, m - 1) <= len(m + 1, r)) {
            for (int i = l; i <= m; i++) {
                int low = m;
                int hi = r;
                int rm = a[rangeMax.query(i, m)];
                while (low < hi) {
                    int mid = (low + hi + 1) >> 1;
                    if (a[rangeMax.query(m, mid)] >= rm) {
                        hi = mid - 1;
                    } else {
                        low = mid;
                    }
                }
                contrib += ps2L.intervalSum(i, i) * (ps2R.intervalSum(m, low) % mod) % mod * rm % mod;
                contrib += ps2L.intervalSum(i, i) * (sl.query(low + 1, r, 0, n - 1) % mod) % mod;
            }
        } else {
            for (int i = m; i <= r; i++) {
                int low = l;
                int hi = m;
                int rm = a[rangeMax.query(m, i)];
                while (low < hi) {
                    int mid = (low + hi) >> 1;
                    if (a[rangeMax.query(mid, m)] >= rm) {
                        low = mid + 1;
                    } else {
                        hi = mid;
                    }
                }
                contrib += ps2L.intervalSum(low, m) % mod * ps2R.intervalSum(i, i) % mod * rm % mod;
                contrib += sr.query(l, low - 1, 0, n - 1) % mod * ps2R.intervalSum(i, i) % mod;
            }
        }
        sl.updateL(m, r, 0, n - 1, a[rangeMax.query(l, m)]);
        sr.updateR(l, m, 0, n - 1, a[rangeMax.query(m, r)]);
        contrib = contrib % mod * a[m] % mod;
        debug.debug("l", l);
        debug.debug("r", r);
        debug.debug("contrib", contrib);
        debug.debug("sl", sl);
        debug.debug("sr", sr);
        ans += contrib;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    long a;
    long b;
    long sum;
    boolean set;
    static long mod = (int) 1e9 + 7;

    private void modify(long x) {
        set = true;
        a = x;
        sum = a * b % mod;
    }

    public void pushUp() {
        a = Math.max(left.a, right.a);
        sum = left.sum + right.sum;
    }

    public void pushDown() {
        if (set) {
            set = false;
            left.modify(a);
            right.modify(a);
        }
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
            b = left.b + right.b;
            b %= mod;
        } else {
            b = func.apply(l);
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public boolean updateL(int L, int R, int l, int r, long x) {
        if (leave(L, R, l, r)) {
            return true;
        }
        if (enter(L, R, l, r) && x >= a) {
            modify(x);
            return true;
        }
        if (l == r) {
            return false;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        boolean ans = left.updateL(L, R, l, m, x);
        if (ans) {
            ans = right.updateL(L, R, m + 1, r, x);
        }
        pushUp();
        return ans;
    }

    public boolean updateR(int L, int R, int l, int r, long x) {
        if (leave(L, R, l, r)) {
            return true;
        }
        if (enter(L, R, l, r) && x >= a) {
            modify(x);
            return true;
        }
        if (l == r) {
            return false;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        boolean ans = right.updateR(L, R, m + 1, r, x);
        if (ans) {
            ans = left.updateR(L, R, l, m, x);
        }
        pushUp();
        return ans;
    }

    public long query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return 0;
        }
        if (enter(L, R, l, r)) {
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.query(L, R, l, m) +
                right.query(L, R, m + 1, r);
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(a).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}