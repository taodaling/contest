package on2020_11.on2020_11_02_Codeforces___Codeforces_Round__681__Div__1__based_on_VK_Cup_2019_2020___Final_.C__Graph_Transpositions;



import template.datastructure.PairingHeap;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.utils.CompareUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class CGraphTranspositions {

    long inf = (long) 1e18;

    List<DirectedEdge>[] g;
    List<DirectedEdge>[] revg;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        int threshold = 20;

        g = Graph.createGraph(n);
        revg = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;

            Graph.addEdge(g, u, v);
            Graph.addEdge(revg, v, u);
        }

        Node[][] nodes = new Node[threshold][n];
        for (int i = 0; i < threshold; i++) {
            for (int j = 0; j < n; j++) {
                nodes[i][j] = new Node();
                nodes[i][j].id = j * 100 + i;
                nodes[i][j].dist = inf;
            }
        }

        TreeSet<Node> set = new TreeSet<>((a, b) -> a.dist == b.dist ? Integer.compare(a.id, b.id) : Long.compare(a.dist, b.dist));
        nodes[0][0].dist = 0;
        set.add(nodes[0][0]);
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            int v = vertex(head.id);
            int b = time(head.id);

            if (b + 1 < threshold) {
                long cand = head.dist + (1 << b);
                if (nodes[b + 1][v].dist > cand) {
                    set.remove(nodes[b + 1][v]);
                    nodes[b + 1][v].dist = cand;
                    set.add(nodes[b + 1][v]);
                }
            }

            List<DirectedEdge>[] cur = b % 2 == 0 ? g : revg;
            for (DirectedEdge e : cur[v]) {
                long cand = head.dist + 1;
                if (nodes[b][e.to].dist > cand) {
                    set.remove(nodes[b][e.to]);
                    nodes[b][e.to].dist = cand;
                    set.add(nodes[b][e.to]);
                }
            }
        }

        long min = inf;
        for (int i = 0; i < threshold; i++) {
            min = Math.min(min, nodes[i][n - 1].dist);
        }

        if (min < inf) {
            out.println(min);
            return;
        }

        Comparator<Vertex> comparator = (a, b) -> {
            int comp = Integer.compare(a.jump, b.jump);
            if (comp == 0) {
                comp = Long.compare(a.dist, b.dist);
            }
            if (comp == 0) {
                comp = Integer.compare(a.id, b.id);
            }
            return comp;
        };
        TreeSet<Vertex> pq = new TreeSet<>(comparator);
        Vertex[][] vs = new Vertex[2][n];
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < n; i++) {
                vs[k][i] = new Vertex();
                vs[k][i].id = i;
                vs[k][i].jump = (int) 1e9 - k;
                vs[k][i].dist = inf;

                if (k == ((threshold - 1) & 1)) {
                    if (nodes[threshold - 1][i].dist == inf) {
                        vs[k][i].dist = inf;
                        vs[k][i].jump = (int) 1e9 - k;
                    } else {
                        vs[k][i].dist = nodes[threshold - 1][i].dist - ((1 << (threshold - 1)) - 1);
                        vs[k][i].jump = threshold - 1;
                        pq.add(vs[k][i]);
                    }
                }
            }
        }

        while (!pq.isEmpty()) {
            Vertex head = pq.pollFirst();
            List<DirectedEdge>[] cur = head.jump % 2 == 0 ? g : revg;

            Vertex opposite = vs[(head.jump & 1) ^ 1][head.id];
            if (opposite.jump > head.jump + 1 || opposite.jump == head.jump + 1 && opposite.dist > head.dist) {
                pq.remove(opposite);
                opposite.jump = head.jump + 1;
                opposite.dist = head.dist;
                pq.add(opposite);
            }

            for (DirectedEdge e : cur[head.id]) {
                Vertex v = vs[head.jump & 1][e.to];
                if (v.jump > head.jump || v.jump == head.jump && v.dist > head.dist + 1) {
                    pq.remove(v);
                    v.jump = head.jump;
                    v.dist = head.dist + 1;
                    pq.add(v);
                }
            }
        }

        Vertex ans = CompareUtils.min(vs[0][n - 1], vs[1][n - 1], comparator);

        int mod = 998244353;
        Power pow = new Power(mod);
        long sum = ans.dist + pow.pow(2, ans.jump) - 1;
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }


    public int time(int x) {
        return x % 100;
    }

    public int vertex(int x) {
        return x / 100;
    }
}

class Vertex {
    long dist;
    int jump;
    int id;
}

class Node {
    long dist;
    int id;
}
