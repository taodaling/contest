package on2021_08.on2021_08_19_CS_Academy___Virtual_Round__11.Connected_Tree_Subgraphs;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class ConnectedTreeSubgraphs {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e5, mod);

    public void dfs(Node root, Node p) {
        root.size = 0;
        root.dp = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.size += node.size;
            root.dp = root.dp * node.dp % mod
                    * comb.combination(root.size, node.size) % mod;
        }
        root.size++;
    }

    public void dfs2(Node root, Node p, int size, long dp) {
        int n = root.adj.size();
        int[] prevSize = new int[n];
        int[] nextSize = new int[n];
        long[] prev = new long[n];
        long[] next = new long[n];
        for (int i = 0; i < n; i++) {
            Node node = root.adj.get(i);
            if (node == p) {
                prevSize[i] = size;
                prev[i] = dp;
            } else {
                prevSize[i] = node.size;
                prev[i] = node.dp;
            }
            int curSize = prevSize[i];
            if (i > 0) {
                prevSize[i] += prevSize[i - 1];
                prev[i] = prev[i - 1] * prev[i] % mod * comb.combination(prevSize[i], curSize) % mod;
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            Node node = root.adj.get(i);
            if (node == p) {
                nextSize[i] = size;
                next[i] = dp;
            } else {
                nextSize[i] = node.size;
                next[i] = node.dp;
            }
            int curSize = nextSize[i];
            if (i + 1 < n) {
                nextSize[i] += nextSize[i + 1];
                next[i] = next[i + 1] * next[i] % mod * comb.combination(nextSize[i], curSize) % mod;
            }
        }

        root.asRoot = prev[n - 1];
        for (int i = 0; i < n; i++) {
            Node node = root.adj.get(i);
            if (node == p) {
                continue;
            }
            int totalSize = 0;
            long way = 1;
            if (i > 0) {
                totalSize += prevSize[i - 1];
                way = way * prev[i - 1] % mod;
            }
            if (i + 1 < n) {
                totalSize += nextSize[i + 1];
                way = way * next[i + 1] % mod;
                way = way * comb.combination(totalSize, nextSize[i + 1]) % mod;
            }
            totalSize++;
            dfs2(node, root, totalSize, way);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        if (n == 1) {
            out.println(1);
            return;
        }
        dfs(nodes[0], null);
        dfs2(nodes[0], null, 0, 1);
        long sum = 0;
        for (Node node : nodes) {
            debug.debug("node", node);
            debug.debug("asRoot", node.asRoot);
            sum += node.asRoot;
        }
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }

    Debug debug = new Debug(false);
}

class Node {
    List<Node> adj = new ArrayList<>();
    long dp;
    long asRoot;
    int size;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}