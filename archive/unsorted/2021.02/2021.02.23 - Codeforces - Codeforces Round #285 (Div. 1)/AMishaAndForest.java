package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class AMishaAndForest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].deg = in.ri();
            nodes[i].xor = in.ri();
            set.add(nodes[i]);
        }

        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            if (head.deg == 0) {
                continue;
            }
            assert head.deg == 1;
            addEdge(head, nodes[head.xor]);
        }

        out.println(edges.size());
        for(Edge e : edges){
            out.append(e.a.id).append(' ').append(e.b.id).println();
        }
    }

    public void addEdge(Node a, Node b) {
        a.xor ^= b.id;
        b.xor ^= a.id;
        set.remove(a);
        set.remove(b);
        a.deg--;
        b.deg--;
        set.add(a);
        set.add(b);
        edges.add(new Edge(a, b));
    }

    List<Edge> edges = new ArrayList<>();
    TreeSet<Node> set = new TreeSet<>(Comparator.<Node>comparingInt(x -> x.deg).thenComparingInt(x -> x.id));
}

class Edge {
    Node a;
    Node b;

    public Edge(Node a, Node b) {
        this.a = a;
        this.b = b;
    }
}

class Node {
    int deg;
    int xor;
    int id;
}
