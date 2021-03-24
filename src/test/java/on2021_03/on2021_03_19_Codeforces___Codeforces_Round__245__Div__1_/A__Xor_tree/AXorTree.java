package on2021_03.on2021_03_19_Codeforces___Codeforces_Round__245__Div__1_.A__Xor_tree;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class AXorTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        for (int i = 0; i < n; i++) {
            nodes[i].init = in.ri();
        }
        for (int i = 0; i < n; i++) {
            nodes[i].goal = in.ri();
        }

        dfs(nodes[0], null, 0, 0);
        out.println(seq.size());
        for(Node node : seq){
            out.append(node.id + 1).println();
        }
    }

    List<Node> seq = new ArrayList<>();

    public void dfs(Node root, Node p, int gf, int f) {
        int cast = root.init ^ root.goal ^ gf;
        if (cast == 1) {
            seq.add(root);
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root, f, gf ^ cast);
        }
    }
}

class Node {
    int id;
    List<Node> adj = new ArrayList<>();
    int init;
    int goal;
}
