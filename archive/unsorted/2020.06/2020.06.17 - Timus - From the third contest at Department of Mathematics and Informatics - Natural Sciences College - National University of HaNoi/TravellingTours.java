package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TravellingTours {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        DSU dsu = new DSU(n);

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        List<List<Node>> ans = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            if (dsu.find(a.id) == dsu.find(b.id)) {
                //find loop
                trace.clear();
                dfs(a, null, b);
                ans.add(new ArrayList<>(trace));
            } else {
                dsu.merge(a.id, b.id);
                a.adj.add(b);
                b.adj.add(a);
            }
        }

        out.println(ans.size());
        for (List<Node> list : ans) {
            out.append(list.size()).append(' ');
            for (Node node : list) {
                out.append(node.id + 1).append(' ');
            }
            out.println();
        }
    }

    Deque<Node> trace = new ArrayDeque<>(200);

    public boolean dfs(Node root, Node p, Node target) {
        trace.addLast(root);

        if (root == target) {
            return true;
        }

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (dfs(node, root, target)) {
                return true;
            }
        }

        trace.removeLast();
        return false;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
}

