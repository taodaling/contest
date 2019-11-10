package on2019_11.on2019_11_10_.LUOGU47790;




import template.FastInput;
import template.FastOutput;
import template.PairingHeap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class LUOGU4779 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i <= m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.dist = in.readInt();
            a.next.add(e);
        }
        nodes[s].dist = 0;

        TreeSet<Node> set = new TreeSet<>(Node.sortByDist);
        set.add(nodes[s]);

        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (Edge e : head.next) {
                Node next = e.a == head ? e.b : e.a;
                int dist = head.dist + e.dist;
                if (next.dist <= dist) {
                    continue;
                }
                set.remove(next);
                next.dist = dist;
                set.add(next);
            }
        }

        for (int i = 1; i <= n; i++) {
            out.append(nodes[i].dist).append(' ');
        }
    }
}

class Edge {
    Node a;
    Node b;
    int dist;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    int dist = (int) 1e9;
    int id;

    static Comparator<Node> sortByDist = (a, b) -> a.dist == b.dist ? a.id - b.id : a.dist - b.dist;
}
