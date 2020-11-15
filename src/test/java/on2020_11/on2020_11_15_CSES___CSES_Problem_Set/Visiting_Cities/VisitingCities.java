package on2020_11.on2020_11_15_CSES___CSES_Problem_Set.Visiting_Cities;



import template.graph.DirectedEdge;
import template.graph.DominatorTree;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.IntegerWeightDirectedEdge;
import template.primitve.generated.graph.IntegerWeightGraph;
import template.primitve.generated.graph.IntegerWeightUndirectedEdge;
import template.primitve.generated.graph.LongWeightGraph;
import template.utils.Debug;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class VisitingCities {
    //Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<IntegerWeightDirectedEdge>[] g = Graph.createGraph(n);
        List<DirectedEdge>[] minDistG = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int c = in.readInt();
            IntegerWeightGraph.addEdge(g, a, b, c);
        }
        long inf = (long) 1e18;
        long[] dists = new long[n];
        Arrays.fill(dists, inf);
        dists[0] = 0;
        TreeSet<Integer> pq = new TreeSet<>((a, b) -> dists[a] == dists[b] ? a.compareTo(b) : Long.compare(dists[a], dists[b]));
        pq.add(0);
        while (!pq.isEmpty()) {
            int head = pq.pollFirst();
            for (IntegerWeightDirectedEdge e : g[head]) {
                if (dists[head] + e.weight < dists[e.to]) {
                    pq.remove(e.to);
                    dists[e.to] = dists[head] + e.weight;
                    pq.add(e.to);
                }
            }
        }
        //debug.debug("dists", dists);
        for (int i = 0; i < n; i++) {
            for (IntegerWeightDirectedEdge e : g[i]) {
                if (dists[i] + e.weight == dists[e.to]) {
                    minDistG[i].add(e);
                    //debug.debug("(a, b)", String.format("(%d, %d)", i + 1, e.to + 1));
                }
            }
        }

        DominatorTree dt = new DominatorTree(minDistG, 0);
        IntegerArrayList ancestors = new IntegerArrayList(n);
        int cur = n - 1;
        while (cur >= 0) {
            ancestors.add(cur);
            cur = dt.parent(cur);
        }
        out.println(ancestors.size());
        ancestors.sort();
        for (int x : ancestors.toArray()) {
            out.append(x + 1).append(' ');
        }
    }

}

