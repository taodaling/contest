package template.datastructure;

import java.util.function.IntUnaryOperator;

public class SegmentBeat {
    private SegmentBeat left;
    private SegmentBeat right;
    private long first;
    private long second;
    private int firstCnt;
    private static long inf = (long) 2e18;
    private long sum;

    private void setMin(long x) {
        if (first <= x) {
            return;
        }
        sum -= (first - x) * firstCnt;
        first = x;
    }

    public void pushUp() {
        first = Math.max(left.first, right.first);
        if (left.first == right.first) {
            second = Math.max(left.second, right.second);
        } else {
            second = Math.max(Math.max(left.second, right.second), Math.min(left.first, right.first));
        }
        firstCnt = (left.first == first ? left.firstCnt : 0) + (right.first == first ? right.firstCnt : 0);
        sum = left.sum + right.sum;
    }

    public void pushDown() {
        left.setMin(first);
        right.setMin(first);
    }

    public SegmentBeat(int l, int r, IntUnaryOperator func) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new SegmentBeat(l, m, func);
            right = new SegmentBeat(m + 1, r, func);
            pushUp();
        } else {
            sum = first = func.applyAsInt(l);
            second = -inf;
            firstCnt = 1;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updateMin(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            if (first <= x) {
                return;
            }
            if (second < x) {
                setMin(x);
                return;
            }
        }
        pushDown();
        int m = (l + r) >> 1;
        left.updateMin(ll, rr, l, m, x);
        right.updateMin(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long querySum(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = (l + r) >> 1;
        return left.querySum(ll, rr, l, m) +
                right.querySum(ll, rr, m + 1, r);
    }

    public long queryMax(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return -inf;
        }
        if (covered(ll, rr, l, r)) {
            return first;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.max(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }
}
