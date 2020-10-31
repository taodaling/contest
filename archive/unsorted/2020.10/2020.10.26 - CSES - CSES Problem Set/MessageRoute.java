package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.*;

public class MessageRoute {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        int inf = (int) 1e9;
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].dist = inf;
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            u.adj.add(v);
            v.adj.add(u);
        }

        nodes[0].dist = 0;
        Deque<Node> dq = new ArrayDeque<>(n);
        dq.addLast(nodes[0]);
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();
            for (Node node : head.adj) {
                if (node.dist < head.dist + 1) {
                    continue;
                }
                node.dist = head.dist + 1;
                dq.addLast(node);
                node.prev = head;
            }
        }
        Node tail = nodes[n - 1];
        if (tail.dist == inf) {
            out.println("IMPOSSIBLE");
            return;
        }
        List<Node> seq = new ArrayList<>(n);
        while (tail != null) {
            seq.add(tail);
            tail = tail.prev;
        }
        Collections.reverse(seq);
        out.println(seq.size());
        for(Node node : seq){
            out.append(node.id + 1).append(' ');
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int dist;
    Node prev;
    int id;
}
