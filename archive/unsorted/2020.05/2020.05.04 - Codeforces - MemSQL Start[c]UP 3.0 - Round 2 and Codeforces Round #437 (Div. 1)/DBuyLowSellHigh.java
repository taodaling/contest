package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToIntFunction;
import template.utils.SequenceUtils;

public class DBuyLowSellHigh {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] prices = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            prices[i] = in.readInt();
        }

        PQQuery maxQuery = new PQQuery();
        PQQuery minQuery = new PQQuery();
        HallQuery hallQuery = new HallQuery();
        PQSegment pq = new PQSegment(1, n, i -> prices[i]);
        HallSegment hs = new HallSegment(1, n);

        int inf = (int) 1e9;
        long ans = 0;
        while (true) {
            maxQuery.index = -1;
            maxQuery.val = -inf;
            pq.queryMax(1, n, 1, n, maxQuery);

            if (maxQuery.val < 0) {
                break;
            }
            pq.update(maxQuery.index, maxQuery.index, 1, n);
            hs.update(maxQuery.index, n, 1, n, -1);
            hallQuery.index = -1;
            hs.query(1, n, 1, n, hallQuery);

            minQuery.index = -1;
            minQuery.val = inf;
            pq.queryMin(1, hallQuery.index, 1, n, minQuery);

            if (maxQuery.val - minQuery.val < 0) {
                //no way, restore
                hs.update(maxQuery.index, n, 1, n, 1);
                continue;
            }

            pq.update(minQuery.index, minQuery.index, 1, n);
            hs.update(minQuery.index, n, 1, n, 1);
            ans += maxQuery.val - minQuery.val;
        }

        out.println(ans);
    }
}

class PQQuery {
    int index;
    int val;
}

class PQSegment implements Cloneable {
    private PQSegment left;
    private PQSegment right;

    private int min;
    private int max;

    static int inf = (int) 1e9;

    private void modify() {
        min = inf;
        max = -inf;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {

    }

    public PQSegment(int l, int r, IntToIntFunction function) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new PQSegment(l, m, function);
            right = new PQSegment(m + 1, r, function);
            pushUp();
        } else {
            min = max = function.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m);
        right.update(ll, rr, m + 1, r);
        pushUp();
    }

    public void queryMax(int ll, int rr, int l, int r, PQQuery q) {
        if (noIntersection(ll, rr, l, r) || q.val >= max) {
            return;
        }
        if (l == r) {
            q.val = max;
            q.index = l;
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);

        if (left.max >= right.max) {
            left.queryMax(ll, rr, l, m, q);
            right.queryMax(ll, rr, m + 1, r, q);
        } else {
            right.queryMax(ll, rr, m + 1, r, q);
            left.queryMax(ll, rr, l, m, q);
        }
    }

    public void queryMin(int ll, int rr, int l, int r, PQQuery q) {
        if (noIntersection(ll, rr, l, r) || q.val <= min) {
            return;
        }
        if (l == r) {
            q.val = min;
            q.index = l;
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);

        if (left.max <= right.max) {
            left.queryMin(ll, rr, l, m, q);
            right.queryMin(ll, rr, m + 1, r, q);
        } else {
            right.queryMin(ll, rr, m + 1, r, q);
            left.queryMin(ll, rr, l, m, q);
        }
    }

    private PQSegment deepClone() {
        PQSegment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected PQSegment clone() {
        try {
            return (PQSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(min + ":" + max).append(",");
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

class HallQuery {
    int index;
}

class HallSegment implements Cloneable {
    private HallSegment left;
    private HallSegment right;
    private int dirty;
    private int min;

    private void modify(int x) {
        dirty += x;
        min += x;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }

    public HallSegment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new HallSegment(l, m);
            right = new HallSegment(m + 1, r);
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

    public void query(int ll, int rr, int l, int r, HallQuery q) {
        if (noIntersection(ll, rr, l, r) || q.index != -1 || min >= 0) {
            return;
        }
        if (l == r) {
            q.index = l;
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, q);
        right.query(ll, rr, m + 1, r, q);
    }

    private HallSegment deepClone() {
        HallSegment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected HallSegment clone() {
        try {
            return (HallSegment) super.clone();
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
