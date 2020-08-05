package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BEvenDegrees {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        Edge[] edges = new Edge[m];

        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt() - 1];
            e.b = nodes[in.readInt() - 1];
            e.toA = true;
            e.b.xor ^= 1;
            edges[i] = e;

            if (dsu.find(e.a.id) == dsu.find(e.b.id)) {
                continue;
            }
            dsu.merge(e.a.id, e.b.id);

            e.a.adj.add(e);
            e.b.adj.add(e);
        }

        if (1 == dfs(nodes[0], null)) {
            out.println(-1);
            return;
        }

        for (Edge e : edges) {
            if (!e.toA) {
                Node tmp = e.a;
                e.a = e.b;
                e.b = tmp;
            }

            out.append(e.b.id + 1).append(' ').append(e.a.id + 1).println();
        }
    }

    public int dfs(Node root, Edge p) {
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            int inv = dfs(node, e);
            if (inv == 1) {
                e.toA = !e.toA;
                root.xor ^= 1;
            }
        }

        return root.xor;
    }
}

class Edge {
    Node a;
    Node b;
    boolean toA;

    public Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int xor;
    int id;
}
