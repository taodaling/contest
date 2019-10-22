package contest;

import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int e = in.readInt();
        int t = in.readInt();

        long[] bears = new long[n + 2];
        for (int i = 1; i <= n; i++) {
            bears[i] = in.readInt();
        }
        bears[n + 1] = e;

        Segment seg1 = new Segment(0, n);
        Segment seg2 = new Segment(0, n);

        long[] dp = new long[n + 1];
        seg1.update(0, 0, 0, n, dp[0] + bears[1] - bears[0] - 3 * bears[1] + 2 * bears[1]);
        seg2.update(0, 0, 0, n, dp[0] + bears[1] - bears[0] - 3 * bears[1]);

        int j = -1;
        for(int i = 1; i <= n; i++){
            while(j + 1 < n && bears[j + 2] * 2 <= bears[i] * 2 - t){
                j++;
            }
            long ans1 = seg1.query(j + 1, i - 1, 0, n) + bears[i] + t;
            long ans2 = seg2.query(0, j, 0, n) + 3 * bears[i];
            dp[i] = Math.min(ans1, ans2);

            seg1.update(i, i, 0, n, dp[i] + bears[i + 1] - bears[i] - 3 * bears[i + 1] + 2 * bears[i + 1]);
            seg2.update(i, i, 0, n, dp[i] + bears[i + 1] - bears[i] - 3 * bears[i + 1]);
        }

        long ans = e - bears[n] + dp[n];
        out.println(ans);
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long min = (long) 1e18;

    public void set(long x) {
        min = x;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {}

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
            set(x);
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
            return (long) 1e18;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.min(left.query(ll, rr, l, m), right.query(ll, rr, m + 1, r));
    }
}
