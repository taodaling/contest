package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BPuzzles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            nodes[in.readInt() - 1].adj.add(nodes[i]);
        }

        dfsForSize(nodes[0]);
        dfsForExp(nodes[0], null);
        for(Node node : nodes){
            out.println(node.exp);
        }

    }

    public void dfsForSize(Node root) {
        root.size = 1;
        for (Node node : root.adj) {
            dfsForSize(node);
            root.size += node.size;
        }
    }

    public void dfsForExp(Node root, Node p) {
        if (p == null) {
            root.exp = 1;
        } else {
            root.exp = p.exp + (p.size - 1 - root.size) / 2.0D + 1;
        }
        for(Node node : root.adj){
            dfsForExp(node, root);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int size;
    double exp;
}
