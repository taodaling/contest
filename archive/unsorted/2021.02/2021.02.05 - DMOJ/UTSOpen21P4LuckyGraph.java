package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UTSOpen21P4LuckyGraph {
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
        List<Edge> edges = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            nodes[a].deg ^= 1;
            nodes[b].deg ^= 1;
            if (dsu.find(a) == dsu.find(b)) {
                continue;
            }
            dsu.merge(a, b);
            Edge e = new Edge();
            e.a = nodes[a];
            e.b = nodes[b];
            nodes[a].adj.add(e);
            nodes[b].adj.add(e);
            edges.add(e);
            e.index = i;
        }

        for (Node node : nodes) {
            if (node.visited) {
                continue;
            }
            dfs(node, null);
        }
        int ans = 0;
        for (Node node : nodes) {
            ans += node.deg;
        }
        out.println(ans);
        List<Edge> deleted = edges.stream().filter(x -> x.delete).collect(Collectors.toList());
        out.println(deleted.size());
        for (Edge e : deleted) {
            out.append(e.index + 1).append(' ');
        }
    }

    public void dfs(Node root, Edge p) {
        root.visited = true;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            dfs(e.other(root), e);
        }
        if (root.deg == 0 && p != null) {
            p.delete();
        }
    }
}

class Edge {
    Node a;
    Node b;
    boolean delete;
    int index;

    Node other(Node x) {
        return a == x ? b : a;
    }

    void delete() {
        delete = !delete;
        a.deg ^= 1;
        b.deg ^= 1;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int deg;
    int id;
    boolean visited;
}
