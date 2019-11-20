package on2019_11.on2019_11_20_Codeforces_Round__601__Div__1_.D___Tree_Queries1;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskD {
    NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
    NumberTheory.Power pow = new NumberTheory.Power(mod);

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

        dfs1(nodes[1], null);
        dfs2(nodes[1], null);
        Segment seg = new Segment(1, n);
        int invN = pow.inverse(n);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int v = in.readInt();
                int d = in.readInt();
                nodes[v].count = mod.plus(nodes[v].count, d);
                if (nodes[v].heavy != null) {
                    int prob = mod.mul(n - nodes[v].heavy.size, invN);
                    int plus = mod.mul(prob, d);
                    seg.update(nodes[v].heavy.l, nodes[v].heavy.r, 1, n, plus);
                }
                int prob = mod.mul(nodes[v].size, invN);
                int plus = mod.mul(prob, d);
                seg.update(1, nodes[v].l - 1, 1, n, plus);
                seg.update(nodes[v].r + 1, n, 1, n, plus);
            }else{
                int v = in.readInt();
                int exp = nodes[v].count;
                exp = mod.plus(exp, seg.query(nodes[v].l, nodes[v].l, 1, n));

                Node sub = nodes[v].lightFather;
                while(sub != null){
                    Node p = sub.parent;
                    int prob = mod.mul(n - sub.size, invN);
                    int plus = mod.mul(prob, p.count);
                    exp = mod.plus(exp, plus);
                    sub = p.lightFather;
                }

                out.println(exp);
            }
        }
    }

    int dfn = 1;

    public void dfs2(Node root, Node light) {
        root.lightFather = light;
        for (Node node : root.next) {
            if (node == root.heavy) {
                dfs2(node, light);
            } else {
                dfs2(node, node);
            }
        }
    }

    public void dfs1(Node root, Node p) {
        root.parent = p;
        root.next.remove(p);
        root.size = 1;
        root.l = dfn++;
        for (Node node : root.next) {
            dfs1(node, root);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
        }
        root.r = dfn - 1;
    }
}


class Node {
    List<Node> next = new ArrayList<>();
    Node heavy;
    int size;
    Node lightFather;
    Node parent;
    int l;
    int r;
    int count;
}


class Segment implements Cloneable {
    private static final NumberTheory.Modular MOD = new NumberTheory.Modular(998244353);

    private Segment left;
    private Segment right;
    private int sum;
    private int mod;
    private int size;

    private void mod(int x) {
        sum = MOD.plus(sum, MOD.mul(x, size));
        mod = MOD.plus(mod, x);
    }

    public void pushUp() {
        size = left.size + right.size;
        sum = left.sum + right.sum;
    }

    public void pushDown() {
        if (mod != 0) {
            left.mod(mod);
            right.mod(mod);
            mod = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            size = 1;
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
            mod(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = (l + r) >> 1;
        return MOD.plus(left.query(ll, rr, l, m), right.query(ll, rr, m + 1, r));
    }
}
