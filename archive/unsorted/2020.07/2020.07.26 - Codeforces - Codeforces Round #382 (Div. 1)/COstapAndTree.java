package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class COstapAndTree {
    int n;
    int k;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for(int i = 0; i < n - 1; i++){
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        dfs(nodes[0], null);
        int ans = 0;
        int[][] dp = nodes[0].dp;
        for(int i = 0; i <= k; i++){
            ans = mod.plus(dp[0][i], ans);
        }
        out.println(ans);
    }

    Modular mod = new Modular(1e9 + 7);

    public void dfs(Node root, Node p) {
        //set
        //or not
        int[][] last = new int[2][k + 1];
        int[][] next = new int[2][k + 1];

        last[0][0] = last[1][0] = 1;
        for (Node node : root.adj) {
            if(node == p){
                continue;
            }
            dfs(node, root);
            SequenceUtils.deepFill(next, 0);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j <= k; j++) {
                    for (int t = 0; t < 2; t++) {
                        for (int z = 0; z <= k; z++) {
                            int way = mod.mul(last[i][j], node.dp[t][z]);
                            if (way == 0) {
                                continue;
                            }
                            if (i == t) {
                                if (i == 0) {
                                    //min
                                    next[i][Math.min(j, z + 1)] = mod.plus(next[i][Math.min(j, z + 1)], way);
                                } else {
                                    next[i][Math.max(j, z + 1)] = mod.plus(next[i][Math.max(j, z + 1)], way);
                                }
                            } else {
                                int zero = i == 0 ? j : z + 1;
                                int one = i == 0 ? z + 1 : j;
                                if (zero + one <= k) {
                                    next[0][zero] = mod.plus(next[0][zero], way);
                                } else {
                                    next[1][one] = mod.plus(next[1][one], way);
                                }
                            }
                        }
                    }
                }
            }

            int[][] tmp = last;
            last = next;
            next = tmp;
        }

        last[1][k] = 0;
        root.dp = last;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    //black
    int[][] dp;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}