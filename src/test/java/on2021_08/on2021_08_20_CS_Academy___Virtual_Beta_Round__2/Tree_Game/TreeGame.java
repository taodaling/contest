package on2021_08.on2021_08_20_CS_Academy___Virtual_Beta_Round__2.Tree_Game;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeGame {
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

        dfs(nodes[0], null);
        int[] dp = nodes[0].dp;
        int ans = Arrays.stream(dp).max().orElse(-1);
        out.println(ans);
    }

    int inf = (int) 1e8;

    public void dfs(Node root, Node p) {
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }

        root.dp = new int[3];
        {
            //put
            int cost = 0;
            for (Node node : root.adj) {
                if (node == p) {
                    continue;
                }

                int contrib = node.dp[0];
                contrib = Math.max(contrib, node.dp[1] + 1);

                cost += contrib;
            }

            root.dp[2] = cost;
        }

        {
            //not put
            int[] prev = new int[3];
            int[] next = new int[3];
            Arrays.fill(prev, -inf);
            prev[0] = 0;
            for (Node node : root.adj) {
                if (node == p) {
                    continue;
                }
                Arrays.fill(next, -inf);
                for (int i = 0; i < 3; i++) {
                    for (int k = 0; k < 3; k++) {
                        int ni = i + Integer.signum(k);
                        ni = Math.min(ni, 2);
                        next[ni] = Math.max(next[ni], prev[i] + node.dp[k]);
                    }
                }
                int[] tmp = prev;
                prev = next;
                next = tmp;
            }

            root.dp[0] = Math.max(prev[0], prev[2] + 1);
            root.dp[1] = prev[1];
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int[] dp;
}
