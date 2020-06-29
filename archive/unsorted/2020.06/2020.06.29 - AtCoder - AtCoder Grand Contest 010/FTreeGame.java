package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class FTreeGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].w = in.readInt();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        for(int i = 0; i < n; i++){
            if(dfs(nodes[i], null)){
                out.append(i + 1).append(' ');
            }
        }
    }

    public boolean dfs(Node root, Node p) {
        for (Node node : root.adj) {
            if (node == p || node.w >= root.w) {
                continue;
            }
            if (!dfs(node, root)) {
                return true;
            }
        }
        return false;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int w;
}