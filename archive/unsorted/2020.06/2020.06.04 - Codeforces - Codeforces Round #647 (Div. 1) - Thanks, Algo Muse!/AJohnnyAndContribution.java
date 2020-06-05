package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AJohnnyAndContribution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        for (int i = 0; i < n; i++) {
            nodes[i].val = in.readInt() - 1;
        }
        Arrays.sort(nodes, (a, b) -> a.val - b.val);
        IntegerVersionArray iva = new IntegerVersionArray(n);
        for (Node node : nodes) {
            iva.clear();
            for (Node next : node.adj) {
                if (next.val > node.val) {
                    continue;
                }
                iva.set(next.val, 1);
            }
            int val = 0;
            while (iva.get(val) > 0) {
                val++;
            }
            if (node.val != val) {
                out.println(-1);
                return;
            }
        }

        for (Node node : nodes) {
            out.append(node.id + 1).append(' ');
        }
    }
}

class Node {
    int id;
    int val;
    List<Node> adj = new ArrayList<>();
}
