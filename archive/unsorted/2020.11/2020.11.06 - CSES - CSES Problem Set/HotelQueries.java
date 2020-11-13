package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class HotelQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        Segment seg = new Segment(0, n - 1, i -> a[i]);
        for (int i = 0; i < m; i++) {
            int x = in.readInt();
            int l = seg.query(0, n - 1, 0, n - 1, x);
            out.println(l + 1);
            seg.update(l, l, 0, n - 1, -x);
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int max;

    private void modify(int x) {
        max += x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            max = func.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r) || max < x) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        int ans = left.query(ll, rr, l, m, x);
        if (ans == -1) {
            ans = right.query(ll, rr, m + 1, r, x);
        }
        return ans;
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