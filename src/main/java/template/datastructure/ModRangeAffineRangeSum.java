package template.datastructure;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class ModRangeAffineRangeSum implements Cloneable {
    private ModRangeAffineRangeSum left;
    private ModRangeAffineRangeSum right;
    private static int mod;
    private long sum;
    private int size;
    private long da;
    private long db;

    public static void setMod(int x) {
        mod = x;
    }

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

    public void init(int l, int r, IntToIntegerFunction function) {
        da = 1;
        db = 0;
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.init(l, m, function);
            right.init(m + 1, r, function);
            pushUp();
        } else {
            sum = function.apply(l);
            size = 1;
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

    public long query(int ll, int rr, int l, int r) {
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
