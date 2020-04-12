package on2020_04.on2020_04_12_Codeforces_Round__633__Div__1_.B__Edge_Weight_Assignment;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BEdgeWeightAssignment {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            nodes[a].next.add(nodes[b]);
            nodes[b].next.add(nodes[a]);
        }
        Node root = null;
        for (int i = 0; i < n; i++) {
            if (nodes[i].next.size() == 1) {
                root = nodes[i];
            }
        }
        dfs(root, null, 0);
        for (Node node : nodes) {
            for (Node next : node.next) {
                if (next.next.size() == 1) {
                    node.leaf++;
                }
            }
        }

        int min = 1;
        for (Node node : nodes) {
            if (node.next.size() == 1 && node.depth % 2 == 1) {
                min = 3;
            }
        }

        int max = n - 1;
        for (Node node : nodes) {
            if (node.leaf > 0) {
                max -= node.leaf - 1;
            }
        }

        out.append(min).append(' ').append(max);
    }

    public void dfs(Node root, Node p, int d) {
        root.depth = d;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root, d + 1);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int depth;
    int leaf;
}