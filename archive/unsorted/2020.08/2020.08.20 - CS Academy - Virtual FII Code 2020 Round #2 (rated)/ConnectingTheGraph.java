package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectingTheGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].a = in.readLong();
        }
        Node[] sorted = nodes.clone();
        Arrays.sort(sorted, (a, b) -> Long.compare(a.a, b.a));
        List<Edge> edgeList = new ArrayList<>(n);
        for (int i = 1; i < sorted.length; i++) {
            Node a = sorted[i - 1];
            Node b = sorted[i];
            Edge e = new Edge(a, b, (b.a - a.a) * (b.a - a.a));
            edgeList.add(e);
        }
        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            dsu.merge(a, b);
        }
        edgeList.sort((a, b) -> Long.compare(a.w, b.w));
        long ans = 0;
        for (Edge e : edgeList) {
            if (dsu.find(e.a.id) == dsu.find(e.b.id)) {
                continue;
            }
            ans += e.w;
            dsu.merge(e.a.id, e.b.id);
        }
        out.println(ans);
    }
}

class Node {
    int id;
    long a;
}

class Edge {
    Node a;
    Node b;
    long w;

    public Edge(Node a, Node b, long w) {
        this.a = a;
        this.b = b;
        this.w = w;
    }
}
