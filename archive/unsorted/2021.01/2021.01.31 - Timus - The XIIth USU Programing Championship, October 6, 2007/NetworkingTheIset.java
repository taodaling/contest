package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.*;

public class NetworkingTheIset {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        nodes = new Node[n];
        dq = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.ri() - 1];
            edges[i].b = nodes[in.ri() - 1];
            edges[i].a.adj.add(edges[i]);
            edges[i].b.adj.add(edges[i]);
            edges[i].eid = i;
        }
        int diameter = inf;
        List<Node> center = null;
        Edge middle = null;
        int[][] dist = new int[n][n];
        SequenceUtils.deepFill(dist, inf);
        for (Edge e : edges) {
            dist[e.a.id][e.b.id] = 1;
            dist[e.b.id][e.a.id] = 1;
        }
        for (int i = 0; i < n; i++) {
            dist[i][i] = 0;
        }
        for (int i = 0; i < n; i++) {
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    dist[u][v] = Math.min(dist[u][v], dist[u][i] + dist[i][v]);
                }
            }
        }

        //even
        for (int i = 0; i < n; i++) {
            int max = 0;
            for (int j = 0; j < n; j++) {
                max = Math.max(max, dist[i][j]);
            }
            if (max * 2 < diameter) {
                diameter = max * 2;
                center = Arrays.asList(nodes[i]);
            }
        }
        //odd
        for (Edge e : edges) {
            int max = 0;
            for (int i = 0; i < n; i++) {
                max = Math.max(max, Math.min(dist[e.a.id][i], dist[e.b.id][i]));
            }
            if (max * 2 + 1 < diameter) {
                diameter = max * 2 + 1;
                center = Arrays.asList(e.a, e.b);
                middle = e;
            }
        }
        List<Edge> eset = new ArrayList<>(n);
        bfs(center);
        if (middle != null) {
            eset.add(middle);
        }
        for (Node node : nodes) {
            if (node.prev != null) {
                eset.add(node.prev);
            }
        }
        eset.sort(Comparator.comparingInt(x -> x.eid));
        for (Edge e : eset) {
            out.append(e.a.id + 1).append(' ').append(e.b.id + 1).println();
        }
    }

    int inf = (int) 1e9;
    Node[] nodes;
    Deque<Node> dq;

    int bfs(List<Node> src) {
        for (Node node : nodes) {
            node.dist = inf;
            node.prev = null;
        }
        dq.clear();
        for (Node node : src) {
            node.dist = 0;
            dq.addLast(node);
        }
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();
            for (Edge e : head.adj) {
                Node node = e.other(head);
                if (node.dist > head.dist + 1) {
                    node.dist = head.dist + 1;
                    node.prev = e;
                    dq.addLast(node);
                }
            }
        }
        int max = 0;
        for (Node node : nodes) {
            max = Math.max(max, node.dist);
        }
        return max;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int dist;
    Edge prev;
    int id;
}

class Edge {
    Node a;
    Node b;

    int eid;

    Node other(Node x) {
        return x == a ? b : a;
    }
}
