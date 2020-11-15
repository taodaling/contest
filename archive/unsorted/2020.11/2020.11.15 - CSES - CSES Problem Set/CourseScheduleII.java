package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class CourseScheduleII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            b.adj.add(a);
            a.deg++;
        }

        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> -Integer.compare(a.id, b.id));
        for(Node node : nodes){
            if(node.deg == 0){
                pq.add(node);
            }
        }
        List<Node> seq = new ArrayList<>(n);
        while(!pq.isEmpty()){
            Node head = pq.remove();
            seq.add(head);
            for(Node node : head.adj){
                node.deg--;
                if(node.deg == 0){
                    pq.add(node);
                }
            }
        }
        Collections.reverse(seq);
        for(Node node : seq){
            out.append(node.id + 1).append(' ');
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int deg;
    int id;
}
