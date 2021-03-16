package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.LongComparator;
import template.primitve.generated.datastructure.LongMinQueue;

import java.util.Arrays;

public class BStrip {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int s = in.ri();
        int l = in.ri();
        long[] a = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.ri();
        }
        a[0] = (long) 1e18;
        int inf = (int) 1e9;
        Segment st = new Segment(0, n, i -> i == 0 ? 0 : inf);
        LongMinQueue minQueue = new LongMinQueue(n + 1, LongComparator.NATURE_ORDER);
        LongMinQueue maxQueue = new LongMinQueue(n + 1, LongComparator.REVERSE_ORDER);
        for (int i = 1; i <= n; i++) {
            minQueue.addLast(a[i]);
            maxQueue.addLast(a[i]);
            while (!maxQueue.isEmpty() && maxQueue.min() - minQueue.min() > s) {
                maxQueue.removeFirst();
                minQueue.removeFirst();
            }
            int L = i - maxQueue.size();
            int R = i - l;
            int best = st.queryL(L, R, 0, n) + 1;
            st.update(i, i, 0, n, best);
        }
        int ans = st.queryL(n, n, 0, n);
        out.println(ans >= inf ? -1 : ans);
    }
}

class Segment implements Cloneable {
    static int inf = (int) 1e9;
    private Segment left;
    private Segment right;
    int min;

    private void modify(int x) {
        min = x;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
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
            min = func.apply(l);
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r, int x) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m, x);
        right.update(L, R, m + 1, r, x);
        pushUp();
    }

    public int query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return inf;
        }
        if (enter(L, R, l, r)) {
            return min;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryL(L, R, l, m),
                right.queryL(L, R, m + 1, r));
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

