package on2021_03.on2021_03_20_Codeforces___Codeforces_Round__240__Div__1_.D__Mashmokh_and_Water_Tanks;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.IntegerFunction;

import java.util.ArrayList;
import java.util.List;

public class DMashmokhAndWaterTanks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int k = in.ri();
        int p = in.ri();
        Node[] nodes = new Node[m];
        for (int i = 0; i < m; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        int[] cnt = new int[m];
        for (Node node : nodes) {
            cnt[node.depth]++;
        }
        cnt[0] = 0;
        Segment st = new Segment(0, m - 1, i -> cnt[i]);
        Query query = new Query();
        long best = 0;
        for (int i = 0; i < m; i++) {
            query.clear(p);
            st.update(0, i - 1, 0, m - 1, 1);
            st.query(0, i, 0, m - 1, query);
            best = Math.max(best, query.size);
        }
        best = Math.min(best, k);
        out.println(best);
    }

    public void dfs(Node root, Node p) {
        root.depth = p == null ? 0 : p.depth + 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
    }

}

class Node {
    List<Node> adj = new ArrayList<>();
    int depth;
}

class Query {
    int k;
    int size;
    boolean hasMore;

    public void clear(int k) {
        this.k = k;
        size = 0;
        hasMore = true;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int size;
    private long totalCost;
    private int unitCost;

    private void modify(int x) {
        unitCost += x;
        totalCost += (long) size * x;
    }

    public void pushUp() {
        size = left.size + right.size;
        totalCost = left.totalCost + right.totalCost;
    }

    public void pushDown() {
        if (unitCost != 0) {
            left.modify(unitCost);
            right.modify(unitCost);
            unitCost = 0;
        }
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            size = func.apply(l);
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

    public void query(int L, int R, int l, int r, Query q) {
        if (leave(L, R, l, r) || !q.hasMore) {
            return;
        }
        if (enter(L, R, l, r)) {
            if (q.k >= totalCost) {
                q.k -= totalCost;
                q.size += size;
                return;
            }

            if (l == r) {
                q.size += q.k / unitCost;
                q.k %= unitCost;
                q.hasMore = false;
                return;
            }
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        right.query(L, R, m + 1, r, q);
        left.query(L, R, l, m, q);
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

