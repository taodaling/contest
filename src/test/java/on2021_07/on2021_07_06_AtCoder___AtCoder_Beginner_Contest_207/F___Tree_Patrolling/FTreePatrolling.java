package on2021_07.on2021_07_06_AtCoder___AtCoder_Beginner_Contest_207.F___Tree_Patrolling;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FTreePatrolling {
    int mod = (int) 1e9 + 7;

    public void calcSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            calcSize(node, root);
            root.size += node.size;
        }
    }

    public void dfs(Node root, Node p) {
        long[][][] prev = new long[2][2][root.size + 1];
        long[][][] next = new long[2][2][root.size + 1];
        prev[1][1][1] = 1;
        prev[0][0][0] = 1;
        int curSize = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    Arrays.fill(next[i][j], 0, curSize + node.size + 1, 0);
                }
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int us = 0; us <= curSize; us++) {
                        if (prev[i][j][us] == 0) {
                            continue;
                        }
                        for (int cover = 0; cover < 2; cover++) {
                            for (int k = 0; k < 2; k++) {
                                for (int vs = 0; vs <= node.size; vs++) {
                                    if (node.dp[cover][k][vs] == 0) {
                                        continue;
                                    }
                                    int active = us + vs;
                                    if (k == 1) {
                                        active += 1 - i;
                                    }
                                    if (j == 1) {
                                        active += 1 - cover;
                                    }
                                    next[k | i][j][active] += prev[i][j][us] * node.dp[cover][k][vs] % mod;
                                }
                            }
                        }
                    }
                }
            }
            curSize += node.size;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int us = 0; us <= curSize; us++) {
                        next[i][j][us] %= mod;
                    }
                }
            }
            long[][][] tmp = prev;
            prev = next;
            next = tmp;
        }
        root.dp = prev;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        calcSize(nodes[0], null);
        dfs(nodes[0], null);
        long[][][] dp = nodes[0].dp;
        for (int i = 0; i <= n; i++) {
            long ans = 0;
            for(int j = 0; j < 2; j++){
                for(int k = 0; k < 2; k++){
                    ans += dp[j][k][i];
                }
            }
            ans %= mod;
            out.println(ans);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    long[][][] dp;
    int size;
}
