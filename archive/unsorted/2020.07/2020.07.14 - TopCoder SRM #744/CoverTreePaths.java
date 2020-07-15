package contest;

import template.math.DigitUtils;
import template.utils.Buffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoverTreePaths {
    public long minimumCost(int n, int[] p, int[] d, int[] c, int[] params) {
        int t = d.length;
        p = Arrays.copyOf(p, n - 1);
        d = Arrays.copyOf(d, n);
        c = Arrays.copyOf(c, n);
        for (int i = t - 1; i <= n - 2; i++) {
            p[i] = (int) (((long) params[0] * p[i - 1] + params[1]) % (i + 1));
        }
        for (int i = t; i <= n - 1; i++) {
            d[i] = (int) (1 + (((long) params[2] * d[i - 1] + params[3]) % params[4]));
        }

        for (int i = t; i <= n - 1; i++) {
            c[i] = (int) (1 + (((long) params[5] * c[i - 1] + params[6]) % params[7]));
        }

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].d = d[i];
            nodes[i].c = c[i];
        }
        for (int i = 1; i < n; i++) {
            Node fa = nodes[p[i - 1]];
            fa.adj.add(nodes[i]);
        }

        dfs(nodes[0], 0);
        SparseSegment seg = dp(nodes[0]);
        long ans = seg.query(1, limit);

        return ans;
    }

    public void dfs(Node root, int ps) {
        root.ps = ps;
        ps = Math.max(ps, root.d);
        for (Node node : root.adj) {
            dfs(node, ps);
        }
    }

    int limit = (int) 1e6;

    public SparseSegment dp(Node root) {
        SparseSegment cur = new SparseSegment();
        for (Node node : root.adj) {
            SparseSegment sub = dp(node);
            cur = SparseSegment.merge(cur, sub);
        }
        int l = Math.max(root.ps, root.d);
        cur.updateMin(l + 1, limit, 1, limit, root.c);
        cur.updateFill(root.ps + 1, root.d, 1, limit, root.c);
        return cur;
    }
}


class Node {
    int c;
    int d;
    int ps;
    List<Node> adj = new ArrayList<>();
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}

class SparseSegment {
    private SparseSegment left = NIL;
    private SparseSegment right = NIL;
    public static final SparseSegment NIL = new SparseSegment();
    private long fill;
    private long plus;
    private long min;
    private long max;

    public void fill(long f) {
        fill = f;
        plus = 0;
        min = f;
        max = f;
    }

    public void plus(long p) {
        plus += p;
        min += p;
        max += p;
    }

    static {
        NIL.left = NIL.right = NIL;
    }

    private SparseSegment getLeft() {
        if (left == NIL) {
            left = new SparseSegment();
        }
        return left;
    }

    private SparseSegment getRight() {
        if (right == NIL) {
            right = new SparseSegment();
        }
        return right;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
        if (fill != 0) {
            getLeft().fill(fill);
            getRight().fill(fill);
            fill = 0;
        }
        if (plus != 0) {
            getLeft().plus(plus);
            getRight().plus(plus);
            plus = 0;
        }
    }

    public SparseSegment(int l, int r) {
    }

    public SparseSegment() {
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updateFill(int ll, int rr, int l, int r, long f) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            fill(f);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        getLeft().updateFill(ll, rr, l, m, f);
        getRight().updateFill(ll, rr, m + 1, r, f);
        pushUp();
    }

    public void updateMin(int ll, int rr, int l, int r, long f) {
        if (noIntersection(ll, rr, l, r) || max <= f) {
            return;
        }
        if (covered(ll, rr, l, r) && min >= f) {
            fill(f);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        getLeft().updateMin(ll, rr, l, m, f);
        getRight().updateMin(ll, rr, m + 1, r, f);
        pushUp();
    }


    public static SparseSegment merge(SparseSegment a, SparseSegment b) {
        if (b.max == b.min) {
            a.plus(b.max);
            return a;
        }
        if (a.max == a.min) {
            b.plus(a.max);
            return b;
        }
        a.pushDown();
        b.pushDown();
        a.left = merge(a.left, b.left);
        a.right = merge(a.right, b.right);
        a.pushUp();
        return a;
    }

    public long query(int l, int r) {
        if (min == max) {
            return min * (r - l + 1);
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return getLeft().query(l, m) +
                getRight().query(m + 1, r);
    }
}