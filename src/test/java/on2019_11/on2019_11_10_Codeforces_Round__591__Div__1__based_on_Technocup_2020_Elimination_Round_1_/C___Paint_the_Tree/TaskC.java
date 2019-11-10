package on2019_11.on2019_11_10_Codeforces_Round__591__Div__1__based_on_Technocup_2020_Elimination_Round_1_.C___Paint_the_Tree;



import template.FastInput;
import template.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }

        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];

            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.val = in.readInt();

            a.next.add(e);
            b.next.add(e);
        }

        dfs(nodes[1], null, k);

        long ans = nodes[1].dp[0];
        out.println(ans);
    }

    public void dfs(Node root, Edge e, int k) {
        root.next.remove(e);

        long sum = 0;
        PriorityQueue<Long> profits = new PriorityQueue<>(Math.max(1, root.next.size()), (a, b) -> -Long.compare(a, b));
        for (Edge edge : root.next) {
            Node node = edge.other(root);
            dfs(node, edge, k);
            sum += node.dp[0];
            profits.add(node.dp[1] + edge.val - node.dp[0]);
        }

        for (int i = 1; i < k && !profits.isEmpty(); i++) {
            if (profits.peek() < 0) {
                continue;
            }
            sum += profits.remove();
        }
        root.dp[1] = sum;
        for (int i = k; i <= k && !profits.isEmpty(); i++) {
            if (profits.peek() < 0) {
                continue;
            }
            sum += profits.remove();
        }
        root.dp[0] = sum;
    }
}

class Edge {
    Node a;
    Node b;
    int val;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    long[] dp = new long[2];
}
