package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ALinovaAndKingdom {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }

        dfs(nodes[0], null, 0);
        Arrays.sort(nodes, (a, b) -> -Integer.compare(a.weight(), b.weight()));
        long sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nodes[i].weight();
        }
        out.println(sum);
    }

    public void dfs(Node root, Node p, int d) {
        root.size = 1;
        root.depth = d;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root, d + 1);
            root.size += node.size;
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int depth;
    int size;

    public int weight() {
        return depth - (size - 1);
    }
}