package on2021_05.on2021_05_24_Codeforces___Codeforces_Round__722__Div__1_.A__Parsa_s_Humongous_Tree;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class AParsasHumongousTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].l = in.ri();
            nodes[i].r = in.ri();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        long[] ans = dp(nodes[0], null);
        long best = Math.max(ans[0], ans[1]);
        out.println(best);
    }

    public long eval(long l, long r, int x){
        return x == 0 ? l : r;
    }

    public long[] dp(Node root, Node p) {
        long[][] ans = new long[2][2];
        for(Node node : root.adj){
            if(node == p){
                for(int i = 0; i < 2; i++){
                    for(int j = 0; j < 2; j++){
                        ans[i][j] += Math.abs(eval(root.l, root.r, i) - eval(p.l, p.r, j));
                    }
                }
                continue;
            }
            long[] res = dp(node, root);
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 2; j++){
                    ans[i][j] += res[i];
                }
            }
        }
        long[] dp = new long[2];
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                dp[j] = Math.max(dp[j], ans[i][j]);
            }
        }
        return dp;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int l;
    int r;
}
