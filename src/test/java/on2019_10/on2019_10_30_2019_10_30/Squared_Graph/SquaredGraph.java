package on2019_10.on2019_10_30_2019_10_30.Squared_Graph;



import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;

public class SquaredGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);

            Node.merge(a, b);
        }

        int even = 0;
        int odd = 0;
        int single = 0;
        for (int i = 1; i <= n; i++) {
            if (nodes[i].find() != nodes[i]) {
                continue;
            }
            if (paint(nodes[i], 0)) {
                nodes[i].even = true;
            } else {
                nodes[i].even = false;
            }
        }

        for (int i = 1; i <= n; i++) {
            nodes[i].find().size++;
        }

        for (int i = 1; i <= n; i++) {
            if (nodes[i].find() != nodes[i]) {
                continue;
            }
            if (nodes[i].even) {
                if (nodes[i].size == 1) {
                    single++;
                } else {
                    even++;
                }
            } else {
                odd++;
            }
        }

        long ans = (long) even * even * 2 + (long) odd * odd + (long) even * odd * 2
                        + (long) single * single + (long)single * (n - single) * 2;
        out.println(ans);

    }

    public boolean paint(Node root, int color) {
        if (root.visited) {
            return root.color == color;
        }
        root.visited = true;
        root.color = color;
        for (Node node : root.next) {
            if (!paint(node, 1 - color)) {
                return false;
            }
        }
        return true;
    }
}


class Node {
    List<Node> next = new ArrayList<>();
    boolean visited;
    boolean even;
    int color;

    Node p = this;
    int size;
    int rank;

    public Node find() {
        return p.p == p ? p : (p = p.find());
    }

    public static void merge(Node a, Node b) {
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
