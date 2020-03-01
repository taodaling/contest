package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.*;

public class BNavigationSystem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            nodes[a].out.add(nodes[b]);
            nodes[b].in.add(nodes[a]);
        }
        int k = in.readInt();
        int[] seq = new int[k];
        for (int i = 0; i < k; i++) {
            seq[i] = in.readInt() - 1;
        }
        int end = seq[k - 1];
        Deque<Node> deque = new ArrayDeque<>();
        deque.addLast(nodes[end]);
        nodes[end].dist = 0;
        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            for (Node node : head.in) {
                if (node.dist == -1) {
                    node.dist = head.dist + 1;
                    deque.addLast(node);
                }
            }
        }
        int min = 0;
        int max = 0;
        for (int i = 0; i < k - 1; i++) {
            Node cur = nodes[seq[i]];
            Node next = nodes[seq[i + 1]];
            int cnt = 0;
            int minDist = next.dist;
            for (Node node : cur.out) {
                if (node.dist < minDist) {
                    minDist = node.dist;
                    cnt = 0;
                }
                if (node.dist == minDist) {
                    cnt++;
                }
            }
            if (minDist < next.dist) {
                min++;
                max++;
            } else if (cnt > 1) {
                max++;
            }
        }

        out.println(min);
        out.println(max);
    }
}

class Node {
    int id;
    List<Node> in = new ArrayList<>();
    List<Node> out = new ArrayList<>();
    int dist = -1;
}
