package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class DCampus {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] U = new Node[n];
        Node[] M = new Node[n];
        for (int i = 0; i < n; i++) {
            U[i] = new Node();
            M[i] = new Node();
        }
        Node[] cloneU = U.clone();
        Node[] cloneM = M.clone();
        Query[] queries = new Query[m];
        for (int i = 0; i < m; i++) {
            char c = in.rc();
            queries[i] = new Query();
            queries[i].t = c;
            queries[i].a = in.ri() - 1;
            if (c == 'U' || c == 'M') {
                queries[i].b = in.ri() - 1;
                if (c == 'U') {
                    Node node = new Node();
                    node.left = U[queries[i].a];
                    node.right = U[queries[i].b];
                    U[queries[i].a] = node;
                    U[queries[i].b] = null;
                } else {
                    Node node = new Node();
                    node.left = M[queries[i].a];
                    node.right = M[queries[i].b];
                    M[queries[i].a] = node;
                    M[queries[i].b] = null;
                }
            }
        }

        indicator = 0;
        for (Node node : U) {
            if (node == null) {
                continue;
            }
            allocId(node);
        }
        indicator = 0;
        for (Node node : M) {
            if (node == null) {
                continue;
            }
            allocId(node);
        }
        U = cloneU;
        M = cloneM;
        int[][] rangeU = new int[n][2];
        int[][] rangeM = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                rangeU[i][j] = U[i].id;
                rangeM[i][j] = M[i].id;
            }
        }

        RangeAffineRangeSum st = new RangeAffineRangeSum(0, n - 1);
        st.init(0, n - 1, i -> 0);
        int now = 0;

        MultiWayStack<Query> delete = new MultiWayStack<>(m + 1, m);
        MultiWayStack<Query> add = new MultiWayStack<>(m + 1, m);
        for (Query q : queries) {
            now++;
            if (q.t == 'U') {
                //merge
            } else if (q.t == 'M') {
                merge(rangeM[q.a], rangeM[q.b]);
            } else if (q.t == 'A') {
            } else if (q.t == 'Z') {
                st.update(rangeM[q.a][0], rangeM[q.a][1], 0, n - 1, 0, now);
            } else if (q.t == 'Q') {
                int lastPurgeTime = (int) st.query(M[q.a].id, M[q.a].id, 0, n - 1);
                delete.addLast(lastPurgeTime, q);
                add.addLast(now, q);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                rangeU[i][j] = U[i].id;
                rangeM[i][j] = M[i].id;
            }
        }
        st.init(0, n - 1, i -> 0);
        now = 0;

        for (Query q : queries) {
            now++;
            for (Query x : delete.getStack(now)) {
                x.ans -= st.query(U[x.a].id, U[x.a].id, 0, n - 1);
            }
            for (Query x : add.getStack(now)) {
                x.ans += st.query(U[x.a].id, U[x.a].id, 0, n - 1);
            }

            if (q.t == 'U') {
                //merge
                merge(rangeU[q.a], rangeU[q.b]);
            } else if (q.t == 'M') {
            } else if (q.t == 'A') {
                int len = rangeU[q.a][1] - rangeU[q.a][0] + 1;
                st.update(rangeU[q.a][0], rangeU[q.a][1], 0, n - 1, 1, len);
            } else if (q.t == 'Z') {
            } else if (q.t == 'Q') {
                out.println(q.ans);
            }
        }

    }

    public void merge(int[] a, int[] b) {
        a[0] = Math.min(a[0], b[0]);
        a[1] = Math.max(a[1], b[1]);
    }

    int indicator;

    public void allocId(Node root) {
        if (root.left == null) {
            root.id = indicator++;
            return;
        }
        allocId(root.left);
        allocId(root.right);
    }

}

class Query {
    char t;
    int a;
    int b;
    long ans;
}

class Node {
    Node left;
    Node right;
    int id;
}

class RangeAffineRangeSum implements Cloneable {
    private RangeAffineRangeSum left;
    private RangeAffineRangeSum right;
    private long sum;
    private long da;
    private long db;

    private void modify(long a, long b) {
        sum = (sum * a + b);
        da = (da * a);
        db = (db * a + b);
    }

    public void pushUp() {
    }

    public void pushDown() {
        if (!(da == 1 && db == 0)) {
            left.modify(da, db);
            right.modify(da, db);
            da = 1;
            db = 0;
        }
    }

    public RangeAffineRangeSum(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new RangeAffineRangeSum(l, m);
            right = new RangeAffineRangeSum(m + 1, r);
            pushUp();
        }
    }

    public void init(int l, int r, IntToIntegerFunction function) {
        da = 1;
        db = 0;
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.init(l, m, function);
            right.init(m + 1, r, function);
            pushUp();
        } else {
            sum = function.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long a, long b) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(a, b);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, a, b);
        right.update(ll, rr, m + 1, r, a, b);
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
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    private RangeAffineRangeSum deepClone() {
        RangeAffineRangeSum seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    protected RangeAffineRangeSum clone() {
        try {
            return (RangeAffineRangeSum) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(sum).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

}
