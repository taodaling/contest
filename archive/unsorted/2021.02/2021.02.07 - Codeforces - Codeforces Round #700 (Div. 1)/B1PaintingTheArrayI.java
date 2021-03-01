package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class B1PaintingTheArrayI {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Segment st = new Segment(0, n);
        st.updateSet(0, 0, 0, n, 1);
        for (int i = 0; i < n - 1; i++) {
            int cand = st.query(a[i + 1], a[i + 1], 0, n);
            cand = Math.max(cand, st.query(0, a[i + 1] - 1, 0, n) + 1);
            cand = Math.max(cand, st.query(a[i + 1] + 1, n, 0, n) + 1);
            if (a[i] != a[i + 1]) {
                st.update(0, n, 0, n, 1);
            }
            st.updateSet(a[i], a[i], 0, n, cand);
        }
        int ans = st.query(0, n, 0, n);
        out.println(ans);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    int max;
    int dirty;

    private void modify(int x) {
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
            max = (int) -1e9;
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

    public void updateSet(int L, int R, int l, int r, int x) {
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

    public int query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return (int) -1e9;
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