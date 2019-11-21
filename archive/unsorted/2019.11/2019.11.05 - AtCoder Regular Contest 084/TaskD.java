package contest;

import java.util.TreeSet;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class TaskD {
    int inf = (int) 1e8;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        if (k == 100000) {
            out.println(1);
            return;
        }


        int limit = 100000;
        Node[] nodes = new Node[limit];
        for (int i = 0; i < limit; i++) {
            nodes[i] = new Node();
            nodes[i].dist = inf;
            nodes[i].id = i;
        }
        nodes[k].dist = 0;
        TreeSet<Node> set = new TreeSet<>((a, b) -> a.dist == b.dist ? a.id - b.id : a.dist - b.dist);
        set.add(nodes[k]);
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (int i = 0; i < 10; i++) {
                int val = i * k;
                int dist = head.dist + (head.id + val) % 10;
                Node node = nodes[(val + head.id) / 10];
                if (node.dist <= dist) {
                    continue;
                }
                set.remove(node);
                node.dist = dist;
                set.add(node);
            }
        }

        int ans = inf;
        DigitBase base = new DigitBase(10);
        for (int i = 0; i < limit; i++) {
            int sum = 0;
            for (int j = 0; j < 6; j++) {
                sum += base.getBit(i, j);
            }
            ans = Math.min(ans, sum + nodes[i].dist);
        }

        out.println(ans);
    }
}


class Node {
    int id;
    int dist;

    @Override
    public String toString() {
        return "" + id;
    }
}
