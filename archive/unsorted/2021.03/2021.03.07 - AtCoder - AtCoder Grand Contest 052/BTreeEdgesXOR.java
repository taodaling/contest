package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BTreeEdgesXOR {
    public void dfs(Node root, Node p, int w1, int w2) {
        root.a = w1;
        root.b = w2;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root, w1 ^ e.w1, w2 ^ e.w2);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.w1 = in.ri();
            e.w2 = in.ri();
            e.a.adj.add(e);
            e.b.adj.add(e);
        }
        dfs(nodes[0], null, 0, 0);
        int x = 0;
        for (Node node : nodes) {
            x ^= node.a;
            x ^= node.b;
        }
        for (Node node : nodes) {
            node.a ^= x;
        }
        Node[] sortByA = nodes.clone();
        Node[] sortByB = nodes.clone();
        Arrays.sort(sortByA, Comparator.comparingInt(node -> node.a));
        Arrays.sort(sortByB, Comparator.comparingInt(node -> node.b));
        for (int i = 0; i < n; i++) {
            if (sortByA[i].a != sortByB[i].b) {
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}

class Edge {
    Node a;
    Node b;
    int w1;
    int w2;

    Node other(Node x) {
        return x == a ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int a;
    int b;
}
