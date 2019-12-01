package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TaskF {
    int k;
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].val = in.readInt();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[0], null);
        out.println(nodes[0].dp[0]);
    }


    public void dfs(Node root, Node p) {
        root.next.remove(p);
        root.dp = new int[k + 1];
        root.dp[0] = root.val;
        for (Node node : root.next) {
            dfs(node, root);
            root.dp[0] += node.dp[k];
        }
        for (int i = 0; i < k; i++) {
            int sum = 0;
            int which = Math.max(k - i - 1, i);
            for (Node node : root.next) {
                sum += node.dp[which];
            }
            int local = 0;
            for (Node node : root.next) {
                local = Math.max(local, sum - node.dp[which] + node.dp[i]);
            }
            root.dp[i + 1] = local;
        }

        for (int i = k - 1; i >= 0; i--) {
            root.dp[i] = Math.max(root.dp[i + 1], root.dp[i]);
        }
    }


}

class Node {
    List<Node> next = new ArrayList<>();
    int[] dp;
    int val;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
