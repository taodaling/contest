package on2021_08.on2021_08_30_Facebook_Coding_Competitions___Facebook_Hacker_Cup_2021_Qualification_Round.C2__Gold_Mine___Chapter_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class C2GoldMineChapter2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        int n = in.ri();
        int k = 1;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].weight = in.rl();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        if (k == 0) {
            out.println(nodes[0].weight);
            return;
        }

        size(nodes[0], null);
        dfs(nodes[0], null, 1);
        long[][] dp = nodes[0].dp;
        long maxGot = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= n; j++) {
                int take = i + j;
                if (take <= k - 1) {
                    maxGot = Math.max(maxGot, dp[i][j]);
                }
            }
        }
        out.println(maxGot);
    }

    long inf = (long) 1e18;

    public void size(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            size(node, root);
            root.size += node.size;
        }
    }

    public void dfs(Node root, Node p, int free) {
        long[][][][] prev = new long[2][2][2][root.size + 1];
        long[][][][] next = new long[2][2][2][root.size + 1];
        long curSize = 1;
        SequenceUtils.deepFill(prev, -inf);
        SequenceUtils.deepFill(next, -inf);
        prev[0][0][free][0] = 0;
        prev[1][1][free][0] = 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root, 0);
            for (int a = 0; a < 2; a++) {
                for (int b = 0; b < 2; b++) {
                    for (int c = 0; c < 2; c++) {
                        for (int i = 0; i <= curSize; i++) {
                            next[a][b][c][i] = -inf;
                        }
                    }
                }
            }
            for (int a = 0; a < 2; a++) {
                for (int b = 0; b < 2; b++) {
                    for (int c = 0; c < 2; c++) {
                        for (int i = 0; i <= curSize; i++) {
                            for (int d = 0; d < 2; d++) {
                                for (int j = 0; j <= node.size; j++) {
                                    int na = a | d;
                                    int nb = b ^ d;
                                    int nc = c;
                                    int ni = i + j;
                                    if (b + d == 2) {
                                        ni = ni + 1 - c;
                                        nc = 0;
                                    }
                                    if (ni > curSize + node.size) {
                                        continue;
                                    }
                                    next[na][nb][nc][ni] = Math.max(next[na][nb][nc][ni],
                                            prev[a][b][c][i] + node.dp[d][j]);
                                }
                            }
                        }
                    }
                }
            }
            long[][][][] tmp = prev;
            prev = next;
            next = tmp;
            curSize += node.size;
        }
        long[][] dp = new long[2][root.size + 1];
        SequenceUtils.deepFill(dp, -inf);
        for (int a = 0; a < 2; a++) {
            for (int b = 0; b < 2; b++) {
                for (int c = 0; c < 2; c++) {
                    for (int i = 0; i <= root.size; i++) {
                        int nb = b;
                        if (nb == 1 && c == 1) {
                            nb = 0;
                        }
                        dp[nb][i] = Math.max(dp[nb][i], prev[a][b][c][i] + root.weight * a);
                    }
                }
            }
        }
        root.dp = dp;
    }

}


class Node {
    int size;
    List<Node> adj = new ArrayList<>();
    long[][] dp;
    long weight;
}
