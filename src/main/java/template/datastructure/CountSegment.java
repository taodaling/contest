package template.datastructure;

import template.math.DigitUtils;

public class CountSegment implements Cloneable {
    private static final CountSegment NIL = new CountSegment();
    private CountSegment left = NIL;
    private CountSegment right = NIL;
    private long sum;
    private long dirty;

    static {
        NIL.left = NIL.right = NIL;
    }

    public void pushUp() {
        sum = left.sum + right.sum;
    }

    public void pushDown(int l, int r) {
        if (left == NIL) {
            left = left.clone();
            right = right.clone();
        }
        if (dirty != 0) {
            int m = DigitUtils.floorAverage(l, r);
            left.modify(l, m, dirty);
            right.modify(m + 1, r, dirty);
            dirty = 0;
        }
    }

    public void modify(int l, int r, long d) {
        assert this != NIL;
        dirty += d;
        sum += (r - l + 1) * d;
    }

    public CountSegment() {
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int mod) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(l, r, mod);
            return;
        }
        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, mod);
        right.update(ll, rr, m + 1, r, mod);
        pushUp();
    }


    public int kth(int l, int r, long k) {
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        pushDown(l, r);
        if (left.sum >= k) {
            return left.kth(l, m, k);
        }
        return right.kth(m + 1, r, k - left.sum);
    }

    public long query(int ll, int rr, int l, int r) {
        if (leave(ll, rr, l, r)) {
            return 0;
        }
        if (enter(ll, rr, l, r)) {
            return sum;
        }
        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    public CountSegment merge(int l, int r, CountSegment segment) {
        if (this == NIL) {
            return segment;
        } else if (segment == NIL) {
            return this;
        }
        if (l == r) {
            sum += segment.sum;
        }
        int m = DigitUtils.floorAverage(l, r);
        left = merge(l, m, segment.left);
        right = merge(m + 1, r, segment.right);
        pushUp();
        return this;
    }

    @Override
    public CountSegment clone() {
        try {
            return (CountSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}