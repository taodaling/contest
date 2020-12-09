package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.LongWeightGraph;
import template.primitve.generated.graph.LongWeightUndirectedEdge;

import java.util.ArrayList;
import java.util.List;

public class TreeDiameter {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Edge e = new Edge();
            e.a = nodes[in.ri()];
            e.b = nodes[in.ri()];
            e.c = in.ri();
            e.a.adj.add(e);
            e.b.adj.add(e);
        }
        dfs(nodes[0], null);
        Node root = findMaxDepthNode(nodes);
        dfs(root, null);
        Node end = findMaxDepthNode(nodes);

        out.println(end.depth);
        IntegerArrayList seq = new IntegerArrayList(n);
        Node cur = end;
        while (cur != null) {
            seq.add(cur.id);
            cur = cur.p;
        }
        out.println(seq.size());
        for (int x : seq.toArray()) {
            out.append(x).append(' ');
        }
    }

    public Node findMaxDepthNode(Node[] nodes) {
        Node ans = nodes[0];
        for (Node node : nodes) {
            if (node.depth > ans.depth) {
                ans = node;
            }
        }
        return ans;
    }

    public static void dfs(Node root, Edge from) {
        root.depth = from == null ? 0 : (from.other(root).depth + from.c);
        root.p = from == null ? null : from.other(root);
        for (Edge e : root.adj) {
            if (e == from) {
                continue;
            }
            dfs(e.other(root), e);
        }
    }

}

class Edge {
    Node a;
    Node b;
    int c;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>(2);
    long depth;
    Node p;
    int id;
}