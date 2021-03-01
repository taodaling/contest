package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class CHelpingPeople {
    int low;
    int high;

    public double[] dfs(Node root) {
        double[] dp = new double[high - low + 1];
        int v = root.init - low;
        v = Math.max(v, 0);
        for (int i = v; i < dp.length; i++) {
            dp[i] = 1;
        }
        for (Node node : root.adj) {
            double[] prob = dfs(node);
            for (int i = 0; i < dp.length; i++) {
                dp[i] *= prob[i];
            }
        }
        for (int i = dp.length - 1; i >= 1; i--) {
            dp[i] = dp[i] * (1 - root.prob) + dp[i - 1] * root.prob;
        }
        return dp;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        int max = Arrays.stream(a).max().orElse(-1);
        low = max - q;
        high = max + q;
        Deque<Node> dq = new ArrayDeque<>(q);
        Node[] nodes = new Node[q + 1];
        for (int i = 0; i < q; i++) {
            nodes[i] = new Node();
            nodes[i].l = in.ri() - 1;
            nodes[i].r = in.ri() - 1;
            nodes[i].prob = in.rd();
        }
        Node whole = new Node();
        whole.l = 0;
        whole.r = n - 1;
        whole.prob = 0;
        nodes[q] = whole;
        Arrays.sort(nodes, Comparator.<Node>comparingInt(x -> x.l).thenComparingInt(x -> -x.r));

        for (Node node : nodes) {
            while (!dq.isEmpty() && dq.peekLast().r < node.l) {
                dq.removeLast();
            }
            if (!dq.isEmpty() && dq.peekLast().r >= node.r) {
                dq.peekLast().adj.add(node);
            }
            dq.addLast(node);
        }
        for (Node node : nodes) {
            node.init = -(int) 1e9;
            int last = node.l;
            for (Node child : node.adj) {
                for (int i = last; i < child.l; i++) {
                    node.init = Math.max(node.init, a[i]);
                }
                last = child.r + 1;
            }
            for (int i = last; i <= node.r; i++) {
                node.init = Math.max(node.init, a[i]);
            }
        }

        double[] prob = dfs(nodes[0]);
        double exp = 0;
        for (int i = 1; i < prob.length; i++) {
            exp += (i + low) * (prob[i] - prob[i - 1]);
        }
        out.println(exp);
    }
}

class Node {
    double prob;
    int init;
    int l;
    int r;
    List<Node> adj = new ArrayList<>();
}
