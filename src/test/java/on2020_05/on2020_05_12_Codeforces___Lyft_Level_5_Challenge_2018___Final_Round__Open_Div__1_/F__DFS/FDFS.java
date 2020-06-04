package on2020_05.on2020_05_12_Codeforces___Lyft_Level_5_Challenge_2018___Final_Round__Open_Div__1_.F__DFS;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashSet;

import java.util.ArrayList;
import java.util.List;

public class FDFS {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        LongHashSet set = new LongHashSet(q, false);

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[0], null);
        Segment seg = new Segment(1, n);
        for (int i = 0; i < q; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            if (a.l > b.l) {
                Node tmp = a;
                a = b;
                b = tmp;
            }

            long key = DigitUtils.asLong(Math.min(a.id, b.id), Math.max(a.id, b.id));
            if (set.contain(key)) {
                set.remove(key);
                seg.update(b.l, b.r, 1, n, -1);
                if (a.l <= b.l && a.r >= b.r) {
                    //ancestor
                    seg.update(1, n, 1, n, -1);
                    seg.update(a.l + 1, a.r, 1, n, 1);
                } else {
                    seg.update(a.l, a.r, 1, n, -1);
                }
            } else {
                set.add(key);
                seg.update(b.l, b.r, 1, n, 1);
                if (a.l <= b.l && a.r >= b.r) {
                    //ancestor
                    seg.update(1, n, 1, n, 1);
                    seg.update(a.l + 1, a.r, 1, n, -1);
                } else {
                    seg.update(a.l, a.r, 1, n, 1);
                }
            }

            int ans = seg.max == set.size() ? seg.cnt : 0;
            out.println(ans);
        }
    }

    int order;

    public void dfs(Node root, Node p) {
        root.l = ++order;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
        root.r = order;
    }
}

class Node {
    int id;
    int l;
    int r;
    List<Node> next = new ArrayList<>();
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    int max;
    int cnt;
    private int dirty;

    private void modify(int x) {
        dirty += x;
        max += x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
        cnt = 0;
        if (max == left.max) {
            cnt += left.cnt;
        }
        if (max == right.max) {
            cnt += right.cnt;
        }
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
            cnt = 1;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m);
        right.query(ll, rr, m + 1, r);
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

