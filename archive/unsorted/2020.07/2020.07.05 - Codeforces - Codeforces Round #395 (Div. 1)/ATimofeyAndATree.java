package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class ATimofeyAndATree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        for (int i = 0; i < n; i++) {
            nodes[i].c = in.readInt();
        }

        for (Node node : nodes) {
            for (Node sub : node.adj) {
                if (node.c == sub.c) {
                    continue;
                }
                if (tryNode(node)) {
                    out.println("YES");
                    out.println(node.id + 1);
                    return;
                }
                if (tryNode(sub)) {
                    out.println("YES");
                    out.println(sub.id + 1);
                    return;
                }
                out.println("NO");
                return;
            }
        }

        out.println("YES");
        out.println(1);
    }

    public boolean tryNode(Node root) {
        for (Node node : root.adj) {
            if (!dfs(node, root)) {
                return false;
            }
        }
        return true;
    }

    public boolean dfs(Node root, Node p) {
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (node.c != root.c || !dfs(node, root)) {
                return false;
            }
        }
        return true;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int c;
    int id;
}