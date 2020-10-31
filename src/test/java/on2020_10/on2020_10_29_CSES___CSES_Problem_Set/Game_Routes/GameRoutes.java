package on2020_10.on2020_10_29_CSES___CSES_Problem_Set.Game_Routes;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class GameRoutes {
    int inf = (int) 1e9;
    int n;
    int mod = (int) (1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
        }
        nodes[n - 1].dp = 1;
        out.println(dp(nodes[0]));
    }


    public long dp(Node root) {
        if (root.dp == -1) {
            root.dp = 0;
            for (Node node : root.adj) {
                root.dp += dp(node);
            }
            root.dp %= mod;
        }
        return root.dp;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    long dp = -1;
}