package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class EYoungMaids {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[n];
        in.populate(p);
        int[] even = new int[n];
        int[] odd = new int[n];
        int inf = (int) 1e9;
        Arrays.fill(even, inf);
        Arrays.fill(odd, inf);
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                even[i] = p[i];
            } else {
                odd[i] = p[i];
            }
        }

        rmqs[0] = new RMQ(n);
        rmqs[1] = new RMQ(rmqs[0]);
        rmqs[0].reset(0, n - 1, (a, b) -> Integer.compare(even[a], even[b]));
        rmqs[1].reset(0, n - 1, (a, b) -> Integer.compare(odd[a], odd[b]));
        Node root = build(0, n - 1, 0, null);

        PriorityQueue<Node> pq = new PriorityQueue<>(n, (a, b) -> Integer.compare(p[a.first], p[b.first]));
        pq.add(root);

        List<Node> ordered = new ArrayList<>(n);
        while (!pq.isEmpty()) {
            Node top = pq.remove();
            ordered.add(top);
            pq.addAll(top.next);
        }

        for (Node node : ordered) {
            out.append(p[node.first]).append(' ').append(p[node.second]).append(' ');
        }
    }

    RMQ[] rmqs = new RMQ[2];

    public Node build(int l, int r, int xor, Node top) {
        if (l >= r) {
            return null;
        }
        Node node = new Node();
        node.p = top;
        if (top != null) {
            top.next.add(node);
        }
        node.first = rmqs[0 ^ xor].query(l, r);
        node.second = rmqs[1 ^ xor].query(node.first + 1, r);
        build(l, node.first - 1, xor, node);
        build(node.first + 1, node.second - 1, xor ^ 1, node);
        build(node.second + 1, r, xor, node);

        return node;
    }
}

class Node {
    Node p;
    List<Node> next = new ArrayList<>();
    int first;
    int second;
}
