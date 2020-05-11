package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class DDynamicShortestPath {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        Node[] nodes = new Node[n];

        long inf = (long) 1e18;
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].dist = inf;
        }
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            int c = in.readInt();
            edges[i] = new Edge(a, b, c);
            a.next.add(edges[i]);
        }

        TreeSet<Node> pq = new TreeSet<>((a, b) -> a.dist == b.dist ? a.id - b.id : Long.compare(a.dist, b.dist));
        Node root = nodes[0];
        root.dist = 0;
        pq.add(root);
        while (!pq.isEmpty()) {
            Node head = pq.pollFirst();
            for (Edge e : head.next) {
                Node node = e.to;
                if (node.dist > head.dist + e.w) {
                    pq.remove(node);
                    node.dist = head.dist + e.w;
                    pq.add(node);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            nodes[i].last = nodes[i].dist;
            nodes[i].dist = inf;
        }

        MultiWayStack<Node> stack = new MultiWayStack<>(1000001, 1000000);
        while (q-- > 0) {
            int op = 0;

            int t = in.readInt();
            if (t == 1) {
                Node v = nodes[in.readInt() - 1];
                out.println(v.last >= 1e12 ? -1 : v.last);
                continue;
            }

            int c = in.readInt();
            //stack.expandStackNum(c + 1);
            stack.danger();
            for (int j = 0; j < c; j++) {
                op++;
                int x = in.readInt();
                Edge e = edges[x - 1];
                e.reserve++;
            }
            for (Edge e : edges) {
                op++;
                e.w = e.reserve + e.from.last - e.to.last;
            }
            root.dist = 0;
            stack.addLast(0, root);
            for (int i = 0; i <= c; i++) {
                op++;
                while (!stack.isEmpty(i)) {
                    op++;
                    Node head = stack.removeLast(i);
                    if (head.dist != i) {
                        continue;
                    }
                    for (Edge e : head.next) {
                        op++;
                        Node node = e.to;
                        if (node.dist > head.dist + e.w) {
                            node.dist = head.dist + e.w;
                            if (node.dist <= c) {
                                stack.addLast((int) node.dist, node);
                            }
                        }
                    }
                }
            }

            //restore
            for (int i = 0; i < n; i++) {
                nodes[i].last = Math.min(nodes[i].dist + nodes[i].last, inf);
                nodes[i].dist = inf;
            }

            //debug.debug("q", q);
            //debug.debug("op", op);
            //debug.debug("nodes", nodes);
        }
    }
}

class Edge {
    Node to;
    long w;
    long reserve;
    Node from;

    public Edge(Node from, Node to, long w) {
        this.from = from;
        this.to = to;
        this.reserve = this.w = w;
    }
}

class Node {
    long dist;
    long last;

    int id;
    List<Edge> next = new ArrayList<>();

    @Override
    public String toString() {
        return "" + id + ":" + dist;
    }
}