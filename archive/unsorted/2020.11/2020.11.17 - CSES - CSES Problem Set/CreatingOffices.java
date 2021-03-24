package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerFixedMinHeap;
import template.utils.SortUtils;

import java.util.List;

public class CreatingOffices {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        d = in.readInt();
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.readInt() - 1, in.readInt() - 1);
        }
        added = new boolean[n];
        depth = new int[n];
        highest = new int[n];
        buf = new int[n];
        dfsForAns(0, -1);
        int size = 0;
        for (int i = 0; i < n; i++) {
            if (added[i]) {
                size++;
            }
        }
        out.println(size);
        for (int i = 0; i < n; i++) {
            if (added[i]) {
                out.append(i + 1).append(' ');
            }
        }
    }

    List<UndirectedEdge>[] g;
    int d;

    boolean[] added;
    int[] depth;
    int[] highest;
    int[] buf;

    public void dfsForAns(int root, int p) {
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForAns(e.to, root);
        }
        int wpos = 0;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            buf[wpos++] = highest[e.to];
        }
        SortUtils.quickSort(buf, (a, b) -> Integer.compare(depth[a], depth[b]), 0, wpos);
        int begin = 0;
        while (begin + 1 < wpos && depth[buf[begin]] + depth[buf[begin + 1]] - depth[root] * 2 < d) {
            added[buf[begin]] = false;
            begin++;
        }
        if (begin == wpos || depth[buf[begin]] - depth[root] >= d) {
            added[root] = true;
            highest[root] = root;
        }else{
            highest[root] = buf[begin];
        }
    }
}
