package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CupsTransportation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt() - 1];
            e.b = nodes[in.readInt() - 1];
            e.len = in.readInt();
            e.w = DigitUtils.floorDiv(in.readInt() - (int) 3e6, 100);
            if (e.w < 0) {
                continue;
            }
            e.a.adj.add(e);
            e.b.adj.add(e);
        }

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return dist(nodes, mid) > 24 * 60;
            }
        };

        int ans = ibs.binarySearch(0, (int) 1e7);
        if (ibs.check(ans)) {
            ans--;
        }
        ans = Math.max(ans, 0);
        out.println(ans);
    }

    public int dist(Node[] nodes, int w) {
        int inf = (int) 1e9;
        for (Node node : nodes) {
            node.dist = inf;
        }
        TreeSet<Node> set = new TreeSet<>((a, b) -> a.dist == b.dist ? Integer.compare(a.id, b.id) : Integer.compare(a.dist, b.dist));
        nodes[0].dist = 0;
        set.add(nodes[0]);
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (Edge e : head.adj) {
                if (e.w < w) {
                    continue;
                }
                Node node = e.other(head);
                if (node.dist <= e.len + head.dist) {
                    continue;
                }
                set.remove(node);
                node.dist = e.len + head.dist;
                set.add(node);
            }
        }
        return nodes[nodes.length - 1].dist;
    }
}

class Edge {
    Node a;
    Node b;
    int len;
    int w;

    public Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int dist;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}