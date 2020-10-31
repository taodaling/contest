package contest;

import template.datastructure.DSU;
import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MailDelivery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            Edge e = new Edge(a, b);
            a.adj.add(e);
            b.adj.add(e);
            dsu.merge(a.id, b.id);
        }
        for (int i = 0; i < n; i++) {
            if (nodes[i].adj.isEmpty()) {
                continue;
            }
            if (dsu.find(i) != dsu.find(0) || nodes[i].adj.size() % 2 != 0) {
                out.println("IMPOSSIBLE");
                return;
            }
        }

        dfs(nodes[0]);
        for (Node node : trace) {
            out.append(node.id + 1).append(' ');
        }
    }

    List<Node> trace = new ArrayList<>((int) 2e5);


    public void dfs(Node root) {
        while (!root.adj.isEmpty()) {
            Edge tail = root.adj.remove(root.adj.size() - 1);
            if (tail.visited) {
                continue;
            }
            tail.visited = true;
            dfs(tail.other(root));
        }
        trace.add(root);
    }
}

class Edge {
    Node a;
    Node b;
    boolean visited;

    Node other(Node x) {
        return a == x ? b : a;
    }

    public Edge(Node a, Node b) {
        this.a = a;
        this.b = b;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int id;
}