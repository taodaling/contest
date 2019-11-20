package on2019_11.on2019_11_20_AISing_Programming_Contest_2019.E___Attack_to_a_Tree;



import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;

public class TaskE {
    private int inf = (int) 1e8;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].val = in.readInt();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[1], null);
        dsuOnTree(nodes[1], null);
        int minCost = nodes[1].allPositive;
        for (int i = 0; i < nodes[1].size; i++) {
            if (nodes[1].dp[i] < 0) {
                minCost = Math.min(minCost, i);
                break;
            }
        }

        out.println(minCost);
    }

    public void dsuOnTree(Node root, long[] dp) {
        if (dp == null) {
            dp = new long[root.size];
        }
        root.dp = dp;
        if (root.size == 1) {
            root.allPositive = root.val > 0 ? 0 : inf;
            root.dp[0] = root.val;
            return;
        }

        dsuOnTree(root.heavy, dp);
        root.allPositive = root.heavy.allPositive;
        for (int i = 0; i < root.heavy.size; i++) {
            if (dp[i] < 0) {
                root.allPositive = Math.min(root.allPositive, i + 1);
                break;
            }
        }
        for (int i = root.heavy.size; i >= 0; i--) {
            dp[i] += root.val;
            if (i > 0 && dp[i - 1] < 0) {
                dp[i] = Math.min(dp[i], root.val);
            }
            if (i > root.heavy.allPositive) {
                dp[i] = Math.min(dp[i], root.val);
            }
        }
        for (int i = root.heavy.size + 1; i < root.size; i++) {
            dp[i] = (long) 1e18;
        }

        for (Node node : root.next) {
            if (node == root.heavy) {
                continue;
            }
            dsuOnTree(node, null);
            // update all positive
            int localAllPos = node.allPositive;
            for (int i = 0; i < node.size; i++) {
                if (node.dp[i] < 0) {
                    localAllPos = Math.min(localAllPos, i + 1);
                    break;
                }
            }
            root.allPositive += localAllPos;

            int splitCost = node.allPositive + 1;
            for (int i = 0; i < node.size; i++) {
                if (node.dp[i] < 0) {
                    splitCost = Math.min(splitCost, i + 1);
                    break;
                }
            }
            // split or not
            for (int i = root.size - 1; i >= 0; i--) {
                // split
                dp[i] += node.dp[0];
                if (i >= splitCost) {
                    dp[i] = Math.min(dp[i], dp[i - splitCost]);
                }
                for (int j = 1; j < node.size && j <= i; j++) {
                    int k = i - j;
                    dp[i] = Math.min(dp[i], dp[k] + node.dp[j]);
                }
            }
        }

        if (root.val < 0) {
            root.allPositive = inf;
        }

    }

    public void dfs(Node root, Node p) {
        root.next.remove(p);
        root.size = 1;
        for (Node node : root.next) {
            dfs(node, root);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
        }
    }


}


class Node {
    List<Node> next = new ArrayList<>();
    Node heavy;
    int size;
    long[] dp;
    int allPositive;
    int val;

    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
