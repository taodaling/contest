package on2020_02.on2020_02_21_Codeforces_Round__287__Div__2_.E__Breaking_Good;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EBreakingGood {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        Edge[] edges = new Edge[m];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt() - 1];
            edges[i].b = nodes[in.readInt() - 1];
            edges[i].z = in.readInt();
            edges[i].a.next.add(edges[i]);
            edges[i].b.next.add(edges[i]);
        }
        int s = 0;
        int t = n - 1;
        for (int i = 0; i < n; i++) {
            nodes[i].dist = (int) 1e8;
            nodes[i].cost = (int) 1e8;
        }
        Deque<Node> deque = new ArrayDeque<>(n);
        nodes[s].dist = 0;
        nodes[s].cost = 0;
        deque.addLast(nodes[s]);
        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            for (Edge e : head.next) {
                Node node = e.other(head);
                if (node.dist > head.dist + 1) {
                    node.dist = head.dist + 1;
                    node.cost = (int) 1e8;
                    node.prev = e;
                    deque.addLast(node);
                }
                int fee = e.z == 1 ? -1 : 1;
                if (node.dist == head.dist + 1 &&
                        node.cost > head.cost + fee) {
                    node.cost = head.cost + fee;
                    node.prev = e;
                }
            }
        }

        for (Node node = nodes[t]; node.prev != null; node = node.prev.other(node)) {
            node.prev.path = true;
        }

        List<Edge> edgeList = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            if (edges[i].path) {
                if (edges[i].z == 0) {
                    edgeList.add(edges[i]);
                }
            } else {
                if (edges[i].z == 1) {
                    edgeList.add(edges[i]);
                }
            }
        }

        out.println(edgeList.size());
        for (Edge e : edgeList) {
            out.append(e.a.id + 1).append(' ')
                    .append(e.b.id + 1).append(' ').append(1 - e.z).println();
        }
    }
}

class Edge {
    int z;
    Node a;
    Node b;
    boolean path;

    Node other(Node x) {
        return x == a ? b : a;
    }
}

class Node {
    int id;
    int dist;
    int cost;
    Edge prev;
    List<Edge> next = new ArrayList<>();
}