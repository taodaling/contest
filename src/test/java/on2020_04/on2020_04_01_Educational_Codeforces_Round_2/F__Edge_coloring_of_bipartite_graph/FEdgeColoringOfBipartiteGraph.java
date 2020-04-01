package on2020_04.on2020_04_01_Educational_Codeforces_Round_2.F__Edge_coloring_of_bipartite_graph;



import dp.Lis;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class FEdgeColoringOfBipartiteGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[a + b];
        for (int i = 0; i < a + b; i++) {
            nodes[i] = new Node();
            nodes[i].id = i < a ? i : -(i - a);
        }

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt() - 1];
            edges[i].b = nodes[a + in.readInt() - 1];
            edges[i].a.deg++;
            edges[i].b.deg++;
        }

        int maxDeg = 0;
        for (Node node : nodes) {
            maxDeg = Math.max(maxDeg, node.deg);
        }

        for (Node node : nodes) {
            node.colored = new Edge[maxDeg];
        }

        for (Edge e : edges) {

            int color = -1;
            int c1 = -1;
            int c2 = -1;
            for (int i = 0; i < maxDeg; i++) {
                if (e.a.colored[i] == null && e.b.colored[i] == null) {
                    color = i;
                    break;
                }
                if (e.a.colored[i] == null) {
                    c2 = i;
                }
                if (e.b.colored[i] == null) {
                    c1 = i;
                }
            }
            if (color != -1) {
                e.setColor(color);
                continue;
            }
            if (c1 == c2) {
                throw new IllegalArgumentException();
            }
            replace(e.b, c2, c1);
            e.setColor(c2);
        }

        out.println(maxDeg);
        for (Edge e : edges) {
            out.append(e.c + 1).append(' ');
        }
    }


    //replace c1 with c2
    public void replace(Node root, int c1, int c2) {
        if (root.colored[c1] == null) {
            return;
        }
        Edge e = root.colored[c1];
        Node node = e.other(root);
        replace(node, c2, c1);
        e.setColor(c2);
    }
}

class Edge {
    Node a;
    Node b;
    int c = -1;

    Node other(Node x) {
        return a == x ? b : a;
    }

    void setColor(int c) {
        if (this.c != -1) {
            if(a.colored[this.c] == this){
                a.colored[this.c] = null;
            }
            if(b.colored[this.c] == this){
                b.colored[this.c] = null;
            }
        }
        this.c = c;
        a.colored[c] = b.colored[c] = this;
    }

    @Override
    public String toString() {
        return a + "->" + b;
    }
}

class Node {
    int deg;
    Edge[] colored;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}