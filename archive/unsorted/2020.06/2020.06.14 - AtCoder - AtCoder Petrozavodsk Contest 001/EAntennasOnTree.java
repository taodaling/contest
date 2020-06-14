package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.rand.Randomized;

import java.util.ArrayList;
import java.util.List;

public class EAntennasOnTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.adj.add(b);
            b.adj.add(a);
        }

        List<Node> leaf = new ArrayList<>(n);
        for (Node node : nodes) {
            if (node.adj.size() == 1) {
                leaf.add(node);
            }
        }

        int ans = n;
        int randTime = 100;
        RandomWrapper rw = new RandomWrapper();
        while (randTime-- > 0) {
            Node node = leaf.get(rw.nextInt(0, leaf.size() - 1));
            ans = Math.min(ans, dfs(node, null) + 1);
        }
        out.println(ans);
    }

    public int dfs(Node root, Node p) {
        int zero = 0;
        int cnt = 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            int ans = dfs(node, root);
            if (ans == 0) {
                zero++;
            } else {
                cnt += ans;
            }
        }

        if (zero > 0) {
            cnt += zero - 1;
            zero = 1;
        }

        return cnt;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
}
