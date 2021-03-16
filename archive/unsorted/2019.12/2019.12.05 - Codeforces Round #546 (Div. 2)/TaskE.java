package contest;

import template.algo.PreSum;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.TreeMap;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] k = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            k[i] = in.readInt();
        }
        long[] b = new long[n];
        for (int i = 1; i < n; i++) {
            b[i] = b[i - 1] + k[i - 1];
        }
        PreSum psB = new PreSum(b);
        long[] aa = new long[n + 1];
        for (int i = 0; i < n; i++) {
            aa[i] = a[i] - b[i];
        }
        int q = in.readInt();
        Segment seg = new Segment(0, n, aa);
        seg.updatePlus(n, n, 0, n, (long)1e18);
        for (int i = 0; i < q; i++) {
            char cmd = in.readChar();
            if (cmd == '+') {
                int index = in.readInt() - 1;
                int x = in.readInt();
                long val = seg.queryL(index, index, 0, n) + x;
                int until = seg.queryFloorIndex(val, 0, n);
                seg.updatePlus(index, until, 0, n, val);
            } else {
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                long sum = seg.queryL(l, r, 0, n) +
                        psB.intervalSum(l, r);
                out.println(sum);
            }
        }
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long sum;
    private long dirtyVal;
    private int size;
    private boolean dirty;
    private long min;

    public void pushUp() {
        sum = left.sum + right.sum;
        size = left.size + right.size;
        min = Math.min(left.min, right.min);
    }

    public void set(long val) {
        dirtyVal = val;
        dirty = true;
        sum = dirtyVal * size;
        min = dirtyVal;
    }

    public void pushDown() {
        if (dirty) {
            left.set(dirtyVal);
            right.set(dirtyVal);
            dirty = false;
        }
    }

    public Segment(int l, int r, long[] vals) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, vals);
            right = new Segment(m + 1, r, vals);
            pushUp();
        } else {
            size = 1;
            min = sum = vals[l];
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
        int m = DigitUtils.floorAverage(l, r);
        left.updatePlus(ll, rr, l, m, x);
        right.updatePlus(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.queryL(ll, rr, l, m) +
                right.queryL(ll, rr, m + 1, r);
    }

    public int queryFloorIndex(long x, int l, int r) {
        Segment seg = this;
        while (l < r) {
            seg.pushDown();
            int m = (l + r) >>> 1;
            if (seg.right.min <= x) {
                l = m + 1;
                seg = seg.right;
            } else {
                r = m;
                seg = seg.left;
            }
        }
        return l;
    }
}
