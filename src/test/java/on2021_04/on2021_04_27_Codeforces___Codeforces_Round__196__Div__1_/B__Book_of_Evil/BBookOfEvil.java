package on2021_04.on2021_04_27_Codeforces___Codeforces_Round__196__Div__1_.B__Book_of_Evil;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BBookOfEvil {
    int inf = (int) 1e9;

    void dfs(Node root, Node p) {
        root.depth = p == null ? 0 : p.depth + 1;
        root.farthest = -inf;
        if (root.special) {
            root.farthest = 0;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.farthest = Math.max(root.farthest, node.farthest + 1);
        }
    }

    void dfsUpDown(Node root, Node p, int farthest) {
        int n = root.adj.size();
        root.farthest = Math.max(root.farthest, farthest);
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            Node node = root.adj.get(i);
            if (node == p) {
                prev[i] = farthest;
            } else {
                prev[i] = node.farthest + 1;
            }
        }
        int[] post = prev.clone();
        for (int i = 1; i < n; i++) {
            prev[i] = Math.max(prev[i - 1], prev[i]);
        }
        for (int i = n - 2; i >= 0; i--) {
            post[i] = Math.max(post[i], post[i + 1]);
        }
        for (int i = 0; i < n; i++) {
            Node node = root.adj.get(i);
            if (node == p) {
                continue;
            }
            int forChild = -inf;
            if (root.special) {
                forChild = Math.max(forChild, 0);
            }
            if (i > 0) {
                forChild = Math.max(forChild, prev[i - 1]);
            }
            if (i + 1 < n) {
                forChild = Math.max(forChild, post[i + 1]);
            }
            dfsUpDown(node, root, forChild + 1);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int d = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            nodes[in.ri() - 1].special = true;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        dfsUpDown(nodes[0], null, -inf);
        int ans = 0;
        for (Node node : nodes) {
            if (node.farthest <= d) {
                ans++;
            }
        }
        out.println(ans);
    }
}

class Node {
    int depth;
    List<Node> adj = new ArrayList<>();
    boolean special;
    int farthest;
}