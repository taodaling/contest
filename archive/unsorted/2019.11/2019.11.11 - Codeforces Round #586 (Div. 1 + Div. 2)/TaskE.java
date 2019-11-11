package contest;

import template.CollectionUtils;
import template.FastInput;
import template.FastOutput;

import java.util.*;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].w = in.readInt();
        }

        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        for (int i = 1; i <= n; i++) {
            tarjan(nodes[i], null);
        }

        Node root = nodes[in.readInt()].set;
        dfs(root);
        out.println(root.dp[0]);
    }

    void dfs(Node root) {
        root.visited = true;
        for (Node node : root.next) {
            node = node.set;
            if (node.visited) {
                continue;
            }
            dfs(node);
            root.dp[0] = Math.max(node.dp[0] - node.dp[1], root.dp[0]);
            root.dp[1] += node.dp[1];
            root.meet = root.meet || node.meet;
        }
        root.dp[0] += root.dp[1] + root.w;
        root.meet = root.meet || root.size > 1;
        if(root.meet){
            root.dp[1] += root.w;
        }
    }

    int dfn = 1;
    Deque<Node> stack = new ArrayDeque<>();

    void tarjan(Node root, Node father) {
        if (root.dfn != 0) {
            return;
        }
        root.low = root.dfn = dfn++;
        root.instk = true;
        stack.addLast(root);
        for (Node node : root.next) {
            if (node == father) {
                continue;
            }
            tarjan(node, root);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }
        if (root.low == root.dfn) {
            while (true) {
                Node last = stack.removeLast();
                last.set = root;
                last.instk = false;
                if (last == root) {
                    break;
                }
                root.w += last.w;
                root.size++;
                root.next = CollectionUtils.mergeHeuristically(root.next, last.next);
            }
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    Node set;
    int dfn = 0;
    int low;
    boolean instk = false;
    long w;
    long[] dp = new long[2];
    boolean meet;
    boolean visited;
    int size = 1;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}