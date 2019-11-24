package on2019_11.on2019_11_24_AtCoder_Beginner_Contest_146.D___Coloring_Edges_on_Tree;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        List<Edge> edges = new ArrayList<>(n);
        for (int i = 1; i < n; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt()];
            e.b = nodes[in.readInt()];
            e.a.next.add(e);
            e.b.next.add(e);
            edges.add(e);
        }
        dfs(nodes[1], null);
        out.println(nodes[1].max);
        for (Edge e : edges) {
            out.println(e.color);
        }
    }

    public void dfs(Node root, Edge p) {
        int color = 0;
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.a == root ? e.b : e.a;
            color++;
            if (p != null && color == p.color) {
                color++;
            }
            e.color = color;
            dfs(node, e);
            root.max = Math.max(root.max, node.max);
        }
        root.max = Math.max(root.max, root.next.size());
    }
}

class Edge {
    Node a;
    Node b;
    int color;

}

class Node {
    List<Edge> next = new ArrayList<>();
    int max;
}