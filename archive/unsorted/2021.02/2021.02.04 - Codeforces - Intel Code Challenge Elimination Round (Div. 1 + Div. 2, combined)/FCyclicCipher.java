package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ExtCRT;
import template.math.LCMs;
import template.math.Permutation;

public class FCyclicCipher {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        MultiWayStack<int[]> stack = new MultiWayStack<>(m, (int) 2e5);
        int[] len = new int[n];
        for (int i = 0; i < n; i++) {
            int k = in.ri();
            len[i] = k;
            for (int j = 0; j < k; j++) {
                int x = in.ri() - 1;
                stack.addLast(x, new int[]{i, j});
            }
        }
        Sum sum = new Sum();
        Segment seg = new Segment(0, n - 1);
        for (int i = 0; i < m; i++) {
            long max = 0;
            for (int[] pos : stack.getStack(i)) {
                seg.update(pos[0], pos[0], 0, n - 1, len[pos[0]], pos[1]);
            }
            for (int[] pos : stack.getStack(i)) {
                sum.clear();
                seg.query(pos[0], n - 1, 0, n - 1, sum);
                max = Math.max(max, sum.len);
            }
            for (int[] pos : stack.getStack(i)) {
                seg.update(pos[0], pos[0], 0, n - 1, 0, 0);
            }
            out.println(max);
        }
    }
}

class Sum {
    ExtCRT crt = new ExtCRT();
    int len;

    public void clear() {
        crt.clear();
        len = 0;
    }

    public boolean add(long rm, long mod, int len) {
        if (crt.add(rm, mod)) {
            this.len += len;
            return true;
        }
        return false;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    static ExtCRT crt = new ExtCRT();

    long m;
    long r;
    boolean valid;

    private void modify(int m, int r) {
        this.m = m;
        this.r = r;
        this.valid = m > 0;
    }

    public void pushUp() {
        valid = left.valid && right.valid;
        if (valid) {
            crt.set(left.r, left.m);
            valid = crt.add(right.r, right.m);
            m = crt.m;
            r = crt.r;
        }
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            valid = false;
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r, int mod, int rm) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify(mod, rm);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m, mod, rm);
        right.update(L, R, m + 1, r, mod, rm);
        pushUp();
    }

    public boolean query(int L, int R, int l, int r, Sum sum) {
        if (leave(L, R, l, r)) {
            return true;
        }
        if (enter(L, R, l, r) && valid) {
            if (sum.add(this.r, this.m, r - l + 1)) {
                return true;
            }
        }
        if (l == r) {
            return false;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        boolean ans = left.query(L, R, l, m, sum);
        ans = ans && right.query(L, R, m + 1, r, sum);
        return ans;
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
            builder.append("val").append(",");
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
