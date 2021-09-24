package on2021_08.on2021_08_15_CS_Academy___Virtual_Beta_Round__8.Max_Score_Tree;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MaxScoreTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        scores = in.ri(n);
        scores = Arrays.copyOf(scores, n + 10);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        long ans = Arrays.stream(nodes).mapToLong(x -> x.dp[0]).max().orElse(-1);
        out.println(ans);
    }

    int[] scores;

    public void dfs(Node root, Node p) {
        long[] dp = new long[2];
        dp[0] = scores[0];
        dp[1] = scores[1];
        List<Node> children = root.adj.stream().filter(x -> x != p).collect(Collectors.toList());
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
        children.sort(Comparator.comparingLong(x -> -x.dp[1]));
        long sum = 0;
        for (int i = 0; i < children.size(); i++) {
            sum += children.get(i).dp[1];
            dp[0] = Math.max(dp[0], sum + scores[i + 1]);
            dp[1] = Math.max(dp[1], sum + scores[i + 2]);
        }
        root.dp = dp;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    long[] dp;
}