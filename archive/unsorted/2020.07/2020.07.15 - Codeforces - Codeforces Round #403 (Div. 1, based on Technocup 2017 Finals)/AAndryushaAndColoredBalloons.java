package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AAndryushaAndColoredBalloons {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        dfs(nodes[0], null);
        int max = Arrays.stream(nodes).mapToInt(x -> x.color).max().orElse(0) + 1;
        out.println(max);
        for(Node node : nodes){
            out.append(node.color + 1).append(' ');
        }
    }

    public void dfs(Node root, Node p) {
        int pColor = p == null ? -1 : p.color;
        int cur = 0;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            while (cur == pColor || cur == root.color) {
                cur++;
            }
            node.color = cur++;
            dfs(node, root);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int color;
}
