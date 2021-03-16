package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class FStrangeFunction {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }
        int m = in.readInt();
        IntegerHashMap map = new IntegerHashMap(m, false);
        int[] b = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            b[i] = in.readInt();
            map.put(b[i], i);
        }

        Segment seg = new Segment(0, m);
        seg.updatePlus(0, 0, 0, m, -Segment.inf);
        for (int i = 0; i < n; i++) {
            int occur = map.getOrDefault(a[i], -1);
            long contrib = seg.queryL(occur - 1, occur - 1, 0, m);
            int right = SequenceUtils.upperBound(b, a[i], 1, m);
            debug.debug("a[i]", a[i]);
            debug.debug("right", right);
            if (p[i] < 0) {
                seg.updatePlus(0, m, 0, m, p[i]);
            } else {
                seg.updatePlus(0, right - 1, 0, m, p[i]);
            }
            if (occur != -1) {
                long cur = seg.queryL(occur, occur, 0, m);
                if (cur > contrib) {
                    seg.updatePlus(occur, occur, 0, m, contrib - cur);
                }
            }
            debug.debug("i", i);
            debug.debug("seg", seg);
        }

        long ans = seg.queryL(m, m, 0, m);
        if(ans > (long)1e15){
            out.println("NO");
            return;
        }
        out.println("YES");
        out.println(ans);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    static long inf = (long) 1e18;
    private long val = inf;
    private long dirty;

    public void modify(long d) {
        val += d;
        dirty += d;
    }

    public void pushUp() {
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
            return -inf;
        }
        if (covered(ll, rr, l, r)) {
            return val;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.queryL(ll, rr, l, m),
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
            builder.append(val).append(",");
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