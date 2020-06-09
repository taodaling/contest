package contest;

import template.graph.HungaryAlgo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MaxCutFree {
    public int solve(int n, int[] a, int[] b) {
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        int m = a.length;
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[a[i]];
            edges[i].b = nodes[b[i]];
            edges[i].a.adj.add(edges[i]);
            edges[i].b.adj.add(edges[i]);
        }

        dq = new ArrayDeque<>(n);
        for (Node node : nodes) {
            tarjan(node, null);
            dfsForSide(node, 0);
        }

        HungaryAlgo algo = new HungaryAlgo(n, n);
        for (Edge e : edges) {
            if (e.a.side == e.b.side) {
                continue;
            }
            if (e.a.side == 1) {
                Node tmp = e.a;
                e.a = e.b;
                e.b = tmp;
            }
            algo.addEdge(e.a.id, e.b.id, true);
        }

        int match = 0;
        for (int i = 0; i < n; i++) {
            match += algo.matchLeft(i) ? 1 : 0;
        }

        int ans = n - match;
        return ans;
    }

    Deque<Node> dq;
    int dfn = 0;

    public void dfsForSide(Node root, int parity) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        root.side = parity;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node.set != root.set) {
                dfsForSide(node, parity ^ 1);
            } else {
                dfsForSide(node, parity);
            }
        }
    }

    public void tarjan(Node root, Edge p) {
        if (root.dfn != 0) {
            return;
        }
        root.instk = true;
        root.low = root.dfn = ++dfn;
        dq.addLast(root);
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            tarjan(node, e);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }

        if (root.low == root.dfn) {
            while (true) {
                Node tail = dq.removeLast();
                tail.instk = false;
                tail.set = root;
                if (tail == root) {
                    break;
                }
            }
        }
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
    List<Edge> adj = new ArrayList<>();
    Node set;
    int dfn;
    boolean instk;
    int id;
    int low;
    int side;
    boolean visited;
}
