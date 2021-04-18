package on2021_03.on2021_03_31_CS_Academy___Virtual_FII_Code_Round__3.Alice_s_Tree;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class AlicesTree {

    public void any(Node[] nodes) {
        int n = nodes.length;
        PriorityQueue<Node> pq = new PriorityQueue<>(n, Comparator.comparingInt(x -> -x.deg));
        Arrays.sort(nodes, Comparator.comparingInt(x -> -x.deg));
        pq.add(nodes[0]);
        for (int i = 1; i < n; i++) {
            Node node = nodes[i];
            Node head = pq.remove();
            head.deg--;
            node.deg--;
            pq.add(head);
            pq.add(node);
        }
    }

    void output(Node[] nodes, FastOutput out) {
        for (Node node : nodes) {
            for (Node child : node.adj) {
                if (node.id < child.id) {
                    out.append(node.id + 1).append(' ').append(child.id + 1).println();
                }
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int X = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].deg = in.ri();
        }
        if (X <= 2) {
            any(nodes);
            output(nodes, out);
            return;
        }

        Arrays.sort(nodes, Comparator.comparingInt(x -> -x.deg));
        int moreThanOne = 0;
        for (Node node : nodes) {
            if (node.deg > 1) {
                moreThanOne++;
            }
        }
        PriorityQueue<Node> pq = new PriorityQueue<>(n, Comparator.comparingInt(x -> x.dist));
        if (X % 2 == 0) {
            Node center = nodes[0];
            Node[] derive = new Node[2];
            Node[] end = new Node[2];
            pq.add(center);
            boolean finalStep = false;
            for (int i = 1; i < n; i++) {
                while (true) {
                    if (end[0] != null && end[1] != null && end[0].dist + end[1].dist + 2 + moreThanOne - i == X) {
                        finalStep = true;
                        break;
                    }
                }
            }
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int dist;
    int deg;
    int id;

    Node farthest;
}