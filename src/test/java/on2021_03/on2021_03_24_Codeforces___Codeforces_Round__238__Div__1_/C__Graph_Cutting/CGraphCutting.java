package on2021_03.on2021_03_24_Codeforces___Codeforces_Round__238__Div__1_.C__Graph_Cutting;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CGraphCutting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        DSU dsu = new DSU(n);
        dsu.init();
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge(nodes[in.ri() - 1], nodes[in.ri() - 1]);
            if (dsu.find(edges[i].a.id) != dsu.find(edges[i].b.id)) {
                dsu.merge(edges[i].a.id, edges[i].b.id);
                edges[i].a.adj.add(edges[i]);
                edges[i].b.adj.add(edges[i]);
            }
        }

        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                dfs(nodes[i], null);
                if (nodes[i].deg == 1) {
                    out.println("No solution");
                    return;
                }
            }
        }

        Edge[] carry = new Edge[n];
        for (Edge e : edges) {
            Node bind = e.to;
            if (carry[bind.id] != null) {
                out.append(carry[bind.id].other(bind).id + 1).append(' ')
                        .append(bind.id + 1).append(' ')
                        .append(e.other(bind).id + 1).println();
                carry[bind.id] = null;
            } else {
                carry[bind.id] = e;
            }
        }
    }

    public void dfs(Node root, Edge p) {
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (e == p) {
                continue;
            }
            dfs(node, e);
        }
        if (root.deg == 1 && p != null) {
            p.inv();
        }
    }
}

class Edge {
    Node a;
    Node b;
    Node to;

    public Edge(Node a, Node b) {
        this.a = a;
        this.b = b;
        to = a;
        to.deg ^= 1;
    }

    Node other(Node x) {
        return x == a ? b : a;
    }

    void inv() {
        to.deg ^= 1;
        to = other(to);
        to.deg ^= 1;
    }
}

class Node {
    int id;
    int deg;
    List<Edge> adj = new ArrayList<>();
}
