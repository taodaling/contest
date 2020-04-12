package on2020_04.on2020_04_12_Codeforces_Round__633__Div__1_.D__Nested_Rubber_Bands;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DNestedRubberBands {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }
        dfs(nodes[0], null);
        int ans = Math.max(nodes[0].dp[0], nodes[0].dp[1]);
        out.println(ans);
    }

    public void dfs(Node root, Node p) {
        root.next.remove(p);
        root.dp[1] = 1;

        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.dp[1] = Math.max(root.dp[1], node.dp[0] + 1);
            root.dp[0] = Math.max(root.dp[0], Math.max(node.dp[0], node.dp[1]) + root.next.size() - 1);
        }
    }
}

class Node {
    int[] dp = new int[2];
    List<Node> next = new ArrayList<>();
}