package contest;

import template.graph.DescartesNode;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToLongFunction;

public class LockedSafe {
    long[] a;
    long[] orBuf;
    long[] andBuf;
    int n;
    long total;
    Segment st;

    public boolean ok(int l, int r, int mid) {
        return (orBuf[l] | orBuf[r]) - (andBuf[l] & andBuf[r]) >= a[mid];
    }

    Sum sumBuf = new Sum();

    public void dfs(Node root) {
        root.l = root.r = root.index;
        if (root.left != null) {
            Node left = (Node) root.left;
            dfs(left);
            root.l = left.l;
        }
        if (root.right != null) {
            Node right = (Node) root.right;
            dfs(right);
            root.r = right.r;
        }
        int l = root.l;
        int r = root.r;
        int mid = root.index;
        orBuf[mid] = andBuf[mid] = a[mid];
        if (mid - l <= r - mid) {
            for (int i = mid - 1; i >= l; i--) {
                orBuf[i] = a[i] | orBuf[i + 1];
                andBuf[i] = a[i] & andBuf[i + 1];
            }
            //choose left
            for (int i = l; i <= mid; i++) {
                sumBuf.reset(andBuf[i], orBuf[i], a[mid], mid);
                st.queryL(mid, r, 0, n - 1, sumBuf);
                if (sumBuf.ok()) {
                    total += r - sumBuf.index + 1;
                }
            }
        } else {
            //mirror
            for (int i = mid + 1; i <= r; i++) {
                orBuf[i] = a[i] | orBuf[i - 1];
                andBuf[i] = a[i] & andBuf[i - 1];
            }
            for (int i = r; i >= mid; i--) {
                sumBuf.reset(andBuf[i], orBuf[i], a[mid], mid);
                st.queryR(l, mid, 0, n - 1, sumBuf);
                if (sumBuf.ok()) {
                    total += sumBuf.index - l + 1;
                }
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        a = in.rl(n);
        orBuf = new long[n];
        andBuf = new long[n];
        total = 0;
        st = new Segment(0, n - 1, i -> a[i]);
        Node root = DescartesNode.build(0, n - 1, (i, j) -> -Long.compare(a[i], a[j]), Node::new);
        dfs(root);
        out.println(total);
    }
}

class Node extends DescartesNode {
    int l;
    int r;
}

class Sum {
    long and;
    long or;
    long max;
    int index;

    void reset(long and, long or, long max, int index) {
        this.and = and;
        this.or = or;
        this.max = max;
        this.index = index;
    }

    public boolean ok() {
        return or - and >= max;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    long or;
    long and;

    private void modify() {
    }

    public void pushUp() {
        or = left.or | right.or;
        and = left.and & right.and;
    }

    public void pushDown() {
    }

    public Segment(int l, int r, IntToLongFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            and = or = func.apply(l);
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void queryL(int L, int R, int l, int r, Sum sum) {
        if (leave(L, R, l, r) || sum.ok()) {
            return;
        }
        if (enter(L, R, l, r)) {
            if ((sum.or | or) - (sum.and & and) < sum.max) {
                sum.or |= or;
                sum.and &= and;
                return;
            }
            if (l == r) {
                sum.or |= or;
                sum.and &= and;
                sum.index = l;
                return;
            }
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.queryL(L, R, l, m, sum);
        right.queryL(L, R, m + 1, r, sum);
    }

    public void queryR(int L, int R, int l, int r, Sum sum) {
        if (leave(L, R, l, r) || sum.ok()) {
            return;
        }
        if (enter(L, R, l, r)) {
            if ((sum.or | or) - (sum.and & and) < sum.max) {
                sum.or |= or;
                sum.and &= and;
                return;
            }
            if (l == r) {
                sum.or |= or;
                sum.and &= and;
                sum.index = l;
                return;
            }
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        right.queryR(L, R, m + 1, r, sum);
        left.queryR(L, R, l, m, sum);
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
            builder.append(and).append(",");
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
