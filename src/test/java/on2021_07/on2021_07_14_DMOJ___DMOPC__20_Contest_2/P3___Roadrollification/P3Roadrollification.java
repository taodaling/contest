package on2021_07.on2021_07_14_DMOJ___DMOPC__20_Contest_2.P3___Roadrollification;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class P3Roadrollification {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].size = in.ri();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.revAdj.add(a);
        }
        long now = 0;
        for (Node node : nodes) {
            now += node.size * (node.size - 1);
            now += node.size * (out(node) - node.size);
        }

        long best = 0;
        for (Node a : nodes) {
            for (Node b : a.adj) {
                long contrib = (out(a) - out(b)) * (in(b) - in(a));
                best = Math.max(best, contrib);
            }
        }

        now += best;
        out.println(now);
    }

    public long out(Node root) {
        if (root.out == -1) {
            root.out = root.size;
            for (Node node : root.adj) {
                root.out += out(node);
            }
        }
        return root.out;
    }

    public long in(Node root) {
        if (root.in == -1) {
            root.in = root.size;
            for (Node node : root.revAdj) {
                root.in += in(node);
            }
        }
        return root.in;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    List<Node> revAdj = new ArrayList<>();
    long out = -1;
    long in = -1;
    long size;
}
