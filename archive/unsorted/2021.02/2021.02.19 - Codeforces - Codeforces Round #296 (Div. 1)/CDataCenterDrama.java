
package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class CDataCenterDrama {
    DSUExt dsu;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        dsu = new DSUExt(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            addEdge(a, b);
        }
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != i) {
                continue;
            }
            if (dsu.edge[i] % 2 == 1) {
                addEdge(nodes[i], nodes[i]);
            }
        }
        for (Node node : nodes) {
            if (node.visited) {
                continue;
            }
            dfs(node, null);
        }
        int need = edges.size();
        out.println(need);
        for (Edge e : edges) {
            out.append(e.a.id + 1).append(' ').append(e.b.id + 1).println();
        }
    }

    void dfs(Node root, Edge p) {
        root.visited = true;
        for (Edge e : root.adj) {
            Node to = e.other(root);
            if (to.visited) {
                continue;
            }
            dfs(to, e);
        }
        if (root.deg == 1) {
            assert p != null;
            p.swap();
        }
    }

    List<Edge> edges = new ArrayList<>();

    void addEdge(Node a, Node b) {
        Edge e = new Edge();
        e.a = a;
        e.b = b;
        e.b.deg ^= 1;
        a.adj.add(e);
        b.adj.add(e);
        edges.add(e);
        dsu.merge(a.id, b.id);
        dsu.edge[dsu.find(a.id)]++;
    }
}

class DSUExt extends DSU {
    int[] edge;

    public DSUExt(int n) {
        super(n);
        edge = new int[n];
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        edge[a] += edge[b];
    }
}

class Edge {
    Node a;
    Node b;
    boolean used;

    Node other(Node x) {
        return a == x ? b : a;
    }

    void swap() {
        Node tmp = a;
        a = b;
        b = tmp;
        a.deg ^= 1;
        b.deg ^= 1;
    }
}

class Node {
    int id;
    int deg;
    List<Edge> adj = new ArrayList<>();
    boolean visited;
}