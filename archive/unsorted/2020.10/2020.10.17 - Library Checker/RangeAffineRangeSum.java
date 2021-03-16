package contest;

import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

import java.io.PrintWriter;

public class RangeAffineRangeSum {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        Segment tree = new Segment(0, n - 1, i -> a[i]);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int l = in.readInt();
            int r = in.readInt() - 1;
            if (t == 0) {
                int b = in.readInt();
                int c = in.readInt();
                tree.update(l, r, 0, n - 1, b, c);
            } else {
                long sum = tree.queryL(l, r, 0, n - 1) % 998244353;
                out.println(sum);
            }
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    static int mod = 998244353;
    private long sum;
    private int size;
    private long da = 1;
    private long db = 0;

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

    public Segment(int l, int r, IntToIntegerFunction function) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, function);
            right = new Segment(m + 1, r, function);
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

    public void update(int ll, int rr, int l, int r, long a, long b) {
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
        return left.queryL(ll, rr, l, m) +
        right.queryL(ll, rr, m + 1, r);
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
            builder.append("val").append(",");
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
