package contest;

import dp.Lis;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CEhabAndPathEticMEXs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        Edge[] edges = new Edge[n - 1];
        for (int i = 0; i < n - 1; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt() - 1];
            edges[i].b = nodes[in.readInt() - 1];
            edges[i].a.edges.add(edges[i]);
            edges[i].b.edges.add(edges[i]);
        }
        int index = -1;
        for (int i = 0; i < n; i++) {
            if (nodes[i].edges.size() > 2) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            for (int i = 0; i < n - 1; i++) {
                edges[i].w = i;
            }
        } else {

            int order = 0;
            for (Edge e : nodes[index].edges) {
                e.w = order++;
            }
            for (Edge e : edges) {
                if (e.w == -1) {
                    e.w = order++;
                }
            }
        }

        for (int i = 0; i < n - 1; i++) {
            out.append(edges[i].w).append(' ');
        }
        out.println();
    }
}

class Edge {
    Node a;
    Node b;
    int w = -1;
}

class Node {
    List<Edge> edges = new ArrayList<>();
}