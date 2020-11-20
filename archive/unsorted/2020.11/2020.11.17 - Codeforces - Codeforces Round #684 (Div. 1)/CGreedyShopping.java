package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class CGreedyShopping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        Segment seg = new Segment(1, n, i -> a[i - 1]);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int x = in.readInt();
            int y = in.readInt();
            if (t == 1) {
                int index = seg.bs(1, n, y);
                if (index >= 0) {
                    seg.update(index, x, 1, n, y);
                }
            } else {
                Sum sum = new Sum();
                sum.size = 0;
                sum.remain = y;
                seg.query(x, n, 1, n, sum);
                out.println(sum.size);
            }
        }
    }
}

class Sum {
    int size;
    long remain;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int min;
    private int setDirty;
    private long sum;
    private int size;

    private void modify(int x) {
        setDirty = x;
        sum = (long) x * size;
        min = setDirty;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        sum = left.sum + right.sum;
        size = left.size + right.size;
    }

    public void pushDown() {
        if (setDirty != 0) {
            left.modify(setDirty);
            right.modify(setDirty);
            setDirty = 0;
        }
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            size = 1;
            modify(func.apply(l));
        }
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, Sum x) {
        if (leave(ll, rr, l, r) || min > x.remain) {
            return;
        }
        if (enter(ll, rr, l, r) && sum <= x.remain) {
            x.size += size;
            x.remain -= sum;
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m, x);
        right.query(ll, rr, m + 1, r, x);
    }

    public int bs(int l, int r, int x) {
        if (min >= x) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        int ans = left.bs(l, m, x);
        if (ans == -1) {
            ans = right.bs(m + 1, r, x);
        }
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
            builder.append(min).append(",");
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
