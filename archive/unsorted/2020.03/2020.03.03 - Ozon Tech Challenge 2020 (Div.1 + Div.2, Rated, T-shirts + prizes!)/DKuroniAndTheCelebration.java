package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DKuroniAndTheCelebration {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        Node root = nodes[1];
        while (true) {
            for (int i = 1; i <= n; i++) {
                nodes[i].depth = -1;
            }
            int cnt = dfs(root, null, 0);
            if (cnt == 1) {
                out.append("! ").append(root.id).println();
                out.flush();
                return;
            }
            dfs(root, null, 0);
            for (int i = 1; i <= n; i++) {
                if (!nodes[i].deleted && nodes[i].depth > root.depth) {
                    root = nodes[i];
                }
            }
            dfs(root, null, 0);
            Node end = root;
            for (int i = 1; i <= n; i++) {
                if (!nodes[i].deleted && nodes[i].depth > end.depth) {
                    end = nodes[i];
                }
            }
            out.append("? ").append(root.id).append(' ').append(end.id).println();
            out.flush();
            botToUp(end);
            root = nodes[in.readInt()];
            root.deleted = false;
        }
    }

    public void botToUp(Node root) {
        if (root == null) {
            return;
        }
        root.deleted = true;
        botToUp(root.parent);
    }

    public int dfs(Node root, Node p, int depth) {
        if (root.deleted) {
            return 0;
        }
        root.parent = p;
        root.depth = depth;
        int ans = 1;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            ans += dfs(node, root, depth + 1);
        }
        return ans;
    }

}

class Node {
    Node parent;
    int depth;
    int id;
    boolean deleted;
    List<Node> next = new ArrayList<>();
}