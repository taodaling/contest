package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class ANastyaAndStrangeGenerator {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        int[] inv = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt() - 1;
            inv[p[i]] = i;
        }

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].size = 1;
            nodes[i].id = i;
            nodes[i].inv = inv[i];
        }

        TreeMap<Integer, Node> next = new TreeMap<>();
        TreeSet<Node> set = new TreeSet<>((a, b) -> a.size == b.size ? Integer.compare(a.id, b.id) : -Integer.compare(a.size, b.size));
        for (int i = 0; i < n; i++) {
            next.put(nodes[i].inv, nodes[i]);
            set.add(nodes[i]);
        }

        int last = -1;
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            if (head.id != last + 1) {
                out.println("No");
                return;
            }
            next.remove(head.inv);
            Map.Entry<Integer, Node> follow = next.ceilingEntry(head.inv);
            if (follow != null) {
                set.remove(follow.getValue());
                follow.getValue().size += head.size;
                set.add(follow.getValue());
            }
            last++;
        }

        out.println("Yes");
    }
}

class Node {
    int size;
    int id;
    int inv;
}

