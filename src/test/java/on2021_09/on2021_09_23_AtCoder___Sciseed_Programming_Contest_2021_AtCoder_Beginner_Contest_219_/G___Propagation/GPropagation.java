package on2021_09.on2021_09_23_AtCoder___Sciseed_Programming_Contest_2021_AtCoder_Beginner_Contest_219_.G___Propagation;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GPropagation {
    int B = 700;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = nodes[i].color = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        for (Node node : nodes) {
            if (node.adj.size() >= B) {
                node.large = true;
            }
        }
        for (Node node : nodes) {
            node.largeAdj = node.adj.stream().filter(x -> x.large).collect(Collectors.toList());
        }

        for (int i = 1; i <= q; i++) {
            Node root = nodes[in.ri() - 1];
            computeRealTimeColor(root);
            root.lastTime = i;
            root.lastColor = root.color;
            for (Node node : root.large ? root.largeAdj : root.adj) {
                node.color = root.lastColor;
                node.time = root.lastTime;
            }
        }

        for (Node node : nodes) {
            computeRealTimeColor(node);
            out.println(node.color + 1);
        }
    }

    public void computeRealTimeColor(Node root) {
        if (root.large) {
            return;
        }
        for (Node node : root.largeAdj) {
            if (node.lastTime > root.time) {
                root.time = node.lastTime;
                root.color = node.lastColor;
            }
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    List<Node> largeAdj = new ArrayList<>();
    boolean large;
    int id;
    int color;
    int time;

    int lastColor = -1;
    int lastTime = -1;
}