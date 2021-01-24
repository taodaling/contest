package template.datastructure;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;

public class ActiveSegment implements Cloneable {
    private ActiveSegment left;
    private ActiveSegment right;
    private long min;
    private long minWeight;
    private long dirty;


    private void modify(long x) {
        min += x;
        dirty += x;
        assert min >= 0;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        minWeight = 0;
        if (min == left.min) {
            minWeight += left.minWeight;
        }
        if (min == right.min) {
            minWeight += right.minWeight;
        }
    }

    public void pushDown() {
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }

    public ActiveSegment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new ActiveSegment(l, m);
            right = new ActiveSegment(m + 1, r);
        } else {
        }
    }

    public void init(int l, int r, IntToLongFunction weight) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.init(l, m, weight);
            right.init(m + 1, r, weight);
            pushUp();
        } else {
            min = 0;
            minWeight = weight.apply(l);
        }
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    /**
     * query the unactive cell weight
     */
    public long query(int ll, int rr, int l, int r) {
        if (leave(ll, rr, l, r) || min > 0) {
            return 0;
        }
        if (enter(ll, rr, l, r)) {
            return minWeight;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    public long queryNonActive() {
        return min == 0 ? minWeight : 0;
    }

    public long queryActive(long totalWeight) {
        return totalWeight - queryNonActive();
    }

    private ActiveSegment deepClone() {
        ActiveSegment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected ActiveSegment clone() {
        try {
            return (ActiveSegment) super.clone();
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
