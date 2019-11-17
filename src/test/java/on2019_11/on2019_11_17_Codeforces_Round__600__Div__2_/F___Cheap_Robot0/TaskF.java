package on2019_11.on2019_11_17_Codeforces_Round__600__Div__2_.F___Cheap_Robot0;



import template.DynamicMST;
import template.FastInput;
import template.FastOutput;

import java.util.*;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int q = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt()];
            edges[i].b = nodes[in.readInt()];
            edges[i].w = in.readInt();
            edges[i].a.next.add(edges[i]);
            edges[i].b.next.add(edges[i]);
        }

        for (int i = k + 1; i <= n; i++) {
            nodes[i].dist = (long) 1e18;
        }

        TreeSet<Node> set = new TreeSet<>((a, b) -> a.dist == b.dist ? a.id - b.id : Long.compare(a.dist, b.dist));
        for (int i = 1; i <= k; i++) {
            nodes[i].dist = 0;
            set.add(nodes[i]);
        }

        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (Edge e : head.next) {
                Node node = e.other(head);
                if (head.dist + e.w >= node.dist) {
                    continue;
                }
                set.remove(node);
                node.dist = head.dist + e.w;
                set.add(node);
            }
        }

        for (Edge e : edges) {
            e.w += e.a.dist + e.b.dist;
        }

        DynamicMST mst = new DynamicMST(n);
        for(Edge e : edges){
            mst.addEdge(e.a.id - 1, e.b.id - 1, e.w);
        }

        for(int i = 0; i < q; i++){
            int a = in.readInt();
            int b = in.readInt();
            long maxW = mst.maxWeightBetween(a - 1, b - 1);
            out.println(maxW);
        }
    }

}


class Query {
    Node a;
    Node b;
    long ans;
}

class Edge {
    Node a;
    Node b;
    long w;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> next = new ArrayList<>();
    long dist;
    int id;

}