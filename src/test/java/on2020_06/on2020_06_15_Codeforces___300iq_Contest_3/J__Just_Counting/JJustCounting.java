package on2020_06.on2020_06_15_Codeforces___300iq_Contest_3.J__Just_Counting;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

import java.util.ArrayList;
import java.util.List;

public class JJustCounting {
    Modular mod = new Modular(998244353);
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt() - 1];
            e.b = nodes[in.readInt() - 1];
            Node.merge(e.a, e.b);
            e.a.adj.add(e);
            e.b.adj.add(e);
            edges[i] = e;
        }

        for (Node node : nodes) {
            if(node.visited){
                continue;
            }
            dfs(node, 0);
        }

        int cnt = 0;
        for (Edge e : edges) {
            if(e.tree){
                continue;
            }
            if (e.a.depth % 2 != e.b.depth % 2) {
                cnt++;
                continue;
            }
            if (e.a.find().added) {
                cnt++;
                continue;
            }
            e.a.find().added = true;
        }

        int ans = pow.pow(5, cnt);
        out.println(ans);
    }

    public void dfs(Node root, int d) {
        root.visited = true;
        root.depth = d;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node.visited) {
                continue;
            }
            e.tree = true;
            dfs(node, d + 1);
        }
    }
}

class Edge {
    Node a;
    Node b;
    boolean tree;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    boolean visited;
    int id;
    int depth;
    Node p = this;
    int rank = 0;
    boolean added;

    Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank < b.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        b.p = a;
    }

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}