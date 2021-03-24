package on2021_03.on2021_03_17_Codeforces___Codeforces_Round__254__Div__1_.E__DZY_Loves_Planting;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerWeightDirectedEdge;

import java.util.ArrayList;
import java.util.List;

public class EDZYLovesPlanting {
    int total;
    boolean valid = true;

    public void dfs(Node root, Edge from) {
        root.cnt = 1;
        root.sum = root.cntMatch;
        for (Edge e : root.adj) {
            if (e == from) {
                continue;
            }
            Node node = e.other(root);
            dfs(node, e);
            if (!e.cut) {
                root.cnt += node.cnt;
                root.sum += node.sum;
            }
        }
        if (from == null || from.cut) {
            if (root.cnt > total - root.sum) {
                valid = false;
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        Edge[] edges = new Edge[n - 1];
        for (int i = 0; i < n - 1; i++) {
            Edge e = new Edge();
            e.a = nodes[in.ri() - 1];
            e.b = nodes[in.ri() - 1];
            e.val = in.ri();
            e.a.adj.add(e);
            e.b.adj.add(e);
            edges[i] = e;
        }
        for (Node node : nodes) {
            node.cntMatch = in.ri();
            total += node.cntMatch;
        }

        int lo = 0;
        int hi = 10000;
        while (lo < hi) {
            int mid = (lo + hi + 1) / 2;
            for (Edge e : edges) {
                e.cut = e.val >= mid;
            }
            valid = true;
            dfs(nodes[0], null);
            if (valid) {
                lo = mid;
            }else{
                hi = mid - 1;
            }
        }

        out.println(lo);
    }

}

class Edge {
    boolean cut;
    Node a;
    Node b;
    int val;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int sum;
    int cnt;
    int cntMatch;
}

class Block {
    int cnt;
    int sum;

    public Block(int cnt, int sum) {
        this.cnt = cnt;
        this.sum = sum;
    }
}