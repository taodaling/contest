package on2019_11.on2019_11_20_AtCoder_Grand_Contest_018.D___Tree_and_Hamilton_Path;



import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 1; i < n; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt()];
            e.b = nodes[in.readInt()];
            e.length = in.readInt();
            e.a.next.add(e);
            e.b.next.add(e);
        }
        dfs(nodes[1], null);
        List<Node> gs = new ArrayList<>();
        findGravity(nodes[1], null, n, gs);

        long ans = 0;
        for (Node g : gs) {
            dfsForDepth(g, null, 0);

            long totalDepth = 0;
            long minDepth = (long) 1e18;
            for (int i = 1; i <= n; i++) {
                if (nodes[i].depth == 0) {
                    continue;
                }
                totalDepth += nodes[i].depth;
                minDepth = Math.min(minDepth, nodes[i].depth);
            }
            for (Node node : gs) {
                if (g == node) {
                    continue;
                }
                minDepth = node.depth;
            }
            ans = Math.max(ans, totalDepth * 2 - minDepth);
        }

        out.println(ans);
    }

    public void dfsForDepth(Node root, Edge p, long depth) {
        root.depth = depth;
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfsForDepth(node, e, depth + e.length);
        }
    }

    public void findGravity(Node root, Edge p, int total, List<Node> list) {
        int maxSize = 0;
        if (root.p != null) {
            maxSize = Math.max(maxSize, total - root.size);
        }
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            maxSize = Math.max(maxSize, node.size);
        }
        if (maxSize <= total / 2) {
            list.add(root);
        }
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            findGravity(node, e, total, list);
        }
    }

    public void dfs(Node root, Edge p) {
        if (p != null) {
            root.p = p.other(root);
        }
        root.size = 1;
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfs(node, e);
            root.size += node.size;
        }
    }
}


class Edge {
    Node a;
    Node b;
    long length;

    public Node other(Node x) {
        return a == x ? b : a;
    }
}


class Node {
    List<Edge> next = new ArrayList<>();
    int size;
    Node p;
    long depth;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
