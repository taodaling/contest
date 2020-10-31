package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class ShortestRoutesI {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].dist = (long) 1e18;
        }
        nodes[0].dist = 0;
        for (int i = 0; i < m; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            u.adj.add(new Edge(in.readInt(), v));
        }
        TreeSet<Node> pq = new TreeSet<>((a, b) -> a.dist == b.dist ? Integer.compare(a.id, b.id) : Long.compare(a.dist, b.dist));
        pq.add(nodes[0]);
        while (!pq.isEmpty()) {
            Node head = pq.pollFirst();
            for (Edge e : head.adj) {
                if (e.to.dist <= head.dist + e.c) {
                    continue;
                }
                pq.remove(e.to);
                e.to.dist = head.dist + e.c;
                pq.add(e.to);
            }
        }
        for(int i = 0; i < n; i++){
            out.append(nodes[i].dist).append(' ');
        }
    }
}

class Edge {
    int c;
    Node to;

    public Edge(int c, Node to) {
        this.c = c;
        this.to = to;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    long dist;
    int id;
}
