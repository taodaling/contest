package contest;

import sun.security.x509.EDIPartyName;
import template.datastructure.DSU;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class DWeightingATree {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].w = in.readInt();
        }
        DSU dsu = new DSU(n);
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt() - 1];
            edges[i].b = nodes[in.readInt() - 1];
            if (dsu.find(edges[i].a.id) != dsu.find(edges[i].b.id)) {
                edges[i].tree = true;
                dsu.merge(edges[i].a.id, edges[i].b.id);
                edges[i].a.next.add(edges[i]);
                edges[i].b.next.add(edges[i]);
            }
        }

        dfs(nodes[0], null, 0);
        long diff = 0;
        for (Node node : nodes) {
            if (node.sign) {
                diff += node.w;
            } else {
                diff -= node.w;
            }
        }

        if (diff % 2 != 0) {
            out.println("NO");
            return;
        }

        for (Edge e : edges) {
            if (e.tree) {
                continue;
            }
            if (e.a.sign != e.b.sign) {
                continue;
            }
            if (e.a.sign) {
                e.val = diff / 2;
            } else {
                e.val = -diff / 2;
            }
            e.a.w -= e.val;
            e.b.w -= e.val;
            diff = 0;
            break;
        }

        if (diff != 0) {
            out.println("NO");
            return;
        }

        dfs(nodes[0], null, 0);
        out.println("YES");
        for (Edge e : edges) {
            out.println(e.val);
        }
    }


    public long dfs(Node root, Edge p, int d) {
        root.sign = d % 2 == 0;
        long w = root.w;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (e == p) {
                continue;
            }
            long need = dfs(node, e, d + 1);
            e.val = need;
            w -= need;
        }
        return w;
    }
}

class Edge {
    Node a;
    Node b;
    long val;
    boolean tree;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    int id;
    List<Edge> next = new ArrayList<>();
    long w;
    boolean sign;
    Edge p;
}