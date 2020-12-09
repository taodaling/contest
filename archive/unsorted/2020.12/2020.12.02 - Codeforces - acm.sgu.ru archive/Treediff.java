package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Treediff {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.ri() - 1];
            p.adj.add(nodes[i]);
        }
        for (int i = n - m; i < n; i++) {
            nodes[i].val = in.ri();
            if (!nodes[i].adj.isEmpty()) {
                throw new RuntimeException();
            }
        }
        dfs(nodes[0]);
        for (int i = 0; i < n - m; i++) {
            out.println(nodes[i].ans);
            if (nodes[i].adj.isEmpty()) {
                throw new RuntimeException();
            }
        }
    }


    public TreeSet<Integer> dfs(Node root) {
        TreeSet<Integer> ans = new TreeSet<>();
        root.ans = Integer.MAX_VALUE;
        if (root.adj.isEmpty()) {
            ans.add(root.val);
            return ans;
        }
        for (Node node : root.adj) {
            TreeSet<Integer> sub = dfs(node);
            root.ans = Math.min(root.ans, node.ans);
            if (sub.size() > ans.size()) {
                TreeSet<Integer> tmp = ans;
                ans = sub;
                sub = tmp;
            }
            for (Integer x : sub) {
                Integer floor = ans.floor(x);
                Integer ceil = ans.ceiling(x);
                if (floor != null) {
                    root.ans = Math.min(root.ans, x - floor);
                }
                if (ceil != null) {
                    root.ans = Math.min(root.ans, ceil - x);
                }
            }
            ans.addAll(sub);
        }
        return ans;
    }
}


class Node {
    List<Node> adj = new ArrayList<>();
    int ans;
    int val;
}
