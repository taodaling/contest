package template.datastructure;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class RangeAffineRangeSum implements Cloneable{
    private RangeAffineRangeSum left;
    private RangeAffineRangeSum right;
    private long sum;
    private int size;
    private long da;
    private long db;

    private void modify(long a, long b) {
        sum = (sum * a + b * size);
        da = (da * a) ;
        db = (db * a + b) ;
    }

    public void pushUp() {
        size = left.size + right.size;
        sum = left.sum + right.sum;
    }

    public void pushDown() {
        if (!(da == 1 && db == 0)) {
            left.modify(da, db);
            right.modify(da, db);
            da = 1;
            db = 0;
        }
    }

    public RangeAffineRangeSum(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new RangeAffineRangeSum(l, m);
            right = new RangeAffineRangeSum(m + 1, r);
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
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    private RangeAffineRangeSum deepClone() {
        RangeAffineRangeSum seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    protected RangeAffineRangeSum clone() {
        try {
            return (RangeAffineRangeSum) super.clone();
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
