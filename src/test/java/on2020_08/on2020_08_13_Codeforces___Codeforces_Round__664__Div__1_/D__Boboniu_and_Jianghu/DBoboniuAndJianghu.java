package on2020_08.on2020_08_13_Codeforces___Codeforces_Round__664__Div__1_.D__Boboniu_and_Jianghu;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBoboniuAndJianghu {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            nodes[i].t = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            nodes[i].h = in.readInt();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        long total = 0;
        Arrays.sort(nodes, (a, b) -> Integer.compare(a.h, b.h));
        for (int i = 0; i < n; i++) {
            int l = i;
            int r = i;
            while (r + 1 < n && nodes[r + 1].h == nodes[r].h) {
                r++;
            }
            i = r;

            for (int j = l; j <= r; j++) {
                if (nodes[j].visited) {
                    continue;
                }
                PseudoNode ps = build(nodes[j], null);
                dp(ps);
                long req = Math.min(ps.dp[1], ps.dp[2]);
                total += req;
            }
        }

        out.println(total);
    }


    public long extraCost(PseudoNode root) {
        return root.dp[2] - (Math.min(root.dp[0], root.dp[1]) + root.cost);
    }

    public void contrib(PseudoNode root, int in, int out, long sum) {
        in += root.in;
        out += root.out;
        root.dp[0] = Math.min(root.dp[0], Math.max(in - 1, out) * root.cost + sum);
        root.dp[1] = Math.min(root.dp[1], Math.max(in, out) * root.cost + sum);
        if (in >= out) {
            root.dp[2] = Math.min(root.dp[2], (in + 1) * root.cost + sum);
        } else {
            root.dp[2] = Math.min(root.dp[2], out * root.cost + sum);
        }
    }

    long inf = (long) 1e18;
    public void dp(PseudoNode root) {
        long sum = 0;
        for (PseudoNode node : root.adj) {
            dp(node);
            sum += Math.min(node.dp[0], node.dp[1]) + node.cost;
        }
        int n = root.adj.size();
        root.adj.sort((a, b) -> Long.compare(extraCost(a), extraCost(b)));
        Arrays.fill(root.dp, inf);
        contrib(root, n, 0, sum);
        for (int i = 0; i < n; i++) {
            PseudoNode node = root.adj.get(i);
            sum += extraCost(node);
            int out = i + 1;
            int in = n - i - 1;
            contrib(root, in, out, sum);
        }
    }

    public PseudoNode build(Node root, Node p) {
        root.visited = true;
        PseudoNode ans = new PseudoNode(root.t);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (node.h != root.h) {
                if (node.h < root.h) {
                    ans.out++;
                } else {
                    ans.in++;
                }
            } else {
                ans.adj.add(build(node, root));
            }
        }

        return ans;
    }
}

class Node {
    int id;
    int h;
    long t;
    List<Node> adj = new ArrayList<>();
    boolean visited;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}

class PseudoNode {
    long cost;
    long[] dp = new long[3];
    List<PseudoNode> adj = new ArrayList<>();
    int in;
    int out;

    public PseudoNode(long cost) {
        this.cost = cost;
    }
}