package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt()];
            edges[i].b = nodes[in.readInt()];
            edges[i].w = in.readInt();
        }

        Arrays.sort(edges, (a, b) -> a.w - b.w);
        if (edges.length > k) {
            edges = Arrays.copyOf(edges, k);
        }
        PriorityQueue pq = find(edges).pq;
        while (pq.size() > k) {
            pq.remove();
        }

        out.println(pq.peek());
    }

    PQ find(Edge[] edges) {
        PQ pq = new PQ();
        for (Edge e : edges) {
            e.a.next.add(e);
            e.b.next.add(e);
        }
        Node[] nodes = Arrays.stream(edges).flatMap(x -> Stream.of(x.a, x.b)).toArray(x -> new Node[x]);
        for (Node node : nodes) {
            List<Node> trace = new ArrayList<>();
            trace(node, trace);
            if (trace.isEmpty()) {
                continue;
            }
            maintain(trace, pq);
        }
        return pq;
    }

    private void maintain(List<Node> trace, PQ pq) {
        Node[] nodes = trace.toArray(new Node[0]);
        int n = nodes.length;
        for (int i = 0; i < n; i++) {
            nodes[i].id = i;
        }
        long[][] dist = new long[n][n];
        SequenceUtils.deepFill(dist, (long) 1e18);
        for (Node node : nodes) {
            for (Edge e : node.next) {
                dist[e.a.id][e.b.id] = dist[e.b.id][e.a.id] = e.w;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    dist[j][k] = Math.min(dist[j][k], dist[j][i] + dist[i][k]);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                pq.add(dist[i][j]);
            }
        }
    }

    public void trace(Node root, List<Node> trace) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        trace.add(root);
        for (Edge e : root.next) {
            Node node = e.a == root ? e.b : e.a;
            trace(node, trace);
        }
    }
}

class PQ {
    PriorityQueue<Long> pq = new PriorityQueue<>(400, (a, b) -> -a.compareTo(b));

    public void add(long x) {
        if (pq.size() < 400) {
            pq.add(x);
            return;
        }
        if (pq.peek() > x) {
            pq.remove();
            pq.add(x);
        }
    }
}

class Edge {
    Node a;
    Node b;
    int w;
}

class Node {
    int id;
    boolean visited;
    List<Edge> next = new ArrayList<>();
}
