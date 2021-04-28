package on2021_04.on2021_04_20_Codeforces___Codeforces_Round__202__Div__1_.B__Apple_Tree;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BAppleTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].a = in.ri();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        long lcm = nodes[0].lcm;
        long sum = Arrays.stream(nodes).mapToLong(x -> x.a).sum();
        if (lcm == -1 || lcm == 0) {
            out.println(sum);
            return;
        }
        dfsDown(nodes[0], null, lcm);
        long minDiv = Long.MAX_VALUE;
        for (Node node : nodes) {
            if (node.leaf) {
                assert node.base > 0;
                minDiv = Math.min(minDiv, node.a / node.base);
            }
        }
        long retain = lcm * minDiv;
        out.println(sum - retain);
    }

    public long mul(long a, long b) {
        if (a == -1 || b == -1) {
            return -1;
        }
        if (DigitUtils.isMultiplicationOverflow(a, b, (long) 1e18)) {
            return -1;
        }
        return a * b;
    }

    public long lcm(long a, long b) {
        if (a == -1 || b == -1) {
            return -1;
        }
        long g = GCDs.gcd(a, b);
        return mul(a / g, b);
    }

    public void dfs(Node root, Node p) {
        root.lcm = 1;
        int child = root.adj.size();
        if (p != null) {
            child--;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.lcm = lcm(root.lcm, node.lcm);
        }
        root.lcm = mul(root.lcm, child);
        if (child == 0) {
            root.lcm = 1;
        }
    }

    public void dfsDown(Node root, Node p, long val) {
        int child = root.adj.size();
        if (p != null) {
            child--;
        }
        if (child == 0) {
            root.base = val;
            root.leaf = true;
            return;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            assert val % child == 0;
            dfsDown(node, root, val / child);
        }
    }

}

class Node {
    boolean leaf;
    List<Node> adj = new ArrayList<>();
    int a;
    long lcm;
    long base;
}
