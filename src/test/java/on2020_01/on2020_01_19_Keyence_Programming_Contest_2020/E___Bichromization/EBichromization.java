package on2020_01.on2020_01_19_Keyence_Programming_Contest_2020.E___Bichromization;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class EBichromization {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].d = in.readInt();
        }
        List<Edge> edges = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            edges.add(e);

            if (a.d <= b.d) {
                b.satisfy = true;
            }
            if (b.d <= a.d) {
                a.satisfy = true;
            }
        }

        for (int i = 1; i <= n; i++) {
            if (!nodes[i].satisfy) {
                out.println(-1);
                return;
            }
        }

        List<Edge> sorted = new ArrayList<>(edges);
        sorted.sort((a, b) -> (a.a.d + a.b.d) - (b.a.d + b.b.d));
        for (Edge e : sorted) {
            if (e.a.color != -1 && e.b.color != -1) {
                e.len = (int) 1e9;
            } else {
                if (e.a.color != -1) {
                    e.b.color = 1 - e.a.color;
                } else if (e.b.color != -1) {
                    e.a.color = 1 - e.b.color;
                } else {
                    e.a.color = 0;
                    e.b.color = 1;
                }
                e.len = Math.max(e.a.d, e.b.d);
            }
        }

        for (int i = 1; i <= n; i++) {
            out.append(nodes[i].color == 0 ? 'W' : 'B');
        }
        out.println();
        for (Edge e : edges) {
            out.println(e.len);
        }
    }


}

class Edge {
    Node a;
    Node b;
    int len = -1;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    int color = -1;
    int d;
    boolean satisfy;
}