package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EGraphXY {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int A = in.readInt();
        int B = in.readInt();
        int[][] mat = new int[A][B];
        for (int i = 0; i < A; i++) {
            for (int j = 0; j < B; j++) {
                mat[i][j] = in.readInt();
            }
        }
        Node s = new Node();
        Node t = new Node();
        Node[] xSeq = new Node[101];
        Node[] ySeq = new Node[101];
        xSeq[0] = ySeq[0] = s;
        xSeq[100] = ySeq[100] = t;
        for (int i = 1; i < 100; i++) {
            xSeq[i] = new Node();
            ySeq[i] = new Node();
        }
        for (int i = 0; i + 1 < 100; i++) {
            Edge e = new Edge();
            e.from = xSeq[i];
            e.x = true;
            e.to = xSeq[i + 1];
            xSeq[i].adj.add(e);
        }
        for (int i = 1; i < 100; i++) {
            Edge e = new Edge();
            e.y = true;
            e.to = ySeq[i + 1];
            e.from = ySeq[i];
            ySeq[i].adj.add(e);
        }
        Set<Node> set = new HashSet<>();
        set.addAll(Arrays.asList(xSeq));
        set.addAll(Arrays.asList(ySeq));
        Node[] nodes = set.toArray(new Node[0]);
        String no = "Impossible";
        String yes = "Possible";
        int[][] atLeast = new int[101][101];
        SequenceUtils.deepFill(atLeast, 0);
        for (int i = 0; i < A; i++) {
            for (int j = 0; j < B; j++) {
                for (int k = 0; k < 100; k++) {
                    for (int z = 0; z < 100; z++) {
                        atLeast[k][z] = Math.max(atLeast[k][z], mat[i][j] - (i + 1) * k - (j + 1) * z);
                    }
                }
            }
        }

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                Edge e = new Edge();
                e.w = atLeast[i][j];
                e.from = xSeq[i];
                e.to = ySeq[100 - j];
                e.from.adj.add(e);
            }
        }

        for (int i = 0; i < A; i++) {
            for (int j = 0; j < B; j++) {
                for (Node node : nodes) {
                    for (Edge e : node.adj) {
                        if (e.x) {
                            e.w = i + 1;
                        }
                        if (e.y) {
                            e.w = j + 1;
                        }
                    }
                }
                int ans = distance(nodes, s, t);
                assert ans >= mat[i][j];
                if (ans != mat[i][j]) {
                    out.println(no);
                    return;
                }
            }
        }
        out.println(yes);
        List<Edge> edges = new ArrayList<>();
        for (Node node : nodes) {
            edges.addAll(node.adj);
        }
        out.append(nodes.length).append(' ').append(edges.size()).println();
        for (Edge e : edges) {
            out.append(e.from.id + 1).append(' ').append(e.to.id + 1).append(' ');
            if (e.x) {
                out.append('X');
            } else if (e.y) {
                out.append('Y');
            } else {
                out.append(e.w);
            }
            out.println();
        }
        out.append(s.id + 1).append(' ').append(t.id + 1).println();
    }

    public int distance(Node[] nodes, Node src, Node dst) {
        int inf = (int) 1e9;
        for (Node node : nodes) {
            node.dist = inf;
        }
        src.dist = 0;
        TreeSet<Node> set = new TreeSet<>((a, b) -> a.dist == b.dist ? Integer.compare(a.id, b.id) :
                Integer.compare(a.dist, b.dist));
        set.add(src);
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (Edge e : head.adj) {
                if (e.to.dist <= e.w + head.dist) {
                    continue;
                }
                set.remove(e.to);
                e.to.dist = e.w + head.dist;
                set.add(e.to);
            }
        }
        return dst.dist;
    }
}

class Edge {
    boolean x;
    boolean y;
    int w;
    Node to;
    Node from;

    @Override
    public String toString() {
        return to.toString();
    }
}

class Node {
    static int idAlloc = 0;
    List<Edge> adj = new ArrayList<>();
    int id = idAlloc++;
    int dist;

    @Override
    public String toString() {
        return "" + id;
    }
}
