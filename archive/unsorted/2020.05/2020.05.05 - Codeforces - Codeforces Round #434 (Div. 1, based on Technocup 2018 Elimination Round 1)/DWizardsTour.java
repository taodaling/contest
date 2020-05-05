package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DWizardsTour {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        Edge[] edges = new Edge[m];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            edges[i] = new Edge();
            edges[i].a = a;
            edges[i].b = b;
            a.next.add(edges[i]);
            b.next.add(edges[i]);

            a.odd ^= 1;
            edges[i].dir = a;
        }

        for (int i = 0; i < n; i++) {
            dfs(nodes[i], null);
        }

        for (int i = 0; i < m; i++) {
            edges[i].dir.to.add(edges[i]);
        }

        List<int[]> ans = new ArrayList<>(m);
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < nodes[i].to.size(); j += 2) {
                Edge e1 = nodes[i].to.get(j - 1);
                Edge e2 = nodes[i].to.get(j);
                ans.add(new int[]{e1.other(nodes[i]).id, i, e2.other(nodes[i]).id});
            }
        }

        out.println(ans.size());
        for (int[] way : ans) {
            for (int x : way) {
                out.append(x + 1).append(' ');
            }
            out.println();
        }
    }

    public void dfs(Node root, Edge from) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        for (Edge e : root.next) {
            Node node = e.other(root);
            dfs(node, e);
        }

        if (root.odd == 1 && from != null) {
            from.reverse();
        }
    }
}

class Edge {
    Node a;
    Node b;
    Node dir;

    public void reverse() {
        dir = other(dir);
        a.odd ^= 1;
        b.odd ^= 1;
    }

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    int odd;
    int id;
    boolean visited;

    List<Edge> to = new ArrayList<>();

    @Override
    public String toString() {
        return "" + id;
    }
}
