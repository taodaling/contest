package on2020_06.on2020_06_12_Virtual_Judge___be_greater_contest_1.B___Avoiding_Collision;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class BAvoidingCollision {
    Modular mod = new Modular(1e9 + 7);
    long inf = (long) 1e18;

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        Node s = nodes[in.readInt() - 1];
        Node t = nodes[in.readInt() - 1];

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt() - 1];
            e.b = nodes[in.readInt() - 1];
            e.len = in.readInt();
            e.a.adj.add(e);
            e.b.adj.add(e);
            edges[i] = e;
        }

        dijkstra(nodes, s, 0);
        dijkstra(nodes, t, 1);

        long best = t.dists[0];
        int sum = 0;

        //vertex
        for (Node node : nodes) {
            if (node.dists[0] + node.dists[1] != best ||
                    node.dists[0] != node.dists[1]) {
                continue;
            }
            int local = mod.mul(node.ways[0], node.ways[1]);
            local = mod.mul(local, local);
            sum = mod.plus(sum, local);
        }

        //edge
        for (Edge e : edges) {
            if (e.a.dists[0] > e.b.dists[0]) {
                Node tmp = e.a;
                e.a = e.b;
                e.b = tmp;
            }

            if (e.a.dists[0] + e.b.dists[1] + e.len != best) {
                continue;
            }

            if (e.a.dists[0] >= e.b.dists[1] + e.len || e.b.dists[1] >= e.a.dists[0] + e.len) {
                continue;
            }
            int local = mod.mul(e.a.ways[0], e.b.ways[1]);
            local = mod.mul(local, local);
            sum = mod.plus(sum, local);
        }

        int ans = mod.mul(t.ways[0], s.ways[1]);
        debug.debug("ans", ans);
        debug.debug("sum", sum);
        ans = mod.subtract(ans, sum);



        out.println(ans);
    }

    public void dijkstra(Node[] nodes, Node s, int i) {
        for (Node node : nodes) {
            node.ways[i] = 0;
            node.dists[i] = inf;
        }
        s.dists[i] = 0;
        s.ways[i] = 1;
        TreeSet<Node> pq = new TreeSet<>((a, b) -> a.dists[i] == b.dists[i] ? Integer.compare(a.id, b.id) : Long.compare(a.dists[i], b.dists[i]));

        pq.add(s);
        while (!pq.isEmpty()) {
            Node top = pq.pollFirst();
            for (Edge e : top.adj) {
                Node node = e.other(top);
                long newDist = top.dists[i] + e.len;
                if (newDist < node.dists[i]) {
                    pq.remove(node);
                    node.ways[i] = 0;
                    node.dists[i] = newDist;
                    pq.add(node);
                }
                if (newDist == node.dists[i]) {
                    node.ways[i] = mod.plus(node.ways[i], top.ways[i]);
                }
            }
        }
    }
}

class Edge {
    Node a;
    Node b;
    long len;

    public Node other(Node x) {
        return x == a ? b : a;
    }
}

class Node {
    int id;
    List<Edge> adj = new ArrayList<>();
    long[] dists = new long[2];
    int[] ways = new int[2];

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}