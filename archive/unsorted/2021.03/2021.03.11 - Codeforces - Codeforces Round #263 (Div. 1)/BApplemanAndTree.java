package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BApplemanAndTree {
    int mod = (int) 1e9 + 7;

    void dfs(Node root, Node p) {
        long[] prev = new long[2];
        long[] next = new long[2];
        prev[root.val] = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            Arrays.fill(next, 0);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if (i + j < 2) {
                        next[i + j] += prev[i] * node.dp[j] % mod;
                    }
                }
            }
            for (int i = 0; i < 2; i++) {
                next[i] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        if (p != null) {
            prev[0] += prev[1];
            prev[0] %= mod;
        }
        root.dp = prev;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.ri()];
            p.adj.add(nodes[i]);
            nodes[i].adj.add(p);
        }
        for (Node node : nodes) {
            node.val = in.ri();
        }
        dfs(nodes[0], null);
        long ans = nodes[0].dp[1];
        out.println(ans);
    }
}

class Node {
    int id;
    int val;
    long[] dp;
    List<Node> adj = new ArrayList<>();
}