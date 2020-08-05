package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CThreeCircuits {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] deg = new int[n];
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            deg[a]++;
            deg[b]++;
            Edge e = new Edge();
            e.a = nodes[a];
            e.b = nodes[b];
            e.a.adj.add(e);
            e.b.adj.add(e);
        }

        String no = "No";
        String yes = "Yes";
        for (int i = 0; i < n; i++) {
            if (deg[i] % 2 == 1) {
                out.println("No");
                return;
            }
        }


        List<Node> xy = new ArrayList<>(2);
        for (int i = 0; i < n; i++) {
            if (deg[i] >= 6) {
                out.println(yes);
                return;
            }
            if (deg[i] == 4) {
                xy.add(nodes[i]);
            }
        }

        if (xy.size() >= 3) {
            out.println(yes);
            return;
        }
        if (xy.size() <= 1) {
            out.println(no);
            return;
        }

        dfs(nodes[0]);
        List<Node> list = new ArrayList<>(trace);
        SequenceUtils.reverse(list);

        List<Node> compressed = new ArrayList<>();
        for (Node node : list) {
            if (xy.contains(node)) {
                compressed.add(node);
            }
        }

        if (compressed.get(0) == compressed.get(1) || compressed.get(1) == compressed.get(2)) {
            out.println(yes);
        } else {
            out.println(no);
        }
    }

    Deque<Node> trace = new ArrayDeque<>();

    public void dfs(Node root) {
        while (!root.adj.isEmpty()) {
            Edge tail = root.adj.remove(root.adj.size() - 1);
            if (tail.handled) {
                continue;
            }
            tail.handled = true;
            Node node = tail.other(root);
            dfs(node);
        }
        trace.addLast(root);
    }
}

class Edge {
    Node a;
    Node b;
    boolean handled;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
}