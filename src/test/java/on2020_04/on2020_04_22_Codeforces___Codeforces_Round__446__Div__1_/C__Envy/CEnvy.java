package on2020_04.on2020_04_22_Codeforces___Codeforces_Round__446__Div__1_.C__Envy;



import template.datastructure.DSU;
import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.PriorityQueue;

public class CEnvy {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Edge[] edges = new Edge[m];
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt() - 1];
            edges[i].b = nodes[in.readInt() - 1];
            edges[i].w = in.readInt();
        }
        DSU dsu = new DSU(n);
        Edge[] sortedByWeight = edges.clone();
        Arrays.sort(sortedByWeight, (a, b) -> Integer.compare(a.w, b.w));
        for (int i = 0; i < m; i++) {
            int r = i;
            while (r + 1 < m && sortedByWeight[i].w == sortedByWeight[r + 1].w) {
                r++;
            }
            for (int j = i; j <= r; j++) {
                Edge e = sortedByWeight[j];
                if (dsu.find(e.a.id) == dsu.find(e.b.id)) {
                    e.maybe = false;
                } else {
                    e.maybe = true;
                }
            }

            for (int j = i; j <= r; j++) {
                Edge e = sortedByWeight[j];
                dsu.merge(e.a.id, e.b.id);
            }

            i = r;
        }

        int q = in.readInt();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            int k = in.readInt();
            Edge[] set = new Edge[k];
            qs[i] = new Query();
            for (int j = 0; j < k; j++) {
                set[j] = edges[in.readInt() - 1];
                qs[i].valid = qs[i].valid && set[j].maybe;
            }
            Arrays.sort(set, (a, b) -> Integer.compare(a.w, b.w));
            qs[i].dq = new Range2DequeAdapter<>(j -> set[j], 0, k - 1);
        }


        PriorityQueue<Query> pq = new PriorityQueue<>(q, (a, b) -> Integer.compare(a.front, b.front));
        for (int i = 0; i < q; i++) {
            if (qs[i].valid) {
                qs[i].front = qs[i].dq.peekFirst().w;
                pq.add(qs[i]);
            }
        }
        Deque<Node> sp = new ArrayDeque<>(n);
        for (int i = 0; i < m && !pq.isEmpty(); i++) {
            int w = sortedByWeight[i].w;
            while (!pq.isEmpty() && pq.peek().front <= w) {
                Query top = pq.remove();
                while (!top.dq.isEmpty() && top.dq.peekFirst().w <= w) {
                    Edge head = top.dq.removeFirst();
                    Node v = Node.merge(head.a, head.b);
                    if (v == null) {
                        top.valid = false;
                        break;
                    }
                    sp.addLast(v);
                }
                while (!sp.isEmpty()) {
                    Node.rollback(sp.removeLast());
                }
                if (top.valid && !top.dq.isEmpty()) {
                    top.front = top.dq.peekFirst().w;
                    pq.add(top);
                }
            }
            Node.merge(sortedByWeight[i].a, sortedByWeight[i].b);
        }

        for (Query query : qs) {
            out.println(query.valid ? "YES" : "NO");
        }
    }
}

class Node {
    Node p;
    int rank = 1;
    int id;

    public Node find() {
        Node root = this;
        while (root.p != null) {
            root = root.p;
        }
        return root;
    }

    public static Node merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return null;
        }

        if (a.rank > b.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        a.p = b;
        b.rank += a.rank;
        return a;
    }

    public static void rollback(Node node) {
        for (Node trace = node.p; trace != null; trace = trace.p) {
            trace.rank -= node.rank;
        }
        node.p = null;
    }
}

class Query {
    SimplifiedDeque<Edge> dq;
    boolean valid = true;
    int front;
}

class Edge {
    Node a;
    Node b;
    int w;
    boolean maybe;
}