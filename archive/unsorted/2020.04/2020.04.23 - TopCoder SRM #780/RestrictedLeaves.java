package contest;

import template.math.Modular;

import java.util.ArrayList;
import java.util.List;

public class RestrictedLeaves {
    Modular mod = new Modular(1e9 + 7);

    public int count(int[] parent) {
        int n = parent.length;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[parent[i]];
            p.next.add(nodes[i]);
        }

        dfs(nodes[0]);
        int ans = 0;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                for(int k = 0; k < 2; k++){
                    if(j + k == 2){
                        continue;
                    }
                    ans = mod.plus(ans, nodes[0].dp[i][j][k]);
                }
            }
        }
        //ans = mod.subtract(ans, 1);

//        if(n >= 20){
//            throw new RuntimeException();
//        }

        return ans;
    }

    public void dfs(Node root) {
        if (root.next.isEmpty()) {
            root.dp[0][0][0] = 1;
            root.dp[1][1][1] = 1;
            return;
        }

        root.dp[0][0][0] = 1;
        root.dp[1][0][0] = 1;

        int[][][] dp = null;
        for (Node node : root.next) {
            dfs(node);
            if (dp == null) {
                dp = node.dp;
            } else {
                dp = merge(dp, node.dp);
            }
        }


        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                root.dp[0][j][k] = mod.plus(dp[0][j][k], dp[1][j][k]);
                root.dp[1][j][k] = dp[0][j][k];
            }
        }
    }

    public int[][][] merge(int[][][] a, int[][][] b) {
        int[][][] c = new int[2][2][2];
        for (int ai = 0; ai < 2; ai++) {
            for (int aj = 0; aj < 2; aj++) {
                for (int ak = 0; ak < 2; ak++) {
                    for (int bi = 0; bi < 2; bi++) {
                        for (int bj = 0; bj < 2; bj++) {
                            for (int bk = 0; bk < 2; bk++) {
                                if (ak + bj == 2) {
                                    continue;
                                }
                                c[ai | bi][aj][bk] = mod.plus(c[ai | bi][aj][bk],
                                        mod.mul(a[ai][aj][ak], b[bi][bj][bk]));
                            }
                        }
                    }
                }
            }
        }

        return c;
    }
}

class Node {
    List<Node> next = new ArrayList<Node>();
    int[][][] dp = new int[2][2][2];
}
