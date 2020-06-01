package on2020_06.on2020_06_02_Codeforces___Codeforces_Round__522__Div__1__based_on_Technocup_2019_Elimination_Round_3_.C__Vasya_and_Maximum_Matching;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CVasyaAndMaximumMatching {
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
            a.adj.add(b);
            b.adj.add(a);
        }

        dfs(nodes[0], null);
        int[] dp = nodes[0].dp;

        int ans = mod.plus(dp[0], dp[1]);
        out.println(ans);
    }

    Modular mod = new Modular(998244353);

    public void dfs(Node root, Node p) {
        int[] last = new int[3];
        int[] cur = new int[3];
        last[0] = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            Arrays.fill(cur, 0);
            dfs(node, root);
            //delete
            for (int i = 0; i < 3; i++) {
                cur[i] = mod.plus(cur[i], mod.mul(last[i], mod.plus(node.dp[0], node.dp[1])));
            }
            //retain
            //1
            cur[1] = mod.plus(cur[1], mod.mul(last[1], node.dp[1]));
            cur[2] = mod.plus(cur[2], mod.mul(mod.plus(last[0], last[2]), node.dp[1]));
            //0 2
            cur[1] = mod.plus(cur[1], mod.mul(mod.plus(last[0], last[2]), mod.plus(node.dp[0], node.dp[2])));

            int[] tmp = last;
            last = cur;
            cur = tmp;
        }

        root.dp = last;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;

    //0 leaf
    //1 matched
    //2 unmatched
    int[] dp;

    @Override
    public String toString() {
        return "" + id;
    }
}