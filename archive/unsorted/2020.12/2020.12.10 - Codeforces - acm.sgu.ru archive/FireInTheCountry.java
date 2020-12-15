package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FireInTheCountry {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        int inf = (int) 1e9;
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].dist = inf;
        }
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            nodes[a].adj.add(nodes[b]);
            nodes[b].adj.add(nodes[a]);
        }
        nodes[0].dist = 0;
        Deque<Node> dq = new ArrayDeque<>(n);
        dq.addLast(nodes[0]);
        List<Node> seq = new ArrayList<>(n);
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();
            seq.add(head);
            for (Node node : head.adj) {
                if (node.dist <= head.dist + 1) {
                    continue;
                }
                node.dist = head.dist + 1;
                dq.addLast(node);
            }
        }
        SequenceUtils.reverse(seq);
        IntegerVersionArray iva = new IntegerVersionArray(n);
        for (Node node : seq) {
            iva.clear();
            for (Node to : node.adj) {
                if (to.dist == node.dist + 1) {
                    iva.set(to.sg, 1);
                }
            }
            while(iva.get(node.sg) == 1){
                node.sg++;
            }
        }

        out.println(nodes[0].sg != 0 ? "Vladimir" : "Nikolay");
    }

}

class Node {
    List<Node> adj = new ArrayList<>();
    int sg;
    int dist;
}
