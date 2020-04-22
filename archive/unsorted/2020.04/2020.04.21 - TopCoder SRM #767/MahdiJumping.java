package contest;

import java.util.Set;
import java.util.TreeSet;

public class MahdiJumping {
    public long minDis(int n, int A, int B, int a, int b) {
        long inf = (long) 1e18;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].index = i;
            nodes[i].dist = inf;
        }

        nodes[0].dist = 0;
        TreeSet<Node> set = new TreeSet<Node>((x, y) -> x.dist == y.dist ? Integer.compare(x.index, y.index) :
                Long.compare(x.dist, y.dist));
        set.add(nodes[0]);
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            if (head.index + 1 < n && nodes[head.index + 1].dist >
                    head.dist + a) {
                set.remove(nodes[head.index + 1]);
                nodes[head.index + 1].dist = head.dist + a;
                set.add(nodes[head.index + 1]);
            }
            Node next = nodes[(int) (((long) A * head.index + B) % n)];
            if (next.dist > head.dist + b) {
                set.remove(next);
                next.dist = head.dist + b;
                set.add(next);
            }
        }

        return nodes[n - 1].dist;
    }
}

class Node {
    int index;
    long dist;
}