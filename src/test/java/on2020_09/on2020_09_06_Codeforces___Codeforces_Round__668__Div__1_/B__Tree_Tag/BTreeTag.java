package on2020_09.on2020_09_06_Codeforces___Codeforces_Round__668__Div__1_.B__Tree_Tag;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BTreeTag {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        Node a = nodes[in.readInt() - 1];
        Node b = nodes[in.readInt() - 1];
        int da = in.readInt();
        int db = in.readInt();
        for(int i = 0; i < n - 1; i++){
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            u.adj.add(v);
            v.adj.add(u);
        }

        dfs(nodes[0], null);
        Node side0 = find(nodes);
        dfs(side0, null);
        Node side1 = find(nodes);

        int diameter = side1.depth;

        dfs(a, null);
        if (diameter + 1 > 2 * da + 1 && db >= 2 * da + 1 && b.depth > da) {
            out.println("Bob");
        }else{
            out.println("Alice");
        }
    }

    public Node find(Node[] nodes) {
        Node side0 = nodes[0];
        for (Node node : nodes) {
            if (node.depth > side0.depth) {
                side0 = node;
            }
        }
        return side0;
    }

    static void dfs(Node root, Node p) {
        root.depth = p == null ? 0 : p.depth + 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int depth;
}