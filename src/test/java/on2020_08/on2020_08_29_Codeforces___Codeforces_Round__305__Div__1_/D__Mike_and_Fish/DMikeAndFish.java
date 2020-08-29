package on2020_08.on2020_08_29_Codeforces___Codeforces_Round__305__Div__1_.D__Mike_and_Fish;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.*;

public class DMikeAndFish {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        Edge[] edges = new Edge[n];
        Map<Integer, Node> rows = new HashMap<>(n);
        Map<Integer, Node> cols = new HashMap<>(n);

        for (int i = 0; i < n; i++) {
            Node a = rows.computeIfAbsent(in.readInt(), x -> new Node());
            Node b = cols.computeIfAbsent(in.readInt(), x -> new Node());
            edges[i] = new Edge();
            edges[i].a = a;
            edges[i].b = b;
            a.adj.add(edges[i]);
            b.adj.add(edges[i]);
        }

        List<Node> nodes = new ArrayList<>();
        nodes.addAll(rows.values());
        nodes.addAll(cols.values());
        Deque<Node> odd = new ArrayDeque<>();
        for (Node node : nodes) {
            if (node.adj.size() % 2 == 1) {
                odd.add(node);
            }
            node.deg = node.adj.size();
        }

        List<Edge> redundant = new ArrayList<>();
        while (!odd.isEmpty()) {
            Node head = odd.removeFirst();
            if (head.deg % 2 == 0) {
                continue;
            }
            Edge e;
            while ((e = pop(head.adj)).removed) ;
            Node other = e.other(head);
            e.removed = true;
            redundant.add(e);
            head.deg--;
            other.deg--;
            odd.addLast(other);
        }

        for (Node node : nodes) {
            if (node.adj.isEmpty()) {
                continue;
            }
            dq.clear();
            euler(node, null);
            boolean red = true;
            while (!dq.isEmpty()) {
                Edge e = dq.removeFirst();
                setColor(e, red);
                red = !red;
            }
        }

        SequenceUtils.reverse(redundant);
        for (Edge e : redundant) {
            setColor(e, e.a.redMore + e.b.redMore > 0 ? false : true);
        }

        for (Edge e : edges) {
            out.append(e.red ? 'r' : 'b');
        }
    }

    public void setColor(Edge e, boolean red) {
        e.red = red;
        int val = red ? 1 : -1;
        e.a.redMore += val;
        e.b.redMore += val;
    }

    Deque<Edge> dq = new ArrayDeque<>();

    public void euler(Node root, Edge p) {
        while (!root.adj.isEmpty()) {
            Edge e = pop(root.adj);
            if (e.removed) {
                continue;
            }
            e.removed = true;
            euler(e.other(root), e);
        }
        if(p != null) {
            dq.addLast(p);
        }
    }


    public Edge pop(List<Edge> list) {
        return list.remove(list.size() - 1);
    }

    public void intersect(int[] a, int[] b) {
        a[0] = Math.max(a[0], b[0]);
        a[1] = Math.min(a[1], b[1]);
    }

}

class Edge {
    Node a;
    Node b;
    boolean red;
    boolean removed;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int redMore;
    int deg;
}