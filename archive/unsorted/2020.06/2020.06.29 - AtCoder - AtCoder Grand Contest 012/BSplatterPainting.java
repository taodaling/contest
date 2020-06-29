package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BSplatterPainting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int j = 0; j < m; j++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        int q = in.readInt();
        int[][] qs = new int[3][q];
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < 3; j++) {
                qs[j][i] = in.readInt();
            }
        }

        for (int i = q - 1; i >= 0; i--) {
            Node v = nodes[qs[0][i] - 1];
            int d = qs[1][i];
            int c = qs[2][i];
            paint(v, c, d);
        }

        for(Node node : nodes){
            int c = node.color == -1 ? 0 : node.color;
            out.println(c);
        }
    }

    public void paint(Node root, int c, int power) {
        if (power < 0 || root.used[power]) {
            return;
        }
        root.used[power] = true;
        if (root.color == -1) {
            root.color = c;
        }
        for (Node node : root.adj) {
            paint(node, c, power - 1);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    boolean[] used = new boolean[11];
    int color = -1;
}