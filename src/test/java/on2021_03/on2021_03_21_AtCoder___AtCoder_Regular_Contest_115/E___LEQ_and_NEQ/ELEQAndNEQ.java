package on2021_03.on2021_03_21_AtCoder___AtCoder_Regular_Contest_115.E___LEQ_and_NEQ;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ELEQAndNEQ {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList unique = new IntegerArrayList(a);
        unique.unique();
        int m = unique.size();
        ModRangeAffineRangeSum.mod = mod;
        ModRangeAffineRangeSum st = new ModRangeAffineRangeSum(0, m - 1);
        st.init(0, m - 1, i -> 0, i -> i == 0 ? unique.get(i) : unique.get(i) - unique.get(i - 1));
        for (int i = 0; i < n; i++) {
            a[i] = unique.binarySearch(a[i]);
        }
        st.update(0, a[0], 0, m - 1, 0, 1);
        for (int i = 1; i < n; i++) {
            int sum = st.query(0, m - 1, 0, m - 1);
            st.update(0, a[i], 0, m - 1, -1, sum);
            if (a[i] < a[i - 1]) {
                st.update(a[i] + 1, a[i - 1], 0, m - 1, 0, 0);
            }
        }
        int ans = st.query(0, m - 1, 0, m - 1);
        out.println(ans);
    }
}

class ModRangeAffineRangeSum implements Cloneable {
    private ModRangeAffineRangeSum left;
    private ModRangeAffineRangeSum right;
    public static int mod;
    private long sum;
    private int size;
    private long da;
    private long db;

    private void modify(long a, long b) {
        sum = (sum * a + b * size) % mod;
        da = (da * a) % mod;
        db = (db * a + b) % mod;
    }

    public void pushUp() {
        size = left.size + right.size;
        sum = DigitUtils.mod(left.sum + right.sum, mod);
    }

    public void pushDown() {
        if (!(da == 1 && db == 0)) {
            left.modify(da, db);
            right.modify(da, db);
            da = 1;
            db = 0;
        }
    }

    public ModRangeAffineRangeSum(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new ModRangeAffineRangeSum(l, m);
            right = new ModRangeAffineRangeSum(m + 1, r);
            pushUp();
        }
    }

    public void init(int l, int r, IntToIntegerFunction valFunc, IntToIntegerFunction sizeFunc) {
        da = 1;
        db = 0;
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.init(l, m, valFunc, sizeFunc);
            right.init(m + 1, r, valFunc, sizeFunc);
            pushUp();
        } else {
            size = sizeFunc.apply(l);
            sum = (long) valFunc.apply(l) * size % mod;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int a, int b) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(a, b);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, a, b);
        right.update(ll, rr, m + 1, r, a, b);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return (int) sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return DigitUtils.modplus(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r), mod);
    }

    private ModRangeAffineRangeSum deepClone() {
        ModRangeAffineRangeSum seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    protected ModRangeAffineRangeSum clone() {
        try {
            return (ModRangeAffineRangeSum) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(sum).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

}
