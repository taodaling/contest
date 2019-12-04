package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskQ {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] h = new int[n];
        for (int i = 0; i < n; i++) {
            h[i] = in.readInt();
        }
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        Segment inc = new Segment(0, n);
        for (int i = 0; i < n; i++) {
            long x = inc.queryMax(0, h[i], 0, n);
            inc.update(h[i], h[i], 0, n, x + a[i]);
        }
        long ans = inc.queryMax(0, n, 0, n);

        out.println(ans);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long max;

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
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
            max = x;
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return max;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.max(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }
}

