package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class CMonsterInvaders {
    long inf = (long) 1e18;
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long r1 = in.readInt();
        long r2 = in.readInt();
        long r3 = in.readInt();
        long rMin = Math.min(Math.min(r1, r2), r3);
        long d = in.readLong();
        int[] a = new int[n];
        in.populate(a);

        Segment seg = new Segment(1, n);
        seg.updateSet(1, 1, 1, n, 0);
        long ans = inf;
        for (int i = 1; i <= n; i++) {
            long cnt = a[i - 1];
            //use 1
            long cost1 = r1 * cnt + r1 + rMin;
            //use 2
            long cost2 = r2 + rMin;
            long cost3 = r1 * cnt + r3;
            long costMin = Math.min(cost3, Math.min(cost1, cost2));

            if (i < n) {
                long cur = seg.queryL(i, i, 1, n);
                long next = inf;
                //go back now
                long pref = seg.queryL(1, i - 1, 1, n);
                next = Math.min(next, pref + costMin + d);

                seg.updatePlus(1, i, 1, n, d * 2 + costMin + d);

                //stand here
                next = Math.min(next, cur + cost3 + d);

                seg.updateSet(i + 1, i + 1, 1, n, next);
            } else {
                for (int j = 1; j < i; j++) {
                    long pref = seg.queryL(j, j, 1, n);
                    long best = costMin + pref;
                    ans = Math.min(ans, best);
                }
                cost1 += 2 * d;
                cost2 += 2 * d;
                costMin = Math.min(cost3, Math.min(cost1, cost2));
                for (int j = 1; j <= i; j++) {
                    long pref = seg.queryL(j, j, 1, n);
                    long best = costMin + pref - (i - j) * d;
                    ans = Math.min(ans, best);
                }
            }

            debug.debug("dp", seg);
        }

        out.println(ans);

    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static long inf = (long) 1e18;
    private long min = inf;
    private long plus;

    private void set(long x) {
        min = Math.min(inf, x);
    }

    private void modify(long d) {
        plus += d;
        min += d;
        plus = Math.min(plus, inf);
        min = Math.min(min, inf);
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {
        if (plus != 0) {
            left.modify(plus);
            right.modify(plus);
            plus = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updatePlus(int ll, int rr, int l, int r, long d) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(d);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updatePlus(ll, rr, l, m, d);
        right.updatePlus(ll, rr, m + 1, r, d);
        pushUp();
    }

    public void updateSet(int ll, int rr, int l, int r, long d) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            set(d);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updateSet(ll, rr, l, m, d);
        right.updateSet(ll, rr, m + 1, r, d);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
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
