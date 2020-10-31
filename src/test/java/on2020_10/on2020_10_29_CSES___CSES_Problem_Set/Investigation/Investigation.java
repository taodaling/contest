package on2020_10.on2020_10_29_CSES___CSES_Problem_Set.Investigation;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Investigation {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long inf = (long) 1e18;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].dist = inf;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(new Edge(b, in.readInt()));
        }
        nodes[0].dist = 0;
        nodes[0].way = 1;
        nodes[0].maxEdge = 0;
        nodes[0].minEdge = 0;
        TreeSet<Node> pq = new TreeSet<>((a, b) -> a.dist == b.dist ? Integer.compare(a.id, b.id) : Long.compare(a.dist, b.dist));
        pq.add(nodes[0]);
        while (!pq.isEmpty()) {
            Node head = pq.pollFirst();
            head.way %= mod;
            for (Edge e : head.adj) {
                long cand = head.dist + e.w;
                if (e.to.dist > cand) {
                    pq.remove(e.to);
                    e.to.update(cand);
                    pq.add(e.to);
                }
                if (e.to.dist == cand) {
                    e.to.way += head.way;
                    e.to.minEdge = Math.min(e.to.minEdge, head.minEdge + 1);
                    e.to.maxEdge = Math.max(e.to.maxEdge, head.maxEdge + 1);
                }
            }
        }

        if (nodes[n - 1].dist == inf) {
            out.println("IMPOSSIBLE");
            return;
        }
        Node end = nodes[n - 1];
        out.println(end.dist)
                .println(end.way)
                .println(end.minEdge)
                .println(end.maxEdge);
    }
}

class Edge {
    Node to;
    int w;

    public Edge(Node to, int w) {
        this.to = to;
        this.w = w;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int id;
    long dist;
    long way;
    long minEdge;
    long maxEdge;

    public void update(long d) {
        dist = d;
        way = 0;
        minEdge = Integer.MAX_VALUE;
        maxEdge = 0;
    }
}