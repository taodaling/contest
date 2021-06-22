package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.Burnished_Security_Updates;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BurnishedSecurityUpdates {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        cnts = new int[2];
        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();
        }
        for(int i = 0; i < m; i++){
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        int ans = 0;
        for(Node node : nodes){
            if(node.color != -1){
                continue;
            }
            Arrays.fill(cnts, 0);
            dfs(node, 0);
            ans += Math.min(cnts[0], cnts[1]);
        }
        if(!valid){
            out.println(-1);
            return;
        }
        out.println(ans);
    }
    int[] cnts;
    boolean valid = true;
    void dfs(Node root, int c){
        if(root.color != -1){
            valid = valid && root.color == c;
            return;
        }
        root.color = c;
        cnts[c]++;
        for(Node node : root.adj){
            dfs(node, c ^ 1);
        }
    }
}

class Node {
    int color = -1;
    List<Node> adj = new ArrayList<>();
}


