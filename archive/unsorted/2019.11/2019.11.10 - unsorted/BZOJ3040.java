package contest;

import template.FastInput;
import template.FastOutput;
import template.PairingHeap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BZOJ3040 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }

        int t = in.readInt();
        long rxa = in.readInt();
        long rxc = in.readInt();
        long rya = in.readInt();
        long ryc = in.readInt();
        long rp = in.readInt();

        long x = 0;
        long y = 0;
        long z = 0;

        for (int i = 0; i < t; i++) {
            x = (x * rxa + rxc) % rp;
            y = (y * rya + ryc) % rp;
            int a = (int) Math.min(x % n + 1, y % n + 1);
            int b = (int) (y % n + 1);
            int dist = (int)1e8 - 100 * a;
            if(a == b){
                continue;
            }
            Edge e = new Edge();
            e.a = nodes[a];
            e.b = nodes[b];
            e.dist = dist;
            nodes[a].next.add(e);
            nodes[b].next.add(e);
        }

        for(int i = t; i < m; i++){
            int a = in.readInt();
            int b = in.readInt();
            int dist = in.readInt();
            if(a == b){
                continue;
            }
            Edge e = new Edge();
            e.a = nodes[a];
            e.b = nodes[b];
            e.dist = dist;
            nodes[a].next.add(e);
            nodes[b].next.add(e);
        }

        nodes[1].dist = 0;
        PairingHeap<Node> heap = PairingHeap.NIL;
        for(int i = 1; i <= n; i++){
            heap = PairingHeap.merge(heap, nodes[i].heap, Node.sortByDist);
        }

        while(!PairingHeap.isEmpty(heap)){
            Node head = PairingHeap.peek(heap);
            heap = PairingHeap.pop(heap, Node.sortByDist);
            for(Edge e : head.next){
                Node next = e.a == head ? e.b : e.a;
                long dist = head.dist + e.dist;
                if(next.dist <= dist){
                    continue;
                }

                next.dist = dist;
                heap = PairingHeap.decrease(heap, next.heap, next, Node.sortByDist);
            }
        }

        out.println(nodes[n].dist);
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
    long dist = (long)1e18;

    static Comparator<Node> sortByDist = new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.dist == o2.dist ? 0 : o1.dist < o2.dist ? -1 : 1;
        }
    };
}
