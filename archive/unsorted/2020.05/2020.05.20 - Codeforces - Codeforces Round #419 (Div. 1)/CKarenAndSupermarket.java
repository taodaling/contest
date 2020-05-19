package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class CKarenAndSupermarket {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int b = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].c = in.readInt();
            nodes[i].d = in.readInt();

            if(i > 0){
                Node p = nodes[in.readInt() - 1];
                p.adj.add(nodes[i]);
            }
        }



        dfsForSize(nodes[0]);
        dfs(nodes[0]);

        int ans = 0;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j <= n; j++){
                if(nodes[0].dp[i][j] <= b){
                    ans = Math.max(ans, j);
                }
            }
        }

        out.println(ans);
    }

    public void dfsForSize(Node root) {
        root.size = 1;
        for (Node node : root.adj) {
            dfsForSize(node);
            root.size += node.size;
        }
    }

    long inf = (long) 1e18;

    public void dfs(Node root) {
        root.dp = new long[2][root.size + 1];
        SequenceUtils.deepFill(root.dp, inf);
        root.dp[0][0] = 0;
        root.dp[0][1] = root.c;
        root.dp[1][1] = root.c - root.d;
        int curSize = 1;

        for (Node node : root.adj) {
            dfs(node);
            for (int i = node.size + curSize; i >= 0; i--) {
                //i - j <= node.size
                //j >= i - node.size
                long dp0 = inf;
                long dp1 = inf;
                for (int j = Math.max(0, i - node.size); j <= curSize && j <= i; j++) {
                    int k = i - j;
                    //for zero
                    dp0 = Math.min(dp0, node.dp[0][k] + root.dp[0][j]);
                    dp1 = Math.min(dp1, Math.min(node.dp[1][k], node.dp[0][k]) + root.dp[1][j]);
                }
                root.dp[0][i] = dp0;
                root.dp[1][i] = dp1;
            }

            curSize += node.size;
        }

        return;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int c;
    int d;
    int size;
    long[][] dp;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
