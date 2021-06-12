package on2021_06.on2021_06_07_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.F__Interval_Graph;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class FIntervalGraph {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[][] pts = new long[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                pts[i][j] = in.rl();
            }
        }
        int[] L = new int[n];
        Arrays.sort(pts, Comparator.comparingLong(x -> x[1]));
        for (int i = 0; i < n; i++) {
            int l = 0;
            int r = i;
            while (l < r) {
                int m = (l + r) / 2;
                if (pts[m][1] < pts[i][0]) {
                    l = m + 1;
                } else {
                    r = m;
                }
            }
            L[i] = l;
        }

        debug.debug("pts", pts);
        debug.debug("L", L);
        Segment st = new Segment(0, n - 1);
        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debugArray("pts[i]", pts[i]);
            debug.debug("st", st);
            if (L[i] == i) {
                st.update(0, i, 0, n - 1, pts[i][2]);
            } else {
                //use
                long localBest = st.query(0, L[i], 0, n - 1) + pts[i][2];
                st.updateSet(i, i, 0, n - 1, localBest);
            }
        }

        long ans = st.query(0, n - 1, 0, n - 1);
        out.println(ans);
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    long max;
    long dirty;

    private void modify(long x) {
        dirty += x;
        max += x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
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

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r, long x) {
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

    public void updateSet(int L, int R, int l, int r, long x) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            max = Math.max(max, x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updateSet(L, R, l, m, x);
        right.updateSet(L, R, m + 1, r, x);
        pushUp();
    }

    public long query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return 0;
        }
        if (enter(L, R, l, r)) {
            return max;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.query(L, R, l, m),
                right.query(L, R, m + 1, r));
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
            builder.append(max).append(",");
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

