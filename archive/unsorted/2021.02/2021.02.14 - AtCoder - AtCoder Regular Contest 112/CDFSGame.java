package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CDFSGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.ri() - 1];
            p.adj.add(nodes[i]);
        }

        dfs(nodes[0], null);
        int delta = nodes[0].delta;
        int ans = (n - delta) / 2 + delta;
        out.println(ans);
    }

    public void dfs(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.size += node.size;
        }
        root.delta = 1;
        int sign = 1;

        root.adj.sort(Comparator.comparingInt(x -> x.delta));
        for (Node node : root.adj) {
            if (node == p || node.type != 1) {
                continue;
            }
            root.delta += sign * node.delta;
        }
        for (Node node : root.adj) {
            if (node == p || node.type != 0) {
                continue;
            }
            root.delta += sign * node.delta;
            sign = -sign;
        }
        for (Node node : root.adj) {
            if (node == p || node.type != 2) {
                continue;
            }
            root.delta += sign * node.delta;
            sign = -sign;
        }
        for (Node node : root.adj) {
            if (node == p || node.type != 3) {
                continue;
            }
            root.delta += sign * node.delta;
        }
        if (root.size % 2 == 0) {
            root.type |= 1;
        }
        if (root.delta >= 0) {
            root.type |= 2;
        }
    }

}

class Node {
    List<Node> adj = new ArrayList<>();
    int delta;
    int size;
    //3 for other return and positive delta
    //2 for self return and positive delta
    //1 for other return and negative delta
    //0 for self return and negative delta
    int type;
}