package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class D13thLabourOfHeracles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].w = in.ri();
            sum += nodes[i].w;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>(n, Comparator.reverseOrder());
        for (Node node : nodes) {
            for (int i = 1; i < node.adj.size(); i++) {
                pq.add(node.w);
            }
        }
        long ans = sum;
        out.append(ans).append(' ');
        while (!pq.isEmpty()) {
            ans += pq.remove();
            out.append(ans).append(' ');
        }
        out.println();
    }
}

class Node {
    int w;
    List<Node> adj = new ArrayList<>();
}
