package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.ArrayList;
import java.util.List;

public class CKeepGraphConnected {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        va = new IntegerVersionArray(n + 1);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];

            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.c = in.ri();

            a.adj.add(e);
            b.adj.add(e);
        }

        dfs(nodes[0], 1);
        for (int i = 0; i < n; i++) {
            out.println(nodes[i].c);
        }
    }

    IntegerVersionArray va;

    public void dfs2(Node root, int forbidden) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        List<Node> sub = new ArrayList<>();
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (!node.visited) {
                dfs(node, e.c);
                sub.add(node);
            }
        }

        va.clear();
        va.set(forbidden, 1);
        for (Node node : sub) {
            va.set(node.c, 1);
        }
        root.c = 1;
        while (va.get(root.c) == 1) {
            root.c++;
        }
    }

    public void dfs(Node root, int must) {
        if (root.visited) {
            return;
        }

        root.visited = true;
        root.c = must;

        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (e.c == root.c) {
                dfs2(node, e.c);
            } else {
                dfs(node, e.c);
            }
        }
    }

}

class Edge {
    Node a;
    Node b;
    int c;

    public Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    boolean visited;
    List<Edge> adj = new ArrayList<>();
    int c;
}
