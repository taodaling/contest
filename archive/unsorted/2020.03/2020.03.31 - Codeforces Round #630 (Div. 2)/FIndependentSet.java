package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FIndependentSet {
    Modular mod = new Modular(998244353);
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[0], null);
        int ans = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i < j) {
                    continue;
                }
                ans = mod.plus(ans, nodes[0].dp[i][j]);
            }
        }
        ans = mod.subtract(ans, 1);
        out.println(ans);
    }

    public void dfs(Node root, Node p) {
        for (int j = 0; j < 2; j++) {
            root.dp[0][j] = 1;
        }
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            {
                //1 0
                int a = mod.plus(node.dp[0][0], mod.plus(node.dp[0][1], mod.plus(node.dp[1][0], node.dp[1][1])));
                int b = mod.plus(node.dp[0][0], mod.plus(node.dp[1][0], node.dp[1][1]));
                root.dp[1][0] = mod.plus(mod.mul(root.dp[1][0], mod.plus(a, b)), mod.mul(a, root.dp[0][0]));
            }
            {
                //0 0
                int a = mod.plus(node.dp[1][1], mod.plus(node.dp[0][0], node.dp[1][0]));
                root.dp[0][0] = mod.mul(root.dp[0][0], a);
            }
            {
                //1 1
                int a = mod.plus(node.dp[0][0], node.dp[1][0]);
                int b = mod.plus(node.dp[0][0], mod.plus(node.dp[1][0], node.dp[1][1]));
                root.dp[1][1] = mod.plus(mod.mul(root.dp[1][1], mod.plus(a, b)), mod.mul(a, root.dp[0][1]));
            }
            {
                //0 1
                int a = mod.plus(node.dp[1][1], mod.plus(node.dp[0][0], node.dp[1][0]));
                root.dp[0][1] = mod.mul(root.dp[0][1], a);
            }

        }
    }
}

class Node {
    //0 no link not exist
    //1 link but not exist
    //2 link and exist
    //3 link and exist and require father link
    int[][] dp = new int[2][2];
    List<Node> next = new ArrayList<>();
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}