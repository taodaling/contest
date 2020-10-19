package contest;

import template.io.FastInput;

import java.io.PrintWriter;
import java.util.*;

public class ETreeFolding {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.length, b.length));
        for (Node node : nodes) {
            if (node.adj.size() == 1) {
                pq.add(node);
            }
        }
        while (!pq.isEmpty()) {
            Node head = pq.remove();
            int len = head.length + 1;
            if (head.adj.isEmpty()) {
                int ans = head.set.stream().mapToInt(Integer::intValue).sum();
                while (ans % 2 == 0) {
                    ans /= 2;
                }
                out.print(ans);
                return;
            }
            Node other = head.adj.stream().findAny().get();
            other.adj.remove(head);
            other.set.add(len);
            if (other.adj.size() + other.set.size() <= 2) {
                other.length = other.set.stream().findAny().get();
                pq.add(other);
            }
        }

        out.print(-1);

    }
}

class Node {
    Set<Node> adj = new HashSet<>();
    Set<Integer> set = new HashSet<>();
    int length;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}