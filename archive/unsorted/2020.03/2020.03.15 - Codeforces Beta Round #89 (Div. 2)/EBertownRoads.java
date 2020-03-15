package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EBertownRoads {
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
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt() - 1];
            edges[i].b = nodes[in.readInt() - 1];
            edges[i].a.next.add(edges[i]);
            edges[i].b.next.add(edges[i]);
        }

        for (Node node : nodes) {
            tarjan(node, null);
        }

        for (Node node : nodes) {
            if (node.set != nodes[0].set) {
                out.println(0);
                return;
            }
        }

        for (Edge e : edges) {
            if (e.tree) {
                out.append(e.min().id + 1).append(' ').append(e.max().id + 1).println();
            }else{
                out.append(e.max().id + 1).append(' ').append(e.min().id + 1).println();
            }
        }
    }

    Deque<Node> dq = new ArrayDeque<>(100000);
    int order = 1;

    public void tarjan(Node root, Edge p) {
        if (root.dfn != 0) {
            return;
        }
        if (p != null) {
            p.tree = true;
        }
        root.low = root.dfn = order++;
        root.instk = true;
        dq.addLast(root);
        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            tarjan(node, e);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }
        if (root.dfn == root.low) {
            while (true) {
                Node tail = dq.removeLast();
                tail.instk = false;
                tail.set = root;
                if (tail == root) {
                    break;
                }
            }
        }
    }
}

class Edge {
    Node a;
    Node b;
    boolean tree;

    Node other(Node x) {
        return x == a ? b : a;
    }

    Node min() {
        return a.dfn < b.dfn ? a : b;
    }

    Node max() {
        return other(min());
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    int dfn;
    int low;
    int id;
    Node set;
    boolean instk;
}