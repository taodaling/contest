package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.LongBIT;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;

public class MaximizeLetterBeauty {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        long[] vals = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            long x = in.readInt();
            vals[i] = x;
        }
        LongPreSum ps = new LongPreSum(vals);
        Segment left = new Segment(1, n, i -> ps.prefix(i));
        Segment right = new Segment(1, n, i -> ps.post(i));

        long sum = ps.intervalSum(1, n);
        debug.debug("left", left);
        debug.debug("right", right);
        for (int i = 0; i < q; i++) {
            char c = in.readChar();
            int x = in.readInt();
            int y = in.readInt();
            if (c == 'Q') {
                long lDiscard = Math.min(0, left.queryL(1, x - 1, 1, n));
                long rDiscard = Math.min(0, right.queryL(y + 1, n, 1, n));
                debug.debug("l", lDiscard);
                debug.debug("r", rDiscard);
                long ans = sum - lDiscard - rDiscard;
                out.println(ans);
            } else {
                left.updatePlus(x, n, 1, n, -vals[x]);
                right.updatePlus(1, x, 1, n, -vals[x]);
                sum -= vals[x];
                vals[x] = y;
                left.updatePlus(x, n, 1, n, vals[x]);
                right.updatePlus(1, x, 1, n, vals[x]);
                sum += vals[x];
                debug.debug("left", left);
                debug.debug("right", right);
            }
        }
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long min;
    private long dirty;

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    private void modify(long x) {
        dirty += x;
        min += x;
    }

    public void pushDown() {
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }

    public Segment(int l, int r, IntToLongFunction function) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, function);
            right = new Segment(m + 1, r, function);
            pushUp();
        } else {
            min = function.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updatePlus(ll, rr, l, m, x);
        right.updatePlus(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryL(ll, rr, l, m),
                right.queryL(ll, rr, m + 1, r));
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
            builder.append(min).append(",");
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
