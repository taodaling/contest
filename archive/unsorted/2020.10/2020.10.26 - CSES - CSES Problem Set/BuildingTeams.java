package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BuildingTeams {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            u.adj.add(v);
            v.adj.add(u);
        }

        for (Node node : nodes) {
            if (node.color == -1) {
                dfs(node, 0);
            }
        }

        if (!valid) {
            out.println("IMPOSSIBLE");
            return;
        }

        for (int i = 0; i < n; i++) {
            out.append(nodes[i].color + 1).append(' ');
        }
    }

    boolean valid = true;

    public void dfs(Node root, int c) {
        if (root.color != -1) {
            if (root.color != c) {
                valid = false;
            }
            return;
        }
        root.color = c;
        for (Node node : root.adj) {
            dfs(node, c ^ 1);
        }
    }
}


class Node {
    List<Node> adj = new ArrayList<>();
    int color = -1;
}