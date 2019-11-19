package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

import java.util.ArrayList;
import java.util.List;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }
        dfs(nodes[1], null);
        Segment seg = new Segment(1, n);
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Power pow = new NumberTheory.Power(mod);
        int invN = pow.inverse(n);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int v = in.readInt();
                int d = in.readInt();
                seg.update(nodes[v].l, nodes[v].l, 1, n, d);
            } else {
                int v = in.readInt();
                long total = seg.query(1, n, 1, n);
                long self = seg.query(nodes[v].l, nodes[v].r, 1, n);
                long subtree = seg.query(nodes[v].l + 1, nodes[v].r, 1, n);
                long outside = total - subtree - self;
                int exp = 0;
                exp = mod.plus(exp, mod.mul(mod.valueOf(outside), mod.mul(nodes[v].size, invN)));
                exp = mod.plus(exp, self);
                exp = mod.plus(exp, mod.mul(mod.valueOf(subtree), mod.mul(n - nodes[v].size + 1, invN)));
                out.println(exp);
            }
        }
    }

    int dfn = 1;

    public void dfs(Node root, Node p) {
        root.size = 1;
        root.l = dfn++;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.size += node.size;
        }
        root.r = dfn - 1;
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int size;
    int l;
    int r;
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long sum;
    private int size;

    private void inc(long x) {
        sum += x;
    }

    public void pushUp() {
        sum = left.sum + right.sum;
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            inc(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
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
        int m = (l + r) >> 1;
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }
}
