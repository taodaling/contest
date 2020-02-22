package template.primitve.generated.graph;

import template.primitve.generated.datastructure.IntegerList;

import java.util.List;

public class DoubleGomoryHuTree {
    private List<DoubleWeightUndirectedEdge>[] ug;
    private double[][] minCuts;
    private static final double INF = (double) 2e18;

    public DoubleGomoryHuTree(List<DoubleFlowEdge>[] g, DoubleMaximumFlow mf) {
        int n = g.length;
        ug = DoubleWeightGraph.createUndirectedGraph(n);
        minCuts = new double[n][n];
        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        dac(list, g, mf, new boolean[n]);
        for (int i = 0; i < n; i++) {
            dfs(i, -1, INF, minCuts[i]);
        }
    }

    private void addEdge(int s, int t, double f) {
        //System.err.printf("(%d,%d)=%d\n", s, t, f);
        DoubleWeightGraph.addUndirectedEdge(ug, s, t, f);
    }

    private void dfs(int root, int p, double w, double[] minCuts) {
        minCuts[root] = w;
        for (DoubleWeightUndirectedEdge e : ug[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, Math.min(w, e.weight), minCuts);
        }
    }

    public double minCut(int s, int t) {
        return minCuts[s][t];
    }


    public void dac(IntegerList set, List<DoubleFlowEdge>[] g, DoubleMaximumFlow mf, boolean[] visited) {
        if (set.size() <= 1) {
            return;
        }
        //rebuild the flow
        DoubleFlow.rewind(g);
        int s = set.get(0);
        int t = set.get(1);
        double f = mf.apply(g, s, t, (double)2e18);
        addEdge(s, t, f);
        DoubleFlow.findSetST(g, s, visited);
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
