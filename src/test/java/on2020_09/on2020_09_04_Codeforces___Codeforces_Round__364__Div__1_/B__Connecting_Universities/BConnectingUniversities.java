package on2020_09.on2020_09_04_Codeforces___Codeforces_Round__364__Div__1_.B__Connecting_Universities;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BConnectingUniversities {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < 2 * k; i++) {
            nodes[in.readInt() - 1].u = true;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        dfs(nodes[0], null);
        dp(nodes[0], null, 0);
        long ans = 0;
        for (Node node : nodes) {
            if (node.u) {
                ans += node.depth;
            }
            ans -= node.lca * 2L * node.depth;
        }

        out.println(ans);
    }

    public static void dfs(Node root, Node p) {
        root.size = root.u ? 1 : 0;
        root.depth = p == null ? 0 : p.depth + 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.size += node.size;
        }
    }

    public static void dp(Node root, Node p, int remove) {
        root.adj.remove(p);
        root.adj.sort((a, b) -> Integer.compare(a.size, b.size));
        int max = root.adj.get(root.adj.size() - 1).size;
        if (max - remove > root.size - max) {
            root.lca = root.size - max;
            dp(root.adj.get(root.adj.size() - 1), root, remove + root.lca);
        } else {
            root.lca = (root.size - remove) / 2;
        }
    }
}

class Node {
    boolean u;
    int depth;
    int lca;
    int size;
    List<Node> adj = new ArrayList<>();
}
