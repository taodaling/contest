package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class CLeavingTheBar {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            nodes[i] = new Node();
            nodes[i].x = x;
            nodes[i].y = y;
        }
        Deque<Node> deque = new ArrayDeque<>(Arrays.asList(nodes));
        long limit = (long) 1e12;
        while (deque.size() >= 3) {
            Node a = deque.removeFirst();
            Node b = deque.removeFirst();
            Node c = deque.removeFirst();
            Node[] abc = new Node[]{a, b, c};
            if (same(abc[0], abc[1]) <= limit) {
                abc[0].sign = 1;
                abc[1].sign = 1;
            } else if (diff(abc[0], abc[1]) <= limit) {
                abc[0].sign = 1;
                abc[1].sign = -1;
            } else if (same(abc[0], abc[2]) <= limit) {
                abc[0].sign = 1;
                abc[2].sign = 1;
                SequenceUtils.swap(abc, 1, 2);
            } else if (diff(abc[0], abc[2]) <= limit) {
                abc[0].sign = 1;
                abc[2].sign = -1;
                SequenceUtils.swap(abc, 1, 2);
            } else if (same(abc[1], abc[2]) <= limit) {
                abc[1].sign = 1;
                abc[2].sign = 1;
                SequenceUtils.swap(abc, 0, 2);
            } else if (diff(abc[1], abc[2]) <= limit) {
                abc[1].sign = 1;
                abc[2].sign = -1;
                SequenceUtils.swap(abc, 0, 2);
            }
            deque.addLast(abc[2]);
            Node merge = new Node();
            abc[0].p = merge;
            abc[1].p = merge;
            merge.x = abc[0].sign * abc[0].x + abc[1].sign * abc[1].x;
            merge.y = abc[0].sign * abc[0].y + abc[1].sign * abc[1].y;
            deque.addLast(merge);
        }

        if (deque.size() == 2) {
            if (same(deque.getFirst(), deque.getLast()) <= limit) {
                deque.getFirst().sign = 1;
                deque.getLast().sign = 1;
            } else {
                deque.getFirst().sign = 1;
                deque.getLast().sign = -1;
            }
        }

        for (Node node : nodes) {
            out.println(getSign(node));
        }
    }

    public int getSign(Node node) {
        if (node == null) {
            return 1;
        }
        if (node.visited) {
            return node.sign;
        }
        node.visited = true;
        node.sign *= getSign(node.p);
        return node.sign;
    }

    public long same(Node a, Node b) {
        return dist(a.x + b.x, a.y + b.y);
    }

    public long diff(Node a, Node b) {
        return dist(a.x - b.x, a.y - b.y);
    }

    public long dist(int x, int y) {
        return (long) x * x + (long) y * y;
    }
}

class Node {
    Node p;
    boolean visited;
    int sign = 1;
    int x;
    int y;
}