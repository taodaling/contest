package on2019_12.on2019_12_03_Educational_DP_Contest.P___Independent_Set;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.ArrayList;
import java.util.List;

public class TaskP {
    Modular mod = new Modular(1e9 + 7);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for(int i = 1; i <= n; i++){
            nodes[i] = new Node();
        }
        for(int i = 1; i < n; i++){
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }
        dfs(nodes[1], null);
        int ans = mod.plus(nodes[1].dp[0], nodes[1].dp[1]);
        out.println(ans);
    }

    public void dfs(Node root, Node p){
        root.next.remove(p);
        root.dp[1] = root.dp[0] = 1;
        for(Node node : root.next){
            dfs(node, root);
            root.dp[0] = mod.mul(root.dp[0], mod.plus(node.dp[0],
                    node.dp[1]));
            root.dp[1] = mod.mul(root.dp[1], node.dp[0]);
        }
    }
}

class Node{
    List<Node> next = new ArrayList<>();
    int[] dp = new int[2];
}
