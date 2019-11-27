package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.ArrayList;
import java.util.List;

public class TaskB {
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
        int ans = mod.plus(nodes[1].dp[0], nodes[1].dp[1]);
        out.println(ans);
    }

    Modular mod = new Modular(1e9 + 7);

    public void dfs(Node root, Node p) {
        root.next.remove(p);
        if(root.next.isEmpty()){
            root.dp[1] = 1;
            return;
        }

        for (Node node : root.next) {
            dfs(node, root);
        }
        long[] dp0 = new long[3];
        dp0[0] = 1;
        for (Node node : root.next) {
            dp0[2] = mod.valueOf(dp0[1] * (node.dp[1] + node.dp[2]) + dp0[2] *
                    (node.dp[0] + node.dp[1]));
            dp0[1] = mod.valueOf(dp0[0] * (node.dp[1] + node.dp[2]) + dp0[1] *
                    (node.dp[0] + node.dp[1]));
            dp0[0] = mod.valueOf(dp0[0] * (node.dp[0] + node.dp[1]));
        }
        root.dp[0] = (int) dp0[2];

        long[] dp1 = new long[2];
        dp1[0] = 1;
        for (Node node : root.next) {
            dp1[1] = mod.valueOf(dp1[0] * (node.dp[1] + node.dp[2]) + dp1[1] *
                    node.dp[0]);
            dp1[0] = mod.valueOf(dp1[0] * node.dp[0]);
        }
        root.dp[1] = mod.plus(dp1[0], dp1[1]);

        long[] dp2 = new long[2];
        dp2[0] = 1;
        for (Node node : root.next) {
            dp2[1] = mod.valueOf(dp2[0] * (node.dp[1] + node.dp[2]) + dp2[1] *
                    (node.dp[0] + node.dp[1]));
            dp2[0] = mod.valueOf(dp2[0] * (node.dp[0] + node.dp[1]));
        }
        root.dp[2] = mod.subtract(dp2[1], dp1[1]);
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int[] dp = new int[3];
    int id;
}
