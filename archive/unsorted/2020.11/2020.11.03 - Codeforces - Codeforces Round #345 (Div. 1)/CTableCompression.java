package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CTableCompression {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[][] nodes = new Node[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                nodes[i][j] = new Node();
                nodes[i][j].val = in.readInt();
            }
        }

        Node[] buf = new Node[Math.max(n, m)];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                buf[j] = nodes[i][j];
            }
            Arrays.sort(buf, 0, m, (a, b) -> Integer.compare(a.val, b.val));
            for (int j = 1; j < m; j++) {
                if (buf[j].val > buf[j - 1].val) {
                    buf[j].adj.add(buf[j - 1]);
                } else {
                    Node.merge(buf[j], buf[j - 1]);
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                buf[j] = nodes[j][i];
            }
            Arrays.sort(buf, 0, n, (a, b) -> Integer.compare(a.val, b.val));
            for (int j = 1; j < n; j++) {
                if (buf[j].val > buf[j - 1].val) {
                    buf[j].adj.add(buf[j - 1]);
                } else {
                    Node.merge(buf[j], buf[j - 1]);
                }
            }
        }

        for (Node[] x : nodes) {
            for (Node y : x) {
                y.find().set.addAll(y.adj);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.append(find(nodes[i][j])).append(' ');
            }
            out.println();
        }
    }

    public static int find(Node root) {
        root = root.find();
        if (root.assign == -1) {
            int ans = 1;
            for (Node node : root.set) {
                ans = Math.max(ans, find(node) + 1);
            }
            root.assign = ans;
        }
        return root.assign;
    }
}

class Node {
    Node p = this;
    int rank;
    int val;
    List<Node> adj = new ArrayList<>(2);
    List<Node> set = new ArrayList<>();
    int assign = -1;

    Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }

}
