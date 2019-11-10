package on2019_11.on2019_11_10_.LUOGU4779;



import template.FastInput;
import template.FastOutput;
import template.PairingHeap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
            b.next.add(e);
        }
        nodes[s].dist = 0;
        PairingHeap<Node> heap = PairingHeap.NIL;
        for (int i = 1; i <= n; i++) {
            heap = PairingHeap.merge(heap, nodes[i].heap, Node.sortByDist);
        }

        while (!PairingHeap.isEmpty(heap)) {
            Node head = PairingHeap.peek(heap);
            heap = PairingHeap.pop(heap, Node.sortByDist);
            for (Edge e : head.next) {
                Node next = e.a == head ? e.b : e.a;
                int dist = head.dist + e.dist;
                if (next.dist <= dist) {
                    continue;
                }

                next.dist = dist;
                heap = PairingHeap.decrease(heap, next.heap, next, Node.sortByDist);
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
}

class Node {
    List<Edge> next = new ArrayList<>();
    PairingHeap<Node> heap = new PairingHeap<>(this);
    int dist = (int) 1e9;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }

    static Comparator<Node> sortByDist = (a, b) -> Integer.compare(a.dist, b.dist);
}
