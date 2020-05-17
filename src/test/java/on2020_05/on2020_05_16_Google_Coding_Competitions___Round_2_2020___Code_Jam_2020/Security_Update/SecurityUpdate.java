package on2020_05.on2020_05_16_Google_Coding_Competitions___Round_2_2020___Code_Jam_2020.Security_Update;



import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecurityUpdate {
    //Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);

        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            if (i != 0) {
                nodes[i].dist = in.readInt();
            }
        }

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            edges[i] = new Edge();
            edges[i].a = a;
            edges[i].b = b;
            a.adj.add(edges[i]);
            b.adj.add(edges[i]);
        }

        List<Node> t1 = new ArrayList<>(n);
        List<Node> t2 = new ArrayList<>(n);
        for (int i = 1; i < n; i++) {
            if (nodes[i].dist < 0) {
                t2.add(nodes[i]);
                nodes[i].dist = -nodes[i].dist;
            } else {
                t1.add(nodes[i]);
            }
        }

        t1.sort((a, b) -> a.dist - b.dist);
        t2.sort((a, b) -> a.dist - b.dist);

        int lastDist = 0;
        int front = 1;
        Range2DequeAdapter<Node> rdq = new Range2DequeAdapter<>(i -> t1.get(i), 0, t1.size() - 1);
        for (int i = 0; i < t2.size(); i++) {
            int r = i;
            while (r + 1 < t2.size() && t2.get(r + 1).dist == t2.get(r).dist) {
                r++;
            }
            int need = t2.get(i).dist;
            while (front < need) {
                lastDist = rdq.removeFirst().dist;
                front++;
            }
            lastDist++;
            front += r - i + 1;
            for (int j = i; j <= r; j++) {
                t2.get(j).dist = lastDist;
            }
            i = r;
        }

        int inf = (int) 1e6;
        for (Edge e : edges) {
            if (e.a.dist == e.b.dist) {
                e.w = inf;
                continue;
            }
            e.w = Math.abs(e.a.dist - e.b.dist);
        }

        //debug.debug("nodes", nodes);
        for(Edge e : edges){
            out.append(e.w).append(' ');
        }
        out.println();
    }
}

class Edge {
    Node a;
    Node b;
    int w;
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int dist;
    int id;

    @Override
    public String toString() {
        return String.format("%d:%d", id, dist);
    }
}
