package on2020_05.on2020_05_15_Codeforces___Codeforces_Round__426__Div__1_.D__Red_Black_Cobweb;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class DRedBlackCobweb {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            int x = in.readInt();
            int c = in.readInt();

            Edge edge = new Edge();
            edge.a = u;
            edge.b = v;
            edge.w = x;
            if (c == 0) {
                edge.c0 = 1;
            } else {
                edge.c1 = 1;
            }

            u.adj.add(edge);
            v.adj.add(edge);
        }

        dac(nodes[0]);
        out.println(ans);
    }

    int fix = 250000;

    Debug debug = new Debug(true);
    IntegerBIT add = new IntegerBIT(fix * 2);
    IntegerBIT sub = new IntegerBIT(fix * 2);

    public void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    public Node dfsForCentroid(Node root, Node p, int total) {
        int max = total - root.size;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            Node ans = dfsForCentroid(node, root, total);
            if (ans != null) {
                return ans;
            }
            max = Math.max(max, node.size);
        }

        if (max * 2 <= total) {
            return root;
        }
        return null;
    }

    int ans = 1;

    public void dfsForAns(Node root, Node p, int a, int b, int prod) {
        int exp = mod.subtract(add.query(2 * a - b + fix), sub.query(a - 2 * b - 1 + fix));
        ans = mod.mul(ans, power.pow(prod, exp));
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfsForAns(node, root, a + e.c0, b + e.c1, mod.mul(prod, e.w));
        }
    }

    public void dfsForModify(Node root, Node p, int a, int b, int x) {
        add.update(b - 2 * a + fix, x);
        sub.update(b * 2 - a + fix, x);
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfsForModify(node, root, a + e.c0, b + e.c1, x);
        }
    }

    public void dac(Node root) {
        dfsForSize(root, null);
        root = dfsForCentroid(root, null, root.size);
        debug.debug("root", root);
        add.update(0 + fix, 1);
        sub.update(0 + fix, 1);

        for (int i = 0; i < 2; i++) {
            for (Edge e : root.adj) {
                Node node = e.other(root);
                dfsForAns(node, root, e.c0, e.c1, e.w);
                dfsForModify(node, root, e.c0, e.c1, 1);
            }
            for (Edge e : root.adj) {
                Node node = e.other(root);
                dfsForModify(node, root, e.c0, e.c1, -1);
            }
            if (i == 0) {
                SequenceUtils.reverse(root.adj);
                add.update(0 + fix, -1);
                sub.update(0 + fix, -1);
            }
        }
        for (Edge e : root.adj) {
            Node node = e.other(root);
            node.adj.remove(e);
            dac(node);
        }
    }
}

class Edge {
    Node a;
    Node b;
    int c0;
    int c1;
    int w;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int id;
    int size;

    @Override
    public String toString() {
        return "" + id;
    }
}