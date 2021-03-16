package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DRandomFunctionAndTree {
    int mod = (int) 1e9 + 7;

    void dfs(Node root) {
        long[][][] prev = new long[2][2][2];
        long[][][] next = new long[2][2][2];
        prev[0][0][0] = 1;
        for (Node node : root.adj) {
            dfs(node);
            SequenceUtils.deepFill(next, 0L);

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2; k++) {
                        //skip
                        next[i][j][k] += prev[i][j][k];
                        //add odd
                        next[i ^ 1][j][1] += prev[i][j][k] * node.dp[1] % mod;
                        //add even
                        next[i][1][k] += prev[i][j][k] * node.dp[0] % mod;
                    }
                }
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2; k++) {
                        next[i][j][k] %= mod;
                    }
                }
            }
            long[][][] tmp = prev;
            prev = next;
            next = tmp;
        }
        long[] dp = new long[2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i ^ 1] += prev[i][j][k];
                }
            }
        }
        dp[0] += prev[1][1][1];
        dp[1] += prev[0][0][1] + prev[0][1][1];
        dp[0] %= mod;
        dp[1] %= mod;
        root.dp = dp;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node node = nodes[i + 1];
            Node p = nodes[in.ri() - 1];
            p.adj.add(node);
        }
        dfs(nodes[0]);
        long[] dp = nodes[0].dp;
        long ans = dp[0] + dp[1];
        ans %= mod;
        out.println(ans);
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    long[] dp;
}
