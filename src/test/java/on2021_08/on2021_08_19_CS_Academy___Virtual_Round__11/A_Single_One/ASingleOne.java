package on2021_08.on2021_08_19_CS_Academy___Virtual_Round__11.A_Single_One;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Arrays;

public class ASingleOne {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] ans = new int[n];
        Arrays.fill(ans, -1);
        int k = in.ri();
        int m = in.ri();
        int s = in.ri() - 1;

        Segment st = new Segment(0, n - 1);
        st.update(s, s, 0, n - 1, s & 1, 0);
        for (int i = 0; i < m; i++) {
            int index = in.ri() - 1;
            st.delete(index, index, 0, n - 1);
        }
        while (true) {
            debug.debug("st", st);
            PopOp res = new PopOp();
            st.query(0, n - 1, 0, n - 1, res);
            if (res.index == -1 || res.val >= Segment.inf) {
                break;
            }

            debug.debug("res.index", res.index);
            ans[res.index] = res.val;
            int L = Math.max(0, res.index - k + 1);
            int R = Math.min(n - 1, res.index + k - 1);
            int l = mirror(R - k + 1, R, res.index);
            int r = mirror(L, L + k - 1, res.index);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            int sign = res.index & 1;
            sign ^= k & 1;
            sign ^= 1;
            st.update(l, r, 0, n - 1, sign, res.val + 1);
        }
        for (int x : ans) {
            out.append(x).append(' ');
        }
    }
    Debug debug = new Debug(false);

    public int mirror(int l, int r, int i) {
        return r - (i - l);
    }
}

class PopOp {
    int val = Segment.inf;
    int index = -1;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    static int inf = (int) 1e9;
    int[] min = new int[2];
    int[] dirty = new int[]{inf, inf};
    int[] size = new int[2];

    private void modify(int index, int x) {
        if (size[index] == 0) {
            return;
        }
        min[index] = Math.min(min[index], x);
        dirty[index] = Math.min(dirty[index], x);
    }

    public void pushUp() {
        for (int i = 0; i < 2; i++) {
            min[i] = Math.min(left.min[i], right.min[i]);
            size[i] = left.size[i] + right.size[i];
        }
    }

    public void pushDown() {
        for (int i = 0; i < 2; i++) {
            if (dirty[i] != inf) {
                left.modify(i, dirty[i]);
                right.modify(i, dirty[i]);
                dirty[i] = inf;
            }
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            size[l & 1] = 1;
            min[0] = min[1] = inf;
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }


    public void update(int L, int R, int l, int r, int index, int x) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify(index, x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m, index, x);
        right.update(L, R, m + 1, r, index, x);
        pushUp();
    }

    public void invalid() {
        min[0] = min[1] = inf;
        size[0] = size[1] = 0;
    }

    public void delete(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            invalid();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.delete(L, R, l, m);
        right.delete(L, R, m + 1, r);
        pushUp();
    }

    public void query(int L, int R, int l, int r, PopOp op) {
        if (leave(L, R, l, r) || size[0] + size[1] == 0) {
            return;
        }
        if (l == r) {
            op.index = l;
            op.val = Math.min(min[0], min[1]);
            invalid();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        if (Math.min(left.min[0], left.min[1]) < Math.min(right.min[0], right.min[1])) {
            left.query(L, R, l, m, op);
        } else {
            right.query(L, R, m + 1, r, op);
        }
        pushUp();
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
            Segment ans = (Segment) super.clone();
            ans.dirty = dirty.clone();
            ans.size = size.clone();
            ans.min = min.clone();
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(Math.min(min[0], min[1])).append(",");
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

