package template.datastructure;

import java.util.function.IntUnaryOperator;

public class SegmentBeat {
    private SegmentBeat left;
    private SegmentBeat right;
    private long first;
    private long second;
    private static long inf = (long) 2e18;
    private long minTag = inf;
    private long sum;
    private int size;

    private void setMin(long x) {
        minTag = Math.min(minTag, x);
        first = Math.min(minTag, x);
    }

    public void pushUp() {
        if (left.first >= right.first) {
            first = left.first;
            second = Math.max(left.second, right.first);
        } else {
            first = right.first;
            second = Math.max(left.first, right.second);
        }
        size = left.size + right.size;
        sum = left.sum + right.sum;
    }

    public void pushDown() {
        left.setMin(minTag);
        right.setMin(minTag);
        minTag = inf;
    }

    public SegmentBeat(int l, int r, IntUnaryOperator func) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new SegmentBeat(l, m, func);
            right = new SegmentBeat(m + 1, r, func);
            pushUp();
        } else {
            first = func.applyAsInt(l);
            second = -inf;
            size = 1;
            sum = first;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updateMin(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            if (first <= x) {
                return;
            }
            if (second <= x) {
                sum -= x;
            }
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateMin(ll, rr, l, m, x);
        right.updateMin(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.query(ll, rr, l, m);
        right.query(ll, rr, m + 1, r);
    }
}
