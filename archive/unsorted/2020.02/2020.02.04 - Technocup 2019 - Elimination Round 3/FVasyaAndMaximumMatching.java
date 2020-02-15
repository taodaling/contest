package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.ArrayList;
import java.util.List;

public class FVasyaAndMaximumMatching {
    Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[1], null);
        int ans = mod.plus(nodes[1].dp[0], nodes[1].dp[2]);
        out.println(ans);
    }

    public void dfs(Node root, Node p){
        root.next.remove(p);
        if(root.next.size() == 0){
            root.dp[2] = 1;
            return;
        }

        for(Node node : root.next) {
            dfs(node, root);
        }

        int[] dp = new int[2];
        dp[0] = 1;
        for(Node node : root.next){
            dp[1] = mod.plus(
                    mod.mul(dp[1], mod.plus(mod.mul(2, node.dp[0]), node.dp[2])),
                    mod.mul(dp[0], mod.plus(node.dp[1], node.dp[2]))
            );
            dp[0] = mod.mul(dp[0], mod.plus(mod.mul(2, node.dp[0]), node.dp[2]));
        }

        root.dp[2] = 1;
        for(Node node : root.next){
            root.dp[2] = mod.mul(root.dp[2], mod.plus(node.dp[0], node.dp[2]));
        }

        root.dp[0] = dp[1];
        root.dp[1] = mod.subtract(dp[0], root.dp[2]);
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int id;
    int[] dp = new int[3];

    @Override
    public String toString() {
        return "" + id;
    }
}