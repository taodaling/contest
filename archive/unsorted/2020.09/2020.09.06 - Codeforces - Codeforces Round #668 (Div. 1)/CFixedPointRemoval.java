package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.Arrays;

public class CFixedPointRemoval {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();
        int[] as = new int[n];
        int[] more = new int[n];
        int inf = (int) 1e8;
        for (int i = 0; i < n; i++) {
            as[i] = in.readInt() - 1;
            more[i] = i - as[i];
            if (more[i] < 0) {
                more[i] = inf;
            }
        }

        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            int x = in.readInt();
            int y = in.readInt();
            qs[i].l = x;
            qs[i].r = n - y - 1;
        }
        Query[] sorted = qs.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a.l, b.l));
        SimplifiedDeque<Query> dq = new Range2DequeAdapter<>(i -> sorted[i], 0, q - 1);
        bit = new IntegerBIT(n);
        seg = new Segment(0, n - 1);
        for (int i = n - 1; i >= 0; i--) {
            seg.updateSet(i, i, 0, n - 1, more[i]);
            update(0, 0, false);
            while (!dq.isEmpty() && dq.peekLast().l == i) {
                Query query = dq.removeLast();
                query.ans = bit.query(query.l + 1, query.r + 1);
            }
            debug.debug("bit", bit);
        }

        for (Query query : qs) {
            out.println(query.ans);
        }
    }

    Segment seg;
    int n;
    IntegerBIT bit;

    public void update(int l, int r, boolean update) {
        if (update) {
            seg.update(l, r, 0, n - 1, -1);
        }
        while (true) {
            int first = seg.queryL(0, n - 1, 0, n - 1);
            if (first == -1) {
                break;
            }
            bit.update(first + 1, 1);
            seg.updateSet(first, first, 0, n - 1, (int) 1e8);
            seg.update(first + 1, n - 1, 0, n - 1, -1);
        }
    }
}

class Query {
    int l;
    int r;
    int ans;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static int inf = (int) 1e8;
    private int min = inf;
    private int dirty;

    private void set(int x) {
        min = x;
    }

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

    public void updateSet(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            set(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updateSet(ll, rr, l, m, x);
        right.updateSet(ll, rr, m + 1, r, x);
        pushUp();
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

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r) || min > 0) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        int ans = left.queryL(ll, rr, l, m);
        if (ans == -1) {
            ans = right.queryL(ll, rr, m + 1, r);
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
