package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class EPermutationSeparation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] perm = new int[n];
        int[] index2Value = new int[n + 1];
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            perm[i] = in.readInt();
            index2Value[perm[i]] = i;
        }
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }

        Segment segment = new Segment(1, n);
        for(int i = 1; i <= n; i++){
            segment.update(i, n, 1, n, a[index2Value[i]]);
        }

        long ans = Math.min(a[0], a[n - 1]);
        for(int i = 0; i < n - 1; i++){
            segment.update(perm[i], n, 1, n, -a[i]);
            segment.update(1, perm[i] - 1, 1, n, a[i]);
            ans = Math.min(ans, segment.query(1, n, 1, n));
        }

        out.println(ans);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long min;
    private long dirty;

    public void update(long x){
        dirty += x;
        min += x;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {
        left.update(dirty);
        right.update(dirty);
        dirty = 0;
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
            update(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return (long)1e18;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.query(ll, rr, l, m),
        right.query(ll, rr, m + 1, r));
    }
}