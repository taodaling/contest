package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.primitve.generated.datastructure.LongObjectHashMap;
import template.rand.HashData;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DLabellingCities {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].adj.add(nodes[i]);
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        Map<Long, Node> map = new HashMap<>(n);

        Modular mod = new Modular(1e9 + 7);
        long x1 = RandomWrapper.INSTANCE.nextInt(3, mod.getMod() - 1);
        long x2 = RandomWrapper.INSTANCE.nextInt(3, mod.getMod() - 1);

        for (Node node : nodes) {
            node.adj.sort((a, b) -> a.id - b.id);
            int h1 = 1;
            int h2 = 1;
            for (Node x : node.adj) {
                h1 = mod.valueOf(h1 * x1 + x.id);
                h2 = mod.valueOf(h2 * x2 + x.id);
            }
            node.hash = DigitUtils.asLong(h1, h2);

            Node set = map.get(node.hash);
            if (set != null) {
                node.set = set;
            } else {
                node.set = node;
                map.put(node.hash, node);
            }
        }

        boolean valid = true;
        int[] cnts = new int[n + 1];
        Node root = null;
        for (Node node : nodes) {
            if (node.set != node) {
                continue;
            }
            for (Node x : node.adj) {
                node.unique.add(x.set);
            }
            node.unique.remove(node.set);
            if (node.unique.size() > 2) {
                valid = false;
            }
            if (node.unique.size() <= 1) {
                root = node;
            }
            cnts[node.unique.size()]++;
        }

        //circle
        if (cnts[0] + cnts[1] == 0) {
            valid = false;
        }

        if (!valid) {
            out.println("NO");
            return;
        }

        List<Node> list = new ArrayList<>(n);
        while (root != null) {
            if (list.size() > 0) {
                root.unique.remove(list.get(list.size() - 1));
            }
            list.add(root);
            Node next = null;
            for (Node node : root.unique) {
                next = node;
            }
            root = next;
        }

        int alloc = 1;
        for(Node node : list){
            node.alloc = alloc++;
        }

        out.println("YES");
        for(Node node : nodes){
            out.append(node.set.alloc).append(' ');
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    Set<Node> unique = new HashSet<>();

    int id;
    long hash;
    Node set;

    int alloc;
}
