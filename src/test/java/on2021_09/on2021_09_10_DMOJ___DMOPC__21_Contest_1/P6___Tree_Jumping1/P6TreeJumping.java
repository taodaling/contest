package on2021_09.on2021_09_10_DMOJ___DMOPC__21_Contest_1.P6___Tree_Jumping1;




import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class P6TreeJumping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            nodes[i].r = in.ri();
        }
        for (int i = 0; i < n; i++) {
            nodes[i].c = in.ri();
        }

        for (int i = 0; i < n - 1; i++) {
            Edge e = new Edge();
            e.a = nodes[in.ri() - 1];
            e.b = nodes[in.ri() - 1];
            e.w = in.ri();
            e.a.adj.add(e);
            e.b.adj.add(e);
        }
        dfs(nodes[0], null, 0, null);
        for (int i = 1; i < n; i++) {
            if (nodes[i].ans < 0) {
                out.println("Unreachable");
            } else {
                out.println(1 / nodes[i].ans);
            }
        }
//        debug.debug("time", time);
    }

    int L = 17;
//    int time = 0;
//    Debug debug = new Debug(true);


    public void dfs(Node root, Node p, long d, Line pLine) {
        root.depth = d;
        if (pLine != null) {
            Line best = pLine;
            for (int i = L - 1; i >= 0; i--) {
//                time++;
                if (best == null || best.ancestors[i] == null) {
                    continue;
                }
                double pt = best.ancestors[i].apply(root.c, root.depth);
                if (pt <= best.ancestors[i].begin) {
                    best = best.ancestors[i];
                }
            }
            if (best.apply(root.c, root.depth) <= best.begin) {
                best = best.ancestors[0];
            }
            root.ans = best == null ? -1 : best.apply(root.c, root.depth);
        }
        //calc new pLine
        Line newLine = new Line();
        newLine.ancestors = new Line[L];
        newLine.r = root.r;
        newLine.d = root.depth;
        if (pLine != null) {
            for (int i = L - 1; i >= 0; i--) {
//                time++;
                if (pLine == null || pLine.ancestors[i] == null) {
                    continue;
                }
                double pt = pLine.ancestors[i].apply(newLine.r, newLine.d);
                if (pt <= pLine.ancestors[i].begin) {
                    pLine = pLine.ancestors[i];
                }
            }
            double pt = pLine.apply(newLine.r, newLine.d);
            if (pt <= pLine.begin) {
                pLine = pLine.ancestors[0];
            }
        }
        if (pLine == null) {
            newLine.begin = 0;
        } else {
            newLine.begin = pLine.apply(newLine.r, newLine.d);
        }
        newLine.ancestors[0] = pLine;
        for (int i = 0; i + 1 < L && newLine.ancestors[i] != null; i++) {
//            time++;
            newLine.ancestors[i + 1] = newLine.ancestors[i].ancestors[i];
        }

        for (Edge e : root.adj) {
//            time++;
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root, root.depth + e.w, newLine);
        }
    }
}

class Line {
    double r;
    double d;
    double begin;

    Line[] ancestors;

    public double apply(double rj, double dj) {
        return (rj - r) / (d - dj);
    }
}

class Edge {
    Node a;
    Node b;
    int w;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int r;
    int c;
    long depth;
    double ans;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
