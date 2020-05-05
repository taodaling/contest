package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class EPolicemanAndATree {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.w = in.readInt();
            a.next.add(e);
            b.next.add(e);
        }


        Node root = nodes[in.readInt() - 1];

        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            Node node = nodes[in.readInt() - 1];
            node.thief++;
        }
        m -= root.thief;
        root.thief = 0;
        if (m == 0) {
            out.println(0);
            return;
        }


        f = new int[n][m + 1];
        List<Node> leaf = new ArrayList<>(n);
        fdp = new int[n + 1][m + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 0; j < n; j++) {
                if (!isLeaf(nodes[j])) {
                    continue;
                }
                leaf.clear();
                dfs(nodes[j], null, 0, leaf);
                f[j][i] = maxStep(leaf, i, i);
            }
        }

        debug.debug("f", f);
        int ans = inf;
        for (Edge e : root.next) {
            Node node = e.other(root);
            leaf.clear();
            int thief = dfs(node, root, e.w, leaf);
            if(thief == 0){
                continue;
            }
            int against = maxStep(leaf, thief, m);
            ans = Math.min(ans, against);
        }

        out.println(ans);
    }

    int[][] fdp;
    int inf = (int) 1e9;

    public int maxStep(List<Node> leaf, int m, int total) {
        SequenceUtils.deepFill(fdp, -inf);
        fdp[0][0] = 0;
        int n = leaf.size();
        for (int i = 1; i <= n; i++) {
            Node node = leaf.get(i - 1);
            for (int j = 0; j <= m; j++) {
                fdp[i][j] = fdp[i - 1][j];
                for (int k = 1; k <= j; k++) {
                    if (j - k > 0) {
                        fdp[i][j] = Math.max(fdp[i][j], Math.min(fdp[i - 1][j - k], node.depth + f[node.id][total - k]));
                    } else {
                        fdp[i][j] = Math.max(fdp[i][j], node.depth + f[node.id][total - k]);
                    }
                }
            }
        }
        return fdp[n][m];
    }

    public boolean isLeaf(Node node) {
        return node.next.size() == 1;
    }

    int[][] f;
    Node[] nodes;

    public int dfs(Node root, Node p, int depth, List<Node> leaf) {
        root.depth = depth;
        boolean isLeaf = true;
        int ans = root.thief;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            isLeaf = false;
            ans += dfs(node, root, depth + e.w, leaf);
        }

        if (isLeaf) {
            leaf.add(root);
        }
        return ans;
    }
}

class Edge {
    Node a;
    Node b;
    int w;

    Node other(Node x) {
        return a == x ? b : a;
    }
}


class Node {
    List<Edge> next = new ArrayList<>();
    int id;

    @Override
    public String toString() {
        return "" + id;
    }

    int thief;
    int depth;
    boolean isLeaf;
}