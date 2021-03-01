package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.*;

public class BCubes {
    public void decreaseOutDeg(Node root, int mod) {
        if (root.removed) {
            return;
        }
        root.outdeg -= mod;
        if (root.outdeg == 1) {
            for (Node node : root.out) {
                if (node.removed) {
                    continue;
                }
                node.indeg++;
                if (node.indeg == 1) {
                    pq.remove(node);
                }
            }
        }
    }

    TreeSet<Node> pq = new TreeSet<>(Comparator.comparingInt(x -> x.id));

    public void removeNode(Node root) {
        assert !root.removed;
        root.removed = true;
        if (root.outdeg == 1) {
            int cnt = 0;
            for (Node node : root.out) {
                if (node.removed) {
                    continue;
                }
                cnt++;
                node.indeg--;
                if (node.indeg == 0) {
                    pq.add(node);
                }
            }
            assert cnt == 1;
        }
        for (Node node : root.in) {
            decreaseOutDeg(node, 1);
        }
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        HashMap<Long, Node> nodes = new HashMap<>();
        for (int i = 0; i < m; i++) {
            int x = in.ri();
            int y = in.ri();
            Node node = new Node();
            node.id = i;
            long key = DigitUtils.asLong(x, y);
            nodes.put(key, node);
        }
        for (Map.Entry<Long, Node> entry : nodes.entrySet()) {
            int x = DigitUtils.highBit(entry.getKey());
            int y = DigitUtils.lowBit(entry.getKey());
            Node node = entry.getValue();
            for (int i = -1; i <= 1; i++) {
                long key = DigitUtils.asLong(x + i, y - 1);
                Node exist = nodes.get(key);
                if (exist == null) {
                    continue;
                }
                node.out.add(exist);
                node.outdeg++;
                exist.in.add(node);
            }
            pq.add(node);
        }
        for (Node node : nodes.values()) {
            decreaseOutDeg(node, 0);
        }

        int mod = (int) 1e9 + 9;
        long sum = 0;
        boolean max = true;
        while (!pq.isEmpty()) {
            Node head = max ? pq.pollLast() : pq.pollFirst();
            debug.debug("head", head);
            sum = (sum * m + head.id) % mod;
            max = !max;
            removeNode(head);
        }
        out.println(sum);
    }
}

class Node {
    int id;
    List<Node> out = new ArrayList<>();
    List<Node> in = new ArrayList<>();
    int outdeg;
    int indeg;
    boolean removed;

    @Override
    public String toString() {
        return "" + id;
    }
}
