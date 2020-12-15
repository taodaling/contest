package on2020_12.on2020_12_12_Library_Checker.Shortest_Path;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.LongWeightDirectedEdge;
import template.primitve.generated.graph.LongWeightGraph;

import java.util.*;

public class ShortestPath {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int s = in.ri();
        int t = in.ri();
        long inf = (long) 1e18;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].dist = inf;
        }
        for (int i = 0; i < m; i++) {
            int a = in.ri();
            int b = in.ri();
            int c = in.ri();
            nodes[a].adj.add(new Edge(nodes[b], c));
        }
        PriorityQueue<Event> pq = new PriorityQueue<>(m, Comparator.comparingLong(x -> x.w));
        pq.add(new Event(null, nodes[s], 0));
        while (!pq.isEmpty()) {
            Event event = pq.remove();
            Node head = event.to;
            if (head.dist <= event.w) {
                continue;
            }
            head.prev = event.src;
            head.dist = event.w;
            for (Edge e : head.adj) {
                pq.add(new Event(head, e.to, head.dist + e.w));
            }
        }
        if (nodes[t].dist == inf) {
            out.println(-1);
            return;
        }
        out.println(nodes[t].dist);
        IntegerArrayList list = new IntegerArrayList(n);
        for (Node node = nodes[t]; node != null; node = node.prev) {
            list.add(node.id);
        }
        list.reverse();
        out.println(list.size() - 1);
        for (int i = 0; i < list.size() - 1; i++) {
            out.append(list.get(i)).append(' ').append(list.get(i + 1)).println();
        }
    }
}


class Edge {
    Node to;
    long w;

    public Edge(Node to, long w) {
        this.to = to;
        this.w = w;
    }
}

class Event{
    Node src;
    Node to;
    long w;

    public Event(Node src, Node to, long w) {
        this.src = src;
        this.to = to;
        this.w = w;
    }
}

class Node {
    int id;
    List<Edge> adj = new ArrayList<>();
    Node prev;
    long dist;
}