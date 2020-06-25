package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerFixedMinHeap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class DBlackAndWhiteTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
            a.deg++;
            b.deg++;
        }

        TreeSet<Node> set = new TreeSet<>((a, b) -> a.deg == b.deg ? Integer.compare(a.id, b.id) : Integer.compare(a.deg, b.deg));
        set.addAll(Arrays.asList(nodes));
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            if (head.deg == 0) {
                out.println("First");
                return;
            }
            head.removed = true;
            Node nearby = null;
            for (Node node : head.adj) {
                if (node.removed) {
                    continue;
                }
                nearby = node;
                set.remove(nearby);
            }
            nearby.removed = true;
            for (Node node : nearby.adj) {
                if (node.removed) {
                    continue;
                }
                set.remove(node);
                node.deg--;
                set.add(node);
            }
        }

        out.println("Second");
    }

}

class Node {
    List<Node> adj = new ArrayList<>();
    int deg;
    int id;
    boolean removed;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}