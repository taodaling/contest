package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class LongestFlightRoute {
    int inf = (int) 1e9;
    int n;

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
        if (dp(nodes[0]) < 0) {
            out.println("IMPOSSIBLE");
            return;
        }
        out.println(dp(nodes[0]));
        Node now = nodes[0];
        while (now != null) {
            out.append(now.id + 1).append(' ');
            now = now.prev;
        }
    }


    public int dp(Node root) {
        if (root.dp == -1) {
            root.dp = -inf;
            for (Node node : root.adj) {
                if (dp(node) + 1 > root.dp) {
                    root.dp = dp(node) + 1;
                    root.prev = node;
                }
            }
        }
        return root.dp;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    Node prev;
    int dp = -1;
}