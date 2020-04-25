package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FMonochromeCat {
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

        for (int i = 0; i < n; i++) {
            nodes[i].color = in.readChar() == 'B' ? 0 : 1;
        }

        dfsForSize(nodes[0], null);
        dfsForRemove(nodes[0], null, nodes[0].size);
        Node root = null;
        for (Node node : nodes) {
            if (!node.removed) {
                root = node;
                break;
            }
        }

        if (root == null) {
            out.println(0);
            return;
        }
        dfsForRemoveChildren(root, null);
        dfsForDp(root);
        int ans = root.dp[0];
        out.println(ans);
    }

    public void dfsForDp(Node root) {
        if (root.next.isEmpty()) {
            root.dp[0] = root.color;
            root.dp[1] = (1 ^ root.color) + 1;
            root.dp[2] = root.color + 1;
            root.dp[3] = (1 ^ root.color) + 2;
            return;
        }

        for (Node node : root.next) {
            dfsForDp(node);
            root.dp[3] += node.dp[3];
        }

        root.next.sort((a, b) -> Integer.compare(a.dp[2] - a.dp[3], b.dp[2] - b.dp[3]));
        root.dp[2] = Math.min(root.dp[3], root.dp[3] + root.next.get(0).dp[2] - root.next.get(0).dp[3]);

        root.next.sort((a, b) -> Integer.compare(a.dp[1] - a.dp[3], b.dp[1] - b.dp[3]));
        root.dp[1] = Math.min(root.dp[3], root.dp[3] + root.next.get(0).dp[1] - root.next.get(0).dp[3]);

        int[][] dp = new int[2][2];
        for (Node node : root.next) {
            dp[1][1] = Math.min(dp[1][1] + node.dp[3], Math.min(dp[1][0] + node.dp[2], dp[0][1] + node.dp[1]));
            dp[0][1] = Math.min(dp[0][1] + node.dp[3], dp[0][0] + node.dp[2]);
            dp[1][0] = Math.min(dp[1][0] + node.dp[3], dp[0][0] + node.dp[1]);
            dp[0][0] += node.dp[3];
        }

        int min = (int)1e9;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                min = Math.min(dp[i][j], min);
            }
        }
        root.dp[0] = min;

        root.dp[0] += ((root.next.size() - 1) & 1) ^ root.color;
        root.dp[1] += ((root.next.size()) & 1) ^ root.color;
        root.dp[2] += ((root.next.size()) & 1) ^ root.color;
        root.dp[3] += ((root.next.size() + 1) & 1) ^ root.color;

        root.dp[1] += 1;
        root.dp[2] += 1;
        root.dp[3] += 2;
    }

    public void dfsForRemoveChildren(Node root, Node p) {
        root.next = root.next.stream().filter(x -> !x.removed && x != p).collect(Collectors.toList());
        for (Node node : root.next) {
            dfsForRemoveChildren(node, root);
        }
    }

    public void dfsForSize(Node root, Node p) {
        root.size = root.color;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    public void dfsForRemove(Node root, Node p, int total) {
        int max = total - root.size;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            max = Math.max(max, node.size);
            dfsForRemove(node, root, total);
        }

        if (max == total) {
            root.removed = true;
        }

    }
}

class Node {
    List<Node> next = new ArrayList<>();
    //0 no out
    //1 in
    //2 out
    //3 in and out
    int[] dp = new int[4];
    boolean removed;
    int color;
    int size;

    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}