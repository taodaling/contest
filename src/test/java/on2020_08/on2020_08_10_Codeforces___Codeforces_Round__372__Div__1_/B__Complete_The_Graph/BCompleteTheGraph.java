package on2020_08.on2020_08_10_Codeforces___Codeforces_Round__372__Div__1_.B__Complete_The_Graph;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBinaryFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class BCompleteTheGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int l = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        Node s = nodes[in.readInt()];
        Node t = nodes[in.readInt()];

        List<Edge> unknown = new ArrayList<>(m);
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.a = nodes[in.readInt()];
            e.b = nodes[in.readInt()];
            e.a.adj.add(e);
            e.b.adj.add(e);
            e.w = in.readInt();
            if (e.w == 0) {
                unknown.add(e);
            }
            edges[i] = e;
        }
        for (Edge e : unknown) {
            e.w = 1;
        }
        long min = dist(nodes, s, t);
        if (min > l || min != l && unknown.isEmpty()) {
            out.println("NO");
            return;
        }

        if(!unknown.isEmpty()){
            IntBinarySearch ibs = new IntBinarySearch() {
                @Override
                public boolean check(int mid) {
                    for (Edge e : unknown) {
                        e.w = 1;
                    }
                    for (int i = 0; i <= mid; i++) {
                        unknown.get(i).w = inf;
                    }
                    return dist(nodes, s, t) >= l;
                }
            };

            int key = ibs.binarySearch(0, unknown.size() - 1);
            if (!ibs.check(key)) {
                out.println("NO");
                return;
            }

            for (Edge e : unknown) {
                e.w = 1;
            }
            for (int i = 0; i < key; i++) {
                unknown.get(i).w = inf;
            }
            long dist = dist(nodes, s, t);
            unknown.get(key).w += l - dist;
        }

        out.println("YES");
        for (Edge e : edges) {
            out.append(e.a.id).append(' ').append(e.b.id).append(' ').println(e.w);
        }
    }

    long inf = (long) 1e18;

    public long dist(Node[] nodes, Node s, Node t) {
        for (Node node : nodes) {
            node.dist = inf;
        }
        s.dist = 0;
        TreeSet<Node> set = new TreeSet<>((a, b) -> a.dist == b.dist ? Integer.compare(a.id, b.id) : Long.compare(a.dist, b.dist));
        set.add(s);
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (Edge e : head.adj) {
                Node node = e.other(head);
                if (node.dist <= head.dist + e.w) {
                    continue;
                }
                set.remove(node);
                node.dist = head.dist + e.w;
                set.add(node);
            }
        }
        return t.dist;
    }
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
    List<Edge> adj = new ArrayList<>();
    long dist;
    int id;
}