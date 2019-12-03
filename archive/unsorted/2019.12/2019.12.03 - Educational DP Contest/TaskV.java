package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.ArrayList;
import java.util.List;

public class TaskV {
    Modular mod;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        mod = new Modular(m);
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }
        dfs(nodes[1], null);
        answer(nodes[1], null);
        for(int i = 1; i <= n; i++){
            out.println(nodes[i].ans);
        }
    }

    public void dfs(Node root, Node p) {
        root.next.remove(p);
        for (Node node : root.next) {
            dfs(node, root);
        }
        root.dp[0] = 1;
        root.dp[1] = 1;
        for (Node node : root.next) {
            root.dp[1] = mod.mul(root.dp[1], mod.plus(node.dp[0], node.dp[1]));
        }
        for (Node node : root.next) {
            root.dp[2] = mod.plus(root.dp[2], mod.plus(node.dp[1], node.dp[2]));
        }
    }

    public void answer(Node root, Node p) {
        int dp1 = 1;
        int dp2 = 0;
        if (p != null) {
            dp1 = mod.plus(p.dp[0], p.dp[1]);
            dp2 = mod.plus(p.dp[1], p.dp[2]);
        }
        root.ans = mod.mul(root.dp[1], dp1);
        int originDp2 = mod.plus(root.dp[2], dp2);

        int[] pre = new int[root.next.size()];
        int[] post = new int[root.next.size()];
        for (int i = 0; i < root.next.size(); i++) {
            Node node = root.next.get(i);
            pre[i] = mod.plus(node.dp[0], node.dp[1]);
            if (i > 0) {
                pre[i] = mod.mul(pre[i], pre[i - 1]);
            }
        }
        for (int i = root.next.size() - 1; i >= 0; i--) {
            Node node = root.next.get(i);
            post[i] = mod.plus(node.dp[0], node.dp[1]);
            if (i + 1 < root.next.size()) {
                post[i] = mod.mul(post[i], post[i + 1]);
            }
        }
        for (int i = 0; i < root.next.size(); i++) {
            Node node = root.next.get(i);
            root.dp[1] = dp1;
            if (i > 0) {
                root.dp[1] = mod.mul(root.dp[1], pre[i - 1]);
            }
            if (i + 1 < root.next.size()) {
                root.dp[1] = mod.mul(root.dp[1], post[i + 1]);
            }
            root.dp[2] = mod.subtract(originDp2, mod.plus(node.dp[1], node.dp[2]));
            answer(node, root);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    //zero: never black
    //one: is black
    //two: once black
    int[] dp = new int[3];
    int ans;
}
