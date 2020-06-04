package contest;

import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        int[] leftUntil = new int[n + 1];
        leftUntil[1] = -1;

        PersistentSegment[] segs = new PersistentSegment[n + 1];
        segs[0] = new PersistentSegment();
        for (int i = 1; i <= n; i++) {
            leftUntil[i] = segs[i - 1].query(a[i], a[i], 1, n);
            if (leftUntil[i] == -1) {
                segs[i] = segs[0].clone();
            } else {
                segs[i] = segs[leftUntil[i] - 1].clone();
            }
            segs[i].update(a[i], a[i], 1, n, i);
        }

        long[] dp = new long[n + 1];
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            if (leftUntil[i] == -1) {
                dp[i] = 0;
                continue;
            }
            dp[i] = 1 + dp[leftUntil[i] - 1];
        }

        long ans = 0;
        for (int i = 1; i <= n; i++) {
            ans += dp[i];
        }

        out.println(ans);
    }
}


class PersistentSegment implements Cloneable {
    private static final PersistentSegment NIL = new PersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    private PersistentSegment left;
    private PersistentSegment right;
    private int index = -1;

    public void pushUp() {
    }

    public void pushDown() {
        left = left.clone();
        right = right.clone();
    }

    public PersistentSegment() {
        left = right = NIL;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int val) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            index = val;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (!noIntersection(ll, rr, l, m)) {
            left = left.clone();
            left.update(ll, rr, l, m, val);
        } else {
            right = right.clone();
            right.update(ll, rr, m + 1, r, val);
        }
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (this == NIL || noIntersection(ll, rr, l, r)) {
            return -1;
        }
        if (covered(ll, rr, l, r)) {
            return index;
        }
        //pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
    }

    @Override
    public PersistentSegment clone() {
        try {
            return (PersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
