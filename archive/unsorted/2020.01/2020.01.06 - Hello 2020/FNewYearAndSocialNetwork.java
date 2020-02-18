package contest;


import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerMultiWayStack;

import java.util.ArrayList;
import java.util.List;

public class FNewYearAndSocialNetwork {
    Node[] t1;
    Node[] t2;
    LcaOnTree lcaOnTree;
    FastOutput out;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;

        int n = in.readInt();
        t1 = new Node[n + 1];
        t2 = new Node[n + 1];
        IntegerMultiWayStack edges = new IntegerMultiWayStack(n + 1, (n + 1) * 2);
        edges.addLast(0, 1);

        for (int i = 1; i <= n; i++) {
            t1[i] = new Node();
            t1[i].id = i;

            t2[i] = new Node();
            t2[i].id = i;
        }

        for (int i = 1; i < n; i++) {
            Node a = t1[in.readInt()];
            Node b = t1[in.readInt()];
            addEdge(a, b);
            edges.addLast(a.id, b.id);
            edges.addLast(b.id, a.id);
        }
        lcaOnTree = new LcaOnTree(edges, 0);

        for (int i = 1; i < n; i++) {
            Node a = t2[in.readInt()];
            Node b = t2[in.readInt()];
            addEdge(a, b);
        }

        out.println(n - 1);
        dfsT1(t1[1], null, 0);
        dfsT2(t2[1], null);
    }

    public void dfsT1(Node root, Edge from, int depth) {
        root.from = from;
        root.jump = new Node[20];
        root.depth = depth;
        if (root.from != null) {
            root.jump[0] = from.other(root);
            for (int i = 0; root.jump[i] != null; i++) {
                root.jump[i + 1] = root.jump[i].jump[i];
            }
        }
        for (Edge e : root.next) {
            if (e == from) {
                continue;
            }
            dfsT1(e.other(root), e, depth + 1);
        }
    }

    public void dfsT2(Node root, Edge from) {
        for (Edge e : root.next) {
            if (e == from) {
                continue;
            }
            dfsT2(e.other(root), e);
        }
        for (Edge e : root.next) {
            if (e == from) {
                continue;
            }
            Edge partner = findPartner(e.other(root), root);
            answer(partner, e);
            Node.merge(t2[partner.a.id], t2[partner.b.id]);
        }
    }

    public boolean sameComponent(Node leaf, Node x) {
        return leaf.find() == t2[x.id].find();
    }

    public Edge findPartner(Node leaf, Node p) {
        Node b = t1[lcaOnTree.lca(leaf.id, p.id)];
        Node a;
        if (sameComponent(leaf, b)) {
            a = t1[p.id];
        } else {
            a = t1[leaf.id];
        }

        for (int i = 20 - 1; i >= 0; i--) {
            if (a.jump[i] == null || a.jump[i].depth <= b.depth) {
                continue;
            }
            boolean sameWithA;
            if (sameComponent(leaf, a)) {
                sameWithA = sameComponent(leaf, a.jump[i]);
            } else {
                sameWithA = !sameComponent(leaf, a.jump[i]);
            }
            if (sameWithA) {
                a = a.jump[i];
            }
        }

        return a.from;
    }

    public void answer(Edge e1, Edge e2) {
        out.append(e1.a.id).append(' ').append(e1.b.id).append(' ')
                .append(e2.a.id).append(' ').append(e2.b.id).println();
    }

    public void addEdge(Node a, Node b) {
        Edge e = new Edge();
        e.a = a;
        e.b = b;
        a.next.add(e);
        b.next.add(e);
    }

}

class Edge {
    Node a;
    Node b;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    int id;
    Node[] jump;
    Edge from;
    Node p = this;
    int rank = 0;
    int depth;

    public Node find() {
        return p == p.p ? p : (p = p.find());
    }

    public static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
