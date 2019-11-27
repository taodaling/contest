package on2019_11.on2019_11_27_AtCoder_Regular_Contest_073.F___Many_Moves;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int[] xs = new int[q + 1];
        for (int i = 1; i <= q; i++) {
            xs[i] = in.readInt();
        }
        xs[0] = a;
        Segment seg = new Segment(1, n);
        seg.update(b, b, 1, n, -seg.query(b, b, 1, n));

        for (int i = 1; i <= q; i++) {
            long left = seg.queryMinMinusIndex(1, xs[i], 1, n) + xs[i];
            long right = seg.queryMinPlusIndex(xs[i], n, 1, n) - xs[i];
            seg.update(1, n, 1, n, Math.abs(xs[i - 1] - xs[i]));

            long now = seg.query(xs[i - 1], xs[i - 1], 1, n);
            if (Math.min(left, right) < now) {
                seg.update(xs[i - 1], xs[i - 1], 1, n,
                        Math.min(left, right) - now);
            }
        }

        long ans = Long.MAX_VALUE;
        for(int i = 1; i <= n; i++){
            ans = Math.min(ans, seg.query(i, i, 1, n));
        }

        out.println(ans);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long mod;
    private long val;
    private int index;
    private long minMinusIndex;
    private long minPlusIndex;

    public void mod(long m) {
        mod += m;
        val += m;
        minPlusIndex += m;
        minMinusIndex += m;
    }

    public void pushUp() {
        minMinusIndex = Math.min(left.minMinusIndex, right.minMinusIndex);
        minPlusIndex = Math.min(left.minPlusIndex, right.minPlusIndex);
        val = Math.min(left.val, right.val);
    }

    public void pushDown() {
        if (mod != 0) {
            left.mod(mod);
            right.mod(mod);
            mod = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            index = l;
            val = (long) 1e18;
            minMinusIndex = val - index;
            minPlusIndex = val + index;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long mod) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            mod(mod);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, mod);
        right.update(ll, rr, m + 1, r, mod);
        pushUp();
    }

    public long queryMinMinusIndex(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return Long.MAX_VALUE;
        }
        if (covered(ll, rr, l, r)) {
            return minMinusIndex;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.min(left.queryMinMinusIndex(ll, rr, l, m),
                right.queryMinMinusIndex(ll, rr, m + 1, r));
    }

    public long queryMinPlusIndex(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return Long.MAX_VALUE;
        }
        if (covered(ll, rr, l, r)) {
            return minPlusIndex;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.min(left.queryMinPlusIndex(ll, rr, l, m),
                right.queryMinPlusIndex(ll, rr, m + 1, r));
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return Long.MAX_VALUE;
        }
        if (covered(ll, rr, l, r)) {
            return val;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.min(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
    }
}

