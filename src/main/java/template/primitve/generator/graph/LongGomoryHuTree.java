package template.primitve.generated.graph;

import template.primitve.generated.datastructure.IntegerList;

import java.util.List;

public class LongGomoryHuTree {
    private List<LongWeightUndirectedEdge>[] ug;
    private long[][] minCuts;
    private static final long INF = (long) 2e18;

    public LongGomoryHuTree(List<LongFlowEdge>[] g, LongMaximumFlow mf) {
        int n = g.length;
        ug = LongWeightGraph.createUndirectedGraph(n);
        minCuts = new long[n][n];
        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        dac(list, g, mf, new boolean[n]);
        for (int i = 0; i < n; i++) {
            dfs(i, -1, INF, minCuts[i]);
        }
    }

    private void addEdge(int s, int t, long f) {
        //System.err.printf("(%d,%d)=%d\n", s, t, f);
        LongWeightGraph.addUndirectedEdge(ug, s, t, f);
    }

    private void dfs(int root, int p, long w, long[] minCuts) {
        minCuts[root] = w;
        for (LongWeightUndirectedEdge e : ug[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, Math.min(w, e.weight), minCuts);
        }
    }

    public long minCut(int s, int t) {
        return minCuts[s][t];
    }


    public void dac(IntegerList set, List<LongFlowEdge>[] g, LongMaximumFlow mf, boolean[] visited) {
        if (set.size() <= 1) {
            return;
        }
        //rebuild the flow
        LongFlow.rewind(g);
        int s = set.get(0);
        int t = set.get(1);
        long f = mf.apply(g, s, t, (long)2e18);
        addEdge(s, t, f);
        LongFlow.findSetST(g, s, visited);
        IntegerList l1 = new IntegerList(set.size());
        IntegerList l2 = new IntegerList(set.size());
        for (int i = set.size() - 1; i >= 0; i--) {
            int v = set.get(i);
            if (visited[v]) {
                l1.add(v);
            } else {
                l2.add(v);
            }
        }
        dac(l1, g, mf, visited);
        dac(l2, g, mf, visited);
    }

}
